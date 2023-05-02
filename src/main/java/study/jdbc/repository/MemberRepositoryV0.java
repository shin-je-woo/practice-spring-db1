package study.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import study.jdbc.connection.DBConccectionUtil;
import study.jdbc.domain.Member;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) {
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
        return null;
    }

    public Member findById(final String memberId) {
        String sql = "select * from member where member_id = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    Member member = new Member();
                    member.setMemberId(rs.getString("member_id"));
                    member.setMoney(rs.getInt("money"));
                    return member;
                } else {
                    throw new NoSuchElementException("member not found memberId = " + memberId);
                }
            } catch (SQLException e) {
                log.error("error", e);
            }
        } catch (SQLException e) {
            log.error("error", e);
        }
        return null;
    }

    private Connection getConnection() {
        return DBConccectionUtil.getConnection();
    }

}
