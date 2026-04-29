package vn.hoidanit.springsieutoc.model.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO {

	@NotBlank(message = "content không được để trống")
	private String content;

	@NotNull(message = "userId không được để trống")
	private InputUser user;

	@NotNull(message = "postId không được để trống")
	private inputPost post;

	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InputUser {
		@NotNull(message = "user.id không được để trống")
		private int id;
	}

	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class inputPost {
		@NotNull(message = "post.id không được để trống")
		private Long id;
	}

}
