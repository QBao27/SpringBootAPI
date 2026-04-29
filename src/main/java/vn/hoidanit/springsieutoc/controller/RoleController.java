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
import vn.hoidanit.springsieutoc.model.Role;
import vn.hoidanit.springsieutoc.model.dto.response.RoleResponseDTO;
import vn.hoidanit.springsieutoc.service.RoleService;

@RestController
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	@PostMapping("/roles")
	public ResponseEntity<ApiResponse<RoleResponseDTO>> createRole(@Valid @RequestBody Role role) {
		RoleResponseDTO inputRole = roleService.createRole(role);
		return ApiResponse.created(inputRole);
	}

	@GetMapping("/roles")
	public ResponseEntity<?> fetchAllRoles(Pageable pageable) {
		Page<RoleResponseDTO> roleLists = roleService.fetchAllRoles(pageable);
		return ApiResponse.success(PageResponse.from(roleLists), "fetch role list success");
	}

	@GetMapping("/roles/{id}")
	public ResponseEntity<ApiResponse<Role>> findRoleById(@PathVariable int id) {
		Role role = roleService.findRoleById(id);
		return ApiResponse.success(role, "fetch role by id success");
	}

	@PutMapping("/roles/{id}")
	public ResponseEntity<ApiResponse<Role>> updateRoleById(@PathVariable int id, @Valid @RequestBody Role inputRole) {
		inputRole.setId(id);
		Role updateRole = roleService.updateRole(inputRole);
		return ApiResponse.success(updateRole, "update role success");
	}

	@DeleteMapping("/roles/{id}")
	public ResponseEntity<ApiResponse<String>> deleteRoleById(@PathVariable int id) {
		roleService.deleteRoleById(id);
		return ApiResponse.success("delete role success");
	}
}
