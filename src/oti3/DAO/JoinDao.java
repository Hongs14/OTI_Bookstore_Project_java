package oti3.DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oti3.DTO.UserDto;

public class JoinDao {
	// 회원가입(JoinUser()-JuserDTO)
	public int insertJoinUser(UserDto insJoinUser, Connection conn) {
		int rows = 0;
		try {
			// users 컬럼값 입력
			String sql = "INSERT INTO users(user_id, user_password, user_name, user_email, user_birth, user_gender, user_tel, user_address, user_money, user_point, user_date, user_delete)"
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, 0, 0, sysdate, 'N')";

			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, insJoinUser.getUser_id());
			pstmt.setString(2, insJoinUser.getUser_password());
			pstmt.setString(3, insJoinUser.getUser_name());
			pstmt.setString(4, insJoinUser.getUser_email());
			pstmt.setString(5, insJoinUser.getUser_birth());
			pstmt.setString(6, String.valueOf(insJoinUser.getUser_gender()));
			pstmt.setString(7, insJoinUser.getUser_tel());
			pstmt.setString(8, insJoinUser.getUser_address());

			rows = pstmt.executeUpdate();
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
		return rows;
	}

	// JoccupiedIdDAO :회원가입시 아이디 존재 여부에 대해 검사하는 메소드
	public boolean selectJuserId(UserDto selJuserId, Connection conn) {
		boolean occupiedId = false;
		try {
			// users 테이블에 이미 있는 아이디인지 검사
			String sql = "SELECT user_id From users WHERE user_id=?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selJuserId.getUser_id());
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				occupiedId = true; // 행이 출력되면 이미 있는 아이디
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
		return occupiedId;
	}
}