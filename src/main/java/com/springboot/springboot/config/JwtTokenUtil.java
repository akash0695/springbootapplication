package com.springboot.springboot.config;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
// Akash Gupta
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;
	
	public static final long JWT_TOKEN_VALIDITY = 5*60*60;

	@Value("${jwt.secret:}")
	private String secret;

	private SecretKey signingKey;

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	@PostConstruct
	private void initSigningKey() {
		try {
			if (secret != null && !secret.isBlank()) {
				byte[] keyBytes = null;
				// try Base64 decode first
				try {
					keyBytes = Base64.getDecoder().decode(secret);
				} catch (IllegalArgumentException ex) {
					keyBytes = secret.getBytes(StandardCharsets.UTF_8);
				}

				// HS512 requires a key size >= 512 bits (64 bytes)
				if (keyBytes != null && keyBytes.length >= 64) {
					signingKey = Keys.hmacShaKeyFor(keyBytes);
					return;
				} else {
					logger.warn("Configured jwt.secret is too short ({} bytes). Generating a secure random key for HS512.", keyBytes == null ? 0 : keyBytes.length);
				}
			} else {
				logger.warn("No jwt.secret configured; generating a secure random key for HS512.");
			}
		} catch (Exception e) {
			logger.warn("Failed to use configured jwt.secret: {}", e.getMessage());
		}

		// fallback: generate a secure random key for HS512
		signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		// Optionally expose the generated key in Base64 (helpful for development)
		String generated = Base64.getEncoder().encodeToString(signingKey.getEncoded());
		logger.info("Generated JWT HS512 secret (base64) for runtime use: {}", generated);
	}

	private SecretKey getSigningKey() {
		return signingKey;
	}

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Boolean getIsAdminFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("isAdmin", Boolean.class);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	public String generateToken(UserDetails userDetails, boolean isAdmin) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("isAdmin", isAdmin);
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
