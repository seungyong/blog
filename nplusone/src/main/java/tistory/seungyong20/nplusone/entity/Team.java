package tistory.seungyong20.nplusone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "Team")
@Table(name = "n_team")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Team {
    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer teamId;

    @Column(name = "name")
    private String name;
}
