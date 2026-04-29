package vn.hoidanit.springsieutoc.model.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserFilterRequestDTO {

	private String name;

	private String email;

	private String address;

	private String roleName;
}
