package oti3.View.qnaView;

import java.util.Scanner;

import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.QnaDto;
import oti3.View.mainPageManage.MainPageView;

//문의 게시판 글 생성 뷰
public class QnaCreateView {
	Scanner sc = new Scanner(System.in);

	public QnaCreateView(BookClient bookClient) {
		a : while (true) {
			System.out.println("[1:1 문의 접수]");
			System.out.println("----------------------------------------------------------------");
			System.out.println("| 1. 배송  |  2. 주문/결제  |  3. 도서/상품정보  |  4. 반품/교환/환불  |");
			System.out.println("| 5. 회원정보서비스 | 6. 웹사이트 이용 관련 | 7. 시스템 불편사항 | 8. 기타 |");
			System.out.println("----------------------------------------------------------------");
			System.out.print("문의유형 선택-> ");

			String category = null;
			category = sc.nextLine();
			switch (category) {
			case "1":
				category = "[배송]";
				break;
			case "2":
				category = "[주문/결제]";
				break;
			case "3":
				category = "[도서/상품정보]";
				break;
			case "4":
				category = "[반품/교환/환불]";
				break;
			case "5":
				category = "[회원정보서비스]";
				break;
			case "6":
				category = "[웹사이트 이용 관련]";
				break;
			case "7":
				category = "[시스템 불편사항]";
				break;
			case "8":
				category = "[기타]";
				break;
			default:
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
				continue a;
			}

			System.out.print("제목-> ");
			String title = sc.nextLine();

			System.out.print("내용(300자 내외) \n-> ");
			String content = sc.nextLine();
			System.out.println();

			QnaDto insQna = new QnaDto();
			insQna.setQna_category(category);
			insQna.setUser_id(bookClient.loginUser.getUser_id());
			insQna.setQna_title(title);
			insQna.setQna_content(content);

			int rows = insertQna(bookClient, insQna);
			if (rows == 1) {
				System.out.println("문의가 성공적으로 등록되었습니다.");
				while (true) {
					System.out.println("| 1. 문의 게시판으로 가기 | 2. 홈으로 가기 |");
					System.out.print("-> ");
					int input = sc.nextInt();
					if (input == 1) {
						new QnaPageView(bookClient);
					} else if (input == 2) {
						 new MainPageView(bookClient);
					} else {
						System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
						break;
					}

				}

			} else {
				System.out.println("문의 등록에 실패했습니다. 다시 등록해주세요.");
				break;
			}
		}

	}

	public int insertQna(BookClient bookClient, QnaDto insQna) {
		// 1) 요청 json 만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "insertQna");

		JSONObject data = new JSONObject();
		data.put("qnaCategory", insQna.getQna_category());
		data.put("userId", bookClient.loginUser.getUser_id());
		data.put("qnaTitle", insQna.getQna_title());
		data.put("qnaContent", insQna.getQna_content());
		sendObject.put("data", data);

		// 2) 요청
		bookClient.send(sendObject.toString());

		// 3) 응답 json 파싱
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);
		int rows = jo.getInt("data");

		return rows;
	}
}
