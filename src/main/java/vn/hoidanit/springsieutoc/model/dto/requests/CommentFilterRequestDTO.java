package vn.hoidanit.springsieutoc.model.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentFilterRequestDTO {

	private Long userId;

	private Long postId;

	private String title;

}
