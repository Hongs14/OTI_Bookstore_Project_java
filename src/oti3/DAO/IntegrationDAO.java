package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.AuthorDto;
import oti3.DTO.BookHashDto;
import oti3.DTO.PagerDto;
import oti3.DTO.SearchDto;

public class IntegrationDAO {
	/*
	public static void main(String[] args) {
		IntegrationDAO id = new IntegrationDAO();
		id.Integration(ConnectionProvider.getConnection(), "주먹");
	}
	*/
	public int selectIntegrationCount(Connection conn, SearchDto searchDto) {
		int count = 0;
		try {
			String sql = ""
					+ "SELECT count(*) as count FROM "
					+ "(SELECT b.book_no, b.book_name, b.book_publisher, b.book_price "
					+ "FROM books b, author_book ab, authors a, reviews r, book_hash h "
					+ "WHERE b.book_no = ab.book_no "
					+ "AND a.author_no = ab.author_no "
					+ "AND r.book_no(+) = b.book_no "
					+ "AND h.book_no(+) = b.book_no "
					+ "AND (book_name LIKE ? OR author_name LIKE ? OR book_publisher LIKE ? OR hash_id LIKE ?) "
					+ "GROUP BY b.book_no, b.book_name, b.book_publisher, b.book_price) ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+searchDto.getSearch()+"%");
			pstmt.setString(2, "%"+searchDto.getSearch()+"%");
			pstmt.setString(3, "%"+searchDto.getSearch()+"%");
			pstmt.setString(4, "%"+searchDto.getSearch()+"%");
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt("count");
			}
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
		return count;
	}
	
	// 통합 검색 메소드
	public ArrayList<SearchDto> selectIntegration(Connection conn, SearchDto searchDto, PagerDto pagerDto) {
		ArrayList<SearchDto> IntegrationList = new ArrayList<SearchDto>();
		try {
			String sql = "SELECT * "
					+ "FROM (SELECT rownum as rn, f.book_no, f.book_name, f.book_publisher, f.book_price, f.reviews_avg "
					+ "FROM (SELECT b.book_no, b.book_name, b.book_publisher, b.book_price, avg(review_score) reviews_avg "
					+ "FROM books b, author_book ab, authors a, reviews r, book_hash h "
					+ "WHERE b.book_no = ab.book_no "
					+ "AND a.author_no = ab.author_no "
					+ "AND r.book_no(+) = b.book_no "
					+ "AND h.book_no(+) = b.book_no "
					+ "AND (book_name LIKE ? OR author_name LIKE ? OR book_publisher LIKE ? OR hash_id LIKE ?) "
					+ "GROUP BY b.book_no, b.book_name, b.book_publisher, b.book_price) f "
					+ "WHERE rownum <= ?) ff "
					+ "WHERE rn >= ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+searchDto.getSearch()+"%");
			pstmt.setString(2, "%"+searchDto.getSearch()+"%");
			pstmt.setString(3, "%"+searchDto.getSearch()+"%");
			pstmt.setString(4, "%"+searchDto.getSearch()+"%");
            pstmt.setInt(5, pagerDto.getEndRowNo());
            pstmt.setInt(6, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				SearchDto iDto = new SearchDto();
				iDto.setBook_no(rs.getInt("book_no"));
				iDto.setBook_name(rs.getString("book_name"));
				iDto.setBook_publisher(rs.getString("book_publisher"));
				iDto.setBook_price(rs.getInt("book_price"));
				iDto.setReviews_avg(rs.getInt("reviews_avg"));
				int book_no = iDto.getBook_no();
				
				// 이 책 번호로 등록된 저자 정보
				ArrayList<AuthorDto> authorlist = new ArrayList<AuthorDto>();
				try {
					String sql2 = "SELECT author_name " +
							"FROM books b, author_book ab, authors a " +
							"WHERE b.book_no = ab.book_no " +
							"AND a.author_no = ab.author_no " +
							"AND b.book_no = ? ";
					pstmt = conn.prepareStatement(sql2);
					pstmt.setInt(1, book_no);
					
					ResultSet rs2 = pstmt.executeQuery();
					
					while(rs2.next()) {
						AuthorDto aDto = new AuthorDto();
						aDto.setAuthor_name(rs2.getString("author_name"));
						authorlist.add(aDto);
					}
					rs2.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 이 책 번호로 등록된 해시태그 정보
				ArrayList<BookHashDto> hashlist = new ArrayList<BookHashDto>();
				try {
					String sql3 = "SELECT hash_id " +
							"FROM books b, book_hash h " +
							"WHERE b.book_no = h.book_no " +
							"AND b.book_no = ? ";
					pstmt = conn.prepareStatement(sql3);
					pstmt.setInt(1, book_no);
					
					ResultSet rs3 = pstmt.executeQuery();
					while(rs3.next()) {
						BookHashDto hDto = new BookHashDto();
						hDto.setHash_id(rs3.getString("hash_id"));
						hashlist.add(hDto);
					}
					rs3.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				iDto.setAuthor_name(authorlist);
				iDto.setHash_id(hashlist);
				IntegrationList.add(iDto);
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
		return IntegrationList;
	}
}
