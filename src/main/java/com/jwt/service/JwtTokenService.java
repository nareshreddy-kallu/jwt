package com.jwt.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenService {

	@Value("${token.key}")
	String secret;

	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<>();

		return createToken(claims, email);
	}

	public String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 4 * 60 * 60 * 1000))
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public Date getExpirationDate(String token) {

		return getClaimFromToken(token, Claims::getExpiration);
	}

	public String getUserNameFromToken(String token) {

		return getClaimFromToken(token, Claims::getSubject);
	}

	public boolean tokenExpired(String token) {

		final Date date = getExpirationDate(token);
		return date.before(new Date());
	}

	public boolean valideToken(String token) {

		String clims = getUserNameFromToken(token).toString();
		System.out.println(clims);
		System.out.println(getAllClaimsFromToken(token));

		if (getAllClaimsFromToken(token).getSubject().equals("p@gmail.com") && !tokenExpired(token)) {
			getAllClaimsFromToken(token).setExpiration(new Date(System.currentTimeMillis()+10 * 60 * 60 * 1000));
			return true;
		} else {
			return false;
		}

	}

}
