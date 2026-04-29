package vn.hoidanit.springsieutoc.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.helper.PageResponse;
import vn.hoidanit.springsieutoc.model.dto.requests.CommentFilterRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.requests.CommentRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.response.CommentResponseDTO;
import vn.hoidanit.springsieutoc.service.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/comments")
	public ResponseEntity<ApiResponse<CommentResponseDTO>> createComment(
			@Valid @RequestBody CommentRequestDTO commentRequestDTO) {
		CommentResponseDTO commentResponseDTO = commentService.createComment(commentRequestDTO);
		return ApiResponse.created(commentResponseDTO);
	}

	@GetMapping("/comments")
	public ResponseEntity<?> getAllComments(Pageable pageable, CommentFilterRequestDTO commentFilterRequestDTO) {

		Page<CommentResponseDTO> commentList = commentService.fetchComments(pageable, commentFilterRequestDTO);

		return ApiResponse.success(PageResponse.from(commentList), "fetch comment list success");

	}

	@GetMapping("/comments/{id}")
	public ResponseEntity<ApiResponse<CommentResponseDTO>> getCommentByID(@PathVariable Long id) {
		CommentResponseDTO commentResponseDTO = commentService.fetchCommentById(id);
		return ApiResponse.success(commentResponseDTO);
	}

	@PutMapping("/comments/{id}")
	public ResponseEntity<ApiResponse<CommentResponseDTO>> updateComment(@PathVariable Long id,
			@Valid @RequestBody CommentRequestDTO commentRequestDTO) {
		CommentResponseDTO commentResponseDTO = commentService.updateComment(id, commentRequestDTO);
		return ApiResponse.success(commentResponseDTO);
	}

	@DeleteMapping("/comments/{id}")
	public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long id) {
		commentService.deleteComment(id);
		return ApiResponse.success("");
	}

}
