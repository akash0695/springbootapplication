# SSL Quick Start Guide

## 🚀 Development Setup (5 Minutes)

### Step 1: Generate Certificate
```bash
# Windows
generate-ssl-certificate.bat

# Linux/Mac
chmod +x generate-ssl-certificate.sh
./generate-ssl-certificate.sh
```

### Step 2: Update application.properties
Add these lines to `src/main/resources/application.properties`:

```properties
# SSL Configuration
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=YOUR_PASSWORD
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=springboot
server.port=8443
```

### Step 3: Restart Application
```bash
mvnw.cmd spring-boot:run
```

### Step 4: Test
Open browser: `https://localhost:8443`
- Accept the security warning (normal for self-signed certificates)

---

## 🌐 Production Setup (Let's Encrypt)

### Step 1: Install Certbot
```bash
# Ubuntu/Debian
sudo apt-get install certbot

# CentOS/RHEL
sudo yum install certbot
```

### Step 2: Get Certificate
```bash
sudo certbot certonly --standalone -d yourdomain.com
```

### Step 3: Convert to PKCS12
```bash
sudo openssl pkcs12 -export \
    -in /etc/letsencrypt/live/yourdomain.com/fullchain.pem \
    -inkey /etc/letsencrypt/live/yourdomain.com/privkey.pem \
    -out /etc/letsencrypt/live/yourdomain.com/keystore.p12 \
    -name springboot \
    -password pass:YOUR_PASSWORD
```

### Step 4: Update application.properties
```properties
server.ssl.key-store=/etc/letsencrypt/live/yourdomain.com/keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=springboot
server.port=8443
```

### Step 5: Set Environment Variable
```bash
export SSL_KEYSTORE_PASSWORD=your-password
```

### Step 6: Auto-Renewal
```bash
# Add to crontab
0 0,12 * * * certbot renew --quiet --deploy-hook "systemctl restart your-app"
```

---

## 📋 Configuration Options

### Basic SSL
```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=PKCS12
server.port=8443
```

### With HTTP Redirect
```properties
# HTTPS
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=password

# HTTP (redirects to HTTPS)
server.http.port=8181
```

### Security Headers
```properties
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
```

---

## ✅ Checklist

- [ ] Certificate generated
- [ ] Keystore password saved securely
- [ ] application.properties updated
- [ ] Application restarted
- [ ] HTTPS connection tested
- [ ] Browser warning accepted (dev only)
- [ ] Production certificate obtained (if deploying)

---

## 🔧 Troubleshooting

| Issue | Solution |
|-------|----------|
| "Keystore password incorrect" | Check password in application.properties |
| "Certificate doesn't match hostname" | Use correct domain in certificate |
| "Connection refused" | Check port (8443) and firewall |
| "Not Secure" warning | Normal for self-signed, use Let's Encrypt for production |

---

## 📚 More Information

See `SSL_SETUP_GUIDE.md` for detailed instructions and advanced configuration.
