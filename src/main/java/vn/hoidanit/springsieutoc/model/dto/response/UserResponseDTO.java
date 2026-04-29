package vn.hoidanit.springsieutoc.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

	private int id;

	private String name;

	private String email;

	private String address;

	private RoleResponseDTO role;

}
