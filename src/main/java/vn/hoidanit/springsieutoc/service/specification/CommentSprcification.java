package vn.hoidanit.springsieutoc.service.specification;

import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.springsieutoc.model.Comment;
import vn.hoidanit.springsieutoc.model.dto.requests.CommentFilterRequestDTO;

public class CommentSprcification {

	public static Specification<Comment> hasUserId(CommentFilterRequestDTO commentFilterRequestDTO) {
		return (root, query, cb) -> {
			if (commentFilterRequestDTO.getUserId() == null) {
				return cb.conjunction();
			}
			return cb.equal(root.get("user").get("id"), commentFilterRequestDTO.getUserId());
		};
	}

	public static Specification<Comment> hasPostId(CommentFilterRequestDTO commentFilterRequestDTO) {
		return (root, query, cb) -> {
			if (commentFilterRequestDTO.getPostId() == null) {
				return cb.conjunction();
			}
			return cb.equal(root.get("post").get("id"), commentFilterRequestDTO.getPostId());
		};
	}

	public static Specification<Comment> hasCommnet(CommentFilterRequestDTO commentFilterRequestDTO) {
		return (root, query, cb) -> {
			if (commentFilterRequestDTO.getTitle() == null) {
				return cb.conjunction();
			}
			return cb.like(cb.lower(root.get("title")), "%" + commentFilterRequestDTO.getTitle() + "%");
		};
	}
}
