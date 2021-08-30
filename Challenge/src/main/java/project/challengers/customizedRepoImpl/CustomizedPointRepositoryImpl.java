package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.customizedRepo.CustomizedPointRepository;

import java.util.List;

import static project.challengers.entity.QPoint.point1;

public class CustomizedPointRepositoryImpl implements CustomizedPointRepository {
    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedPointRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public int updatePoint(int deposit, String id) {
        return (int) jpaQueryFactory.update(point1)
                .set(point1.point, deposit)
                .where(point1.member.id.eq(id))
                .execute();
    }

    @Override
    public int updateUserPoint(int deposit, List<String> id) {
        return (int) jpaQueryFactory.update(point1)
                .set(point1.point, point1.point.add(deposit))
                .where(point1.member.id.in(id))
                .execute();
    }
}
