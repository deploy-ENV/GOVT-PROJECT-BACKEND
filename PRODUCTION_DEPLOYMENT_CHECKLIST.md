# Production Deployment Checklist - Chat User ID Fix

## ✅ Code Pushed to Production

**Commit**: `ee6b452`  
**Branch**: `master`  
**Repository**: `https://github.com/deploy-ENV/GOVT-PROJECT-BACKEND.git`  
**Date**: February 4, 2026

---

## 📋 Pre-Deployment Checklist

### ✅ Completed

- [x] Code changes committed
- [x] Pushed to GitHub master branch
- [x] Documentation created (CHAT_USERID_FIX_SUMMARY.md, CHAT_FIX_QUICKSTART.md)

### 🔄 Next Steps (Deploy to Server)

#### 1. **Pull Latest Code on Production Server**

```bash
cd /path/to/production/app
git pull origin master
```

#### 2. **Rebuild the Application**

```bash
mvn clean package -DskipTests
```

#### 3. **Stop Current Application**

```bash
# If using systemd
sudo systemctl stop your-app-name

# OR if using screen/tmux
# Find and kill the Java process
ps aux | grep java
kill <process-id>
```

#### 4. **Backup Current Database (RECOMMENDED)**

```bash
# MongoDB backup
mongodump --db your_database_name --out /backup/$(date +%Y%m%d_%H%M%S)
```

#### 5. **Start Application**

```bash
# If using systemd
sudo systemctl start your-app-name

# OR if running manually
java -jar target/govt-project-backend.jar &

# OR if using screen
screen -S backend
java -jar target/govt-project-backend.jar
# Press Ctrl+A then D to detach
```

#### 6. **Verify Application Started**

```bash
# Check logs
tail -f logs/application.log

# OR if using systemd
sudo journalctl -u your-app-name -f

# Look for:
# ✅ Started Application
# ✅ WebSocket configuration loaded
```

#### 7. **Test WebSocket Endpoint**

```bash
curl -I http://your-domain.com/ws-chat
# Should return 200 or 101 (Switching Protocols)
```

---

## ⚠️ CRITICAL: User Communication

### **Send this message to all users:**

```
🔔 IMPORTANT UPDATE - Action Required

We've deployed an important update to improve the chat system.

ACTION REQUIRED:
Please logout and login again to continue using the chat feature.

Why? We've updated how chat messages are stored to use user IDs
instead of usernames for better security and reliability.

Your old login session will not work with the new chat system.

Thank you for your cooperation!
```

---

## 🧪 Post-Deployment Testing

### Test 1: User Login

```bash
# Test login endpoint
curl -X POST http://your-domain.com/auth/login/supplier \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"testpass"}'

# Response should include token with userId claim
```

### Test 2: Decode JWT Token

Use https://jwt.io to decode the token and verify it contains:

```json
{
  "sub": "username",
  "userId": "507f1f77bcf86cd799439011", // ✅ Should be present
  "iat": 1707034050,
  "exp": 1707041250
}
```

### Test 3: WebSocket Connection

1. Login as User A
2. Login as User B (different browser/incognito)
3. Connect to WebSocket with new token
4. Send message from A to B
5. Check backend logs for:

```
✅ User ID extracted from token: 507f1f77bcf86cd799439011
✅ Message saved to database: <id> from <userIdA> to <userIdB>
```

### Test 4: Database Verification

```javascript
// Connect to MongoDB
use your_database_name

// Check latest message
db.messages.find().sort({timestamp: -1}).limit(1).pretty()

// Verify senderId is user ID (24 char hex), not username
// ✅ Good: "senderId": "507f1f77bcf86cd799439011"
// ❌ Bad:  "senderId": "john_doe"
```

---

## 🔍 Monitoring

### Check These Logs After Deployment

#### Success Indicators:

```
✅ User ID extracted from token: <userId>
✅ WebSocket authentication SUCCESS! User ID stored: <userId>
✅ Message saved to database: <id> from <userIdA> to <userIdB>
```

#### Expected Warnings (for old tokens):

```
⚠️  Failed to extract userId from JWT token
⚠️  This token may have been generated with the old method.
⚠️  Please re-login to get a new token with userId.
```

**This is normal** - users with old tokens will see this until they re-login.

#### Error Indicators (investigate if seen):

```
❌ Failed to extract username from JWT token
❌ User not found in database
❌ JWT token validation failed
❌ ERROR handling message
```

---

## 🚨 Rollback Plan (If Needed)

If critical issues occur:

### 1. **Revert to Previous Commit**

```bash
git revert ee6b452
git push origin master
```

### 2. **Or Reset to Previous Version**

```bash
git reset --hard 2a2a689
git push origin master --force
```

### 3. **Rebuild and Restart**

```bash
mvn clean package -DskipTests
sudo systemctl restart your-app-name
```

---

## 📊 Success Metrics

After 24 hours, verify:

- [ ] All active users have re-logged in
- [ ] New chat messages use user IDs (not usernames)
- [ ] No authentication errors in logs
- [ ] WebSocket connections stable
- [ ] Chat functionality working normally

---

## 📞 Support

### Common Issues

**Issue**: "Failed to extract userId from JWT token"  
**Solution**: User needs to logout and login again

**Issue**: WebSocket connection fails  
**Solution**: Check if server is running, verify CORS settings

**Issue**: Messages not saving  
**Solution**: Check MongoDB connection, verify user IDs are valid

---

## 📝 Files Changed in This Deployment

### New Files:

- `src/main/java/org/govt/Authentication/UserIdPrincipal.java`
- `CHAT_FIX_QUICKSTART.md`
- `CHAT_USERID_FIX_SUMMARY.md`

### Modified Files:

- `src/main/java/org/govt/Authentication/JwtUtil.java`
- `src/main/java/org/govt/Authentication/WebSocketAuthInterceptor.java`
- `src/main/java/org/govt/Controller/ContractorAuth.java`
- `src/main/java/org/govt/Controller/GovtAuth.java`
- `src/main/java/org/govt/Controller/ProjectManagerAuth.java`
- `src/main/java/org/govt/Controller/SupervisorAuth.java`
- `src/main/java/org/govt/Controller/SupplierAuth.java`

---

## ✅ Final Checklist

- [ ] Code pulled on production server
- [ ] Application rebuilt
- [ ] Database backed up
- [ ] Application restarted
- [ ] Logs checked for errors
- [ ] Test login performed
- [ ] JWT token verified (contains userId)
- [ ] Test chat message sent
- [ ] Database verified (shows user IDs)
- [ ] Users notified to re-login
- [ ] Monitoring in place

---

**Deployment Date**: ******\_******  
**Deployed By**: ******\_******  
**Verified By**: ******\_******

**Status**: ⬜ Pending | ⬜ In Progress | ⬜ Complete | ⬜ Rolled Back
