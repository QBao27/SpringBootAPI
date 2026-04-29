/*
 * Author: Hỏi Dân IT - @hoidanit 
 *
 * This source code is developed for the course
 * "Java Spring Siêu Tốc - Tự Học Java Spring Từ Số 0 Dành Cho Beginners từ A tới Z".
 * It is intended for educational purposes only.
 * Unauthorized distribution, reproduction, or modification is strictly prohibited.
 *
 * Copyright (c) 2025 Hỏi Dân IT. All Rights Reserved.
 */

package vn.hoidanit.springsieutoc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.hoidanit.springsieutoc.config.JwtService;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.RefreshToken;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.requests.LoginRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.requests.RegisterRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.response.ExchangeTokenRespone;
import vn.hoidanit.springsieutoc.model.dto.response.LoginResponseDTO;
import vn.hoidanit.springsieutoc.service.RefreshTokenService;
import vn.hoidanit.springsieutoc.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final JwtService jwtService;

	private final AuthenticationManager authenticationManager;

	private final UserService userService;

	private final RefreshTokenService refreshTokenService;

	@PostMapping("/auth/login")
	public ResponseEntity<?> postLogin(@Valid @RequestBody LoginRequestDTO request) {

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getUsername(),
				request.getPassword());

		Authentication authentication = authenticationManager.authenticate(authToken);

		User currentUser = userService.findUserByEmail(authentication.getName());

		String accessToken = jwtService.createToken(authentication, currentUser.getId());

		String refreshToken = jwtService.createRefeshToken(currentUser);

		LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

		loginResponseDTO.setAccessToken(accessToken);

		loginResponseDTO.setUser(new LoginResponseDTO.UserLogin(currentUser.getId(), authentication.getName(),
				authentication.getAuthorities().iterator().next().getAuthority()));

		loginResponseDTO.setRefreshToken(refreshToken);

		// set cookie
		// @formatter:off
		ResponseCookie rfCookie = ResponseCookie
				.from("refreshToken", refreshToken)
				.httpOnly(true)
				.secure(false) // set true if using https
				.path("/")
				.maxAge(7 * 24 * 60 * 60) // 7 days
				.build();
		
		ApiResponse<LoginResponseDTO> finalData = new ApiResponse<>(
				HttpStatus.OK, "", loginResponseDTO,""
				);

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, rfCookie.toString()).body(finalData);	
	}

	@PostMapping("/auth/refresh")
	public ResponseEntity<?> refreshToken(@RequestParam("token") String refreshToken) {

		ExchangeTokenRespone exchangeTokenRespone = jwtService.exchangeRefreshToken(refreshToken);

		return ApiResponse.success(exchangeTokenRespone);

	}
	
	@PostMapping("/auth/refresh-with-cookie")
	public ResponseEntity<?> postRefreshTokenWithCookie(@CookieValue(required = false) String refreshToken) {
		
		ExchangeTokenRespone exchangeTokenRespone = jwtService.exchangeRefreshToken(refreshToken);
		
		// set cookie
				// @formatter:off
				ResponseCookie rfCookie = ResponseCookie
						.from("refreshToken", exchangeTokenRespone.getRefreshToken())
						.httpOnly(true)
						.secure(false) // set true if using https
						.path("/")
						.maxAge(7 * 24 * 60 * 60) // 7 days
						.build();
				
				ApiResponse<ExchangeTokenRespone> finalData = new ApiResponse<>(
						HttpStatus.OK, "", exchangeTokenRespone ,""
						);

				return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, rfCookie.toString()).body(finalData);	
	}
	
	@GetMapping("/auth/account")
	public ResponseEntity<?> getAccount() {
		
		Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
		
		Jwt jwt = (Jwt) authentication.getPrincipal();
		
		String UserId = jwt.getClaimAsString("userId");
		String ussername = jwt.getSubject();
		String scope = jwt.getClaimAsString("scope");
		
		LoginResponseDTO.UserLogin userLogin = new LoginResponseDTO.UserLogin();
		userLogin.setId(Integer.valueOf(UserId));
		userLogin.setUsername(ussername);
		userLogin.setRole(scope);
		
		return ApiResponse.success(userLogin);
	}
	
	@PostMapping("/auth/logout")
	public ResponseEntity<?> postLogout(@AuthenticationPrincipal Jwt jwt, @CookieValue(required = false) String refreshToken) {
		
//		String UserId = jwt.getClaimAsString("userId");
//		String ussername = jwt.getSubject();
		
		RefreshToken currentTokenInDB = refreshTokenService.findByToken(refreshToken);
		
		refreshTokenService.deleteById(currentTokenInDB.getId());
		
		
		// clear cookie
		ResponseCookie rfCookie = ResponseCookie
				.from("refreshToken", null)
				.httpOnly(true)
				.secure(false) // set true if using https
				.path("/")
				.maxAge(0) // expire immediately
				.build();
		
		ApiResponse<String> finalData = new ApiResponse<>(
				HttpStatus.OK, "", "ok" ,"deleted"
				);
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, rfCookie.toString()).body(finalData);
	}
	
	@PostMapping("/auth/register")
	public ResponseEntity<?> postRegister(@Valid @RequestBody RegisterRequestDTO request) {
		
		userService.regsiterUser(request);
		
		return ApiResponse.success("User registered successfully");
	}

}
