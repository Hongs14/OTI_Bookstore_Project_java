package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.AuthorBookDto;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookDto;
import oti3.DTO.BookHashDto;
import oti3.DTO.PagerDto;
import oti3.DTO.WarehousingDto;

public class AdminBookDao {
	// 상품관리
	// 책 제목 검색 행 수
	public int adminBookListByBookNameCount(Connection conn, BookDto book) {
		int count = 0;
		try {
			String sql = "select count(*) as count from books where book_name like ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + book.getBook_name() + "%");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
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
		return count;

	}

	// 책 제목 검색 복수행 결과값
	public ArrayList<BookDto> adminBookListByBookName(Connection conn, PagerDto pagerDto, BookDto book) {
		ArrayList<BookDto> list = new ArrayList<>();
		try {
			// 책 정보
			String sql = "select *" + " from"
					+ " (select rownum rnum, b.book_no, b.book_name, b.book_publisher, b.book_price,"
					+ " b.book_page, to_char(book_date, 'yyyy-mm-dd') as book_date,"
					+ " sc.sub_name as sub_name, c.category_name as category_name"
					+ " from books b, subcategory sc, category c"
					+ " where b.sub_no = sc.sub_no and sc.category_no = c.category_no and b.book_name like ? and rownum <= ?)"
					+ " where rnum >= ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + book.getBook_name() + "%");
			pstmt.setInt(2, pagerDto.getEndRowNo());
			pstmt.setInt(3, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BookDto bookDto = new BookDto();
				ArrayList<AuthorDto> authorList = new ArrayList<>();
				sql = "select a.author_no, a.author_name, nvl(a.author_detail, '저자 소개글이 없습니다.') as author_detail from authors a, author_book ab, books b "
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
				bookDto.setBook_page(rs.getInt("book_page"));
				bookDto.setBook_date(rs.getString("book_date"));
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

	// 출간일 순 행 수
	public int adminBookListOrderByPublishDateCount(Connection conn) {
		int count = 0;
		try {
			String sql = "select count(*) as count from books";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
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
		return count;

	}

	// 전체 책 목록 검색(출간일 순)
	public ArrayList<BookDto> adminBookListOrderByPublishDate(Connection conn, PagerDto pagerDto) {
		ArrayList<BookDto> list = new ArrayList<>();
		try {
			// 책 정보
			String sql = "select * from" + " (select rownum as rnum, s1.* from"
					+ " (select b.book_no, b.book_name, b.book_publisher, b.book_price,"
					+ " b.book_page, to_char(book_date, 'yyyy-mm-dd') as book_date,"
					+ " sc.sub_name as sub_name, c.category_name as category_name"
					+ " from books b, subcategory sc, category c"
					+ " where b.sub_no = sc.sub_no and sc.category_no = c.category_no order by book_date desc) s1"
					+ " where rownum <= ?)" + " where rnum >= ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pagerDto.getEndRowNo());
			pstmt.setInt(2, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BookDto bookDto = new BookDto();
				ArrayList<AuthorDto> authorList = new ArrayList<>();
				sql = "select a.author_no, a.author_name, nvl(a.author_detail, '저자 소개글이 없습니다.') as author_detail from authors a, author_book ab, books b "
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
				bookDto.setBook_page(rs.getInt("book_page"));
				bookDto.setBook_date(rs.getString("book_date"));
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

	// 판매량 순 행 수
	public int adminBookListBySalesCount(Connection conn) {
		int count = 0;
		try {
			String sql = "select count(*) as count from books";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
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
		return count;

	}

	// 전체 책 목록 검색(판매량 순 - 주문 취소건은 합산x)
	public ArrayList<BookDto> adminBookListBySales(Connection conn, PagerDto pagerDto) {
		ArrayList<BookDto> list = new ArrayList<>();
		try {
			// 책 정보
			String sql = "select * from" + " (select rownum as rnum, s1.* from"
					+ " (select b.book_no, b.book_name, b.book_publisher, b.book_price,"
					+ " b.book_page, to_char(book_date, 'yyyy-mm-dd') as book_date,"
					+ " sc.sub_name as sub_name, c.category_name as category_name,"
					+ " (select nvl(sum(od_qty), 0) from orderdetails od, orders o where od.order_no = o.order_no and od.book_no = b.book_no and o.order_status != 'N') as sales"
					+ " from books b, subcategory sc, category c"
					+ " where b.sub_no = sc.sub_no and sc.category_no = c.category_no order by sales desc) s1"
					+ " where rownum <= ?)" + " where rnum >= ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pagerDto.getEndRowNo());
			pstmt.setInt(2, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BookDto bookDto = new BookDto();
				ArrayList<AuthorDto> authorList = new ArrayList<>();
				sql = "select a.author_no, a.author_name, nvl(a.author_detail, '저자 소개글이 없습니다.') as author_detail from authors a, author_book ab, books b "
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
				bookDto.setBook_page(rs.getInt("book_page"));
				bookDto.setSales(rs.getInt("sales"));
				bookDto.setBook_date(rs.getString("book_date"));
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

	// 책 상세정보(to do) 책기본정보 + 저자 다수정보 + 해시태그 다수정보
	public BookDto adminBookInfo(Connection conn, BookDto bookDto) {
		BookDto book = new BookDto();
		try {
			// 1) 책 정보 담기
			String sql = "select b.book_no, b.book_name, b.book_publisher, b.book_detail, b.book_price, b.book_page,"
					+ " b.book_lang, to_char(b.book_date, 'yyyy-mm-dd') as book_date, b.book_store, b.sub_no, sc.sub_name as sub_name, c.category_name as category_name"
					+ " from books b, subcategory sc, category c where b.sub_no = sc.sub_no and sc.category_no = c.category_no and book_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookDto.getBook_no());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				book.setBook_no(rs.getInt("book_no"));
				book.setBook_name(rs.getString("book_name"));
				book.setBook_publisher(rs.getString("book_publisher"));
				book.setBook_detail(rs.getString("book_detail"));
				book.setBook_price(rs.getInt("book_price"));
				book.setBook_page(rs.getInt("book_page"));
				book.setBook_lang(rs.getString("book_lang"));
				book.setBook_date(rs.getString("book_date"));
				book.setBook_store(rs.getInt("book_store"));
				book.setSub_no(rs.getInt("sub_no"));
				book.setSub_name(rs.getString("sub_name"));
				book.setCategory_name(rs.getString("category_name"));

				// 2) 저자 정보 여러개 담기
				ArrayList<AuthorDto> authorList = new ArrayList<>();
				sql = "select a.author_no, a.author_name, nvl(author_detail, '저자 소개글이 없습니다.') as author_detail from authors a, author_book ab, books b "
						+ "where a.author_no = ab.author_no and b.book_no = ab.book_no " + "and b.book_no = ?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bookDto.getBook_no());
				ResultSet rss = pstmt.executeQuery();
				while (rss.next()) {
					AuthorDto authorDto = new AuthorDto();
					authorDto.setAuthor_no(rss.getInt("author_no"));
					authorDto.setAuthor_name(rss.getString("author_name"));
					authorDto.setAuthor_detail(rss.getString("author_detail"));
					authorList.add(authorDto);
				}
				book.setAuthors(authorList);

				// 3) 책 해시태그 정보 여러개 담기
				ArrayList<BookHashDto> hashList = new ArrayList<>();
				sql = "select bh.* from books b, book_hash bh " + "where b.book_no = bh.book_no and b.book_no = ?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bookDto.getBook_no());
				ResultSet rsss = pstmt.executeQuery();
				while (rsss.next()) {
					BookHashDto bookHashDto = new BookHashDto();
					bookHashDto.setBook_no(rsss.getInt("book_no"));
					bookHashDto.setHash_id(rsss.getString("hash_id"));
					hashList.add(bookHashDto);
				}
				book.setHashtags(hashList);

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
		return book;
	}

	// 특정 책의 저자 정보 조회
	public ArrayList<AuthorDto> adminBookAuthorInfo(Connection conn, BookDto bookDto) {
		ArrayList<AuthorDto> list = new ArrayList<>();
		try {
			// 책 정보 변경
			String sql = "select a.author_no, a.author_name, nvl(author_detail, '저자 소개글이 없습니다.') as author_detail from author_book ab, authors a where ab.author_no = a.author_no and ab.book_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookDto.getBook_no());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AuthorDto authorDto = new AuthorDto();
				authorDto.setAuthor_no(rs.getInt("author_no"));
				authorDto.setAuthor_name(rs.getString("author_name"));
				authorDto.setAuthor_detail(rs.getString("author_detail"));
				list.add(authorDto);
			}
		} catch (Exception e) {
			System.out.println("요청이 정상적으로 처리되지 못했습니다.");

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

	// 책 정보 수정
	public int adminBookUpdate(Connection conn, BookDto bookDto) {
		int rows = 0;
		try {
			// 책 정보 변경
			String sql = "update books set book_name = ?, book_publisher = ?, book_detail = ?,"
					+ " book_price = ?, book_page = ?, book_lang = ?, book_date = to_date(?, 'yyyy-mm-dd'), sub_no = ? where book_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bookDto.getBook_name());
			pstmt.setString(2, bookDto.getBook_publisher());
			pstmt.setString(3, bookDto.getBook_detail());
			pstmt.setInt(4, bookDto.getBook_price());
			pstmt.setInt(5, bookDto.getBook_page());
			pstmt.setString(6, bookDto.getBook_lang());
			pstmt.setString(7, bookDto.getBook_date());
			pstmt.setInt(8, bookDto.getSub_no());
			pstmt.setInt(9, bookDto.getBook_no());
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("요청이 정상적으로 처리되지 못했습니다.");

		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rows;
	}

	// 책에 저자 추가
	public int adminBookAuthorAdd(Connection conn, AuthorBookDto authorBookDto) {
		int rows = 0;
		try {
			String sql = "insert into author_book values(?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, authorBookDto.getAuthor_no());
			pstmt.setInt(2, authorBookDto.getBook_no());
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			// 이미 있는 쌍 or 잘못된 bookNo, authorNo
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
		return rows;
	}

	// 책의 저자 삭제
	public int adminBookAuthorPop(Connection conn, AuthorBookDto authorBookDto) {
		int rows = 0;
		try {
			String sql = "delete from author_book where author_no = ? and book_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, authorBookDto.getAuthor_no());
			pstmt.setInt(2, authorBookDto.getBook_no());
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			// 없는 데이터를 지우라고 할 경우
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
		return rows;
	}

	// 특정 책의 해시태그 정보 조회
	public ArrayList<BookHashDto> adminBookHashInfo(Connection conn, BookDto bookDto) {
		ArrayList<BookHashDto> list = new ArrayList<>();
		try {
			// 책 정보 변경
			String sql = "select * from book_hash where book_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookDto.getBook_no());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BookHashDto bookHashDto = new BookHashDto();
				bookHashDto.setBook_no(rs.getInt("book_no"));
				bookHashDto.setHash_id(rs.getString("hash_id"));
				list.add(bookHashDto);
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

	// 책 해시태그 추가(hashId는 20자 이하)
	public int adminHashTagAdd(Connection conn, BookHashDto bookHashDto) {
		int rows = 0;
		try {
			String sql = "insert into book_hash values (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookHashDto.getBook_no());
			pstmt.setString(2, bookHashDto.getHash_id());
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			// bookNo가 없거나 이미 쌍이 존재하거나 hashId가 20자 초과하거나
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
		return rows;
	}

	// 책 해시태그 삭제
	public int adminHashTagPop(Connection conn, BookHashDto bookHashDto) {
		int rows = 0;
		try {
			String sql = "delete from book_hash where book_no = ? and hash_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookHashDto.getBook_no());
			pstmt.setString(2, bookHashDto.getHash_id());
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			// 없는 데이터를 지우라고 할 경우
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
		return rows;
	}

	// 책 삭제 -> rows = 0이면 실패 1이면 성공
	public int adminBookDelete(Connection conn, BookDto bookDto) {
		int rows = 0;
		try {
			String sql = "delete from books where book_no = ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookDto.getBook_no());
			rows = pstmt.executeUpdate();
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
		return rows;
	}

	// 책 추가
	public int adminBookCreate(Connection conn, BookDto bookDto) {
		int rows = 0;
		try {
			String sql = "insert into books values (seq_book_no.nextval, ?, ?, ?, ?, ?, ?, ?, 0, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bookDto.getBook_name());
			pstmt.setString(2, bookDto.getBook_publisher());
			pstmt.setString(3, bookDto.getBook_detail());
			pstmt.setInt(4, bookDto.getBook_price());
			pstmt.setInt(5, bookDto.getBook_page());
			pstmt.setString(6, bookDto.getBook_lang());
			pstmt.setString(7, bookDto.getBook_date());
			pstmt.setInt(8, bookDto.getSub_no());
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("요청이 정상적으로 처리되지 못했습니다.");
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
		return rows;
	}

	// 책 재고 추가 = warehousing 행 추가 + book_store 값 + => 트랜잭션 처리
	public boolean adminWareHousingAdd(Connection conn, WarehousingDto warehousingDto) {
		boolean warehousingAndBookStoreDone = true;
		try {
			conn.setAutoCommit(false);

			String sql = "insert into warehousing values(seq_ware_no.nextval, sysdate, ?, 'IN', ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(2, warehousingDto.getBook_no());
			pstmt.setInt(1, warehousingDto.getWare_amount());
			if (pstmt.executeUpdate() == 0) {
				warehousingAndBookStoreDone = false;
				throw new Exception();
			}
			;

			sql = "update books set book_store = book_store + ? where book_no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, warehousingDto.getWare_amount());
			pstmt.setInt(2, warehousingDto.getBook_no());
			if (pstmt.executeUpdate() == 0) {
				warehousingAndBookStoreDone = false;
				throw new Exception();
			}
			;
			conn.commit();
		} catch (Exception e) {
			System.out.println("요청이 제대로 처리되지 않았습니다.");
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				System.out.println("롤백이 제대로 처리되지 않았습니다.");
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return warehousingAndBookStoreDone;
	}

	// 상품 입출고 이력 행 수
	public int adminWareHistorySearchCount(Connection conn, BookDto bookDto) {
		int rows = 0;
		try {
			String sql = "select count(*) as count from warehousing where book_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookDto.getBook_no());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				rows = rs.getInt("count");
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
		return rows;
	}

	// 상품 입출고 이력 확인(입출고 날짜 최신 순)
	public ArrayList<WarehousingDto> adminWareHistorySearch(Connection conn, BookDto bookDto, PagerDto pagerDto) {
		ArrayList<WarehousingDto> list = new ArrayList<>();
		try {
			String sql = "select * from" + " (select rownum as rnum, w.* from"
					+ " (select ware_no, book_no, to_char(ware_date, 'yyyy-mm-dd') as ware_date, ware_amount, ware_status"
					+ " from warehousing where book_no = ? order by ware_date desc) w where rownum <= ?) where rnum  >= ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookDto.getBook_no());
			pstmt.setInt(2, pagerDto.getEndRowNo());
			pstmt.setInt(3, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				WarehousingDto wh = new WarehousingDto();
				wh.setWare_no(rs.getInt("ware_no"));
				wh.setBook_no(rs.getInt("book_no"));
				wh.setWare_date(rs.getString("ware_date"));
				wh.setWare_amount(rs.getInt("ware_amount"));
				wh.setWare_status(rs.getString("ware_status"));
				list.add(wh);
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
