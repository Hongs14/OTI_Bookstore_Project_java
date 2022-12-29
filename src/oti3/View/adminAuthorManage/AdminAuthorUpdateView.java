package oti3.View.adminAuthorManage;

import java.util.Scanner;

import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;

public class AdminAuthorUpdateView {
	private static Scanner sc = new Scanner(System.in);

	public AdminAuthorUpdateView(BookClient bookClient, AuthorDto original) {
		int rows = adminAuthorUpdate(bookClient, original);
		if (rows == 1) {
			System.out.println("저자 수정 성공");
		} else {
			System.out.println("저자 수정 실패");
		}
	}

	public int adminAuthorUpdate(BookClient bookClient, AuthorDto original) {
		AuthorDto updateAuthor = new AuthorDto();
		updateAuthor.setAuthor_no(original.getAuthor_no());
		
		System.out.println("[이름]을 수정하시겠습니까?");
		System.out.println("| 1. 네  |  2. 아니오  |");
		System.out.println(">");
		String input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			System.out.println("변경할 이름> ");
			String authorName = sc.nextLine();
			updateAuthor.setAuthor_name(authorName);
		} else {
			updateAuthor.setAuthor_name(original.getAuthor_name());
		}
		
		System.out.println("[저자 소개글]을 수정하시겠습니까?");
		System.out.println("| 1. 네  |  2. 아니오  |");
		System.out.println(">");
		input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			System.out.println("변경할 소개글(2000자 이하)> ");
			String authorDetail = sc.nextLine();
			updateAuthor.setAuthor_detail(authorDetail);
		} else {
			updateAuthor.setAuthor_detail(original.getAuthor_detail());
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminAuthorUpdate");
		JSONObject data = new JSONObject();
		data.put("authorNo", updateAuthor.getAuthor_no());
		data.put("authorName", updateAuthor.getAuthor_name());
		data.put("authorDetail", updateAuthor.getAuthor_detail());

		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		return joo.getInt("data");
	}

}
