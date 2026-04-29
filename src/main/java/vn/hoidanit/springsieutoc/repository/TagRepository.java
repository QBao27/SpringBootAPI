package vn.hoidanit.springsieutoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.springsieutoc.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, Long id);
}
