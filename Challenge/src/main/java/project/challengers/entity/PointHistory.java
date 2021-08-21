package project.challengers.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private int status;

    @Column(nullable = false)
    private int point;

}
