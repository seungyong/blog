package tistory.seungyong20.nplusone;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tistory.seungyong20.nplusone.entity.AppUser;
import tistory.seungyong20.nplusone.entity.Team;
import tistory.seungyong20.nplusone.repository.TeamJpaRepository;
import tistory.seungyong20.nplusone.repository.UserJpaRepository;

import java.util.List;

@Slf4j
@SpringBootTest
public class NPlusOneTest {
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private TeamJpaRepository teamJpaRepository;

    @Test
    @DisplayName("EAGER 테스트")
    void findByUserOne() {
        AppUser user = userJpaRepository.findById(1).orElseThrow();
        log.info("user nickname: {}", user.getNickname());
    }

    @Test
    @DisplayName("EAGER 테스트 (컬렉션)")
    void nPlusOneTest() {
        List<AppUser> user = userJpaRepository.findAll();

        for (AppUser appUser : user) {
            log.info("user nickname: {}", appUser.getNickname());
        }
    }

    @Test
    @DisplayName("LAZY 테스트 (단일)")
    void lazyNPlusOneTest() {
        AppUser user = userJpaRepository.findById(1).orElseThrow();
        log.info("user nickname: {}", user.getNickname());
    }

    @Test
    @DisplayName("LAZY 테스트 (컬렉션)")
    void lazyNPlusOneCollectionTest() {
        List<AppUser> users = userJpaRepository.findAll();

        for (AppUser appUser : users) {
            log.info("user nickname: {}", appUser.getNickname());
        }
    }

    @Test
    @DisplayName("LAZY 테스트 (컬렉션, N+1 문제)")
    @Transactional
    void lazyNPlusOneCollectionProblemTest() {
        List<AppUser> users = userJpaRepository.findAll();

        for (AppUser appUser : users) {
            log.info("user nickname: {}, team name: {}", appUser.getNickname(), appUser.getTeam().getName());
        }
    }

    @Test
    @DisplayName("Fetch Join을 통한 N+1 문제 해결")
    void fetchJoinNPlusOneTest() {
        List<AppUser> users = userJpaRepository.findUserAndTeam();

        for (AppUser appUser : users) {
            log.info("user nickname: {}, team name: {}", appUser.getNickname(), appUser.getTeam().getName());
        }
    }

    @Test
    @DisplayName("Entity Graph를 통한 N+1 문제 해결")
    void entityGraphNPlusTest() {
        List<AppUser> users = userJpaRepository.findAllBy();

        for (AppUser appUser : users) {
            log.info("user nickname: {}, team name: {}", appUser.getNickname(), appUser.getTeam().getName());
        }
    }
}
