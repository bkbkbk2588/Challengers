package project.challengers.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "NOTICE")
@Builder
@ApiModel("챌린저스 모집 게시글")
public class Notice {

    @Id
    @Column(name = "notice_seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long noticeSeq;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, name = "max_people")
    private int maxPeople;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, name = "start_time")
    private LocalDateTime startTime;

    @Column(nullable = false, name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
