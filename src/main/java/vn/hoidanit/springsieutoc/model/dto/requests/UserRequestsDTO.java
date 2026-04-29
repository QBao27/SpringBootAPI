package vn.hoidanit.springsieutoc.model.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.springsieutoc.model.dto.response.RoleResponseDTO;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestsDTO {
	private int id;

	@NotBlank(message = "name không được để trống")
	private String name;

	@NotBlank(message = "Address không được để trống")
	private String address;

	private RoleResponseDTO role;
}
