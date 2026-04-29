package vn.hoidanit.springsieutoc.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.springsieutoc.model.dto.response.LoginResponseDTO.UserLogin;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeTokenRespone {

	private String accessToken;

	private String refreshToken;

	private String tokenType = "Bearer";

	private UserLogin user;

}
