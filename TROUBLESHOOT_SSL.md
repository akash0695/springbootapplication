# SSL Troubleshooting Guide

## Error: "Bad Request - This combination of host and port requires TLS"

### Problem
You're accessing the server via HTTP when SSL is enabled.

### Solution

#### ✅ Step 1: Use HTTPS (not HTTP)
**WRONG:** `http://localhost:8443` ❌  
**CORRECT:** `https://localhost:8443` ✅

**Important:** When SSL is enabled, you MUST use `https://` in the URL.

#### ✅ Step 2: Accept Browser Security Warning
For self-signed certificates, browsers will show a security warning:
1. Click "Advanced" or "Show Details"
2. Click "Proceed to localhost" or "Accept the Risk and Continue"
3. This is normal for development certificates

#### ✅ Step 3: Verify Configuration
Check `application.properties`:
```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=akashcreations2026
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=springboot
server.port=8443
```

#### ✅ Step 4: Verify Keystore File
The keystore file should exist at:
- `src/main/resources/keystore.p12`

If missing, generate it:
```bash
generate-ssl-certificate.bat
```

#### ✅ Step 5: Check Application Logs
Look for SSL-related errors in the console:
- "Keystore was tampered with" → Wrong password
- "Certificate not found" → Keystore missing
- "Port already in use" → Another app using port 8443

---

## Alternative: Disable SSL for Development

If you don't need SSL right now, disable it:

1. In `application.properties`, comment out SSL settings:
```properties
# server.ssl.enabled=true
# server.ssl.key-store=classpath:keystore.p12
# server.ssl.key-store-password=akashcreations2026
# server.ssl.key-store-type=PKCS12
# server.ssl.key-alias=springboot
# server.port=8443
```

2. Uncomment HTTP port:
```properties
server.port=8181
```

3. Access via: `http://localhost:8181`

---

## Common Issues

| Error | Cause | Solution |
|-------|-------|----------|
| "Bad Request - requires TLS" | Using `http://` instead of `https://` | Use `https://localhost:8443` |
| "Keystore password incorrect" | Wrong password | Check `server.ssl.key-store-password` |
| "Connection refused" | App not running | Start the application |
| "Certificate error" | Self-signed cert | Accept browser warning (normal for dev) |
| "Port 8443 in use" | Another app using port | Stop other app or change port |

---

## Quick Test

1. **Start application:**
   ```bash
   mvnw.cmd spring-boot:run
   ```

2. **Open browser:**
   ```
   https://localhost:8443
   ```

3. **Accept security warning** (for self-signed certificate)

4. **You should see your application!** ✅

---

## Still Having Issues?

1. Check if keystore exists: `src/main/resources/keystore.p12`
2. Verify password matches in `application.properties`
3. Check application logs for specific errors
4. Try regenerating the certificate: `generate-ssl-certificate.bat`
