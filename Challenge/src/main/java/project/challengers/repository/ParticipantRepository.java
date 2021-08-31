package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedParticipantRepository;
import project.challengers.entity.Notice;
import project.challengers.entity.Participant;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long>, CustomizedParticipantRepository {
    List<Participant> findByNoticeAndParticipantType(Notice notice, int participantType);
    List<Participant> findByNotice(Notice notice);
    Participant findByNoticeAndParticipantId(Notice notice, String id);
}
