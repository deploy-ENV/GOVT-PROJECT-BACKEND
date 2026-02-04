# 🚀 QUICK START - Testing Your Chat System

## ✅ All Fixes Have Been Applied!

Your chat system has been fixed with:

- ✅ WebSocket JWT authentication
- ✅ User ID verification and storage
- ✅ Secure sender ID extraction from Principal
- ✅ Comprehensive error handling and logging
- ✅ Input validation

---

## 🧪 How to Test (3 Simple Steps)

### **Step 1: Start the Application**

**Option A: Using IntelliJ IDEA (Easiest)**

1. Open IntelliJ IDEA
2. Open this project folder
3. Navigate to: `src/main/java/org/govt/Main.java`
4. Right-click on `Main.java`
5. Click "Run 'Main.main()'"
6. Wait for: `Started Main in X.XXX seconds`

**Option B: Using Maven Command Line**

```bash
mvn spring-boot:run
```

---

### **Step 2: Open the Test Page**

1. Open `test-websocket.html` in your web browser
2. You'll see a beautiful test interface

---

### **Step 3: Test the Connection**

1. **Get a JWT Token:**
   - Login to your application first
   - Copy the JWT token you receive

2. **Connect to WebSocket:**
   - Paste the JWT token in the test page
   - Click "Connect to WebSocket"
   - Watch the logs in the browser
   - **IMPORTANT:** Watch the SERVER CONSOLE too!

3. **Expected Server Console Output:**

```
🔵 WebSocket Command: CONNECT
🔵 WebSocket CONNECT request received
✅ JWT Token extracted: eyJhbGciOiJIUzI1Ni...
✅ Username extracted from token: your_username
✅ User found as: Contractor (username: your_username)
✅ User found in database: your_username
✅ JWT token validated successfully for user: your_username
✅✅✅ WebSocket authentication SUCCESS! User ID stored: your_username
    - Principal name: your_username
    - Authorities: [ROLE_CONTRACTOR]
```

4. **Send a Test Message:**
   - Enter a receiver username
   - Type a message
   - Click "Send Message"

5. **Expected Server Console Output:**

```
🔵 WebSocket Command: SEND
✅ Message saved to database: 507f1f77bcf86cd799439011 from your_username to receiver_username
✅ Message sent via WebSocket to user: receiver_username
```

---

## 📊 What to Look For

### ✅ **Success Indicators:**

**In Browser (test-websocket.html):**

- 🟢 Status shows "Connected"
- ✅ Logs show "Successfully connected to WebSocket!"
- ✅ "Ready to send messages!" appears

**In Server Console:**

- ✅ All steps show green checkmarks (✅)
- ✅ "WebSocket authentication SUCCESS!" message
- ✅ User ID is extracted and stored
- ✅ Messages are saved to database

### ❌ **Error Indicators:**

**If you see ❌ in server console:**

- Check which step failed
- Read the error message
- See troubleshooting section below

---

## 🐛 Common Issues & Solutions

### Issue: "No Authorization header found"

**Solution:** Make sure you're pasting the JWT token in the test page before connecting

### Issue: "User not found in database"

**Solution:**

- Verify the username in the JWT token exists in MongoDB
- Check one of these collections: suppliers, contractors, project_managers, govt_users, supervisors

### Issue: "JWT token validation failed"

**Solution:**

- Token might be expired
- Get a fresh token by logging in again

### Issue: "Port 8080 already in use"

**Solution:**

```bash
# Find what's using port 8080
netstat -ano | findstr :8080

# Kill the process (replace <PID> with actual process ID)
taskkill /PID <PID> /F
```

---

## 📁 Files Created for Testing

1. **`test-websocket.html`** - Beautiful WebSocket test client (OPEN THIS!)
2. **`TESTING_INSTRUCTIONS.md`** - Detailed testing guide
3. **`WEBSOCKET_AUTH_VERIFICATION.md`** - Authentication verification guide
4. **`WEBSOCKET_AUTH_QUICK_REFERENCE.md`** - Quick reference
5. **`CHAT_FIXES_AND_TESTING.md`** - All fixes explained

---

## 🎯 Testing Checklist

- [ ] Application starts without errors
- [ ] Port 8080 is listening
- [ ] MongoDB connection successful
- [ ] test-websocket.html opens in browser
- [ ] JWT token is pasted
- [ ] WebSocket connects successfully
- [ ] Server shows "WebSocket authentication SUCCESS!"
- [ ] User ID is extracted from token
- [ ] Message is sent successfully
- [ ] Server shows "Message saved to database"
- [ ] Message appears in MongoDB

---

## 📞 Need Help?

If something doesn't work:

1. **Check the server console** - Look for ❌ error messages
2. **Check the browser console** - Press F12 to see errors
3. **Verify MongoDB** - Make sure it's running and accessible
4. **Check JWT token** - Make sure it's valid and not expired

The comprehensive logging will show you exactly where any issue occurs!

---

## 🎉 You're All Set!

Just:

1. **Run the application** (IntelliJ or Maven)
2. **Open test-websocket.html**
3. **Paste your JWT token**
4. **Click Connect**
5. **Watch the magic happen!** ✨

The server console will show you step-by-step what's happening with lots of ✅ checkmarks!
