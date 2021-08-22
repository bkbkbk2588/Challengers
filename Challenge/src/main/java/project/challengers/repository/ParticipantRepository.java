package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedParticipantRepository;
import project.challengers.entity.Participant;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long>, CustomizedParticipantRepository {
    List<Participant> findByNoticeSeqAndParticipantType(long noticeSeq, int participantType);
    List<Participant> findByNoticeSeq(long noticeSeq);
}
