package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedChallengeRepository;
import project.challengers.entity.Challenge;
import project.challengers.entity.Notice;

public interface ChallengeRepository extends JpaRepository<Challenge, Notice>, CustomizedChallengeRepository {
}
