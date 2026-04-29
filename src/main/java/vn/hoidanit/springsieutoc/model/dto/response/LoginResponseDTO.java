package vn.hoidanit.springsieutoc.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

	private String accessToken;

	private String refreshToken;

	private String tokenType = "Bearer";

	private UserLogin user;

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserLogin {
		private int id;
		private String username;
		private String role;
	}
}
