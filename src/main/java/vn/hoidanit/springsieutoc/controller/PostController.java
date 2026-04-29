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
import vn.hoidanit.springsieutoc.model.dto.requests.PostFilterRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.requests.PostRequestsDTO;
import vn.hoidanit.springsieutoc.model.dto.response.PostResponseDTO;
import vn.hoidanit.springsieutoc.service.PostService;

@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PostMapping("/posts")
	public ResponseEntity<ApiResponse<PostResponseDTO>> createPost(@Valid @RequestBody PostRequestsDTO postRequestDTO) {

		PostResponseDTO postResponseDTO = postService.createPost(postRequestDTO);
		return ApiResponse.created(postResponseDTO);

	}

	@GetMapping("/posts")
	public ResponseEntity<?> getAllPosts(Pageable pageable, @Valid PostFilterRequestDTO postFilterRequestDTO) {
		Page<PostResponseDTO> posts = postService.fethchPosts(pageable, postFilterRequestDTO);

		return ApiResponse.success(PageResponse.from(posts), "fetch post list success");
	}

	@GetMapping("/posts/{id}")
	public ResponseEntity<ApiResponse<PostResponseDTO>> getPostById(@PathVariable Long id) {
		PostResponseDTO postResponseDTO = postService.fetchPostById(id);
		return ApiResponse.success(postResponseDTO);
	}

	@PutMapping("/posts/{id}")
	public ResponseEntity<ApiResponse<PostResponseDTO>> updatePost(@PathVariable Long id,
			@Valid @RequestBody PostRequestsDTO postRequestDTO) {
		PostResponseDTO postResponseDTO = postService.updatePost(id, postRequestDTO);
		return ApiResponse.success(postResponseDTO);
	}

	@DeleteMapping("/posts/{id}")
	public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
		postService.deletePost(id);
		return ApiResponse.success(null, "delete post success");
	}

}
