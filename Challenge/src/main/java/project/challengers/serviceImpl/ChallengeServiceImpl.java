package project.challengers.serviceImpl;

import org.springframework.stereotype.Service;
import project.challengers.service.ChallengeService;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    /**
     * 도전방 시작시간 이후에 방 수동 시작
     * @param noticeSeq
     * @param id
     * @return
     */
    @Override
    public int startChallenge(long noticeSeq, String id) {
        return 0;
    }

    /**
     * 도전방 종료시간 이후 and 포인트 분배 완료했을 경우 도전 방 수동 종료
     * @param noticeSeq
     * @param id
     * @return
     */
    @Override
    public int stopChallenge(long noticeSeq, String id) {
        return 0;
    }

    /**
     * 포인트 분배
     * @param noticeSeq
     * @param id
     * @return
     */
    @Override
    public int pointDistribution(long noticeSeq, String id) {
        return 0;
    }
}
