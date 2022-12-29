package oti3.View.adminUserManage;

import java.util.Scanner;

import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.UserDto;
import oti3.View.mainPageManage.MainPageView;

public class AdminUserReadView {
	private UserDto userDto = new UserDto();
	private static Scanner sc = new Scanner(System.in);

	public AdminUserReadView(BookClient bookClient, String userId) {
		while (true) {
			adminUserInfo(bookClient, userId);
			// 회원 정보 출력
			System.out.println("[회원정보]");
			System.out.println("[아이디 : " + userDto.getUser_id() + "] ");
			System.out.println("[이름 : " + userDto.getUser_name() + "] ");
			System.out.println("[비밀번호 : " + userDto.getUser_password() + "] ");
			System.out.println("[잔액 : " + userDto.getUser_money() + "] ");
			System.out.println("[전화번호 : " + userDto.getUser_tel() + "] ");
			System.out.println("[주소 : " + userDto.getUser_address() + "] ");
			System.out.println("[생년월일 : " + userDto.getUser_birth() + "] ");
			System.out.println("[가입일 : " + userDto.getUser_date() + "] ");
			System.out.println("[성별 : " + userDto.getUser_gender() + "] ");
			System.out.println("[이메일 : " + userDto.getUser_email() + "] ");
			System.out.println("[탈퇴여부 : " + userDto.getUser_delete() + "] ");
			System.out.println("[탈퇴요청일 : " + userDto.getUser_dreq_date() + "] ");

			System.out.println("| 1.회원 탈퇴 처리 | 2.회원 주문 내역 조회 | 3.뒤로 가기 | 4.홈으로 이동 |");
			System.out.println("-> ");
			int menu = Integer.parseInt(sc.nextLine());
			if (menu == 1) {
				if (adminCanDeleteUser(bookClient, userDto.getUser_id())) {
					int rows = adminDeleteUser(bookClient, userDto.getUser_id());
					if (rows == 1) {
						System.out.println("회원 삭제 성공");
					} else {
						System.out.println("회원 삭제 실패");
					}
				} else {
					System.out.println("탈퇴 요청 회원이 아니거나 탈퇴 요청일로부터 15일이 지나지 않았습니다.");
				}
			} else if (menu == 2) {
				new AdminUserOrderPageView(bookClient, userDto);
			} else if (menu == 3) {
				break;
			} else {
				new MainPageView(bookClient);
			}
		}

	}

	public void adminUserInfo(BookClient bookClient, String userId) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminUserInfo");
		JSONObject sendData = new JSONObject();
		sendData.put("userId", userId);
		jsonObject.put("data", sendData);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		userDto.setUser_id(receiveData.getString("userId"));
		userDto.setUser_name(receiveData.getString("userName"));
		userDto.setUser_password(receiveData.getString("userPassword"));
		userDto.setUser_email(receiveData.getString("userEmail"));
		userDto.setUser_birth(receiveData.getString("userBirth"));
		userDto.setUser_gender(receiveData.getString("userGender").charAt(0));
		userDto.setUser_tel(receiveData.getString("userTel"));
		userDto.setUser_address(receiveData.getString("userAddress"));
		userDto.setUser_money(receiveData.getInt("userMoney"));
		userDto.setUser_point(receiveData.getInt("userPoint"));
		userDto.setUser_date(receiveData.getString("userDate"));
		userDto.setUser_delete(receiveData.getString("userDelete").charAt(0));
		userDto.setUser_dreq_date(receiveData.getString("userDreqDate"));
	}

	public boolean adminCanDeleteUser(BookClient bookClient, String userId) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminCanDeleteUser");
		JSONObject data = new JSONObject();
		data.put("userId", userId);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		return joo.getBoolean("data");
	}

	public int adminDeleteUser(BookClient bookClient, String userId) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminDeleteUser");
		JSONObject data = new JSONObject();
		data.put("userId", userId);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		return joo.getInt("data");

	}

}
