package project.challengers.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "point")
@Builder
@ApiModel("포인트")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value="포인트 순번", example="1")
    private int pointSeq;

    @Column(nullable = false)
    @ApiModelProperty(value="아이디", example="user001")
    private String id;

    @Column
    @ApiModelProperty(value="포인트", example="10000")
    private int point;
}
