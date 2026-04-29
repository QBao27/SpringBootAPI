package vn.hoidanit.springsieutoc.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.springsieutoc.helper.exception.ResourceAlreadyExistsException;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Post;
import vn.hoidanit.springsieutoc.model.Tag;
import vn.hoidanit.springsieutoc.model.dto.response.TagResponseDTO;
import vn.hoidanit.springsieutoc.repository.PostRepository;
import vn.hoidanit.springsieutoc.repository.TagRepository;

@Service
@RequiredArgsConstructor
public class TagService {

	private final TagRepository tagReponsitory;
	private final PostRepository postReponsitory;

	public Tag createTag(Tag tag) {

		if (tagReponsitory.existsByName(tag.getName())) {
			throw new ResourceAlreadyExistsException("Tag name already exists");
		}
		return tagReponsitory.save(tag);
	}

	public Page<TagResponseDTO> getAllTags(Pageable pageable) {
		// @formatter:off
		Page<TagResponseDTO> tags = tagReponsitory.findAll(pageable)
				.map(t -> TagResponseDTO.builder().
						id(t.getId())
						.name(t.getName())
						.build());
		return tags;
	}

	public Tag findTagById(Long id) {
		return tagReponsitory.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tag with id " + id + " not found"));
	}

	public Tag updateTag(Tag inputTag) {
		Tag currentTagInDB = findTagById(inputTag.getId());

		if (currentTagInDB == null) {
			throw new ResourceNotFoundException("Tag with id " + inputTag.getId() + " not found");
		}

		if (tagReponsitory.existsByNameAndIdNot(inputTag.getName(), inputTag.getId())) {
			throw new ResourceAlreadyExistsException("Tag name already exists");
		}

		currentTagInDB.setName(inputTag.getName());

		return tagReponsitory.save(currentTagInDB);
	}

	public void deleteTagById(Long id) {
		Tag tagInDB = tagReponsitory.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tag with id " + id + " not found"));

		List<Post> postsWithTag = postReponsitory.findByTagsContains(tagInDB);
		for (Post post : postsWithTag) {
			post.getTags().remove(tagInDB);
			postReponsitory.save(post);
		}

		tagReponsitory.deleteById(id);
	}

}
