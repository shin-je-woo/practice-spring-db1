package study.jdbc.repository;

import org.junit.jupiter.api.Test;
import study.jdbc.domain.Member;

import java.sql.SQLException;

class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        Member member = new Member("memberV0", 10000);
        Member save = repository.save(member);
    }
}