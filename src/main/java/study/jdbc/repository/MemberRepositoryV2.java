
package study.jdbc.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.jdbc.domain.Member;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * JDBC - Connection을 파라미터로 사용
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV2 {

    private final DataSource dataSource;

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

    public Member findById(Connection conn, final String memberId) {
        String sql = "select * from member where member_id = ?";

        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
                //connection은 여기서 close하지 않는다.(트랜잭션을 유지해야 하기 때문)
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

    public void update(String memberId, int money) {
        String sql = "update member set money = ? where member_id = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("error", e);
        }
    }

    public void update(Connection conn, String memberId, int money) {
        String sql = "update member set money = ? where member_id = ?";

        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
                //connection은 여기서 close하지 않는다.(트랜잭션을 유지해야 하기 때문)
        ) {
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("error", e);
        }
    }

    public void delete(String memberId) {
        String sql = "delete from member where member_id = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("error", e);
        }
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }

}
