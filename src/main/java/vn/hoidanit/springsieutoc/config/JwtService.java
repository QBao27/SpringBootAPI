package vn.hoidanit.springsieutoc.config;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.springsieutoc.model.RefreshToken;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.response.ExchangeTokenRespone;
import vn.hoidanit.springsieutoc.model.dto.response.LoginResponseDTO;
import vn.hoidanit.springsieutoc.service.RefreshTokenService;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final JwtEncoder jwtEncoder;

	@Value("${hoidanit.jwt.access-token-validity-in-seconds}")
	private Long accessTokenExpiration;

	@Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
	private Long refreshTokenExpiration;

	@Value("${hoidanit.jwt.base64-secret}")
	private String jwtKey;

	private final RefreshTokenService refreshTokenService;

	public String getScope(Authentication authentication) {
		if (authentication != null) {
			// ghép các quyền thành 1 string: "ROLE_USER ROLE_ADMIN"
			String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.joining(" "));
			return scope;
		}
		return "UNKNOWN";
	}

	// import key from .env
	public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

	public String createToken(Authentication authentication, int userId) {
		Instant now = Instant.now();
		Instant validity = now.plus(accessTokenExpiration, ChronoUnit.SECONDS);
		String scope = getScope(authentication);
		// @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim("userId", userId)
            .claim("scope",scope)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
	}
	
	public String generateSecureToken() {
	    byte[] randomBytes = new byte[64]; // 512 bits
	    SecureRandom secureRandom = new SecureRandom();
	    secureRandom.nextBytes(randomBytes);
	    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
	}
	
	public String createRefeshToken(User user) {
		Instant now = Instant.now();
		Instant validity = now.plus(refreshTokenExpiration, ChronoUnit.SECONDS);
		
		String token = generateSecureToken();
		
		RefreshToken refreshToken = new RefreshToken();
		
		refreshToken.setCreatedAt(now);
		refreshToken.setExpiredAt(validity);
		refreshToken.setToken(token);
		refreshToken.setUser(user);
		
		refreshTokenService.createRefreshToken(refreshToken);
		
		return token;
	}
	
	public ExchangeTokenRespone exchangeRefreshToken(String inputToken) {	
	
		RefreshToken currentRefreshToken = refreshTokenService.findByToken(inputToken);
		
		Instant now = Instant.now();
		if(now.isAfter(currentRefreshToken.getExpiredAt())) {
			refreshTokenService.deleteById(currentRefreshToken.getId());
			throw new RuntimeException("Refresh token expired");
		}
		
		User currentUser = currentRefreshToken.getUser();
		
		String newRefreshToken = createRefeshToken(currentUser);
		
		Instant accessTokenValidity = now.plus(accessTokenExpiration, ChronoUnit.SECONDS);
		
		String scope = "ROLE_" + currentUser.getRole().getName();
		
		// @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(accessTokenValidity)
            .subject(currentUser.getName())
            .claim("userId", currentUser.getId())
            .claim("scope",scope)
            .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        
        String accessToken = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        
        ExchangeTokenRespone exchangeTokenRespone = new ExchangeTokenRespone();
        exchangeTokenRespone.setAccessToken(accessToken);
        exchangeTokenRespone.setRefreshToken(newRefreshToken);
        exchangeTokenRespone.setUser(new LoginResponseDTO.UserLogin(currentUser.getId(), currentUser.getEmail(), scope));
        
        refreshTokenService.deleteById(currentRefreshToken.getId());
        
        return exchangeTokenRespone;
		
	}


}
