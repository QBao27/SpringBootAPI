package vn.hoidanit.springsieutoc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.springsieutoc.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, int id);

	Optional<Role> findByNameOrId(String name, int id);

	Optional<Role> findByName(String name);
}
