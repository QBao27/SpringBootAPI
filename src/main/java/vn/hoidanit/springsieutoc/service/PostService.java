package vn.hoidanit.springsieutoc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Post;
import vn.hoidanit.springsieutoc.model.Tag;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.requests.PostFilterRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.requests.PostRequestsDTO;
import vn.hoidanit.springsieutoc.model.dto.response.PostResponseDTO;
import vn.hoidanit.springsieutoc.repository.PostRepository;
import vn.hoidanit.springsieutoc.repository.TagRepository;
import vn.hoidanit.springsieutoc.repository.UserRepository;
import vn.hoidanit.springsieutoc.service.specification.PostSpecification;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postReponsitory;
	private final TagRepository tagReponsitory;
	private final UserRepository userReponsitory;

	public PostResponseDTO convertPostDTO(Post post) {

		PostResponseDTO postResponseDTO = new PostResponseDTO();
		postResponseDTO.setId(post.getId());
		postResponseDTO.setTitle(post.getTitle());
		postResponseDTO.setContent(post.getContent());
		postResponseDTO.setCreatedAt(post.getCreatedAt().toString());
		postResponseDTO.setUpdatedAt(post.getUpdatedAt().toString());
		postResponseDTO.setAuthorName(post.getUser().getName());

		List<PostResponseDTO.OutputTag> tags = post.getTags().stream().map(t -> {
			PostResponseDTO.OutputTag outputTag = new PostResponseDTO.OutputTag();
			outputTag.setId(t.getId());
			outputTag.setName(t.getName());
			return outputTag;
		}).collect(Collectors.toList());

		postResponseDTO.setTags(tags);
		return postResponseDTO;
	}

	public Post convertPost(PostRequestsDTO postRequestDTO) {

		Post post = new Post();
		post.setTitle(postRequestDTO.getTitle());
		post.setContent(postRequestDTO.getContent());

		List<Tag> tags = postRequestDTO.getTags().stream()
				.map(t -> tagReponsitory.findById(t.getId())
						.orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + t.getId())))
				.collect(Collectors.toList());

		post.setTags(tags);

		User user = userReponsitory.findById(postRequestDTO.getUser().getId()).orElseThrow(
				() -> new ResourceNotFoundException("User not found with id: " + postRequestDTO.getUser().getId()));
		post.setUser(user);

		return post;

	}

	public PostResponseDTO createPost(PostRequestsDTO postRequestDTO) {

		Post post = convertPost(postRequestDTO);
		post = postReponsitory.save(post);
		return convertPostDTO(post);
	}

	public Page<PostResponseDTO> fethchPosts(Pageable pageable, PostFilterRequestDTO postFilterRequestDTO) {
		// @formatter:off
		// build specification
		Specification<Post> spec = Specification.allOf(
				PostSpecification.hasUserId(postFilterRequestDTO),
				PostSpecification.hasTagName(postFilterRequestDTO));	
		
		Page<PostResponseDTO> postResponseDTOs = postReponsitory.findAll(spec, pageable)
				.map(p -> PostResponseDTO.builder()
						.id(p.getId())
						.title(p.getTitle())
						.content(p.getContent())
						.createdAt(p.getCreatedAt().toString())
						.updatedAt(p.getUpdatedAt().toString())
						.authorName(p.getUser().getName())
						.tags(p.getTags().stream()
								.map(t -> {
									PostResponseDTO.OutputTag outputTag = new PostResponseDTO.OutputTag();
									outputTag.setId(t.getId());
									outputTag.setName(t.getName());
									return outputTag;
								})
								.collect(Collectors.toList()))
						.build())
				;
		return postResponseDTOs;	
	}

	public PostResponseDTO fetchPostById(Long id) {
		Post post = postReponsitory.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
		return convertPostDTO(post);
	}

	public PostResponseDTO updatePost(Long id, PostRequestsDTO postRequestDTO) {
		Post postInDB = postReponsitory.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
		postInDB.setTitle(postRequestDTO.getTitle());
		postInDB.setContent(postRequestDTO.getContent());

		if (postRequestDTO.getTags() != null) {
			List<Tag> tags = postRequestDTO.getTags().stream()
					.map(t -> tagReponsitory.findById(t.getId())
							.orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + t.getId())))
					.collect(Collectors.toList());
			postInDB.setTags(tags);
		}

		if (postRequestDTO.getUser() != null) {
			User user = userReponsitory.findById(postRequestDTO.getUser().getId()).orElseThrow(
					() -> new ResourceNotFoundException("User not found with id: " + postRequestDTO.getUser().getId()));
			postInDB.setUser(user);
		}

		postInDB = postReponsitory.save(postInDB);

		return convertPostDTO(postInDB);
	}

	public void deletePost(Long id) {
		Post postInDB = postReponsitory.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
		postReponsitory.delete(postInDB);
	}

}
