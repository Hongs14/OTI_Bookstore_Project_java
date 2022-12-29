package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oti3.DTO.ReviewDto;

public class ReviewPlusDAO {
    // 그 책에 리뷰를 추가하는 메소드
    public int insertReviewBoardPlus(Connection conn, ReviewDto reviewdto) {
        int result = 0;
        try {
            String sql = "INSERT INTO reviews " +
                    "VALUES (seq_review_no.nextval, sysdate, ?, ?, ?, ? ) ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reviewdto.getReview_content());
            pstmt.setInt(2, reviewdto.getReview_score());
            pstmt.setString(3, reviewdto.getUser_id());
            pstmt.setInt(4, reviewdto.getBook_no());
            result = pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    // 리뷰가 있는지 체크하는 메소드
    public boolean selectReviewCheck(Connection conn, ReviewDto reviewdto) {
        boolean result = false;
        try {
            String sql = "SELECT review_no " +
                    "FROM reviews " +
                    "WHERE user_id = ? " +
                    "AND book_no = ? ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reviewdto.getUser_id());
            pstmt.setInt(2, reviewdto.getBook_no());
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()) {
                result = true; // 값이 들어가면 리뷰가 이미 있음.
            }
            
            rs.close();
            pstmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    // 책을 구매했는지 확인하는 메소드
    public boolean selectOrderCheck(Connection conn, ReviewDto reviewdto) {
        boolean result = false;
        try {
            String sql = "" +
                    "SELECT book_no, o.order_no, user_id " +
                    "FROM orders o, orderdetails od " +
                    "WHERE o.order_no = od.order_no " +
                    "AND o.user_id = ? " +
                    "AND book_no = ? ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reviewdto.getUser_id());
            pstmt.setInt(2, reviewdto.getBook_no());
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
