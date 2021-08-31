package project.challengers.service;

import project.challengers.DTO.participant.ParticipantCreditDto;
import project.challengers.DTO.participant.ParticipantDto;

import java.util.List;

public interface ParticipantService {
    int setParticipantType(long noticeSeq, List<String> idList, String masterId);
    List<ParticipantDto> getAllParticipant(long noticeSeq, String id);
    List<ParticipantDto> getParticipantStatus(long noticeSeq, int type, String id);
    int setBlind(long noticeSeq, String id, String masterId);
    int setDelete(long noticeSeq, String id, String masterId);
    List<ParticipantCreditDto> getUserFine(long noticeSeq, String id);
}
