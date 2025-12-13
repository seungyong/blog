package tistory.seungyong20.nplusone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tistory.seungyong20.nplusone.entity.AppUser;
import tistory.seungyong20.nplusone.entity.Team;

import java.util.List;

public interface TeamJpaRepository extends JpaRepository<Team, Integer> {
}
