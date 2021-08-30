package project.challengers.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "POINT_HISTORY")
@Builder
@ApiModel("포인트 이력")
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "point_history_seq")
    private long pointHistorySeq;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(nullable = false)
    private int status;

    @Column(nullable = false)
    private int point;

    @Column(name = "insert_time", nullable = false)
    LocalDateTime insertTime;

}
