package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.customizedRepo.CustomizedPointRepository;

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
                .where(point1.id.eq(id))
                .execute();
    }
}
