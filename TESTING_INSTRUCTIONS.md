# 🧪 TESTING GUIDE - Chat System with WebSocket Authentication

## ⚠️ Prerequisites Check

Before testing, ensure you have:

- ✅ Java 17+ installed (You have Java 25 ✅)
- ⚠️ Maven installed (Not found in PATH)
- ✅ MongoDB running (Check connection string in application.properties)

---

## 📋 Option 1: Test Using IntelliJ IDEA (Recommended)

Since you have `.idea` folder, you're using IntelliJ IDEA:

### Step 1: Open Project in IntelliJ IDEA

1. Open IntelliJ IDEA
2. File → Open → Select this folder
3. Wait for Maven to sync dependencies

### Step 2: Run the Application

1. Navigate to `src/main/java/org/govt/Main.java`
2. Right-click on the file
3. Select "Run 'Main.main()'"
4. Watch the console for startup logs

### Step 3: Watch for Success Logs

You should see:

```
Started Main in X.XXX seconds
```

### Step 4: Test WebSocket Authentication

Once running, you'll see logs when clients connect. Look for:

```
🔵 WebSocket Command: CONNECT
🔵 WebSocket CONNECT request received
✅ JWT Token extracted: ...
✅ Username extracted from token: ...
✅ User found as: ...
✅ JWT token validated successfully
✅✅✅ WebSocket authentication SUCCESS! User ID stored: ...
```

---

## 📋 Option 2: Install Maven and Run from Command Line

### Step 1: Install Maven

1. Download Maven from: https://maven.apache.org/download.cgi
2. Extract to `C:\Program Files\Apache\maven`
3. Add to PATH:
   - Windows Key → "Environment Variables"
   - System Variables → Path → Edit
   - Add: `C:\Program Files\Apache\maven\bin`
4. Restart terminal
5. Verify: `mvn -version`

### Step 2: Compile and Run

```bash
# Navigate to project directory
cd "c:\Users\shubh\Downloads\GOVT-PROJECT-BACKEND-master\GOVT-PROJECT-BACKEND-master"

# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run
```

---

## 📋 Option 3: Use Existing Compiled Classes (If Available)

If you've previously compiled the project:

```bash
# Check if classes exist
dir target\classes\org\govt\Main.class

# If exists, run directly with Java
java -cp "target/classes;%USERPROFILE%\.m2\repository\*" org.govt.Main
```

---

## 🧪 Testing the Chat System

### Test 1: Verify Application Starts

**Expected Console Output:**

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.4)

INFO  o.g.Main - Starting Main...
INFO  o.s.b.w.e.t.TomcatWebServer - Tomcat started on port(s): 8080 (http)
INFO  o.g.Main - Started Main in 5.234 seconds
```

✅ **Success:** Application is running on port 8080

---

### Test 2: Test REST API (Chat History)

Open a new terminal and test the chat history endpoint:

```bash
# Test without authentication (should fail)
curl http://localhost:8080/api/chat/someUser

# Test with authentication (replace YOUR_JWT_TOKEN)
curl -X GET http://localhost:8080/api/chat/someUser ^
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Server Logs:**

```
Fetching chat history between user1 and user2
✅ Found 0 messages
```

---

### Test 3: Test WebSocket Connection

Create a simple HTML test file:

**File: `test-websocket.html`**

```html
<!DOCTYPE html>
<html>
  <head>
    <title>WebSocket Test</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
  </head>
  <body>
    <h1>WebSocket Chat Test</h1>
    <div>
      <label>JWT Token:</label><br />
      <input
        type="text"
        id="token"
        style="width: 500px"
        placeholder="Paste your JWT token here"
      />
    </div>
    <div>
      <button onclick="connect()">Connect</button>
      <button onclick="disconnect()">Disconnect</button>
    </div>
    <div>
      <label>Receiver ID:</label>
      <input type="text" id="receiverId" placeholder="username" />
      <label>Message:</label>
      <input type="text" id="message" placeholder="Hello!" />
      <button onclick="sendMessage()">Send</button>
    </div>
    <div>
      <h3>Logs:</h3>
      <pre
        id="logs"
        style="background: #f0f0f0; padding: 10px; height: 300px; overflow-y: scroll;"
      ></pre>
    </div>

    <script>
      let stompClient = null;

      function log(message) {
        const logs = document.getElementById("logs");
        logs.textContent +=
          new Date().toLocaleTimeString() + " - " + message + "\n";
        logs.scrollTop = logs.scrollHeight;
      }

      function connect() {
        const token = document.getElementById("token").value;
        if (!token) {
          alert("Please enter JWT token");
          return;
        }

        const socket = new SockJS("http://localhost:8080/ws-chat");
        stompClient = Stomp.over(socket);

        stompClient.connect(
          { Authorization: `Bearer ${token}` },
          (frame) => {
            log("✅ Connected to WebSocket!");
            log("Frame: " + frame);

            // Subscribe to receive messages
            stompClient.subscribe("/user/queue/messages", (message) => {
              log("📨 Received message: " + message.body);
              const msg = JSON.parse(message.body);
              log(`   From: ${msg.senderId}, Content: ${msg.content}`);
            });
          },
          (error) => {
            log("❌ Connection error: " + error);
          },
        );
      }

      function disconnect() {
        if (stompClient !== null) {
          stompClient.disconnect();
          log("Disconnected");
        }
      }

      function sendMessage() {
        if (stompClient === null || !stompClient.connected) {
          alert("Not connected to WebSocket");
          return;
        }

        const receiverId = document.getElementById("receiverId").value;
        const content = document.getElementById("message").value;

        if (!receiverId || !content) {
          alert("Please enter receiver ID and message");
          return;
        }

        const message = {
          receiverId: receiverId,
          content: content,
          // Note: senderId is set by server from authenticated Principal
        };

        stompClient.send("/app/chat.send", {}, JSON.stringify(message));
        log("📤 Sent message to " + receiverId + ": " + content);
      }
    </script>
  </body>
</html>
```

**How to Use:**

1. Save this file as `test-websocket.html`
2. Open it in a browser
3. Get a JWT token (login to your app first)
4. Paste the token in the input field
5. Click "Connect"
6. Watch the browser logs AND server console

**Expected Server Logs:**

```
🔵 WebSocket Command: CONNECT
🔵 WebSocket CONNECT request received
✅ JWT Token extracted: eyJhbGciOiJIUzI1Ni...
✅ Username extracted from token: john_doe
✅ User found as: Contractor (username: john_doe)
✅ User found in database: john_doe
✅ JWT token validated successfully for user: john_doe
✅✅✅ WebSocket authentication SUCCESS! User ID stored: john_doe
    - Principal name: john_doe
    - Authorities: [ROLE_CONTRACTOR]
```

---

### Test 4: Send a Message

After connecting successfully:

1. Enter a receiver username
2. Enter a message
3. Click "Send"

**Expected Server Logs:**

```
🔵 WebSocket Command: SEND
✅ Message saved to database: 507f1f77bcf86cd799439011 from john_doe to jane_doe
✅ Message sent via WebSocket to user: jane_doe
```

---

## 🔍 Verification Checklist

After running the tests, verify:

- [ ] Application starts without errors
- [ ] Port 8080 is listening
- [ ] MongoDB connection is successful
- [ ] WebSocket CONNECT shows authentication logs
- [ ] User ID is extracted from JWT token
- [ ] User is found in database
- [ ] JWT token is validated
- [ ] "WebSocket authentication SUCCESS!" message appears
- [ ] Messages are saved to database
- [ ] Messages show correct senderId (from Principal, not client)

---

## 🐛 Troubleshooting

### Issue: "Maven not found"

**Solution:** Use IntelliJ IDEA's built-in Maven (Option 1) or install Maven (Option 2)

### Issue: "Port 8080 already in use"

**Solution:**

```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID)
taskkill /PID <PID> /F
```

### Issue: "MongoDB connection failed"

**Solution:**

- Check MongoDB is running
- Verify connection string in `application.properties`
- Test connection: `mongosh "mongodb+srv://himanshuparida:govtproject@govt-project.xrf0vje.mongodb.net/"`

### Issue: "No Authorization header found"

**Solution:**

- Ensure you're passing JWT token in WebSocket connect
- Format: `{ Authorization: 'Bearer YOUR_TOKEN' }`

### Issue: "User not found in database"

**Solution:**

- Verify the username in JWT token exists in one of the user collections
- Check MongoDB for the user

---

## 📊 Expected Results

### ✅ Successful Test Output:

**Server Console:**

```
🔵 WebSocket Command: CONNECT
🔵 WebSocket CONNECT request received
✅ JWT Token extracted: eyJhbGciOiJIUzI1Ni...
✅ Username extracted from token: john_doe
✅ User found as: Contractor (username: john_doe)
✅ User found in database: john_doe
✅ JWT token validated successfully for user: john_doe
✅✅✅ WebSocket authentication SUCCESS! User ID stored: john_doe
    - Principal name: john_doe
    - Authorities: [ROLE_CONTRACTOR]

🔵 WebSocket Command: SEND
✅ Message saved to database: 507f1f77bcf86cd799439011 from john_doe to jane_doe
✅ Message sent via WebSocket to user: jane_doe
```

**MongoDB Verification:**

```javascript
// Connect to MongoDB
use Govt-project

// Check messages collection
db.messages.find().pretty()

// Should show:
{
    "_id": ObjectId("507f1f77bcf86cd799439011"),
    "senderId": "john_doe",
    "receiverId": "jane_doe",
    "content": "Hello!",
    "timestamp": 1706950800000,
    "status": "SENT"
}
```

---

## 🎯 Next Steps

1. **Run the application** using one of the 3 options above
2. **Watch the console logs** for the ✅ and ❌ indicators
3. **Test with the HTML file** to verify WebSocket authentication
4. **Check MongoDB** to confirm messages are being saved
5. **Report any errors** - the detailed logs will help identify issues

---

## 📞 Need Help?

If you encounter issues:

1. Copy the **exact error message** from the console
2. Check which step failed (look for ❌ in the logs)
3. Verify prerequisites (Java, Maven, MongoDB)
4. Check the troubleshooting section above

The comprehensive logging will show you exactly where any issue occurs!
