package vn.hoidanit.springsieutoc.model.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostFilterRequestDTO {

	private Long userId;

	private String tagName;
}
