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
import vn.hoidanit.springsieutoc.model.Tag;
import vn.hoidanit.springsieutoc.model.dto.response.TagResponseDTO;
import vn.hoidanit.springsieutoc.service.TagService;

@RestController
@RequiredArgsConstructor
public class TagController {

	private final TagService tagService;

	@PostMapping("/tags")
	public ResponseEntity<ApiResponse<Tag>> createTag(@Valid @RequestBody Tag tag) {

		Tag inputTag = tagService.createTag(tag);
		return ApiResponse.created(inputTag);
	}

	@GetMapping("/tags")
	public ResponseEntity<?> fetchTags(Pageable pageable) {
		Page<TagResponseDTO> tagList = tagService.getAllTags(pageable);
		return ApiResponse.success(PageResponse.from(tagList), "fetch tag list success");
	}

	@GetMapping("/tags/{id}")
	public ResponseEntity<ApiResponse<Tag>> getTagById(@PathVariable Long id) {

		Tag getTagId = tagService.findTagById(id);
		return ApiResponse.success(getTagId, "fetch tag by id success");

	}

	@PutMapping("/tags/{id}")
	public ResponseEntity<ApiResponse<Tag>> updateTagById(@PathVariable Long id, @Valid @RequestBody Tag inputTag) {
		inputTag.setId(id);
		Tag updateTag = tagService.updateTag(inputTag);
		return ApiResponse.success(updateTag, "update tag success");
	}

	@DeleteMapping("/tags/{id}")
	public ResponseEntity<ApiResponse<String>> deleteTagById(@PathVariable Long id) {
		tagService.deleteTagById(id);
		return ApiResponse.success("delete tag success");
	}

}
