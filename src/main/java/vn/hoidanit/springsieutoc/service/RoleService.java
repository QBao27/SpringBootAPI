package vn.hoidanit.springsieutoc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.springsieutoc.helper.exception.ResourceAlreadyExistsException;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Role;
import vn.hoidanit.springsieutoc.model.dto.response.RoleResponseDTO;
import vn.hoidanit.springsieutoc.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleReponsitory;

	public RoleResponseDTO createRole(Role role) {

		if (roleReponsitory.existsByName(role.getName())) {
			throw new ResourceAlreadyExistsException("Role name already exists");
		}
		return convertRoleDTO(roleReponsitory.save(role));
	}

	public RoleResponseDTO convertRoleDTO(Role role) {
		return new RoleResponseDTO(role.getId(), role.getName(), role.getDescription());
	}

	public Page<RoleResponseDTO> fetchAllRoles(Pageable pageable) {

		// @formatter:off
		Page<RoleResponseDTO> roleList = this.roleReponsitory.findAll(pageable)
				.map(role -> RoleResponseDTO.builder()
						.id(role.getId())
						.name(role.getName())
						.description(role.getDescription()).build());
		return roleList;
	}

	public Role findRoleById(int id) {
		return roleReponsitory.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " not found"));
	}

	public Role updateRole(Role role) {
		Role currentRoleInDb = findRoleById(role.getId());
		if (currentRoleInDb == null) {
			throw new ResourceNotFoundException("Role with id " + role.getId() + " not found");
		}
		if (roleReponsitory.existsByNameAndIdNot(role.getName(), role.getId())) {
			throw new ResourceAlreadyExistsException("Role name already exists");
		}
		currentRoleInDb.setName(role.getName());
		currentRoleInDb.setDescription(role.getDescription());
		return roleReponsitory.save(currentRoleInDb);
	}

	public String deleteRoleById(int id) {
		Role currentRoleInDb = findRoleById(id);
		if (currentRoleInDb == null) {
			throw new ResourceNotFoundException("Role with id " + id + " not found");
		}
		roleReponsitory.deleteById(id);
		return "Delete role with id " + id + " success";
	}

}
