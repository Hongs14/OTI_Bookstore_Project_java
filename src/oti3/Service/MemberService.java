package oti3.Service;

import java.sql.Connection;

import oti3.DAO.JoinDao;
import oti3.DAO.LoginDao;
import oti3.DTO.UserDto;

public class MemberService {
	/*
	 * public static void main(String args[]) { // 값 검증해보기 // JoinUser() 데이터 값 보내고
	 * 검증 JusersDTO jusersDTO = new JusersDTO(); jusersDTO.setUser_id("ej");
	 * jusersDTO.setUser_password("142313"); jusersDTO.setUser_name("은종");
	 * jusersDTO.setUser_email("abc@def.com"); jusersDTO.setUser_birth("00/01/01");
	 * jusersDTO.setUser_gender('F'); jusersDTO.setUser_tel("010-3434-7777");
	 * jusersDTO.setUser_address("제주");
	 * 
	 * JoinLoginService jls = new JoinLoginService(); JSONObject jo =
	 * jls.JoinUser(jusersDTO, ConnectionProvider.getConnection());
	 * System.out.println(jo.getInt("data"));
	 * 
	 * // JUserid() 검증 JoinLoginService jls = new JoinLoginService(); JSONObject jo
	 * = jls.JUserid("eunjong", ConnectionProvider.getConnection());
	 * System.out.println(jo.getBoolean("data"));
	 * 
	 * // Login() 검증 JoinLoginService jls = new JoinLoginService(); JSONObject jo =
	 * jls.Login("eunjong", "cejjec", ConnectionProvider.getConnection());
	 * System.out.println(jo.get("data").toString());
	 * 
	 * // SearchPw() 검증 JoinLoginService jls = new JoinLoginService(); JSONObject jo
	 * = jls.SearchPw("eunjong", "010-9876-1234",
	 * ConnectionProvider.getConnection());
	 * System.out.println(jo.get("data").toString()); }
	 */
	// 회원가입
	public int insertJoinUser(UserDto insJoinUser, Connection conn) {
		JoinDao joinDAO = new JoinDao();

		int rows = joinDAO.insertJoinUser(insJoinUser, conn);

		return rows;
	}

	// 회원가입시 아이디 존재 여부에 대해 검사
	public boolean selectJuserId(UserDto selJuserId, Connection conn) {
		JoinDao joinDAO = new JoinDao();

		boolean occupiedId = joinDAO.selectJuserId(selJuserId, conn); // DAO에서 수행한 결과값만 들어가니까 id 아닌 나머지는 어차피 null

		return occupiedId;
	}

	// 로그인
	public UserDto selectLogin(UserDto selLogin, Connection conn) {
		LoginDao loginDAO = new LoginDao();

		UserDto juser = loginDAO.selectLogin(selLogin, conn);

		return juser;
	}

	// 아이디 찾기
	public UserDto selectSearchId(UserDto selSearchId, Connection conn) {
		LoginDao loginDAO = new LoginDao();

		UserDto juserid = loginDAO.selectSearchId(selSearchId, conn);

		return juserid;
	}

	// 비밀번호 찾기
	public UserDto selectSearchPw(UserDto selSearchPw, Connection conn) {
		LoginDao loginDAO = new LoginDao();

		UserDto juserpw = loginDAO.selectSearchPw(selSearchPw, conn);

		return juserpw;
	}
}