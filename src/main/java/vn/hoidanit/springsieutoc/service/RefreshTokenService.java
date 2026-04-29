package vn.hoidanit.springsieutoc.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.RefreshToken;
import vn.hoidanit.springsieutoc.repository.RefreshTokenReponsitory;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenReponsitory reFreshTokenReponsitory;

	public void createRefreshToken(RefreshToken refreshToken) {
		reFreshTokenReponsitory.save(refreshToken);

	}

	public RefreshToken findByToken(String token) {
		return reFreshTokenReponsitory.findByToken(token)
				.orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));
	}

	public void deleteById(Long id) {
		reFreshTokenReponsitory.deleteById(id);
	}

}
