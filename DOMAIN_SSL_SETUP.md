# Setting Up SSL for Domain URL

This guide explains how to configure your Spring Boot application to work with a domain name (e.g., `https://yourdomain.com`) instead of just `localhost`.

## Prerequisites

1. ✅ Domain name registered (e.g., `yourdomain.com`)
2. ✅ DNS configured to point to your server IP
3. ✅ Server accessible from the internet
4. ✅ Port 443 (HTTPS) open in firewall

---

## Step 1: Configure DNS

Point your domain to your server's IP address:

### DNS Records Needed:
```
Type    Name    Value           TTL
A       @       YOUR_SERVER_IP  3600
A       www     YOUR_SERVER_IP  3600
```

**Example:**
- Domain: `akashcreations.com`
- Server IP: `123.45.67.89`
- DNS: `akashcreations.com` → `123.45.67.89`
- DNS: `www.akashcreations.com` → `123.45.67.89`

**Verify DNS:**
```bash
# Check if DNS is configured
nslookup yourdomain.com
ping yourdomain.com
```

---

## Step 2: Get SSL Certificate for Domain

### Option A: Let's Encrypt (Free, Recommended) ✅

#### On Your Server:

1. **Install Certbot:**
   ```bash
   # Ubuntu/Debian
   sudo apt-get update
   sudo apt-get install certbot
   
   # CentOS/RHEL
   sudo yum install certbot
   ```

2. **Stop your Spring Boot application** (temporarily)

3. **Get Certificate:**
   ```bash
   sudo certbot certonly --standalone -d yourdomain.com -d www.yourdomain.com
   ```
   
   Replace `yourdomain.com` with your actual domain.

4. **Enter email** when prompted (for renewal notifications)

5. **Certificates will be saved to:**
   - Certificate: `/etc/letsencrypt/live/yourdomain.com/fullchain.pem`
   - Private Key: `/etc/letsencrypt/live/yourdomain.com/privkey.pem`

6. **Convert to PKCS12 format:**
   ```bash
   sudo openssl pkcs12 -export \
       -in /etc/letsencrypt/live/yourdomain.com/fullchain.pem \
       -inkey /etc/letsencrypt/live/yourdomain.com/privkey.pem \
       -out /etc/letsencrypt/live/yourdomain.com/keystore.p12 \
       -name springboot \
       -password pass:YOUR_SECURE_PASSWORD
   
   # Set permissions
   sudo chmod 600 /etc/letsencrypt/live/yourdomain.com/keystore.p12
   sudo chown $USER:$USER /etc/letsencrypt/live/yourdomain.com/keystore.p12
   ```

### Option B: Self-Signed Certificate (Development Only)

**⚠️ Warning:** Self-signed certificates will show "Not Secure" in browsers for domains.

```bash
# Generate certificate for your domain
keytool -genkeypair \
    -alias springboot \
    -keyalg RSA \
    -keysize 2048 \
    -storetype PKCS12 \
    -keystore keystore.p12 \
    -validity 365 \
    -dname "CN=yourdomain.com, OU=Development, O=Your Company, L=City, ST=State, C=US"
```

---

## Step 3: Update application.properties

Update your `application.properties` for domain access:

```properties
# ============================================
# Domain SSL Configuration
# ============================================

# SSL Configuration (Production with Let's Encrypt)
server.ssl.enabled=true
server.ssl.key-store=/etc/letsencrypt/live/yourdomain.com/keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=springboot

# HTTPS Port (standard HTTPS port)
server.port=443

# Optional: HTTP to HTTPS redirect
server.http.port=80

# Server address (optional - binds to all interfaces)
server.address=0.0.0.0
```

**Important Changes:**
- `server.port=443` (standard HTTPS port, instead of 8443)
- `server.ssl.key-store` points to Let's Encrypt certificate
- `server.address=0.0.0.0` allows external connections

---

## Step 4: Set Environment Variable

Set the keystore password as an environment variable (more secure):

```bash
# Linux/Mac
export SSL_KEYSTORE_PASSWORD=your-secure-password

# Windows (Command Prompt)
set SSL_KEYSTORE_PASSWORD=your-secure-password

# Windows (PowerShell)
$env:SSL_KEYSTORE_PASSWORD="your-secure-password"
```

Or create a `.env` file (if using Spring Boot 2.4+):
```properties
SSL_KEYSTORE_PASSWORD=your-secure-password
```

---

## Step 5: Configure Firewall

Open ports 80 (HTTP) and 443 (HTTPS):

```bash
# Ubuntu/Debian (UFW)
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw reload

# CentOS/RHEL (firewalld)
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --reload

# Or using iptables
sudo iptables -A INPUT -p tcp --dport 80 -j ACCEPT
sudo iptables -A INPUT -p tcp --dport 443 -j ACCEPT
```

---

## Step 6: Update Security Configuration (Optional)

If you want to force HTTPS for all connections:

```java
// In WebSecurityConfig.java
@Override
protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .requiresChannel()
            .anyRequest().requiresSecure()  // Force HTTPS
        .and()
        // ... rest of your configuration
}
```

---

## Step 7: Test Domain Access

1. **Start your application:**
   ```bash
   mvnw.cmd spring-boot:run
   # Or
   java -jar target/springboot-0.0.1-SNAPSHOT.jar
   ```

2. **Access via domain:**
   ```
   https://yourdomain.com
   ```

3. **Verify SSL:**
   - Should show green lock (with Let's Encrypt)
   - No security warnings
   - Certificate shows your domain name

---

## Step 8: Auto-Renewal (Let's Encrypt)

Let's Encrypt certificates expire every 90 days. Set up auto-renewal:

```bash
# Test renewal
sudo certbot renew --dry-run

# Add to crontab (runs twice daily)
sudo crontab -e

# Add this line:
0 0,12 * * * certbot renew --quiet --deploy-hook "systemctl restart your-springboot-app"
```

**Or create a renewal script:**
```bash
#!/bin/bash
# /usr/local/bin/renew-ssl.sh

certbot renew --quiet
if [ $? -eq 0 ]; then
    # Convert new certificate to PKCS12
    openssl pkcs12 -export \
        -in /etc/letsencrypt/live/yourdomain.com/fullchain.pem \
        -inkey /etc/letsencrypt/live/yourdomain.com/privkey.pem \
        -out /etc/letsencrypt/live/yourdomain.com/keystore.p12 \
        -name springboot \
        -password pass:$SSL_KEYSTORE_PASSWORD
    
    # Restart application
    systemctl restart your-springboot-app
fi
```

---

## Alternative: Using Reverse Proxy (Nginx/Apache)

Instead of configuring SSL in Spring Boot, you can use Nginx as a reverse proxy:

### Nginx Configuration:

```nginx
# HTTP to HTTPS redirect
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;
    return 301 https://$server_name$request_uri;
}

# HTTPS server
server {
    listen 443 ssl http2;
    server_name yourdomain.com www.yourdomain.com;

    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;
    
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    location / {
        proxy_pass http://localhost:8181;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

**Benefits:**
- ✅ SSL handled by Nginx
- ✅ Spring Boot runs on HTTP (port 8181)
- ✅ Easier certificate management
- ✅ Better performance

---

## Configuration Summary

### For Domain Access:

| Setting | Value | Description |
|---------|-------|-------------|
| `server.port` | `443` | Standard HTTPS port |
| `server.ssl.enabled` | `true` | Enable SSL |
| `server.ssl.key-store` | `/etc/letsencrypt/.../keystore.p12` | Let's Encrypt certificate |
| `server.address` | `0.0.0.0` | Allow external connections |
| `server.http.port` | `80` | HTTP redirect port |

### For Localhost (Development):

| Setting | Value | Description |
|---------|-------|-------------|
| `server.port` | `8443` | Custom HTTPS port |
| `server.ssl.enabled` | `true` | Enable SSL |
| `server.ssl.key-store` | `classpath:keystore.p12` | Self-signed certificate |
| `server.address` | (not set) | Localhost only |

---

## Troubleshooting

### Issue: "Connection refused"
- **Check:** Firewall allows port 443
- **Check:** DNS points to correct IP
- **Check:** Application is running

### Issue: "Certificate doesn't match domain"
- **Check:** Certificate was generated for correct domain
- **Check:** DNS is configured correctly
- **Solution:** Regenerate certificate with correct domain

### Issue: "Port 443 requires root"
- **Solution:** Use port 8443 and configure reverse proxy
- **Or:** Run Spring Boot as root (not recommended)
- **Or:** Use `authbind` or `setcap` to allow binding to port 443

### Issue: Certificate renewal fails
- **Check:** Application is stopped during renewal
- **Check:** Port 80 is accessible for Let's Encrypt validation
- **Solution:** Use webroot mode instead of standalone

---

## Quick Checklist

- [ ] Domain DNS configured (A record pointing to server IP)
- [ ] Let's Encrypt certificate obtained
- [ ] Certificate converted to PKCS12 format
- [ ] `application.properties` updated for domain
- [ ] Environment variable set for password
- [ ] Firewall allows ports 80 and 443
- [ ] Application restarted
- [ ] Domain accessible via `https://yourdomain.com`
- [ ] Auto-renewal configured

---

## Example: Complete Domain Setup

```properties
# application.properties for domain
server.ssl.enabled=true
server.ssl.key-store=/etc/letsencrypt/live/yourdomain.com/keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=springboot
server.port=443
server.http.port=80
server.address=0.0.0.0
```

**Access your application at:**
```
https://yourdomain.com
```

---

## Next Steps

1. ✅ Configure DNS for your domain
2. ✅ Get Let's Encrypt certificate
3. ✅ Update `application.properties`
4. ✅ Set environment variables
5. ✅ Configure firewall
6. ✅ Test domain access
7. ✅ Set up auto-renewal

Your application will be accessible via your domain with trusted SSL! 🎉
