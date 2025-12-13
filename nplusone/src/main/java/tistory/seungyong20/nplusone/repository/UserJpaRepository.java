package tistory.seungyong20.nplusone.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tistory.seungyong20.nplusone.entity.AppUser;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<AppUser, Integer> {
    @Query("SELECT u, t FROM AppUser u JOIN u.team t")
    List<AppUser> findUserAndTeam();

    @EntityGraph(attributePaths = {"team"})
    List<AppUser> findAllBy();
}
