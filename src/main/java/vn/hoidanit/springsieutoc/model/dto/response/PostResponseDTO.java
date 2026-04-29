package vn.hoidanit.springsieutoc.model.dto.response;

import java.util.List;

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
public class PostResponseDTO {

	private Long id;

	private String title;

	private String content;

	private String createdAt;

	private String updatedAt;

	private String authorName;

	private List<OutputTag> tags;

	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OutputTag {

		private Long id;

		private String name;

	}
}
