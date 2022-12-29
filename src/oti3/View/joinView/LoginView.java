package oti3.View.joinView;

import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.UserDto;
import oti3.View.mainPageManage.MainPageView;

public class LoginView {
	Scanner sc = new Scanner(System.in);

	public LoginView(BookClient bookClient) {
		while (true) {
			System.out.println("===========================================================");
			System.out.println("[OTI 서점]");
			System.out.println("-----------------------------------------------------------");
			System.out.println("| 1. 로그인 | 2. 아이디 찾기 | 3. 비밀번호 찾기 | 4. 회원가입 | 5.뒤로 가기 |");
			System.out.print("-> ");

			int input = Integer.parseInt(sc.nextLine()); // 이거 안해주면 아래 nextline()과 겹침
			if (input == 4) {
				new JoinView(bookClient);
				System.out.println();
			}
			if (input == 5) {
				break;
			}
			switch (input) {
			case 1:
				System.out.println();
				System.out.println("[로그인]");
				System.out.println("-----------------------------------------------------------");

				System.out.print("아이디-> ");
				String id = sc.nextLine();
				System.out.print("비밀번호-> ");
				String pw = sc.nextLine();

				System.out.println("-----------------------------------------------------------");

				selectLogin(bookClient, id, pw);
				if (bookClient.loginUser.getUser_id() != null && bookClient.loginUser.getUser_id().equals(id)) {
					System.out.println("**로그인 성공**");
					System.out.println("홈으로 이동합니다.");
					new MainPageView(bookClient);
				} else {
					System.out.println("아이디/비밀번호를 다시 확인해주세요.");
				}
				break;

			case 2:
				System.out.println();
				System.out.println("[아이디 찾기]");
				System.out.println("-----------------------------------------------------------");
				System.out.println("본인 명의의 전화번호-> ");
				String tel = "(010|02|0[3-7][1-5])-\\d{3,4}-\\d{4}";
				String tel1 = sc.nextLine();
				boolean result1 = Pattern.matches(tel, tel1);
				System.out.println("-----------------------------------------------------------");

				if (result1) {
					String juserid = selectSearchId(bookClient, tel1);
					if (juserid.equals("notFound")) {
						System.out.println("회원 정보를 찾을 수 없습니다.");
						System.out.println();
						break;
					} else {
						System.out.println("가입하신 아이디는 " + juserid + " 입니다.");
						System.out.println();
						break;
					}
				} else {
					System.out.println("**전화번호 형식을 다시 확인해주세요.**");
					System.out.println();
					break;
				}

			case 3:
				System.out.println();
				System.out.println("[비밀번호 찾기]");
				System.out.println("-----------------------------------------------------------");
				System.out.print("아이디-> ");
				String id2 = sc.nextLine();
				System.out.println("본인 명의의 전화번호-> ");
				String tel2 = "(010|02|0[3-7][1-5])-\\d{3,4}-\\d{4}";
				String tel3 = sc.nextLine();
				boolean result2 = Pattern.matches(tel2, tel3);
				System.out.println("-----------------------------------------------------------");

				if (result2) {
					String juserpw = selectSearchPw(bookClient, id2, tel3);
					if (juserpw.equals("notFound")) {
						System.out.println("회원 정보를 찾을 수 없습니다.");
						System.out.println();
						break;
					} else {
						System.out.println("가입하신 비밀번호는 " + juserpw + " 입니다.");
						System.out.println();
						break;
					}
				} else {
					System.out.println("**전화번호 형식을 다시 확인해주세요.**");
					System.out.println();
					break;
				}
			}
		}
	}

	// 로그인 하기
	public void selectLogin(BookClient bookClient, String userId, String userPassword) {
		// 1) 요청 json 만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectLogin");

		JSONObject data = new JSONObject();
		data.put("userId", userId);
		data.put("userPassword", userPassword);
		sendObject.put("data", data);

		// 2) 요청
		bookClient.send(sendObject.toString());

		// 3) 응답 json 파싱
		String receiveJson = bookClient.receive();
		JSONObject jo = new JSONObject(receiveJson);
		JSONObject jo2 = jo.getJSONObject("data");

		bookClient.loginUser = new UserDto();
		if (!jo2.getString("userId").equals("notFound")) {
			bookClient.loginUser.setUser_id(jo2.getString("userId"));
			bookClient.loginUser.setUser_password(jo2.getString("userPassword"));
			bookClient.loginUser.setUser_name(jo2.getString("userName"));
			bookClient.loginUser.setUser_email(jo2.getString("userEmail"));
			bookClient.loginUser.setUser_birth(jo2.getString("userBirth"));
			bookClient.loginUser.setUser_gender(jo2.getString("userGender").charAt(0));
			bookClient.loginUser.setUser_tel(jo2.getString("userTel"));
			bookClient.loginUser.setUser_address(jo2.getString("userAddress"));
			bookClient.loginUser.setUser_money(jo2.getInt("userMoney"));
			bookClient.loginUser.setUser_point(jo2.getInt("userPoint"));
			bookClient.loginUser.setUser_date(jo2.getString("userDate"));
			bookClient.loginUser.setUser_delete(jo2.getString("userDelete").charAt(0));
		}

	}

	// ID 찾기
	public String selectSearchId(BookClient bookClient, String userTel) {
		// 1) 요청 json 만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectSearchId");
		JSONObject data = new JSONObject();
		data.put("userTel", userTel);
		sendObject.put("data", data);

		// 2) 요청
		bookClient.send(sendObject.toString());

		// 3) 응답 json 파싱
		// {"command" : "", "data" : {"userId" : "thddudgns79"}}
		String receiveJson = bookClient.receive();
		JSONObject jo = new JSONObject(receiveJson);
		String juserid = jo.getJSONObject("data").getString("userId");
		return juserid;
	}

	// PW 잦기
	public String selectSearchPw(BookClient bookClient, String userId, String userTel) {
		// 1) 요청 json 만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectSearchPw");
		JSONObject data = new JSONObject();
		data.put("userId", userId);
		data.put("userTel", userTel);
		sendObject.put("data", data);

		// 2) 요청
		bookClient.send(sendObject.toString());

		// 3) 응답 json 파싱
		String receiveJson = bookClient.receive();
		JSONObject jo = new JSONObject(receiveJson);
		String juserpw = jo.getJSONObject("data").getString("userPassword");
		return juserpw;
	}
}
