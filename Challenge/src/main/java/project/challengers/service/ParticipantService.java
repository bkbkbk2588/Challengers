package project.challengers.service;

import project.challengers.DTO.participant.ParticipantDto;
import project.challengers.DTO.participant.ParticipantCreditDto;

import java.util.List;

public interface ParticipantService {
    int setParticipantType(long noticeSeq, String id, String masterId, int type);
    List<ParticipantDto> getAllParticipant(long noticeSeq, String id);
    List<ParticipantDto> getParticipantStatus(long noticeSeq, int type, String id);
    int setBlind(long noticeSeq, String id, String masterId);
    int setDelete(long noticeSeq, String id, String masterId);
    List<ParticipantCreditDto> getUserFine(long noticeSeq, String id);
}
