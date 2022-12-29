package oti3.View.qnaView;


import java.util.Scanner;

import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.QnaDto;
import oti3.View.mainPageManage.MainPageView;

public class QnaUpdateView {
	Scanner sc = new Scanner(System.in);
	QnaDto upQna = new QnaDto();

	public QnaUpdateView(BookClient bookClient, QnaDto original) {

		while (true) {
			System.out.println("[문의유형]을 수정하시겠습니까?");
			System.out.println("| 1. 네 | 2. 아니오 |");
			System.out.println("-> ");
			String input = sc.nextLine();
			if (input.equals("1") || input.equals("네")) {
				System.out.println("----------------------------------------------------------------");
				System.out.println("| 1. 배송  |  2. 주문/결제  |  3. 도서/상품정보  |  4. 반품/교환/환불  |");
				System.out.println("| 5. 회원정보서비스 | 6. 웹사이트 이용 관련 | 7. 시스템 불편사항 | 8. 기타 |");
				System.out.println("----------------------------------------------------------------");
				System.out.print("변경할 문의유형-> ");
				String category = sc.nextLine();
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
				}
				upQna.setQna_category(category);
				break;
			} else if (input.equals("2") || input.equals("아니오")) {
				upQna.setQna_category(original.getQna_category());
				break;
			} else {
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
			}
		}

		while (true) {
			System.out.println("[제목]을 수정하시겠습니까?");
			System.out.println("| 1. 네 | 2. 아니오 |");
			System.out.println("-> ");
			String input = sc.nextLine();
			if (input.equals("1") || input.equals("네")) {
				System.out.println("변경할 제목-> ");
				String title = sc.nextLine();
				upQna.setQna_title(title);
				break;
			} else if (input.equals("2") || input.equals("아니오")) {
				upQna.setQna_title(original.getQna_title());
				break;
			} else {
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
			}
		}

		while (true) {
			System.out.println("[내용]을 수정하시겠습니까?");
			System.out.println("| 1. 네 | 2. 아니오 |");
			System.out.println("-> ");
			String input = sc.nextLine();
			if (input.equals("1") || input.equals("네")) {
				System.out.println("변경할 내용(300자 내외)-> ");
				String content = sc.nextLine();
				upQna.setQna_content(content);
				break;
			} else if (input.equals("2") || input.equals("아니오")) {
				upQna.setQna_content(original.getQna_content());
				break;
			} else {
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
			}
		}

		upQna.setQna_no(original.getQna_no());
		System.out.println("변경사항이 저장되었습니다.");

		int rows = updateQna(bookClient, upQna);
		if (rows == 1) {
			System.out.println("수정이 완료되었습니다.");
		} else {
			System.out.println("수정에 실패했습니다. 다시 등록해주세요.");
		}

		while (true) {
			System.out.println("| 1. 문의 게시판으로 가기 | 2. 홈으로 가기 |");
			System.out.print("-> ");
			int input2 = sc.nextInt();
			if (input2 == 1) {
				new QnaPageView(bookClient);
				break;
			} else if (input2 == 2) {
				 new MainPageView(bookClient);
				break;
			} else {
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
			}
		}
	}

	public int updateQna(BookClient bookClient, QnaDto upQna) {
		// 1) 요청 json 만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "updateQna");

		JSONObject data = new JSONObject();
		data.put("qnaCategory", upQna.getQna_category());
		data.put("qnaTitle", upQna.getQna_title());
		data.put("qnaContent", upQna.getQna_content());
		data.put("qnaNo", upQna.getQna_no());
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
