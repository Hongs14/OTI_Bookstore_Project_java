package oti3.Controller;

import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.UserDto;
import oti3.Service.MemberService;

public class MemberController {// 데이터 얻기, 변환, 검사

	private MemberService MemberService = new MemberService();

	// 회원가입
	public String insertJoinUser(JSONObject data) {
		// 1) JSON TO DTO
		UserDto insJoinUser = new UserDto();
		insJoinUser.setUser_id(data.getString("userId"));
		insJoinUser.setUser_password(data.getString("userPassword"));
		insJoinUser.setUser_name(data.getString("userName"));
		insJoinUser.setUser_email(data.getString("userEmail"));
		insJoinUser.setUser_birth(data.getString("userBirth"));
		insJoinUser.setUser_gender(data.getString("userGender").charAt(0));
		insJoinUser.setUser_tel(data.getString("userTel"));
		insJoinUser.setUser_address(data.getString("userAddress"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = MemberService.insertJoinUser(insJoinUser, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "insertJoinUser");
		sendObject.put("data", rows);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// 회원가입시 아이디 존재 여부에 대해 검사
	public String selectJuserId(JSONObject data) {
		// 1) JSON TO DTO
		UserDto selJuserId = new UserDto();
		selJuserId.setUser_id(data.getString("userId"));

		// 2) SERVICE 호출 후 결과값 받기
		boolean occupiedId = MemberService.selectJuserId(selJuserId, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectJuserId");
		sendObject.put("data", occupiedId);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// 로그인
	public String selectLogin(JSONObject data1) {
		// 1) JSON TO DTO
		UserDto selLogin = new UserDto();
		selLogin.setUser_id(data1.getString("userId"));
		selLogin.setUser_password(data1.getString("userPassword"));

		// 2) SERVICE 호출 후 결과값 받기
		UserDto juser = MemberService.selectLogin(selLogin, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectLogin");
		JSONObject data = new JSONObject();

		data.put("userId", juser.getUser_id());
		data.put("userPassword", juser.getUser_password());
		data.put("userName", juser.getUser_name());
		data.put("userEmail", juser.getUser_email());
		data.put("userBirth", juser.getUser_birth());
		data.put("userGender", juser.getUser_gender() + "");
		data.put("userTel", juser.getUser_tel());
		data.put("userAddress", juser.getUser_address());
		data.put("userMoney", juser.getUser_money());
		data.put("userPoint", juser.getUser_point());
		data.put("userDate", juser.getUser_date());
		data.put("userDelete", juser.getUser_delete() + "");
		sendObject.put("data", data);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// 아이디 찾기
	public String selectSearchId(JSONObject data1) {
		// 1) JSON TO DTO
		UserDto selSearchId = new UserDto();
		selSearchId.setUser_tel(data1.getString("userTel"));

		// 2) SERVICE 호출 후 결과값 받기
		UserDto juserid = MemberService.selectSearchId(selSearchId, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectSearchId");
		JSONObject data = new JSONObject();
		if (juserid.getUser_id() == null) {
			data.put("userId", "notFound");
		} else {
			data.put("userId", juserid.getUser_id());
		}
		sendObject.put("data", data);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// 비밀번호 찾기
	public String selectSearchPw(JSONObject data1) {
		// 1) JSON TO DTO
		UserDto selSearchPw = new UserDto();
		selSearchPw.setUser_id(data1.getString("userId"));
		selSearchPw.setUser_tel(data1.getString("userTel"));

		// 2) SERVICE 호출 후 결과값 받기
		UserDto juserpw = MemberService.selectSearchPw(selSearchPw, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectSearchPw");
		JSONObject data = new JSONObject();
		if (juserpw.getUser_password() == null) {
			data.put("userPassword", "notFound");
		} else {
			data.put("userPassword", juserpw.getUser_password());
		}
		sendObject.put("data", data);

		// 4) RETURN JSON
		return sendObject.toString();
	}
}