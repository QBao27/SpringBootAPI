package vn.hoidanit.springsieutoc.model.dto.requests;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class PostRequestsDTO {
	@NotBlank(message = "title không được để trống")
	private String title;

	@NotBlank(message = "content không được để trống")
	private String content;

	@NotNull(message = "userId không được để trống")
	@Valid
	private List<InputTag> tags;

	@NotNull(message = "userId không được để trống")
	@Valid
	private InputUser user;

	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InputTag {

		@NotNull(message = "tag.id không được để trống")
		private Long id;
	}

	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InputUser {

		@NotNull(message = "user.id không được để trống")
		private int id;

	}

}
