package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.AuthorDto;
import oti3.DTO.BoardDto;
import oti3.DTO.DibDto;
import oti3.DTO.ReviewDto;

public class BookboardDAO {
	// 상세 검색 메소드
	public ArrayList<BoardDto> selectBoard(Connection conn, BoardDto boardDto) {
		ArrayList<BoardDto> BoardList = new ArrayList<BoardDto>();
		try {
			String sql = "SELECT book_name, book_detail, book_publisher, book_price, book_store, book_page, book_lang,"
					+ " to_char(book_date, 'yyyy-mm-dd') as book_date "
					+ "FROM books " + "WHERE book_no = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardDto.getBook_no());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardDto bDto = new BoardDto();
				bDto.setBook_name(rs.getString("book_name"));
				bDto.setBook_detail(rs.getString("book_detail"));
				bDto.setBook_publisher(rs.getString("book_publisher"));
				bDto.setBook_price(rs.getInt("book_price"));
				bDto.setBook_store(rs.getInt("book_store"));
				bDto.setBook_page(rs.getInt("book_page"));
				bDto.setBook_lang(rs.getString("book_lang"));
				bDto.setBook_date(rs.getString("book_date"));
				// 상세 페이지의 작가 정보
				ArrayList<AuthorDto> authorlist = new ArrayList<AuthorDto>();
				try {
					String sql2 = "" + "SELECT author_name, nvl(author_detail, '저자 정보가 없습니다.') as author_detail " + "FROM books b, author_book ab, authors a "
							+ "WHERE b.book_no = ab.book_no " + "AND ab.author_no = a.author_no "
							+ "AND b.book_no = ? ";
					pstmt = conn.prepareStatement(sql2);
					pstmt.setInt(1, boardDto.getBook_no());
					ResultSet rs2 = pstmt.executeQuery();
					while (rs2.next()) {
						AuthorDto aDto = new AuthorDto();
						aDto.setAuthor_name(rs2.getString("author_name"));
						aDto.setAuthor_detail(rs2.getString("author_detail"));
						authorlist.add(aDto);
					}
					rs2.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 상세 페이지의 리뷰 정보
				ArrayList<ReviewDto> reviewlist = new ArrayList<ReviewDto>();
				try {
					String sql3 = ""
							+ "SELECT user_id, to_char(review_date, 'yyyy-mm-dd') review_date, review_content, review_score "
							+ "FROM books b, reviews r " + "WHERE b.book_no = r.book_no " + "AND b.book_no = ? ";
					pstmt = conn.prepareStatement(sql3);
					pstmt.setInt(1, boardDto.getBook_no());
					ResultSet rs3 = pstmt.executeQuery();
					while (rs3.next()) {
						ReviewDto rDto = new ReviewDto();
						rDto.setUser_id(rs3.getString("user_id"));
						rDto.setReview_date(rs3.getString("review_date"));
						rDto.setReview_content(rs3.getString("review_content"));
						rDto.setReview_score(rs3.getInt("review_score"));
						reviewlist.add(rDto);
					}
					rs3.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				bDto.setAuthor(authorlist);
				bDto.setReview(reviewlist);
				BoardList.add(bDto);
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return BoardList;
	}

	// userId, bookNo 찜에 존재하는지 여부 조회
	public boolean selectCheckDibs(Connection conn, DibDto dibDto) {
		boolean result = false;
		try {
			String sql = "select * from dibs where book_no = ? and user_id = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dibDto.getBook_no());
			pstmt.setString(2, dibDto.getUser_id());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 상세 페이지에서 책 찜하는 메소드
	public int insertDibs(Connection conn, DibDto dibDto) {
		int result = 0;
		try {
			String sql = "INSERT INTO dibs VALUES (?, ?) ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dibDto.getBook_no());
			pstmt.setString(2, dibDto.getUser_id());
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 찜 삭제하는 메소드
	public int deleteDibs(Connection conn, DibDto dibdto) {
		int result = 0;
		try {
			String sql = "DELETE dibs WHERE (book_no = ? AND user_id = ? ) ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dibdto.getBook_no());
			pstmt.setString(2, dibdto.getUser_id());
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
