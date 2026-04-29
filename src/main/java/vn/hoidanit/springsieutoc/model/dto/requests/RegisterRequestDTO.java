package vn.hoidanit.springsieutoc.model.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

	@NotBlank(message = "email khong duoc de trong")
	@Email(message = "email khong hop le")
	private String email;

	@NotBlank(message = "name khong duoc de trong")
	private String name;

	@NotBlank(message = "password khong duoc de trong")
	@Size(min = 6, message = "password phai co it nhat 6 ky tu")
	private String password;

	@NotBlank(message = "address khong duoc de trong")
	private String address;
}
