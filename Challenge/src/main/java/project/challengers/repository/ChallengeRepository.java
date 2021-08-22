package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedChallengeRepository;
import project.challengers.entity.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, CustomizedChallengeRepository {
}
