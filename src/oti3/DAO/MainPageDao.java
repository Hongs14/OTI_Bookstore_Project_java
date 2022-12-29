package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.UserDto;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookDto;

public class MainPageDao {

	public static void main(String[] args) {
//		mainPageDao.mainPageBestSellerList();

//      java.sql.date 객체를 넘겨줘야 함 
//		ArrayList<mainPageBook> list = mainPageDao.mainPageGenderAgeList("M", Date.valueOf("1997-04-02"));
//		for (mainPageBook b : list) {
//			System.out.println(b.getBook_name());
//		}

//		ArrayList<mainPageBook> list = mainPageDao.mainPageRecentBookList();
//		for (mainPageBook b : list) {
//			System.out.println(b.getBook_name());
//		}
	}

	// 메인페이지 금주의 Best seller Top-5
	public ArrayList<BookDto> mainPageBestSellerList(Connection conn) {
		ArrayList<BookDto> list = new ArrayList<>();
		try {
			// 책 정보
			String sql = "select rownum, b.book_no, book_name, book_publisher, book_price, book_page,"
					+ " to_char(book_date, 'yyyy-mm-dd') as book_date, sub_name, category_name"
					+ " from (select book_no, sum(od_qty) as sales from orders o, orderdetails od"
					+ " where o.order_no = od.order_no and o.order_date between TRUNC(sysdate, 'iw') and TRUNC(sysdate,'iw')+ 6"
					+ " group by book_no order by sales desc) g, books b,"
					+ " category c, subcategory s where b.sub_no = s.sub_no and g.book_no = b.book_no"
					+ " and s.category_no = c.category_no and rownum <= 5";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BookDto bookDto = new BookDto();
				ArrayList<AuthorDto> authorList = new ArrayList<>();
				sql = "select a.author_no, a.author_name, nvl(author_detail, '저자 정보가 없습니다.') as author_detail from authors a, author_book ab, books b "
						+ "where a.author_no = ab.author_no and b.book_no = ab.book_no " + "and b.book_no = ?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, rs.getInt("book_no"));
				ResultSet rss = pstmt.executeQuery();
				while (rss.next()) {
					AuthorDto authorDto = new AuthorDto();
					authorDto.setAuthor_no(rss.getInt("author_no"));
					authorDto.setAuthor_name(rss.getString("author_name"));
					authorDto.setAuthor_detail(rss.getString("author_detail"));
					authorList.add(authorDto);
				}

				bookDto.setAuthors(authorList);
				bookDto.setBook_no(rs.getInt("book_no"));
				bookDto.setBook_name(rs.getString("book_name"));
				bookDto.setBook_publisher(rs.getString("book_publisher"));
				bookDto.setBook_price(rs.getInt("book_price"));
				bookDto.setBook_date(rs.getString("book_date"));
				bookDto.setBook_page(rs.getInt("book_page"));
				bookDto.setSub_name(rs.getString("sub_name"));
				bookDto.setCategory_name(rs.getString("category_name"));
				list.add(bookDto);
			}

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
		return list;
	}

	// 메인페이지 로그인 한 유저의 나이 + 성별에서 많이 팔린 책 TOP 5
	public ArrayList<BookDto> mainPageGenderAgeList(Connection conn, UserDto userDto) {
		ArrayList<BookDto> list = new ArrayList<>();
		try {
			// 책 정보
			String sql = "select rownum, b.book_no, book_name, book_publisher, book_price, book_page,"
					+ " to_char(book_date, 'yyyy-mm-dd') as book_date, book_store, sub_name, category_name"
					+ " from (select book_no, sum(od_qty) as sales from orders o, orderdetails od, users u"
					+ " where o.order_no = od.order_no and o.user_id = u.user_id"
					+ " and u.user_gender = ? and floor((sysdate - to_date(?, 'yyyy-mm-dd')) /365 + 1)"
					+ " between floor(floor((sysdate - to_date(?, 'yyyy-mm-dd')) / 365 + 1) / 10) * 10 and floor(floor((sysdate - to_date(?, 'yyyy-mm-dd')) / 365 + 1) / 10) * 10 + 9"
					+ " group by book_no order by sales desc) g, books b"
					+ " , category c, subcategory s where b.sub_no = s.sub_no and g.book_no = b.book_no"
					+ " and s.category_no = c.category_no and rownum <= 5";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userDto.getUser_gender() + "");
			pstmt.setString(2, userDto.getUser_birth());
			pstmt.setString(3, userDto.getUser_birth());
			pstmt.setString(4, userDto.getUser_birth());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BookDto bookDto = new BookDto();
				ArrayList<AuthorDto> authorList = new ArrayList<>();
				sql = "select a.author_no, a.author_name, nvl(author_detail, '저자 정보가 없습니다.') as author_detail from authors a, author_book ab, books b "
						+ "where a.author_no = ab.author_no and b.book_no = ab.book_no " + "and b.book_no = ?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, rs.getInt("book_no"));
				ResultSet rss = pstmt.executeQuery();
				while (rss.next()) {
					AuthorDto authorDto = new AuthorDto();
					authorDto.setAuthor_no(rss.getInt("author_no"));
					authorDto.setAuthor_name(rss.getString("author_name"));
					authorDto.setAuthor_detail(rss.getString("author_detail"));
					authorList.add(authorDto);
				}

				bookDto.setAuthors(authorList);
				bookDto.setBook_no(rs.getInt("book_no"));
				bookDto.setBook_name(rs.getString("book_name"));
				bookDto.setBook_publisher(rs.getString("book_publisher"));
				bookDto.setBook_price(rs.getInt("book_price"));
				bookDto.setBook_date(rs.getString("book_date"));
				bookDto.setBook_page(rs.getInt("book_page"));
				bookDto.setSub_name(rs.getString("sub_name"));
				bookDto.setCategory_name(rs.getString("category_name"));
				list.add(bookDto);
			}

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
		return list;
	}

	// 메인페이지 최신도서 Top5
	public ArrayList<BookDto> mainPageRecentBookList(Connection conn) {
		ArrayList<BookDto> list = new ArrayList<>();
		try {
			// 책 정보
			String sql = "select rownum, b.book_no, book_name, book_publisher, book_price, book_page,"
					+ " to_char(book_date, 'yyyy-mm-dd') as book_date, book_store, sub_name, category_name"
					+ " from (select od.book_no, sum(od_qty) as sales from orderdetails od, books b"
					+ " where od.book_no = b.book_no group by od.book_no, b.book_date"
					+ " order by b.book_date desc, sales desc) g, books b"
					+ " , category c, subcategory s where b.sub_no = s.sub_no and g.book_no = b.book_no"
					+ " and s.category_no = c.category_no and rownum <= 5";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BookDto bookDto = new BookDto();
				ArrayList<AuthorDto> authorList = new ArrayList<>();
				sql = "select a.author_no, a.author_name, nvl(author_detail, '저자 정보가 없습니다.') as author_detail from authors a, author_book ab, books b "
						+ "where a.author_no = ab.author_no and b.book_no = ab.book_no " + "and b.book_no = ?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, rs.getInt("book_no"));
				ResultSet rss = pstmt.executeQuery();
				while (rss.next()) {
					AuthorDto authorDto = new AuthorDto();
					authorDto.setAuthor_no(rss.getInt("author_no"));
					authorDto.setAuthor_name(rss.getString("author_name"));
					authorDto.setAuthor_detail(rss.getString("author_detail"));
					authorList.add(authorDto);
				}

				bookDto.setAuthors(authorList);
				bookDto.setBook_no(rs.getInt("book_no"));
				bookDto.setBook_name(rs.getString("book_name"));
				bookDto.setBook_publisher(rs.getString("book_publisher"));
				bookDto.setBook_price(rs.getInt("book_price"));
				bookDto.setBook_date(rs.getString("book_date"));
				bookDto.setBook_page(rs.getInt("book_page"));
				bookDto.setSub_name(rs.getString("sub_name"));
				bookDto.setCategory_name(rs.getString("category_name"));
				list.add(bookDto);
			}

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
		return list;
	}
}
