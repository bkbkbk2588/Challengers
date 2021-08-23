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
@Entity(name = "AUTH")
@Builder
@ApiModel("인증수단")
public class Auth {

    @Id
    @Column(name = "auth_seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long authSeq;

    @Column(name = "notice_seq")
    private long noticeSeq;

    @Column(nullable = false)
    private String id;

    @Column(name = "file_name", nullable = true)
    private String fileName;

    @Column(name = "file_path")
    private String filePath;
}
