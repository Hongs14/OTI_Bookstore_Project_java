package oti3.View.joinView;

import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.UserDto;

public class JoinView {
	Scanner sc = new Scanner(System.in);
	boolean result1;

	public JoinView(BookClient bookClient) {
		while (true) {
			try {
				UserDto insJoinUser = new UserDto();
				System.out.println("[회원가입]");
				System.out.println("===========================================================");
				System.out.print("아이디-> ");
				String id = sc.nextLine();
				while (selectJuserId(bookClient, id) == true) {
					System.out.println("<<사용 중인 아이디입니다. 다른 아이디를 입력해주세요.>>");
					System.out.print("아이디-> ");
					id = sc.nextLine();
				}
				insJoinUser.setUser_id(id);

				while (true) {
					System.out.print("비밀번호(대/소문자, 숫자 혼합 6~20자)-> ");
					String pw = "((?=.*[A-Za-z])(?=.*\\d)).{6,20}"; // 영어+숫자 6~20자
					String pw1 = sc.nextLine();
					boolean result1 = Pattern.matches(pw, pw1); // 정규표현식
					if (result1) {
						insJoinUser.setUser_password(pw1);
						break;
					} else {
						System.out.println("**대/소문자와 숫자를 혼합해 6자 이상으로 작성해주세요.**");
						continue;
					}
				}

				System.out.print("사용자 이름-> ");
				String name = sc.nextLine();
				insJoinUser.setUser_name(name);

				while (true) {
					System.out.print("이메일-> ");
					String email = "\\w+@\\w+\\.\\w+(\\.\\w+)?"; // "angel@mycompany.co.kr";
					String email1 = sc.nextLine();
					result1 = Pattern.matches(email, email1);
					if (result1) {
						insJoinUser.setUser_email(email1);
						break;
					} else {
						System.out.println("**이메일 형식을 다시 확인해주세요.**");
						continue;
					}
				}

				while (true) {
					System.out.print("생년월일(yyyy-mm-dd)-> "); // 정규표현식으로 넣게 설정
					String birth = "(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])";
					String birth1 = sc.nextLine();
					result1 = Pattern.matches(birth, birth1);
					if (result1) {
						insJoinUser.setUser_birth(birth1);
						break;
					} else {
						System.out.println("**생년월일 형식을 다시 확인해주세요.**");
						continue;
					}
				}

				while (true) {
					System.out.print("성별(F/M)-> ");
					String gender = "(F|M)";
					String gender1 = sc.nextLine();
					result1 = Pattern.matches(gender, gender1);
					if (result1) {
						insJoinUser.setUser_gender(gender1.charAt(0));
						break;
					} else {
						System.out.println("**성별 형식을 다시 확인해주세요.**");
						continue;
					}
				}

				while (true) {
					System.out.print("전화번호-> ");
					String tel = "(010|02|0[3-7][1-5])-\\d{3,4}-\\d{4}"; // 집 전화번호 or 핸드폰 번호
					String tel1 = sc.nextLine();
					result1 = Pattern.matches(tel, tel1);
					if (result1) {
						insJoinUser.setUser_tel(tel1);
						break;
					} else {
						System.out.println("**전화번호 형식을 다시 확인해주세요.**");
						continue ;
					}
				}

				System.out.print("주소-> ");
				String addr = sc.nextLine();
				insJoinUser.setUser_address(addr);
				System.out.println("===========================================================");

				int rows = insertJoinUser(bookClient, insJoinUser);
				if (rows == 1) {
					System.out.println("회원가입이 완료되었습니다. 환영합니다.");
					System.out.println("로그인 창으로 이동합니다.");
					new LoginView(bookClient);
				} else {
					System.out.println(rows);
					System.out.println("정상적으로 처리되지 않았습니다. 다시 가입해주세요.");
					System.out.println();
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
			}
		}
	}

	// 회원가입
	public int insertJoinUser(BookClient bookClient, UserDto insJoinUser) {
		// 1) 요청 json 만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "insertJoinUser");

		JSONObject data = new JSONObject();
		data.put("userId", insJoinUser.getUser_id());
		data.put("userPassword", insJoinUser.getUser_password());
		data.put("userName", insJoinUser.getUser_name());
		data.put("userEmail", insJoinUser.getUser_email());
		data.put("userBirth", insJoinUser.getUser_birth());
		data.put("userGender", String.valueOf(insJoinUser.getUser_gender()));
		data.put("userTel", insJoinUser.getUser_tel());
		data.put("userAddress", insJoinUser.getUser_address());
		sendObject.put("data", data);

		// 2) 요청
		bookClient.send(sendObject.toString());

		// 3) 응답 json 파싱

		String receiveJson = bookClient.receive();
		JSONObject jo = new JSONObject(receiveJson);
		int rows = jo.getInt("data");
		return rows;
	}

	// ID 중복 검사
	public boolean selectJuserId(BookClient bookClient, String userId) {
		// 1) 요청 json 만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectJuserId");

		JSONObject data = new JSONObject();
		data.put("userId", userId);
		sendObject.put("data", data);

		// 2) 요청
		bookClient.send(sendObject.toString());

		// 3) 응답 json 파싱
		String receiveJson = bookClient.receive();
		return (new JSONObject(receiveJson).getBoolean("data"));
	}
}