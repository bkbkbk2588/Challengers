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
    private long fileSeq;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notice_seq", nullable = false)
    private Notice notice;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;
}
