package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedAuthRepository;
import project.challengers.entity.Auth;

public interface AuthRepository extends JpaRepository<Auth, Long>, CustomizedAuthRepository {
}
