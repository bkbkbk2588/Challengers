package project.challengers.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "POINT")
@Builder
@ApiModel("포인트")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long pointSeq;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column
    @ColumnDefault("0")
    private int point;
}
