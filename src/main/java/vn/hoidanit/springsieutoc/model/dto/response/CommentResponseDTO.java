package vn.hoidanit.springsieutoc.model.dto.response;

import java.time.LocalDateTime;

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
public class CommentResponseDTO {

	private Long id; // object generic<> id == null

	private String content;

	private boolean isApproved = false;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String userFullName;

	private String postTitle;

}
