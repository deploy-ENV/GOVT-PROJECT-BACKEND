# 🚀 Production Deployment Checklist

## ✅ Files Ready for Production

Your backend is now production-ready! Here's what's been configured:

### **Security Configuration** ✅

- ✅ CORS restricted to specific domains
- ✅ WebSocket authentication enabled
- ✅ JWT token validation implemented
- ✅ Sender ID verification (prevents spoofing)

### **Production CORS Domains**

The following domains are allowed:

- `https://gov-ashen-iota.vercel.app` (Production frontend)
- `http://localhost:3000` (Local React development)
- `http://localhost:5173` (Local Vite development)
- `http://localhost:8000` (Local testing)

---

## 📁 Files to Keep (Production Code)

### **Core Application Files** ✅

- All files in `src/main/java/` - Your Java backend code
- `src/main/resources/application.properties` - Configuration
- `pom.xml` - Maven dependencies
- `.gitignore` - Git configuration

### **Documentation Files** (Optional - Keep for reference)

- `CHAT_FIXES_AND_TESTING.md` - Documents what was fixed
- `WEBSOCKET_AUTH_QUICK_REFERENCE.md` - Authentication reference
- `README.md` - Project documentation (if exists)

---

## 🗑️ Files to Delete (Testing/Development Only)

These files were created for local testing and should **NOT** be deployed to production:

### **Testing HTML/Scripts**

- ❌ `test-websocket.html` - Browser test client
- ❌ `test-login.ps1` - Login testing script
- ❌ `check-users.ps1` - User checking script
- ❌ `check-users.bat` - User checking batch script
- ❌ `restart-backend.ps1` - Backend restart script
- ❌ `start-webserver.ps1` - Local web server script
- ❌ `diagnose-websocket.ps1` - Diagnostics script
- ❌ `start-test.ps1` - Test starter script (if exists)

### **Testing Documentation**

- ❌ `TESTING_GUIDE.md` - Testing instructions
- ❌ `TESTING_README.md` - Testing overview
- ❌ `QUICK_TEST_REFERENCE.md` - Quick test reference
- ❌ `WEBSOCKET_FIX.md` - Fix documentation
- ❌ `QUICK_FIX.md` - Quick fix guide

---

## 🔧 Before Deploying

### **1. Update Production Configuration**

Edit `src/main/resources/application.properties`:

```properties
# Production MongoDB URI (use environment variable)
spring.data.mongodb.uri=${MONGODB_URI}
spring.data.mongodb.database=Govt-project

# Production port (or use environment variable)
server.port=${PORT:8080}

# JWT Secret (use environment variable - NEVER commit this!)
jwt.secret=${JWT_SECRET}
```

### **2. Add Production Frontend Domain**

If you have a new production frontend URL, add it to `SecurityConfig.java`:

```java
config.setAllowedOriginPatterns(List.of(
    "https://your-production-domain.com",  // Add your domain
    "https://gov-ashen-iota.vercel.app",
    // ... other domains
));
```

### **3. Environment Variables**

Set these environment variables in your production environment:

```bash
MONGODB_URI=mongodb+srv://user:password@cluster.mongodb.net/database
JWT_SECRET=your-super-secret-key-min-256-bits
PORT=8080
```

---

## 📦 Deployment Steps

### **Option 1: Deploy to Heroku**

```bash
# Login to Heroku
heroku login

# Create app
heroku create your-app-name

# Set environment variables
heroku config:set MONGODB_URI="your-mongodb-uri"
heroku config:set JWT_SECRET="your-secret-key"

# Deploy
git push heroku main
```

### **Option 2: Deploy to Railway**

1. Connect your GitHub repository
2. Set environment variables in Railway dashboard
3. Deploy automatically on push

### **Option 3: Deploy to AWS/Azure/GCP**

1. Build JAR file: `mvn clean package`
2. Upload JAR to your cloud service
3. Configure environment variables
4. Run: `java -jar target/your-app.jar`

---

## 🔒 Security Checklist

Before going to production:

- [ ] MongoDB URI uses environment variable (not hardcoded)
- [ ] JWT secret is strong and in environment variable
- [ ] CORS is restricted to specific domains (no "\*")
- [ ] All endpoints require authentication (except login/register)
- [ ] WebSocket authentication is enabled
- [ ] Sender ID verification is active
- [ ] Input validation is in place
- [ ] Error messages don't expose sensitive info
- [ ] HTTPS is enabled (use wss:// for WebSocket)

---

## 🧪 Testing in Production

After deployment:

### **1. Test REST Endpoints**

```bash
# Health check
curl https://your-api.com/actuator/health

# Login
curl -X POST https://your-api.com/login/contractor \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test123"}'
```

### **2. Test WebSocket**

Update your frontend to use production WebSocket URL:

```javascript
const socket = new SockJS("https://your-api.com/ws-chat");
```

### **3. Monitor Logs**

Check your production logs for:

- ✅ Successful connections
- ✅ Message delivery
- ❌ Any errors or exceptions

---

## 📊 Production Monitoring

Set up monitoring for:

- Server uptime
- Response times
- Error rates
- WebSocket connections
- Database performance
- Memory/CPU usage

---

## 🔄 CI/CD Pipeline (Optional)

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: "17"
      - name: Build with Maven
        run: mvn clean package -DskipTests
      - name: Deploy to production
        # Add your deployment steps here
```

---

## ✅ Final Checklist

Before pushing to production:

- [ ] All testing files deleted
- [ ] CORS configured for production domains
- [ ] Environment variables configured
- [ ] MongoDB connection string secured
- [ ] JWT secret is strong and secured
- [ ] Application builds successfully
- [ ] All tests pass
- [ ] Documentation updated
- [ ] Monitoring set up
- [ ] Backup strategy in place

---

## 🎉 You're Ready!

Your backend is production-ready with:

- ✅ Secure WebSocket chat with JWT authentication
- ✅ Proper CORS configuration
- ✅ Input validation and error handling
- ✅ Message persistence in MongoDB
- ✅ Real-time message delivery

**Good luck with your deployment!** 🚀
