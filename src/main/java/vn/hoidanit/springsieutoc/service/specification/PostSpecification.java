package vn.hoidanit.springsieutoc.service.specification;

import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.springsieutoc.model.Post;
import vn.hoidanit.springsieutoc.model.dto.requests.PostFilterRequestDTO;

public class PostSpecification {
	public static Specification<Post> hasUserId(PostFilterRequestDTO postFilterRequestDTO) {
		return (root, query, cb) -> {
			if (postFilterRequestDTO.getUserId() == null) {
				return cb.conjunction();
			}
			return cb.equal(root.get("user").get("id"), postFilterRequestDTO.getUserId());
		};
	}

	public static Specification<Post> hasTagName(PostFilterRequestDTO postFilterRequestDTO) {
		return (root, query, cb) -> {
			if (postFilterRequestDTO.getTagName() == null) {
				return cb.conjunction();
			}
			return cb.like(cb.lower(root.join("tags").get("name")), "%" + postFilterRequestDTO.getTagName() + "%");
		};
	}

}
