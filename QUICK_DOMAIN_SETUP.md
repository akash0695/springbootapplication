# Quick Domain Setup Guide

## 🚀 Connect Your Domain to Spring Boot Application

### Prerequisites
- ✅ Domain name registered (e.g., `yourdomain.com`)
- ✅ Server with public IP address
- ✅ DNS access to configure A records

---

## Step-by-Step Setup

### 1️⃣ Configure DNS (5 minutes)

Point your domain to your server IP:

```
Type: A
Name: @ (or leave blank)
Value: YOUR_SERVER_IP
TTL: 3600
```

Also add `www` subdomain:
```
Type: A
Name: www
Value: YOUR_SERVER_IP
TTL: 3600
```

**Verify DNS:**
```bash
nslookup yourdomain.com
# Should return your server IP
```

---

### 2️⃣ Get SSL Certificate (10 minutes)

#### Using Let's Encrypt (Free):

```bash
# Install Certbot
sudo apt-get install certbot  # Ubuntu/Debian
# OR
sudo yum install certbot      # CentOS/RHEL

# Stop your Spring Boot app temporarily
# Then get certificate:
sudo certbot certonly --standalone -d yourdomain.com -d www.yourdomain.com

# Convert to PKCS12 for Spring Boot:
sudo openssl pkcs12 -export \
    -in /etc/letsencrypt/live/yourdomain.com/fullchain.pem \
    -inkey /etc/letsencrypt/live/yourdomain.com/privkey.pem \
    -out /etc/letsencrypt/live/yourdomain.com/keystore.p12 \
    -name springboot \
    -password pass:YOUR_PASSWORD

# Set permissions
sudo chmod 600 /etc/letsencrypt/live/yourdomain.com/keystore.p12
```

---

### 3️⃣ Update application.properties

```properties
# Domain SSL Configuration
server.ssl.enabled=true
server.ssl.key-store=/etc/letsencrypt/live/yourdomain.com/keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=springboot

# Use standard HTTPS port
server.port=443

# Optional: HTTP redirect
server.http.port=80

# Allow external connections
server.address=0.0.0.0
```

---

### 4️⃣ Set Environment Variable

```bash
# Linux/Mac
export SSL_KEYSTORE_PASSWORD=your-password

# Windows
set SSL_KEYSTORE_PASSWORD=your-password
```

---

### 5️⃣ Open Firewall Ports

```bash
# Ubuntu/Debian
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp

# CentOS/RHEL
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --reload
```

---

### 6️⃣ Start Application

```bash
mvnw.cmd spring-boot:run
# OR
java -jar target/springboot-0.0.1-SNAPSHOT.jar
```

---

### 7️⃣ Access Your Domain

Open browser:
```
https://yourdomain.com
```

✅ Should show **green lock** (trusted certificate)
✅ No security warnings
✅ Full HTTPS encryption

---

## ⚠️ Important Notes

### Port 443 Requires Root (Linux)
If you get "Permission denied" on port 443:

**Option 1:** Use reverse proxy (Nginx) - Recommended
- Nginx handles SSL on port 443
- Spring Boot runs on port 8181 (HTTP)
- See `DOMAIN_SSL_SETUP.md` for Nginx config

**Option 2:** Use port 8443
```properties
server.port=8443
```
Access: `https://yourdomain.com:8443`

**Option 3:** Run as root (Not recommended)
```bash
sudo java -jar app.jar
```

---

## 🔄 Auto-Renewal Setup

Let's Encrypt certificates expire every 90 days:

```bash
# Add to crontab
sudo crontab -e

# Add this line (runs twice daily):
0 0,12 * * * certbot renew --quiet --deploy-hook "systemctl restart your-app"
```

---

## 📋 Configuration Comparison

| Setting | Localhost | Domain |
|---------|-----------|--------|
| Port | `8443` | `443` |
| Keystore | `classpath:keystore.p12` | `/etc/letsencrypt/.../keystore.p12` |
| Certificate | Self-signed | Let's Encrypt |
| Address | (localhost) | `0.0.0.0` |
| URL | `https://localhost:8443` | `https://yourdomain.com` |

---

## ✅ Checklist

- [ ] DNS configured (A record)
- [ ] Let's Encrypt certificate obtained
- [ ] Certificate converted to PKCS12
- [ ] `application.properties` updated
- [ ] Environment variable set
- [ ] Firewall ports opened
- [ ] Application restarted
- [ ] Domain accessible via HTTPS
- [ ] Auto-renewal configured

---

## 🆘 Troubleshooting

| Issue | Solution |
|-------|----------|
| DNS not resolving | Wait 24-48 hours for DNS propagation |
| Connection refused | Check firewall, verify port 443 is open |
| Certificate error | Verify certificate is for correct domain |
| Port 443 permission denied | Use Nginx reverse proxy or port 8443 |

---

## 📚 More Information

See `DOMAIN_SSL_SETUP.md` for detailed instructions and advanced configuration.
