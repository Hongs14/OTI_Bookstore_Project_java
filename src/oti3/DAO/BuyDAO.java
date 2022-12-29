package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.BookDto;
import oti3.DTO.OrderDto;
import oti3.DTO.OrderdetailDto;
import oti3.DTO.UserDto;

public class BuyDAO {
	// 바로구매 + 장바구니 구매 처리 dao
	public boolean insertBuy(Connection conn, OrderDto orderdto, ArrayList<OrderdetailDto> list) {
		boolean requestDone = true;
		try {
			// 트랜잭션 처리
			conn.setAutoCommit(false);

			// 1. orderDto를 orders 테이블에 한 행 추가
			String sql = "insert into orders values" + " (seq_order_no.nextval, sysdate, ?, ?, ?, ?, ?, 'Y')";
			PreparedStatement pstmt = conn.prepareStatement(sql, new String[] { "order_no" });
			pstmt.setString(1, orderdto.getUser_id());
			pstmt.setString(2, orderdto.getOrder_receivename());
			pstmt.setString(3, orderdto.getOrder_tel());
			pstmt.setString(4, orderdto.getOrder_address());
			pstmt.setString(5, orderdto.getOrder_memo());

			// order 레코드 추가 실패 = 트랜잭션 전체의 실패 -> rollback
			if (pstmt.executeUpdate() == 0) {
				requestDone = false;
				throw new Exception();
			}

			ResultSet rs = pstmt.getGeneratedKeys();
			int originalOrderNo = 0;
			if (rs.next()) {
				originalOrderNo = rs.getInt(1);
			}

			// 2. orderdetails 테이블에 list안에 각 요소들 추가(테이블 칼럼형식에 맞게)
			for (OrderdetailDto od : list) {
				sql = "insert into orderdetails values(?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, originalOrderNo);
				pstmt.setInt(2, od.getBook_no());
				pstmt.setInt(3, od.getOd_qty());

				// orderdetails 레코드 추가 실패 = 트랜잭션 전체의 실패 -> rollback
				if (pstmt.executeUpdate() == 0) {
					requestDone = false;
					throw new Exception();
				}
			}

			// 3. warehousing 테이블에 list안에 각 요소들 추가(테이블 칼럼형식에 맞게)
			for (OrderdetailDto od : list) {
				sql = "insert into warehousing values(seq_ware_no.nextval, sysdate, ?, 'OUT', ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(2, od.getBook_no());
				pstmt.setInt(1, od.getOd_qty());

				// warehousing 레코드 추가 실패 = 트랜잭션 전체의 실패 -> rollback
				if (pstmt.executeUpdate() == 0) {
					requestDone = false;
					throw new Exception();
				}
			}

			// 4. books테이블에 list안에 각 요소들 참고해서 book_store컬럼 빼기 처리
			for (OrderdetailDto od : list) {
				sql = "update books set book_store = book_store - ? where book_no = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, od.getOd_qty());
				pstmt.setInt(2, od.getBook_no());

				// book_store 컬럼값 변경 실패 = 트랜잭션 전체의 실패 -> rollback
				if (pstmt.executeUpdate() == 0) {
					requestDone = false;
					throw new Exception();
				}
			}

			// 5. 총 가격 구하기
			int sum = 0;
			for (OrderdetailDto od : list) {
				sql = "select book_price from books where book_no = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, od.getBook_no());
				ResultSet rsss = pstmt.executeQuery();
				if (rsss.next()) {
					sum = sum + (rsss.getInt("book_price") * od.getOd_qty());
				}
			}

			// 6. 총 가격 알아낸 후 users 테이블 유저 머니 안에 체크 후 뺴기
			sql = "UPDATE users SET user_money = user_money - ? WHERE user_id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sum);
			pstmt.setString(2, orderdto.getUser_id());

			if (pstmt.executeUpdate() == 0) {
				requestDone = false;
				throw new Exception();
			}

			conn.commit();
		} catch (Exception e) {
			System.out.println("요청한 작업이 정상적으로 처리되지 못했습니다.");
			try {
				conn.rollback();
			} catch (SQLException e1) {
				System.out.println("작업이 제대로 종료되지 않았습니다. ");
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					// 하나의 커넥션 객체를 이용하기 때문에 오토커밋 다시 원상복구
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return requestDone;
	}

	// 구매하기 위해 book_no에 맞는 책 재고 조회
	public ArrayList<BookDto> selectBookStore(Connection conn, BookDto bookdto) {
		ArrayList<BookDto> BookStoreList = new ArrayList<BookDto>();
		try {
			String sql = "" + "SELECT book_no, book_store " + "FROM books " + "WHERE book_no = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookdto.getBook_no());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BookDto bDto = new BookDto();
				bDto.setBook_no(rs.getInt("book_no"));
				bDto.setBook_store(rs.getInt("book_store"));
				BookStoreList.add(bDto);
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
		return BookStoreList;
	}

	// 구매하기 위해 user_id에 맞는 user_money 조회
	public ArrayList<UserDto> selectUserMoney(Connection conn, UserDto userdto) {
		ArrayList<UserDto> UserMoneyList = new ArrayList<UserDto>();
		try {
			String sql = "" + "SELECT user_id, user_money " + "FROM users " + "WHERE user_id = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userdto.getUser_id());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				UserDto uDto = new UserDto();
				uDto.setUser_id(rs.getString("user_id"));
				uDto.setUser_money(rs.getInt("user_money"));
				UserMoneyList.add(uDto);
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
		return UserMoneyList;
	}
}
