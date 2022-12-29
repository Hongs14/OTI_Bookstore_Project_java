package oti3.View.adminAuthorManage;

import java.util.Scanner;

import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;

public class AdminAuthorReadView {
	private static Scanner sc = new Scanner(System.in);
	private AuthorDto author = new AuthorDto();

	public AdminAuthorReadView(BookClient bookClient, int authorNo) {
		while (true) {
			adminAuthorInfo(bookClient, authorNo);
			// 저자 정보 출력
			System.out.println("[작가정보]");
			System.out.println("[작가 번호 : " + author.getAuthor_no() + "]");
			System.out.println("[작가명 : " + author.getAuthor_name() + "]");
			System.out.println("[작가 소개 : " + author.getAuthor_detail() + "]");

			System.out.println("1.작가 정보 수정 | 2.저자 삭제 | 3. 뒤로 가기");
			System.out.println("-> ");
			int menu = Integer.parseInt(sc.nextLine());
			if (menu == 3)
				break;
			else if (menu == 1) {
				new AdminAuthorUpdateView(bookClient, author);
			}

			else if (menu == 2) {
				int rows = adminAuthorPop(bookClient, authorNo);
				if (rows == 1) {
					System.out.println("저자 삭제 성공");
					break;
				} else {
					System.out.println("저자 삭제 실패");
				}
			}
		}

	}

	// 저자 상세정보
	public void adminAuthorInfo(BookClient bookClient, int authorNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminAuthorInfo");
		jsonObject.put("data", new JSONObject().put("authorNo", authorNo));
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject jo = joo.getJSONObject("data");
		author.setAuthor_no(jo.getInt("authorNo"));
		author.setAuthor_name(jo.getString("authorName"));
		author.setAuthor_detail(jo.getString("authorDetail"));

	}

	public int adminAuthorPop(BookClient bookClient, int authorNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminAuthorPop");
		jsonObject.put("data", new JSONObject().put("authorNo", authorNo));
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		return joo.getInt("data");

	}

}
