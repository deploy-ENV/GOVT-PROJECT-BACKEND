# Quick Start - Chat User ID Fix

## ✅ What Was Fixed

Chat messages now store **user ID** instead of **username** in the `senderId` field.

---

## 🚀 How to Deploy

### 1. **Rebuild the Application**

```bash
mvn clean package
```

### 2. **Restart the Server**

```bash
java -jar target/your-app.jar
```

---

## ⚠️ IMPORTANT: Users Must Re-Login

**All existing JWT tokens are now invalid for WebSocket chat.**

### Why?

Old tokens don't contain the `userId` claim. New tokens include:

```json
{
  "sub": "username",
  "userId": "507f1f77bcf86cd799439011" // NEW!
}
```

### What Happens with Old Tokens?

Users will see this error when trying to connect to WebSocket:

```
❌ Failed to extract userId from JWT token
⚠️  This token may have been generated with the old method.
⚠️  Please re-login to get a new token with userId.
```

### Solution

**Tell all users to:**

1. Logout from the application
2. Login again
3. They will receive a new token with user ID

---

## 🧪 Quick Test

### Test 1: Verify New Token Format

After user logs in, check the token:

**Frontend (JavaScript)**:

```javascript
const token = localStorage.getItem("jwtToken");
const payload = JSON.parse(atob(token.split(".")[1]));
console.log("✅ User ID:", payload.userId); // Should show MongoDB ID
console.log("✅ Username:", payload.sub); // Should show username
```

### Test 2: Send a Chat Message

1. User A logs in (gets new token)
2. User B logs in (gets new token)
3. User A sends message to User B
4. Check backend logs:

```
✅ Message saved to database: <messageId> from <userIdA> to <userIdB>
```

### Test 3: Check Database

```javascript
// MongoDB
db.messages.findOne()

// Should show:
{
  "_id": "...",
  "senderId": "507f1f77bcf86cd799439011",  // ✅ User ID (24 char hex)
  "receiverId": "507f191e810c19729de860ea",
  "content": "Hello!",
  "timestamp": 1707034050000,
  "status": "SENT"
}
```

**Before the fix**, `senderId` would be `"john_doe"` (username).  
**After the fix**, `senderId` is `"507f1f77bcf86cd799439011"` (user ID).

---

## 📝 Summary of Changes

| File                            | Change                                                  |
| ------------------------------- | ------------------------------------------------------- |
| `JwtUtil.java`                  | Added `generateTokenWithUserId()` and `extractUserId()` |
| `UserIdPrincipal.java`          | NEW - Custom principal that returns user ID             |
| `WebSocketAuthInterceptor.java` | Extract user ID from token, use custom principal        |
| `SupplierAuth.java`             | Use `generateTokenWithUserId()`                         |
| `SupervisorAuth.java`           | Use `generateTokenWithUserId()`                         |
| `ProjectManagerAuth.java`       | Use `generateTokenWithUserId()`                         |
| `GovtAuth.java`                 | Use `generateTokenWithUserId()`                         |
| `ContractorAuth.java`           | Use `generateTokenWithUserId()`                         |

**No changes needed**:

- ChatController.java
- ChatService.java
- ChatMessage.java
- Frontend code

---

## 🔍 Troubleshooting

### Problem: WebSocket connection fails

**Error**: `Failed to extract userId from JWT token`

**Solution**: User needs to re-login to get a new token

---

### Problem: Messages still show username

**Check**:

1. Did user re-login after the fix?
2. Is the new token being used?
3. Check backend logs for "User ID extracted from token"

---

### Problem: Old messages have username

**This is expected**. Old messages in the database will still have usernames.

**Options**:

1. Leave old messages as-is (they still work)
2. Run a migration script to convert usernames to user IDs
3. Clear old messages (if acceptable)

---

## ✅ Deployment Checklist

- [ ] Code changes deployed
- [ ] Server restarted
- [ ] All users notified to re-login
- [ ] Test message sent successfully
- [ ] Database shows user IDs (not usernames)
- [ ] No errors in backend logs

---

**For detailed technical documentation, see**: `CHAT_USERID_FIX_SUMMARY.md`
