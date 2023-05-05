package study.jdbc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.jdbc.domain.Member;
import study.jdbc.repository.MemberRepositoryV2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - Connection 파라미터, 커넥션 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        try (Connection con = dataSource.getConnection()) {
            try {
                con.setAutoCommit(false); //트랜잭션 시작
                //비즈니스 로직 수행
                bizLogic(con, fromId, toId, money);
                con.commit(); //성공 시 커밋
            } catch (Exception e) {
                con.rollback(); //실패 시 롤백
                throw new IllegalStateException(e);
            } finally {
                log.info("==================트랜잭션 작업 수행 후 Connection반납할 때 autoCommit을 돌려놓습니다.");
                con.setAutoCommit(true); //커넥션 풀을 고려해서 Connection반납할 때는 autoCommit을 true로 돌려놓고 반납
            }
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
