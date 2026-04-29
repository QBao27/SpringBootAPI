package vn.hoidanit.springsieutoc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.springsieutoc.model.RefreshToken;

@Repository
public interface RefreshTokenReponsitory extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);;

}
