package project.challengers.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "CHALLENGE")
@Builder
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long challengeSeq;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "notice_seq")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Notice notice;

    @Column
    private int money;

    @Column
    private int status;
}
