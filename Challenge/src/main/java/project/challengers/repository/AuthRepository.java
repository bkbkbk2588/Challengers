package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedAuthRepository;
import project.challengers.entity.Auth;

import java.time.LocalDate;
import java.util.List;

public interface AuthRepository extends JpaRepository<Auth, Long>, CustomizedAuthRepository {
    List<Auth> findByAuthDateBefore(LocalDate now);
}
