package oti3.View.adminQnaManage;

import java.util.Scanner;

import org.json.JSONObject;

import oti3.BookClient;

public class AdminAnswerCreateView {
	private static Scanner sc = new Scanner(System.in);

	public AdminAnswerCreateView(BookClient bookClient, int qnaNo) {
		int rows = adminAnswerCreate(bookClient, qnaNo);
		if (rows == 1) {
			System.out.println("문의답변 작성 성공");
		} else {
			System.out.println("문의답변 작성 실패");
		}
	}

	public int adminAnswerCreate(BookClient bookClient, int qnaNo) {
		JSONObject data = new JSONObject();
		System.out.println("문의 답변 제목 입력>-> ");
		data.put("answerTitle", sc.nextLine());

		System.out.println("문의 답변 내용 입력-> ");
		data.put("answerContent", sc.nextLine());
		data.put("qnaNo", qnaNo);
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("command", "adminAnswerCreate");
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		return joo.getInt("data");

	}
}
