package oti3.DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oti3.DTO.UserDto;

// 로그인 로그아웃 DAO - 로그아웃은 클라이언트에서 null 만들면 돼서 DAO 필요 없음
public class LoginDao {
	// 로그인 메소드 (Login())
	public UserDto selectLogin(UserDto selLogin, Connection conn) {
		UserDto juser = new UserDto();
		try {
			// 가입된 회원인지 검사 (id,password)
			String sql = "SELECT user_id, user_password, user_name, user_email, to_char(user_birth, 'yyyy-mm-dd') as user_birth, user_gender, user_tel"
					+ " ,user_address, user_money, user_point, to_char(user_date, 'yyyy-mm-dd') as user_date, user_delete"
					+ " FROM users WHERE user_id = ? AND user_password = ? ";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selLogin.getUser_id());
			pstmt.setString(2, selLogin.getUser_password());
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				// 행이 출력되면 가입된 회원
				juser.setUser_id(rs.getString("user_id"));
				juser.setUser_password(rs.getString("user_password"));
				juser.setUser_name(rs.getString("user_name"));
				juser.setUser_email(rs.getString("user_email"));
				juser.setUser_birth(rs.getString("user_birth"));
				juser.setUser_gender(rs.getString("user_gender").charAt(0));
				juser.setUser_tel(rs.getString("user_tel"));
				juser.setUser_address(rs.getString("user_address"));
				juser.setUser_money(rs.getInt("user_money"));
				juser.setUser_point(rs.getInt("user_point"));
				juser.setUser_date(rs.getString("user_date"));
				juser.setUser_delete(rs.getString("user_delete").charAt(0));
			}
			else {
				juser.setUser_id("notFound");
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
		return juser;
	}

	// 내 ID 찾기 (SearchId())
	public UserDto selectSearchId(UserDto selSearchId, Connection conn) {
		UserDto juserid = new UserDto();
		try {
			// 전화번호로 아이디 찾기
			String sql = "SELECT user_id FROM users WHERE user_tel=?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selSearchId.getUser_tel());
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				// 행이 출력되면 아이디가 있는 회원
				juserid.setUser_id(rs.getString("user_id"));
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
		return juserid;
	}

	// 내 password 찾기 (SearchPw())
	public UserDto selectSearchPw(UserDto selSearchPw, Connection conn) {
		UserDto juserpw = new UserDto();
		try {
			// 전화번호로 아이디 찾기
			String sql = "SELECT user_password FROM users WHERE user_id=? and user_tel=?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selSearchPw.getUser_id());
			pstmt.setString(2, selSearchPw.getUser_tel());
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				// 행이 출력되면 비밀번호가 있는 회원
				juserpw.setUser_password(rs.getString("user_password"));
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
		return juserpw;
	}
}