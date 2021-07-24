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
@Entity(name = "NOTICE_FILE")
@Builder
@ApiModel("챌린저스 모집 게시글 파일")
public class NoticeFile {
    @Id
    @Column(name = "file_seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value="챌린저 게시글 이미지 순번", example="1")
    private long fileSeq;

    @Column(name = "notice_seq")
    @ApiModelProperty(value="챌린저 게시글 순번", example="1")
    private long noticeSeq;

    @Column(nullable = true, name = "image_path")
    @ApiModelProperty(value="사진경로", example="C://a.png")
    private String imagePath;
}
