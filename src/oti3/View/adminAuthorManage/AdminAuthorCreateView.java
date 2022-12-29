package oti3.View.adminAuthorManage;

import java.util.Scanner;

import org.json.JSONObject;

import oti3.BookClient;

public class AdminAuthorCreateView {
	private static Scanner sc = new Scanner(System.in);

	public AdminAuthorCreateView(BookClient bookClient) {
		int rows = adminAuthorAdd(bookClient);
		if (rows == 1) {
			System.out.println("저자 추가 성공");
		} else {
			System.out.println("저자 추가 실패");
		}
	}

	public int adminAuthorAdd(BookClient bookClient) {
		JSONObject data = new JSONObject();
		System.out.println("추가할 저자이름 입력-> ");
		data.put("authorName", sc.nextLine());

		System.out.println("추가할 저자 소개글 입력-> ");
		data.put("authorDetail", sc.nextLine());
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("command", "adminAuthorAdd");
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		return joo.getInt("data");
	}

}
