/*
 * Author: Hỏi Dân IT - @hoidanit 
 *
 * This source code is developed for the course
 * "Java Spring Siêu Tốc - Tự Học Java Spring Từ Số 0 Dành Cho Beginners từ A tới Z".
 * It is intended for educational purposes only.
 * Unauthorized distribution, reproduction, or modification is strictly prohibited.
 *
 * Copyright (c) 2025 Hỏi Dân IT. All Rights Reserved.
 */

package vn.hoidanit.springsieutoc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.springsieutoc.helper.exception.ResourceAlreadyExistsException;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Role;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.requests.RegisterRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.requests.UserFilterRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.requests.UserRequestsDTO;
import vn.hoidanit.springsieutoc.model.dto.response.RoleResponseDTO;
import vn.hoidanit.springsieutoc.model.dto.response.UserResponseDTO;
import vn.hoidanit.springsieutoc.repository.RoleRepository;
import vn.hoidanit.springsieutoc.repository.UserRepository;
import vn.hoidanit.springsieutoc.service.specification.UserSpecification;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final RoleRepository roleRepository;

	public Page<UserResponseDTO> fetchUsers(Pageable pageable, UserFilterRequestDTO userFilterResquestDTO) {

		// @formatter:off
		// build specification
		Specification<User> spec = Specification.allOf(
				UserSpecification.hasName(userFilterResquestDTO),
				UserSpecification.hasEmail(userFilterResquestDTO), 
				UserSpecification.hasAddress(userFilterResquestDTO),
				UserSpecification.hasRole(userFilterResquestDTO));

		Page<UserResponseDTO> userList = this.userRepository.findAll(spec, pageable)
				.map(user -> UserResponseDTO.builder()
						.id(user.getId())
						.name(user.getName())
						.email(user.getEmail())
						.address(user.getAddress())
						.role(new RoleResponseDTO(user.getRole().getId(),
								user.getRole().getName(), user.getRole().getDescription()))
						.build());
		return userList;
	}

	public List<UserResponseDTO> fetchUsersByRoleName(String rolename) {

		Role roleInDB = roleRepository.findByName(rolename)
				.orElseThrow(() -> new ResourceNotFoundException("Role with name " + rolename + " not found"));
		List<UserResponseDTO> userList = userRepository.findByRole_Name(roleInDB.getName()).stream()
				.map(user -> convertUserDTO(user)).collect(Collectors.toList());
		return userList;
	}

	public UserResponseDTO convertUserDTO(User user) {
		RoleResponseDTO roleDTO = new RoleResponseDTO(
				user.getRole().getId(),
				user.getRole().getName(),
				user.getRole().getDescription());
		return UserResponseDTO.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.address(user.getAddress())
				.role(roleDTO).build();
	}

	public UserResponseDTO createUser(User user) {

		if (userRepository.existsByEmail(user.getEmail())) {
			throw new ResourceAlreadyExistsException("Email already exists" + user.getEmail());
		}

		String hasPassWord = passwordEncoder.encode(user.getPassword());

		// check role
		int roleId = user.getRole().getId();
		String roleName = user.getRole().getName();
		Role roleInDB = roleRepository.findByNameOrId(roleName, roleId).orElseThrow(
				() -> new ResourceNotFoundException("Role with id " + roleId + " or name " + roleName + " not found"));

		user.setRole(roleInDB);
		user.setPassword(hasPassWord);
		// save user to database
		// orm
		return convertUserDTO(userRepository.save(user));

	}

	public UserResponseDTO findUserById(int id) {

		User userInDB = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

		return convertUserDTO(userInDB);
	}

	public User findUserByEmail(String email) {
		return userRepository.findUByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
	}

	public void updateUser(int id, UserRequestsDTO inputUser) {

		User InputUserInDB = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

		if (inputUser.getRole() != null) {
			int roleId = inputUser.getRole().getId();
			Role roleInDB = roleRepository.findById(roleId)
					.orElseThrow(() -> new ResourceNotFoundException("Role with id " + roleId + " not found"));
			InputUserInDB.setRole(roleInDB);
		}

		InputUserInDB.setName(inputUser.getName());
		InputUserInDB.setAddress(inputUser.getAddress());

		userRepository.save(InputUserInDB);
	}

	public void deleteUserById(int id) {
		
		User userInDB = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
		if (userInDB.getRole().getName().equals("ADMIN")) {
			throw new RuntimeException("Cannot delete user with role ADMIN");
		}
		
		this.userRepository.deleteById(id);
	}
	
	public void regsiterUser( RegisterRequestDTO inPutUser) {	
		
		if (userRepository.existsByEmail(inPutUser.getEmail())) {
			throw new ResourceAlreadyExistsException("Email already exists" + inPutUser.getEmail());
		}

		String hasPassWord = passwordEncoder.encode(inPutUser.getPassword());
		
		Role userRole = roleRepository.findByName("USER")
				.orElseThrow(() -> new ResourceNotFoundException("Role with name USER not found"));

		User user =  User.builder()
				.name(inPutUser.getName())
				.email(inPutUser.getEmail())
				.password(hasPassWord)
				.address(inPutUser.getAddress())
				.role(userRole)
				.build();
	
		userRepository.save(user);
	}

}
