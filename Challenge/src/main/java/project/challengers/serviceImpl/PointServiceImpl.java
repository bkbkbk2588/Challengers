package project.challengers.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.challengers.DTO.point.*;
import project.challengers.base.PointHistoryStatus;
import project.challengers.entity.Member;
import project.challengers.entity.Point;
import project.challengers.entity.PointHistory;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.MemberRepository;
import project.challengers.repository.PointHistoryRepository;
import project.challengers.repository.PointRepository;
import project.challengers.service.PointService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PointServiceImpl implements PointService {

    Logger logger = LoggerFactory.getLogger(PointServiceImpl.class);

    @Autowired
    PointRepository pointRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    MessageSource messageSource;

    /**
     * 포인트 충전 및 이력 추가
     *
     * @param point
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int addPoint(PointDto point, String id) {
        // 입력 포인트 없을 경우
        if (point == null || point.getPoint() == 0 || point.getPoint() == null) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.point.notfound.E0012", null, Locale.KOREA));
        }

        Point pointEntity = pointRepository.findByMember(Member.builder().id(id).build());

        // 포인트 처음 추가할 경우 insert
        if (pointEntity == null) {
            pointRepository.save(Point.builder()
                    .member(Member.builder().id(id).build())
                    .point(point.getPoint())
                    .build());
        } else { // 처음이 아닌 경우 update
            pointRepository.updatePoint(pointEntity.getPoint() + point.getPoint(), id);
        }

        // 이력 추가
        PointHistory pointHistory = pointHistoryRepository.save(PointHistory.builder()
                .member(Member.builder()
                        .id(id)
                        .build())
                .point(point.getPoint())
                .status(0)
                .insertTime(LocalDateTime.now())
                .build());

        return pointHistory != null ? 1 : 0;
    }

    /**
     * 포인트 출금 및 이력 추가
     *
     * @param point
     * @param id
     * @return
     */
    @Transactional
    @Override
    public int withdrawPoint(PointDto point, String id) {
        // 입력 포인트 없을 경우
        if (point == null || point.getPoint() == 0 || point.getPoint() == null) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.point.notfound.E0012", null, Locale.KOREA));
        }

        Point pointEntity = pointRepository.findByMember(Member.builder().id(id).build());

        // 출금금액이 더 많을 경우
        if (pointEntity == null || point.getPoint() > pointEntity.getPoint()) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.point.withdraw.big.E0013", null, Locale.KOREA));
        }

        pointRepository.updatePoint(pointEntity.getPoint() - point.getPoint(), id);

        // 이력 추가
        PointHistory pointHistory = pointHistoryRepository.save(PointHistory.builder()
                .member(Member.builder()
                        .id(id)
                        .build())
                .point(point.getPoint())
                .status(PointHistoryStatus.withdraw.ordinal())
                .insertTime(LocalDateTime.now())
                .build());

        return pointHistory != null ? 1 : 0;
    }

    /**
     * 자신의 포인트 조회
     *
     * @param id
     * @return
     */
    @Override
    public PointInfoDto getPoint(String id) {
        Point point = pointRepository.findByMember(Member.builder().id(id).build());

        return PointInfoDto.builder()
                .id(id)
                .point(point.getPoint())
                .build();
    }

    /**
     * 포인트 입금 또는 출금 이력 조회
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public MyPointDto getHistory(String id, int status) {
        List<PointHistory> pointHistory = pointHistoryRepository.findByMemberAndStatusOrderByInsertTimeDesc(
                Member.builder().id(id).build(), status);
        List<PointHistoryDto> pointHistoryList = new ArrayList<>();

        pointHistory.forEach(point -> {
            pointHistoryList.add(PointHistoryDto.builder()
                    .insertTime(point.getInsertTime())
                    .point(point.getPoint())
                    .build());
        });

        return MyPointDto.builder()
                .id(id)
                .pointHistoryList(pointHistoryList)
                .build();
    }

    /**
     * 포인트 입출금 내역 전체 조회
     *
     * @param id
     * @return
     */
    @Override
    public MyAllPointDto getAllHistory(String id) {
        List<PointHistory> pointHistory = pointHistoryRepository.findByMember(Member.builder().id(id).build());
        List<PointAllHistoryDto> pointHistoryList = new ArrayList<>();

        pointHistory.forEach(point -> {
            pointHistoryList.add(PointAllHistoryDto.builder()
                    .insertTime(point.getInsertTime())
                    .point(point.getPoint())
                    .type(point.getStatus() == 0 ? "입금" : "출금")
                    .build());
        });

        return MyAllPointDto.builder()
                .id(id)
                .pointHistoryList(pointHistoryList)
                .build();
    }
}
