# Chat System - Issues Fixed & Testing Guide

## 🔍 Issues Found and Fixed

### 1. **Missing WebSocket Authentication** ❌ → ✅ FIXED

**Problem:** WebSocket connections were not authenticated, allowing anyone to send messages and causing the `Principal` to be null.

**Fix:** Created `WebSocketAuthInterceptor.java` that:

- Intercepts WebSocket CONNECT commands
- Validates JWT tokens from the Authorization header
- Sets the authenticated user principal for all WebSocket messages

### 2. **Sender ID Not Validated** ❌ → ✅ FIXED

**Problem:** `ChatController` accepted senderId from client messages, allowing users to impersonate others.

**Fix:** Modified `ChatController.send()` to:

- Extract senderId from the authenticated `Principal`
- Reject messages from unauthenticated users
- Prevent sender spoofing

### 3. **No Error Handling or Logging** ❌ → ✅ FIXED

**Problem:** Silent failures made it impossible to debug why messages weren't being saved.

**Fix:** Added comprehensive logging to:

- `ChatService.handleMessage()` - logs message saves and WebSocket sends
- `ChatService.markMessagesAsRead()` - logs read status updates
- `ChatHistoryController` - logs all API requests and errors

### 4. **Missing Input Validation** ❌ → ✅ FIXED

**Problem:** No validation of message content, sender, or receiver IDs.

**Fix:** Added validation in `ChatService.handleMessage()`:

- Checks for null/empty senderId
- Checks for null/empty receiverId
- Checks for null/empty content
- Returns early if validation fails

---

## 📋 Files Modified

1. ✅ **Created:** `WebSocketAuthInterceptor.java` - Handles WebSocket JWT authentication
2. ✅ **Modified:** `WebSocketConfig.java` - Registered the auth interceptor
3. ✅ **Modified:** `ChatController.java` - Uses Principal for senderId
4. ✅ **Modified:** `ChatService.java` - Added validation, error handling, and logging
5. ✅ **Modified:** `ChatHistoryController.java` - Added error handling and logging

---

## 🧪 How to Test

### Step 1: Start the Application

```bash
mvn clean install
mvn spring-boot:run
```

Watch the console for startup logs. You should see Spring Boot starting on port 8080.

### Step 2: Test WebSocket Connection with Authentication

**Frontend WebSocket Connection Example:**

```javascript
const token = "your-jwt-token-here"; // Get from login

const socket = new SockJS("http://localhost:8080/ws-chat");
const stompClient = Stomp.over(socket);

stompClient.connect(
  { Authorization: `Bearer ${token}` }, // IMPORTANT: Pass JWT token
  (frame) => {
    console.log("Connected: " + frame);

    // Subscribe to receive messages
    stompClient.subscribe("/user/queue/messages", (message) => {
      console.log("Received:", JSON.parse(message.body));
    });

    // Send a message
    stompClient.send(
      "/app/chat.send",
      {},
      JSON.stringify({
        receiverId: "otherUsername",
        content: "Hello!",
        // Note: senderId is now set by the server from the authenticated user
      }),
    );
  },
  (error) => {
    console.error("Connection error:", error);
  },
);
```

### Step 3: Check Server Logs

When a message is sent, you should see:

```
✅ Message saved to database: 507f1f77bcf86cd799439011 from user1 to user2
✅ Message sent via WebSocket to user: user2
```

If there's an error, you'll see:

```
❌ ERROR handling message: [error details]
```

### Step 4: Test Chat History API

**Request:**

```bash
curl -X GET http://localhost:8080/api/chat/otherUsername \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response:**

```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "senderId": "user1",
    "receiverId": "user2",
    "content": "Hello!",
    "timestamp": 1706950800000,
    "status": "SENT"
  }
]
```

**Server Log:**

```
Fetching chat history between user1 and user2
✅ Found 5 messages
```

### Step 5: Test Mark as Read

**Request:**

```bash
curl -X PUT http://localhost:8080/api/chat/read/senderUsername \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Server Log:**

```
Marking messages from senderUsername to currentUser as READ
Marking 3 messages as READ
✅ Messages marked as READ successfully
```

---

## 🔧 Troubleshooting

### Issue: "ERROR: Unauthenticated WebSocket message received"

**Cause:** JWT token not provided or invalid
**Fix:** Ensure you're passing the Authorization header in the WebSocket CONNECT frame

### Issue: Messages not saving to database

**Check:**

1. MongoDB connection string in `application.properties`
2. Server logs for validation errors
3. Ensure senderId, receiverId, and content are all provided

### Issue: "ERROR: Message senderId is null or empty"

**Cause:** WebSocket authentication failed
**Fix:** Verify JWT token is valid and user exists in one of the user repositories

### Issue: Principal is null

**Cause:** WebSocket interceptor not working
**Fix:** Ensure `WebSocketAuthInterceptor` is properly autowired in `WebSocketConfig`

---

## 🔐 Security Improvements Made

1. ✅ WebSocket connections now require valid JWT tokens
2. ✅ Sender ID is extracted from authenticated principal (cannot be spoofed)
3. ✅ All chat endpoints require authentication
4. ✅ Input validation prevents null/empty messages

---

## 📊 Database Verification

To verify messages are being saved, connect to MongoDB and run:

```javascript
use Govt-project

// Check all messages
db.messages.find().pretty()

// Check messages between two users
db.messages.find({
  $or: [
    { senderId: "user1", receiverId: "user2" },
    { senderId: "user2", receiverId: "user1" }
  ]
}).sort({ timestamp: 1 })

// Check unread messages
db.messages.find({ status: "SENT" })
```

---

## 🎯 Next Steps

1. **Test the application** with the steps above
2. **Monitor server logs** for the ✅ and ❌ indicators
3. **Verify database** to ensure messages are persisting
4. **Test from frontend** with proper JWT authentication

If you still encounter issues, check the server logs for specific error messages. The detailed logging will help identify exactly where the problem is occurring.
