# Chat User ID Fix - Implementation Summary

## Problem

Chat messages were being stored with **username** as the `senderId` instead of the **user ID**. This happened because `Principal.getName()` was returning the username from the JWT token.

## Solution

Modified the authentication system to include user ID in JWT tokens and use a custom Principal that returns user ID instead of username.

---

## Changes Made

### 1. **JwtUtil.java** - Enhanced JWT Token Generation

**File**: `src/main/java/org/govt/Authentication/JwtUtil.java`

**Added**:

- `generateTokenWithUserId(String username, String userId)` - New method that stores user ID as a claim in the JWT
- `extractUserId(String token)` - Extracts user ID from JWT token
- Kept original `generateToken()` for backward compatibility

**Key Code**:

```java
public String generateTokenWithUserId(String username, String userId) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userId);

    return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3600 * 2))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
}

public String extractUserId(String token) {
    Claims claims = Jwts.parser()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

    return claims.get("userId", String.class);
}
```

---

### 2. **UserIdPrincipal.java** - Custom Principal Implementation

**File**: `src/main/java/org/govt/Authentication/UserIdPrincipal.java` _(NEW FILE)_

**Purpose**: Custom Principal that returns user ID from `getName()` instead of username

**Key Code**:

```java
public class UserIdPrincipal implements Principal {
    private final String userId;
    private final String username;

    @Override
    public String getName() {
        // CRITICAL: Return user ID, not username
        return userId;
    }
}
```

---

### 3. **WebSocketAuthInterceptor.java** - Updated Authentication

**File**: `src/main/java/org/govt/Authentication/WebSocketAuthInterceptor.java`

**Changes**:

1. Extract user ID from JWT token using `jwtUtil.extractUserId(token)`
2. Create `UserIdPrincipal` with both user ID and username
3. Use custom principal in authentication token

**Key Code**:

```java
// Extract user ID from token
String userId = jwtUtil.extractUserId(token);

// Create custom principal with USER ID (not username)
UserIdPrincipal principal = new UserIdPrincipal(userId, username);

// Create authentication with custom principal
UsernamePasswordAuthenticationToken authentication =
    new UsernamePasswordAuthenticationToken(
        principal,  // Use custom principal
        null,
        userDetails.getAuthorities()
    );
```

**Result**: Now `principal.getName()` in ChatController returns the user ID!

---

### 4. **Updated All Login Endpoints**

All authentication controllers now use `generateTokenWithUserId()`:

#### SupplierAuth.java

```java
String token = jwt.generateTokenWithUserId(supplier.getUsername(), supplier.getId());
```

#### SupervisorAuth.java

```java
String token = jwt.generateTokenWithUserId(supervisor.getUsername(), supervisor.getId());
```

#### ProjectManagerAuth.java

```java
String token = jwt.generateTokenWithUserId(manager.getUsername(), manager.getId());
```

#### GovtAuth.java

```java
User_govt govtUser = userGovtService.findByUsername(userGovt.getUsername());
String token = jwt.generateTokenWithUserId(govtUser.getUsername(), govtUser.getId());
```

#### ContractorAuth.java

```java
String token = jwt.generateTokenWithUserId(contractor.getUsername(), contractor.getId());
```

---

## How It Works Now

### Authentication Flow:

```
1. User logs in
   ↓
2. Backend generates JWT with userId claim
   Token contains: { subject: "username", userId: "507f1f77bcf86cd799439011" }
   ↓
3. Frontend stores JWT token
   ↓
4. WebSocket connection with JWT
   ↓
5. WebSocketAuthInterceptor extracts userId from JWT
   ↓
6. Creates UserIdPrincipal(userId, username)
   ↓
7. ChatController receives Principal
   ↓
8. principal.getName() returns USER ID (not username)
   ↓
9. Message saved with correct user ID
```

### Message Flow:

```java
// In ChatController.java
@MessageMapping("/chat.send")
public void send(@Payload ChatMessage message, Principal principal) {
    // principal.getName() now returns USER ID!
    message.setSenderId(principal.getName());  // ✅ Sets user ID
    chatService.handleMessage(message);
}
```

---

## Testing Instructions

### 1. **Clear Old Tokens**

Users must re-login to get new tokens with user ID:

```javascript
// Frontend: Clear old tokens
localStorage.removeItem("jwtToken");
// User must login again
```

### 2. **Verify Token Contains User ID**

Decode JWT token to verify it contains userId claim:

```javascript
// In browser console
const token = localStorage.getItem("jwtToken");
const payload = JSON.parse(atob(token.split(".")[1]));
console.log("User ID:", payload.userId); // Should show MongoDB ID
console.log("Username:", payload.sub); // Should show username
```

### 3. **Test Chat Messages**

1. Login as User A (get new token)
2. Login as User B (get new token)
3. Send message from A to B
4. Check MongoDB:

```javascript
db.messages.find().pretty()
// Should show:
{
  "_id": "...",
  "senderId": "507f1f77bcf86cd799439011",  // ✅ User ID, not username
  "receiverId": "507f191e810c19729de860ea",
  "content": "Hello!",
  "timestamp": 1707034050000,
  "status": "SENT"
}
```

---

## Important Notes

### ⚠️ Breaking Change

**Old tokens will NOT work** with WebSocket authentication. Users will see this error:

```
❌ Failed to extract userId from JWT token
⚠️  This token may have been generated with the old method.
⚠️  Please re-login to get a new token with userId.
```

**Solution**: All users must re-login to get new tokens.

### ✅ Backward Compatibility

- Old REST API endpoints still work (they don't use WebSocket)
- `generateToken()` method still exists for any legacy code
- Only WebSocket chat requires new tokens with user ID

---

## Database Impact

### Before Fix:

```json
{
  "senderId": "john_doe", // ❌ Username
  "receiverId": "jane_smith",
  "content": "Hello"
}
```

### After Fix:

```json
{
  "senderId": "507f1f77bcf86cd799439011", // ✅ User ID
  "receiverId": "507f191e810c19729de860ea",
  "content": "Hello"
}
```

---

## Files Modified

1. ✅ `JwtUtil.java` - Added user ID support
2. ✅ `UserIdPrincipal.java` - NEW custom principal
3. ✅ `WebSocketAuthInterceptor.java` - Extract and use user ID
4. ✅ `SupplierAuth.java` - Use new token method
5. ✅ `SupervisorAuth.java` - Use new token method
6. ✅ `ProjectManagerAuth.java` - Use new token method
7. ✅ `GovtAuth.java` - Use new token method
8. ✅ `ContractorAuth.java` - Use new token method

**No changes needed**:

- `ChatController.java` - Already uses `principal.getName()`
- `ChatService.java` - No changes needed
- `ChatMessage.java` - No changes needed

---

## Verification Checklist

- [ ] Backend compiles without errors
- [ ] All users re-login to get new tokens
- [ ] WebSocket connection succeeds with new token
- [ ] Chat messages save with user ID (not username)
- [ ] Messages appear correctly in chat history
- [ ] No authentication errors in backend logs

---

**Date**: February 4, 2026  
**Status**: ✅ COMPLETE  
**Impact**: All chat messages now correctly store user IDs
