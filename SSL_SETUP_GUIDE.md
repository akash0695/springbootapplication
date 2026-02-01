# SSL/TLS Certificate Setup Guide

This guide explains how to add SSL/TLS encryption to your Spring Boot application for secure HTTPS connections.

## Table of Contents
1. [Development (Self-Signed Certificate)](#development-self-signed-certificate)
2. [Production (Let's Encrypt)](#production-lets-encrypt)
3. [Configuration](#configuration)
4. [Testing](#testing)

---

## Development (Self-Signed Certificate)

### Step 1: Generate Self-Signed Certificate

#### Option A: Using Java Keytool (Recommended for Development)

```bash
# Generate keystore with self-signed certificate (valid for 365 days)
keytool -genkeypair -alias springboot -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore src/main/resources/keystore.p12 -validity 365

# When prompted, enter:
# - Password: (choose a strong password, remember it!)
# - First and last name: localhost (or your domain)
# - Organizational unit: (optional)
# - Organization: (optional)
# - City: (optional)
# - State: (optional)
# - Country code: US (or your country)
```

**Important:** 
- Store the password securely - you'll need it in `application.properties`
- For localhost, use `localhost` as the name
- For domain, use your actual domain name

#### Option B: Using OpenSSL (Alternative)

```bash
# Generate private key
openssl genrsa -out server.key 2048

# Generate certificate signing request
openssl req -new -key server.key -out server.csr

# Generate self-signed certificate (valid for 365 days)
openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt

# Convert to PKCS12 format for Java
openssl pkcs12 -export -in server.crt -inkey server.key -out keystore.p12 -name springboot
```

### Step 2: Configure Spring Boot

Add SSL configuration to `application.properties`:

```properties
# SSL Configuration
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=YOUR_KEYSTORE_PASSWORD
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=springboot
server.port=8443

# Optional: Redirect HTTP to HTTPS
server.http.port=8181
```

### Step 3: Update Security Configuration

Update `WebSecurityConfig.java` to require HTTPS (optional for development):

```java
@Override
protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .requiresChannel()
            .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
            .requiresSecure()
        .and()
        // ... rest of your configuration
}
```

---

## Production (Let's Encrypt)

### Option 1: Let's Encrypt with Certbot (Recommended)

#### Step 1: Install Certbot

**On Ubuntu/Debian:**
```bash
sudo apt-get update
sudo apt-get install certbot
```

**On CentOS/RHEL:**
```bash
sudo yum install certbot
```

#### Step 2: Obtain Certificate

```bash
# Standalone mode (stops your app temporarily)
sudo certbot certonly --standalone -d yourdomain.com -d www.yourdomain.com

# Or webroot mode (doesn't stop your app)
sudo certbot certonly --webroot -w /var/www/html -d yourdomain.com -d www.yourdomain.com
```

Certificates will be stored in:
- Certificate: `/etc/letsencrypt/live/yourdomain.com/fullchain.pem`
- Private Key: `/etc/letsencrypt/live/yourdomain.com/privkey.pem`

#### Step 3: Convert to PKCS12 Format

```bash
# Convert PEM to PKCS12
sudo openssl pkcs12 -export \
    -in /etc/letsencrypt/live/yourdomain.com/fullchain.pem \
    -inkey /etc/letsencrypt/live/yourdomain.com/privkey.pem \
    -out /etc/letsencrypt/live/yourdomain.com/keystore.p12 \
    -name springboot \
    -password pass:YOUR_KEYSTORE_PASSWORD

# Set proper permissions
sudo chmod 600 /etc/letsencrypt/live/yourdomain.com/keystore.p12
sudo chown your-user:your-user /etc/letsencrypt/live/yourdomain.com/keystore.p12
```

#### Step 4: Configure Spring Boot

```properties
# SSL Configuration (Production)
server.ssl.key-store=/etc/letsencrypt/live/yourdomain.com/keystore.p12
server.ssl.key-store-password=YOUR_KEYSTORE_PASSWORD
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=springboot
server.port=8443

# HTTP to HTTPS redirect
server.http.port=8181
```

#### Step 5: Auto-Renewal Setup

Let's Encrypt certificates expire every 90 days. Set up auto-renewal:

```bash
# Test renewal
sudo certbot renew --dry-run

# Add to crontab (runs twice daily)
sudo crontab -e
# Add this line:
0 0,12 * * * certbot renew --quiet --deploy-hook "systemctl restart your-springboot-app"
```

### Option 2: Using Nginx as Reverse Proxy (Alternative)

If you're using Nginx as a reverse proxy, configure SSL in Nginx instead:

```nginx
server {
    listen 443 ssl http2;
    server_name yourdomain.com;

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

# Redirect HTTP to HTTPS
server {
    listen 80;
    server_name yourdomain.com;
    return 301 https://$server_name$request_uri;
}
```

---

## Configuration

### application.properties SSL Settings

```properties
# SSL Configuration
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD:changeit}
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=springboot
server.ssl.key-password=${SSL_KEY_PASSWORD:changeit}

# HTTPS Port
server.port=8443

# HTTP Port (for redirect)
server.http.port=8181

# Security Headers
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
```

### Environment Variables (Recommended for Production)

Instead of hardcoding passwords, use environment variables:

```bash
export SSL_KEYSTORE_PASSWORD=your-secure-password
export SSL_KEY_PASSWORD=your-secure-password
```

Then in `application.properties`:
```properties
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-password=${SSL_KEY_PASSWORD}
```

---

## Testing

### Test HTTPS Connection

```bash
# Test with curl
curl -k https://localhost:8443

# Test with browser
# Navigate to: https://localhost:8443
# Accept the security warning (for self-signed certificates)
```

### Verify Certificate

```bash
# Check certificate details
keytool -list -v -keystore src/main/resources/keystore.p12

# Check certificate expiration
openssl x509 -in server.crt -noout -dates
```

### Test SSL Configuration

```bash
# Test SSL/TLS connection
openssl s_client -connect localhost:8443 -showcerts
```

---

## Troubleshooting

### Common Issues

1. **"Keystore was tampered with, or password was incorrect"**
   - Verify the password in `application.properties` matches the keystore password
   - Check for typos in the password

2. **"Certificate doesn't match hostname"**
   - For localhost: Use `localhost` as the certificate name
   - For domain: Ensure certificate is issued for your domain

3. **"Connection refused"**
   - Check if the port is correct (8443 for HTTPS)
   - Verify firewall allows the port

4. **Browser shows "Not Secure"**
   - Self-signed certificates will always show this warning
   - Use Let's Encrypt for production to get trusted certificates

### Security Best Practices

1. ✅ **Use strong passwords** for keystores
2. ✅ **Store passwords in environment variables**, not in code
3. ✅ **Use Let's Encrypt** for production (free, trusted certificates)
4. ✅ **Enable HSTS** (HTTP Strict Transport Security)
5. ✅ **Keep certificates updated** (auto-renewal for Let's Encrypt)
6. ✅ **Use TLS 1.2+** (disable older protocols)
7. ✅ **Enable secure cookies** (`secure` and `httpOnly` flags)

---

## Additional Resources

- [Spring Boot SSL Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.webserver.configure-ssl)
- [Let's Encrypt Documentation](https://letsencrypt.org/docs/)
- [Certbot Documentation](https://certbot.eff.org/docs/)

---

## Quick Start Scripts

See `generate-ssl-certificate.bat` (Windows) or `generate-ssl-certificate.sh` (Linux/Mac) for automated certificate generation.
