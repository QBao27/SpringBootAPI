package vn.hoidanit.springsieutoc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.hoidanit.springsieutoc.model.Post;
import vn.hoidanit.springsieutoc.model.Tag;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
	List<Post> findByTagsContains(Tag tag);

	Optional<Post> findByTitle(String title);

	boolean existsByTitle(String title);
}
