package com.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.request.TokenRequest;
import com.jwt.response.TokenResponse;
import com.jwt.service.JwtTokenService;

@RestController
public class JwtTokenConrtoller {

	@Autowired
	private JwtTokenService jwtTokenService;

	// String
	// token="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XG4gICAgXCJ1c2VyTmFtZVwiOlwibkBnbWFpbC5jb21cIlxufSIsImV4cCI6MTY1MDg3ODIzMCwiaWF0IjoxNjUwODYzODMwfQ.O6xOHdsKaosJTBkPjCNjkw8KDLQq7iFCjXTDYuN89pI";

	@PostMapping("/token")
	public String secretKey(@RequestBody TokenRequest tokenRequest) {
		return jwtTokenService.generateToken(tokenRequest.getUserName());
	}

	@PostMapping("/tokenAuthantication")
	public ResponseEntity<TokenResponse> tokenValidation(@RequestHeader String Autharization) {
		TokenResponse response = null;
		try {
			if (jwtTokenService.valideToken(Autharization)) {
				response = new TokenResponse();

				response.setMessage("Hello User");
				return new ResponseEntity<TokenResponse>(response, (HttpStatus.OK));

			} else {
				response = new TokenResponse();
				String claims = jwtTokenService.getUserNameFromToken(Autharization);
				System.out.println(claims);

				response.setMessage("Token Expired!");
				return new ResponseEntity<TokenResponse>(response, HttpStatus.FORBIDDEN);
			}

		} catch (Exception e) {
			response = new TokenResponse();
			e.printStackTrace();

			response.setMessage("Somting went wrong!!" + e);
			return new ResponseEntity<TokenResponse>(response, HttpStatus.FORBIDDEN);
		}

	}
}
