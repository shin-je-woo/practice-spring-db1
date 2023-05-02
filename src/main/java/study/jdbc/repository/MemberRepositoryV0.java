package study.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import study.jdbc.connection.DBConccectionUtil;
import study.jdbc.domain.Member;

import java.sql.*;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        try (
                Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            int count = pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("error", e);
        }
        return member;
    }

    private Connection getConnection() {
        return DBConccectionUtil.getConnection();
    }

}
