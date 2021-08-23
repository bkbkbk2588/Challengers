package project.challengers.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "CHALLENGE")
@Builder
public class Challenge {
    @Id
    @Column(name = "challenge_seq")
    long challengeSeq;

    @Column
    int money;

    @Column
    int status;
}
