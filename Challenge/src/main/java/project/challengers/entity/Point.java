package project.challengers.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", nullable = false, unique = true)
    private Member member;

    @Column
    @ColumnDefault("0")
    private int point;
}
