#!/bin/bash

echo "========================================"
echo "SSL Certificate Generator for Spring Boot"
echo "========================================"
echo ""

KEYSTORE_PATH="src/main/resources/keystore.p12"
KEYSTORE_ALIAS="springboot"
VALIDITY_DAYS=365
KEY_SIZE=2048

echo "This script will generate a self-signed SSL certificate for development."
echo ""
echo "IMPORTANT: This creates a self-signed certificate suitable for development only."
echo "For production, use Let's Encrypt or a trusted CA certificate."
echo ""

read -p "Enter domain name (or 'localhost' for local development): " DOMAIN
DOMAIN=${DOMAIN:-localhost}

echo ""
echo "Generating certificate for: $DOMAIN"
echo "Keystore will be saved to: $KEYSTORE_PATH"
echo ""

read -sp "Enter keystore password (remember this!): " KEYSTORE_PASSWORD
echo ""

if [ -z "$KEYSTORE_PASSWORD" ]; then
    echo "ERROR: Password cannot be empty"
    exit 1
fi

echo ""
echo "Generating certificate..."
echo ""

keytool -genkeypair \
    -alias "$KEYSTORE_ALIAS" \
    -keyalg RSA \
    -keysize $KEY_SIZE \
    -storetype PKCS12 \
    -keystore "$KEYSTORE_PATH" \
    -validity $VALIDITY_DAYS \
    -storepass "$KEYSTORE_PASSWORD" \
    -keypass "$KEYSTORE_PASSWORD" \
    -dname "CN=$DOMAIN, OU=Development, O=Spring Boot, L=City, ST=State, C=US"

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Failed to generate certificate"
    exit 1
fi

echo ""
echo "========================================"
echo "Certificate generated successfully!"
echo "========================================"
echo ""
echo "Keystore location: $KEYSTORE_PATH"
echo "Alias: $KEYSTORE_ALIAS"
echo "Password: [hidden]"
echo ""
echo "Next steps:"
echo "1. Update application.properties with:"
echo "   server.ssl.key-store=classpath:keystore.p12"
echo "   server.ssl.key-store-password=$KEYSTORE_PASSWORD"
echo "   server.ssl.key-store-type=PKCS12"
echo "   server.ssl.key-alias=$KEYSTORE_ALIAS"
echo "   server.port=8443"
echo ""
echo "2. Restart your Spring Boot application"
echo "3. Access your app at: https://$DOMAIN:8443"
echo ""
echo "WARNING: Self-signed certificates will show a security warning in browsers."
echo "This is normal for development. Use Let's Encrypt for production."
echo ""
