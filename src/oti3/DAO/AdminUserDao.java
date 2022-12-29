package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.OrderDetailBookDto;
import oti3.DTO.OrderDto;
import oti3.DTO.PagerDto;
import oti3.DTO.UserDto;

public class AdminUserDao {
	/*
	 * 회원 관리
	 */

	// 회원 전체 검색 행 수
	public int adminUserListCount(Connection conn) {
		int count = 0;
		try {
			String sql = "select count(*) as count from users";
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

	// 탈퇴 요청 회원 목록 행 수
	public int adminDeleteRequestUserListCount(Connection conn) {
		int count = 0;
		try {
			String sql = "select count(*) as count from users where user_delete = 'R'";
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

	// 회원 id 검색 행 수
	public int adminUserListByIdCount(Connection conn, UserDto userDto) {
		int count = 0;
		try {
			String sql = "select count(*) as count from users where user_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userDto.getUser_id());
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

	// 회원 전체 검색
	public ArrayList<UserDto> adminUserList(Connection conn, PagerDto pagerDto) {
		ArrayList<UserDto> list = new ArrayList<>();
		try {
			String sql = "select * from (select rownum as rnum, user_id, user_name, to_char(user_date, 'yyyy-mm-dd') as user_date, user_delete from users"
					+ " where rownum <= ?) where rnum >= ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pagerDto.getEndRowNo());
			pstmt.setInt(2, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				UserDto ud = new UserDto();
				ud.setUser_id(rs.getString("user_id"));
				ud.setUser_name(rs.getString("user_name"));
				ud.setUser_date(rs.getString("user_date"));
				ud.setUser_delete(rs.getString("user_delete").charAt(0));
				list.add(ud);
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

	// 탈퇴 요청 회원 목록
	public ArrayList<UserDto> adminDeleteRequestUserList(Connection conn, PagerDto pagerDto) {
		ArrayList<UserDto> list = new ArrayList<>();
		try {
			String sql = "select * from (select rownum rnum, user_id, user_name, to_char(user_date, 'yyyy-mm-dd') as user_date, user_delete "
					+ "from users where user_delete = 'R' and rownum <= ?) where rnum >= ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pagerDto.getEndRowNo());
			pstmt.setInt(2, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				UserDto ud = new UserDto();
				ud.setUser_id(rs.getString("user_id"));
				ud.setUser_name(rs.getString("user_name"));
				ud.setUser_date(rs.getString("user_date"));
				ud.setUser_delete(rs.getString("user_delete").charAt(0));
				list.add(ud);
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

	// 회원 id 검색
	public ArrayList<UserDto> adminUserListById(Connection conn, PagerDto pagerDto, UserDto userDto) {
		ArrayList<UserDto> list = new ArrayList<>();
		try {
			String sql = "select * from (select rownum rnum, user_id, user_name, to_char(user_date, 'yyyy-mm-dd') as user_date, user_delete "
					+ "from users where user_id like ? and rownum <= ?) where rnum >= ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + userDto.getUser_id() + "%");
			pstmt.setInt(2, pagerDto.getEndRowNo());
			pstmt.setInt(3, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				UserDto ud = new UserDto();
				ud.setUser_id(rs.getString("user_id"));
				ud.setUser_name(rs.getString("user_name"));
				ud.setUser_date(rs.getString("user_date"));
				ud.setUser_delete(rs.getString("user_delete").charAt(0));
				list.add(ud);
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

	// 회원 상세정보
	public UserDto adminUserInfo(Connection conn, UserDto userDto) {
		UserDto ud = new UserDto();
		try {
			String sql = "select user_id, user_name, user_password, user_email, to_char(user_birth, 'yyyy-mm-dd') as user_birth,"
					+ " user_gender, user_tel, user_address, user_money, user_point, to_char(user_date, 'yyyy-mm-dd') as user_date,"
					+ " user_delete, decode(user_dreq_date, null, 'none', to_char(user_dreq_date, 'yyyy-mm-dd')) as user_dreq_date from users where user_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userDto.getUser_id());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				ud.setUser_id(rs.getString("user_id"));
				ud.setUser_name(rs.getString("user_name"));
				ud.setUser_password(rs.getString("user_password"));
				ud.setUser_email(rs.getString("user_email"));
				ud.setUser_birth(rs.getString("user_birth"));
				ud.setUser_gender(rs.getString("user_gender").charAt(0));
				ud.setUser_tel(rs.getString("user_tel"));
				ud.setUser_address(rs.getString("user_address"));
				ud.setUser_money(rs.getInt("user_money"));
				ud.setUser_point(rs.getInt("user_point"));
				ud.setUser_date(rs.getString("user_date"));
				ud.setUser_delete(rs.getString("user_delete").charAt(0));
				ud.setUser_dreq_date(rs.getString("user_dreq_date"));
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
		return ud;
	}

	// 회원 탈퇴 가능 여부(user_dreq_date로부터 15일 지났는지 + user_delete가 'R'인지)
	public boolean adminCanDeleteUser(Connection conn, UserDto userDto) {
		boolean canDelete = false;
		try {
			String sql = "select user_delete from users where user_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userDto.getUser_id());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				char deleteStatus = rs.getString("user_delete").charAt(0);
				if (deleteStatus == 'R') {
					sql = "select round(sysdate - user_dreq_date) as diff from users where user_id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, userDto.getUser_id());
					ResultSet rss = pstmt.executeQuery();
					if (rss.next()) {
						if (rss.getInt("diff") > 15) {
							canDelete = true;
						} else {
							canDelete = false;
						}
					}
				}
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
		return canDelete;
	}

	// 회원 탈퇴 -> 1이면 탈퇴 성공, 0이면 탈퇴 실패
	public int adminDeleteUser(Connection conn, UserDto userDto) {
		int rows = 0;
		try {
			String sql = "delete from users where user_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userDto.getUser_id());
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

	// 회원 주문 내역 조회 행 수
	public int adminUserOrderListCount(Connection conn, UserDto userDto) {
		int count = 0;
		try {
			String sql = "select count(*) as count from orders where user_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userDto.getUser_id());
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

	// 회원 주문 내역 조회
	public ArrayList<OrderDto> adminUserOrderList(Connection conn, PagerDto pagerDto, UserDto userDto) {
		// 주문객체 - 해당 주문의 세부 주문정보들(어떤책 몇권, 어떤책 몇권...)
		ArrayList<OrderDto> orderList = new ArrayList<>();
		try {
			String sql = "select * from (select rownum rnum, order_no, to_char(order_date, 'yyyy-mm-dd') as order_date, order_receivename,"
					+ " order_tel, order_address, order_memo, order_status from orders where user_id = ? and rownum <= ?) where rnum >= ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userDto.getUser_id());
			pstmt.setInt(2, pagerDto.getEndRowNo());
			pstmt.setInt(3, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			// 사용자의 order 레코드 가져오기
			while (rs.next()) {
				OrderDto od = new OrderDto();
				od.setOrder_no(rs.getInt("order_no"));
				od.setOrder_date(rs.getString("order_date"));
				od.setUser_id(userDto.getUser_id());
				od.setOrder_receivename(rs.getString("order_receivename"));
				od.setOrder_tel(rs.getString("order_tel"));
				od.setOrder_address(rs.getString("order_address"));
				od.setOrder_memo(rs.getString("order_memo"));
				od.setOrder_status(rs.getString("order_status").charAt(0));

				sql = "select * from orderDetails o, books b where o.book_no = b.book_no and order_no = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, rs.getInt("order_no"));
				ResultSet rss = pstmt.executeQuery();
				ArrayList<OrderDetailBookDto> orderDetails = new ArrayList<>();
				while (rss.next()) {
					OrderDetailBookDto orderDetailBookDto = new OrderDetailBookDto();
					orderDetailBookDto.setOrder_no(rss.getInt("order_no"));
					orderDetailBookDto.setBook_name(rss.getString("book_name"));
					orderDetailBookDto.setBook_no(rss.getInt("book_no"));
					orderDetailBookDto.setOd_qty(rss.getInt("od_qty"));
					orderDetails.add(orderDetailBookDto);
				}
				od.setOrderDetails(orderDetails);
				orderList.add(od);
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
		return orderList;
	}
}
