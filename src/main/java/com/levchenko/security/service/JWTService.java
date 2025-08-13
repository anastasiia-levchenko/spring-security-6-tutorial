package com.levchenko.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JWTService
{
	private String secretKey = "fj2847";

	public JWTService()
	{
		try
		{
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		}
		catch (final NoSuchAlgorithmException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	public String generateToken(final String username)
	{
		final Map<String, Object> claims = new HashMap<>();

		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
				.and()
				.signWith(getKey())
				.compact();
	}

	private SecretKey getKey()
	{
		final byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUsername(final String token)
	{
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(final String token, final Function<Claims, T> claimResolver)
	{
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(final String token)
	{
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public boolean validateToken(final String token, final UserDetails userDetails)
	{
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(final String token)
	{
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(final String token)
	{
		return extractClaim(token, Claims::getExpiration);
	}

}
