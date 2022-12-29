package oti3.View.myPageView;

import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.UserDto;
import oti3.View.joinView.LoginView;
import oti3.View.mainPageManage.MainPageView;

public class MyPageView {
	Scanner sc = new Scanner(System.in);

	public MyPageView(BookClient bookClient) {
		if (bookClient.loginUser.getUser_id() == null) {
			System.out.println("로그인을 해주세요.");
			new LoginView(bookClient);

		} else {

			while (true) {
				System.out.println(bookClient.loginUser.getUser_id() + "님의 MyPage");
				System.out.println("-------------------------------------------------------");
				System.out.println("[이름: " + bookClient.loginUser.getUser_name() + "] ");
				System.out.println("[이메일: " + bookClient.loginUser.getUser_email() + "] ");
				System.out.println("[생년월일: " + bookClient.loginUser.getUser_birth() + "] ");
				System.out.println("[성별: " + bookClient.loginUser.getUser_gender() + "] ");
				System.out.println("[전화번호: " + bookClient.loginUser.getUser_tel() + "] ");
				System.out.println("[주소: " + bookClient.loginUser.getUser_address() + "] ");
				System.out.println("[총 보유금액: " + bookClient.loginUser.getUser_money() + "] ");
				System.out.println("[가입일: " + bookClient.loginUser.getUser_date() + "] ");

				System.out.println("----------------------------------------------------------------");
				System.out
						.println("| 1.마이페이지 상세메뉴 | 2.내 장바구니 | 3.내 리뷰 | 4.나의 찜 목록 | 5.나의 주문내역 | 6.내 QNA목록 | 7.메인페이지 |");
				System.out.print("-> ");
				String num = sc.nextLine();
				switch (num) {
				case "1":
					// 1.마이페이지 상세메뉴
					while (true) {
						System.out.println("-------------------------------------------------");
						System.out.println("| 1.내 정보 수정 | 2.탈퇴 요청 | 3.Money충전 | 4.뒤로가기 |");
						System.out.print("-> ");
						String menuNo = sc.nextLine();
						System.out.println();
						if (menuNo.equals("4")) {
							break;
						}

						switch (menuNo) {
						case "1":
							int uu = updateUser(bookClient, bookClient.loginUser);
							if (uu > 0) {
								System.out.println("수정되었습니다.");
							} else {
								System.out.println("수정이 실패되었습니다. 다시 작성해주세요.");
							}
							break;

						case "2":
							int lu = leaveUser(bookClient);
							if (lu > 0) {
								System.out.println("탈퇴요청이 완료 되었습니다.");
							} else {
								System.out.println("탈퇴요청이 실패되었습니다.");
							}
							break;

						case "3":
							while (true) {
								System.out.println("*환불은 불가하니 신중히 입력해주세요");
								System.out.print("충전할 금액을 입력하시오-> ");
								int money = Integer.parseInt(sc.nextLine());
								if (money <= 0) {
									System.out.println("음의 값은 입력 불가능합니다.");
									continue;
								}
								Boolean cm = chargeMoney(bookClient, money);
								if (cm == true) {
									System.out.println(money + "원 충전이 완료되었습니다.");
									bookClient.loginUser.setUser_money(bookClient.loginUser.getUser_money() + money);
								} else {
									System.out.println("충전이 취소되었습니다.");
								}
								break;
							}

							break;
						}
					}
					break;

				case "2":
					// 2.내 장바구니
					new MyCartPageView(bookClient);
					break;
				case "3":
					new MyReviewPageView(bookClient);
					break;
				case "4":
					new MyDibPageView(bookClient);
					break;
				case "5":
					new MyOrderPageView(bookClient);
					break;
				case "6":
					// 6.내 QNA 목록
					new MyQnaPageView(bookClient);
					break;
				case "7":
					new MainPageView(bookClient);
					break;

				}
			}
		}
	}

	public int updateUser(BookClient bookClient, UserDto original) {
		// 사용자 수정
		int row = 0;
		try {

			// 사용자 비밀번호랑 db비밀번호랑 맞는지 확인
			System.out.print("비밀번호를 입력하시오-> ");

			String password = sc.nextLine();
			if (password.equals(bookClient.loginUser.getUser_password())) {
				// json에 객체 넣기
//				{"command":"updateUser","data":[{} {}]}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("command", "updateUser");
//				JSONArray joa = new JSONArray();
				JSONObject sendData = new JSONObject();
				UserDto user = new UserDto();
				user.setUser_id(original.getUser_id());

				String input = "";
				System.out.print("[비밀번호]을 수정하시겠습니까? ");
				System.out.println("| 1.네  | 2.아니오 |");
				System.out.println("-> ");
				input = sc.nextLine();
				if (input.equals("1") || input.equals("네")) {
					while (true) {
						System.out.print("변경할 비밀번호(대/소문자, 숫자 혼합 6~20자)-> ");
						String pw = "((?=.*[A-Za-z])(?=.*\\d)).{6,20}"; // 영어+숫자 6~20자
						String pw1 = sc.nextLine();
						boolean result1 = Pattern.matches(pw, pw1); // 정규표현식
						if (result1) {
							user.setUser_password(pw1);
							bookClient.loginUser.setUser_password(pw1);
							break;
						} else {
							System.out.println("**대/소문자와 숫자를 혼합해 6자 이상으로 작성해주세요.**");
							continue;
						}
					}

				} else {
					user.setUser_password(original.getUser_password());
					bookClient.loginUser.setUser_password(original.getUser_password());
				}

				System.out.print("[이메일]을 수정하시겠습니까? ");
				System.out.println("| 1.네  | 2.아니오 |");
				System.out.print("-> ");
				input = sc.nextLine();
				if (input.equals("1") || input.equals("네")) {
					while (true) {
						System.out.print("변경할 이메일-> ");
						String email = "\\w+@\\w+\\.\\w+(\\.\\w+)?"; // "angel@mycompany.co.kr";
						String email1 = sc.nextLine();
						boolean result1 = Pattern.matches(email, email1);
						if (result1) {
							user.setUser_email(email1);
							bookClient.loginUser.setUser_email(email1);
							break;
						} else {
							System.out.println("**이메일 형식을 다시 확인해주세요.**");
							continue;
						}
					}

				} else {
					user.setUser_email(original.getUser_email());
					bookClient.loginUser.setUser_email(original.getUser_email());
				}

				System.out.print("[생년월일]을 수정하시겠습니까? ");
				System.out.println("| 1.네  | 2.아니오 |");
				System.out.print("-> ");
				input = sc.nextLine();
				if (input.equals("1") || input.equals("네")) {
					while (true) {
						System.out.print("변경할 생년월일(yyyy-mm-dd)-> "); // 정규표현식으로 넣게 설정
						String birth = "(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])";
						String birth1 = sc.nextLine();
						boolean result1 = Pattern.matches(birth, birth1);
						if (result1) {
							user.setUser_birth(birth1);
							bookClient.loginUser.setUser_birth(birth1);
							break;
						} else {
							System.out.println("**생년월일 형식을 다시 확인해주세요.**");
							continue;
						}
					}

				} else {
					user.setUser_birth(original.getUser_birth());
					bookClient.loginUser.setUser_birth(original.getUser_birth());
				}

				System.out.print("[전화번호]를 수정하시겠습니까? ");
				System.out.println("| 1.네  | 2.아니오 |");
				System.out.print("-> ");
				input = sc.nextLine();
				if (input.equals("1") || input.equals("네")) {
					while (true) {
						System.out.print("변경할 전화번호 입력-> ");
						String tel = "(010|02|0[3-7][1-5])-\\d{3,4}-\\d{4}"; // 집 전화번호 or 핸드폰 번호
						String tel1 = sc.nextLine();
						boolean result1 = Pattern.matches(tel, tel1);
						if (result1) {
							user.setUser_tel(tel1);
							bookClient.loginUser.setUser_tel(tel1);
							break;
						} else {
							System.out.println("**전화번호 형식을 다시 확인해주세요.**");
							continue;
						}
					}

				} else {
					user.setUser_tel(original.getUser_tel());
					bookClient.loginUser.setUser_tel(original.getUser_tel());
				}

				System.out.print("[주소]를 수정하시겠습니까? ");
				System.out.println("| 1.네  | 2.아니오 |");
				System.out.print("-> ");
				input = sc.nextLine();
				if (input.equals("1") || input.equals("네")) {
					System.out.print("변경할 주소 입력-> ");
					input = sc.nextLine();
					user.setUser_address(input);
					bookClient.loginUser.setUser_address(input);
				} else {
					user.setUser_address(original.getUser_address());
					bookClient.loginUser.setUser_address(original.getUser_address());
				}

				sendData.put("userPassword", user.getUser_password());
				sendData.put("userEmail", user.getUser_email());
				sendData.put("userBirth", user.getUser_birth());
				sendData.put("userTel", user.getUser_tel());
				sendData.put("userAddress", user.getUser_address());
				sendData.put("userId", user.getUser_id());

				jsonObject.put("data", sendData);

				// json보내기
				bookClient.send(jsonObject.toString());

				// json받기
				String json = bookClient.receive();
				// 파싱하기
				JSONObject jo = new JSONObject(json);
				row = jo.getInt("data");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return row;
	}

	public int leaveUser(BookClient bookClient) {
		// 사용자탈퇴 요청
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "leaveUser");
		JSONObject sendData = new JSONObject();
		sendData.put("userId", bookClient.loginUser.getUser_id());
		jsonObject.put("data", sendData);
		// json보내기

		bookClient.send(jsonObject.toString());
		// json받기
		String json = bookClient.receive();
		// 파싱하기

		JSONObject jo = new JSONObject(json);
		int row = jo.getInt("data");
		return row;
	}

	public boolean chargeMoney(BookClient bookClient, int money) {
		// 돈 충전하기
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "chargeMoney");
		JSONObject data = new JSONObject();
		data.put("userId", bookClient.loginUser.getUser_id());
		data.put("money", money);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());
		String json = bookClient.receive();
		// 파싱하기
		JSONObject jo = new JSONObject(json);
		boolean ans = jo.getBoolean("data");
		return ans;
	}

}
