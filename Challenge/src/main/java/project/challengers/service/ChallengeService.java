package project.challengers.service;

public interface ChallengeService {
    int startChallenge(long noticeSeq, String id);
    int stopChallenge(long noticeSeq, String id);
    int pointDistribution(long noticeSeq, String id);

}
