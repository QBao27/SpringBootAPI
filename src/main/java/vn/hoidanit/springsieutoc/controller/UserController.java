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

package vn.hoidanit.springsieutoc.controller;

import org.springdoc.core.annotations.ParameterObject;
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
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.requests.UserFilterRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.requests.UserRequestsDTO;
import vn.hoidanit.springsieutoc.model.dto.response.UserResponseDTO;
import vn.hoidanit.springsieutoc.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

	// unit test
	private final UserService userService;

	@PostMapping("/users")
	public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody User user) {
		UserResponseDTO InputUser = userService.createUser(user);
		System.out.println("InputUser: " + InputUser.toString());
		return ApiResponse.created(InputUser);
	}

	@GetMapping("/users")
	public ResponseEntity<?> fetchUsers(@ParameterObject @Valid UserFilterRequestDTO userFilterResquestDTO,
			@ParameterObject Pageable pageable) {

		Page<UserResponseDTO> userList = userService.fetchUsers(pageable, userFilterResquestDTO);

		return ApiResponse.success(PageResponse.from(userList), "fetch user list success");
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable int id) {

		UserResponseDTO getUserId = userService.findUserById(id);
		return ApiResponse.success(getUserId, "fetch user by id success");

	}

	@PutMapping("/users/{id}")
	public ResponseEntity<ApiResponse<String>> putUserById(@PathVariable int id,
			@Valid @RequestBody UserRequestsDTO inputUser) {
		userService.updateUser(id, inputUser);
		return ApiResponse.success("update sussces");
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<ApiResponse<String>> deleteUserById(@PathVariable int id) {
		userService.deleteUserById(id);
		return ApiResponse.success("delete sussces");
	}
}
