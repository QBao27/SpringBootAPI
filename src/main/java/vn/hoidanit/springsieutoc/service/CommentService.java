package vn.hoidanit.springsieutoc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Comment;
import vn.hoidanit.springsieutoc.model.Post;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.requests.CommentFilterRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.requests.CommentRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.response.CommentResponseDTO;
import vn.hoidanit.springsieutoc.repository.CommentRepository;
import vn.hoidanit.springsieutoc.repository.PostRepository;
import vn.hoidanit.springsieutoc.repository.UserRepository;
import vn.hoidanit.springsieutoc.service.specification.CommentSprcification;

@Service
@RequiredArgsConstructor
@Builder
public class CommentService {

	private final CommentRepository commentRepository;

	private final UserRepository userRepository;

	private final PostRepository postRepository;

	public Comment convertComment(CommentRequestDTO commentRequestDTO) {
		User user = userRepository.findById(commentRequestDTO.getUser().getId()).orElseThrow(
				() -> new ResourceNotFoundException("User " + commentRequestDTO.getUser().getId() + " not found"));

		Post post = postRepository.findById(commentRequestDTO.getPost().getId()).orElseThrow(
				() -> new ResourceNotFoundException("Post " + commentRequestDTO.getPost().getId() + " not found"));

		return Comment.builder().title(commentRequestDTO.getContent()).user(user).post(post).build();
	}

	public CommentResponseDTO convertCommentDTO(Comment comment) {

		return CommentResponseDTO.builder().id(comment.getId()).content(comment.getTitle())
				.isApproved(comment.isApproved()).createdAt(comment.getCreatedAt()).updatedAt(comment.getUpdatedAt())
				.userFullName(comment.getUser().getName()).postTitle(comment.getPost().getTitle()).build();

	}

	public CommentResponseDTO createComment(CommentRequestDTO commmentRequestDTO) {

		if (!userRepository.existsById(commmentRequestDTO.getUser().getId())) {
			throw new ResourceNotFoundException("User " + +commmentRequestDTO.getUser().getId() + " not found");
		}

		if (!postRepository.existsById(commmentRequestDTO.getPost().getId())) {
			throw new ResourceNotFoundException("Post " + +commmentRequestDTO.getPost().getId() + " not found");
		}

		Comment comment = convertComment(commmentRequestDTO);

		comment = commentRepository.save(comment);

		return convertCommentDTO(comment);
	}

	public List<CommentResponseDTO> fetchCommentByPostTitle(String postTitle) {

		if (!postRepository.existsByTitle(postTitle)) {
			throw new ResourceNotFoundException("Post " + postTitle + " not found");
		}

		List<CommentResponseDTO> commentResponseDTOs = commentRepository.findByPostTitle(postTitle).stream()
				.map(c -> convertCommentDTO(c)).collect(Collectors.toList());
		return commentResponseDTOs;
	}

	public List<CommentResponseDTO> fetchCommentByUserName(String userName) {

		if (!userRepository.existsByName(userName)) {
			throw new ResourceNotFoundException("User " + userName + " not found");
		}
		List<CommentResponseDTO> commentResponseDTOs = commentRepository.findByUserName(userName).stream()
				.map(c -> convertCommentDTO(c)).collect(Collectors.toList());

		return commentResponseDTOs;
	}

	public Page<CommentResponseDTO> fetchComments(Pageable pageable, CommentFilterRequestDTO commentFilterRequestDTO) {

		// @formatter:off
		Specification<Comment> spec = Specification.allOf(
				CommentSprcification.hasUserId(commentFilterRequestDTO),	
				CommentSprcification.hasPostId(commentFilterRequestDTO), 
				CommentSprcification.hasCommnet(commentFilterRequestDTO));	
		
		Page<CommentResponseDTO> commentResponseDTOs = commentRepository.findAll(spec, pageable)
				.map(c -> CommentResponseDTO.builder()
						.id(c.getId())
						.content(c.getTitle())
						.isApproved(c.isApproved())
						.createdAt(c.getCreatedAt())
						.updatedAt(c.getUpdatedAt())
						.userFullName(c.getUser().getName())
						.postTitle(c.getPost().getTitle())
						.build());

		return commentResponseDTOs;	
	}

	public CommentResponseDTO fetchCommentById(Long id) {
		Comment comment = commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Comment " + id + " not found"));

		return convertCommentDTO(comment);
	}

	public CommentResponseDTO updateComment(Long id, CommentRequestDTO commentRequestDTO) {
		Comment comment = commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Comment " + id + " not found"));
		if (commentRequestDTO.getUser() != null) {

			User user = userRepository.findById(commentRequestDTO.getUser().getId()).orElseThrow(
					() -> new ResourceNotFoundException("User " + commentRequestDTO.getUser().getId() + " not found"));
			comment.setUser(user);
		}

		if (commentRequestDTO.getPost() != null) {
			Post post = postRepository.findById(commentRequestDTO.getPost().getId()).orElseThrow(
					() -> new ResourceNotFoundException("Post " + commentRequestDTO.getPost().getId() + " not found"));
			comment.setPost(post);
		}

		comment.setTitle(commentRequestDTO.getContent());

		comment = commentRepository.save(comment);

		return convertCommentDTO(comment);
	}

	public String deleteComment(Long id) {
		Comment comment = commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Comment " + id + " not found"));
		commentRepository.delete(comment);

		return "Comment deleted successfully";
	}

}
