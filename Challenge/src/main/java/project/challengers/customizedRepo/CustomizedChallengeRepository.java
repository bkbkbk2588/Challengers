package project.challengers.customizedRepo;

import project.challengers.entity.Challenge;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomizedChallengeRepository {
    int updateMoney(long noticeSeq, int money);
    int startChallenge(long noticeSeq);
    int startChallengeList(List<Long> noticeSeq);
    int stopChallenge(long noticeSeq);
    int stopChallengeList(List<Long> noticeSeq);
    int pointDistribution(long noticeSeq);
    List<Challenge> findByChallengeBefore(LocalDateTime now);
    List<Challenge> findByChallengeAfter(LocalDateTime now);
}
