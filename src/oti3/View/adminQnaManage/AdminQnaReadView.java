package oti3.View.adminQnaManage;

import java.util.Scanner;

import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AnswerDto;
import oti3.DTO.QnaDto;
import oti3.View.mainPageManage.MainPageView;

public class AdminQnaReadView {
	private static Scanner sc = new Scanner(System.in);
	private QnaDto qna;
	private AnswerDto answer;

	public AdminQnaReadView(BookClient bookClient, int qnaNo) {
		adminQnaInfo(bookClient, qnaNo);
		// 문의글 내용 출력
		System.out.println("[문의글]");
		System.out.println("[글번호 : " + qna.getQna_no() + "]");
		System.out.println("[제목 : " + qna.getQna_title() + "]");
		System.out.println("[내용 : " + qna.getQna_content() + "]");
		System.out.println("[문의유형: " + qna.getQna_category() + "]");
		System.out.println("[조회수 : " + qna.getQna_view() + "]");
		System.out.println("[작성일 : " + qna.getQna_date() + "]");
		System.out.println("[작성자 : " + qna.getUser_id() + "]");
		System.out.println();
		System.out.println("[문의 답변]");
		if (adminAnswerInfo(bookClient, qnaNo)) {
			// 문의 답변 내용 출력
			System.out.println("[답변번호 : " + answer.getAnswer_no() + "]");
			System.out.println("[제목 : " + answer.getAnswer_title() + "]");
			System.out.println("[내용 : " + answer.getAnswer_content() + "]");
			System.out.println("[작성일 : " + answer.getAnswer_date() + "]");
		}

		System.out.println("| 1.문의답변 달기 | 2.문의답변 삭제 | 3.홈으로 이동 | 4.뒤로 가기");
		System.out.println("메뉴 선택-> ");
		int menu = Integer.parseInt(sc.nextLine());
		switch (menu) {
		case 1:
			new AdminAnswerCreateView(bookClient, qna.getQna_no());
			break;

		case 2:
			int rows = adminAnswerDelete(bookClient, answer.getAnswer_no());
			if (rows == 1) {
				System.out.println("문의답변 삭제 성공");
			} else {
				System.out.println("문의답변 삭제 실패");
			}
			break;

		case 3:
			new MainPageView(bookClient);
			break;

		}
	}

	public void adminQnaInfo(BookClient bookClient, int qnaNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminQnaInfo");
		jsonObject.put("data", new JSONObject().put("qnaNo", qnaNo));
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		qna = new QnaDto();
		JSONObject joo = new JSONObject(json);
		JSONObject jo = joo.getJSONObject("data");
		qna.setQna_no(jo.getInt("qnaNo"));
		qna.setQna_title(jo.getString("qnaTitle"));
		qna.setQna_content(jo.getString("qnaContent"));
		qna.setQna_category(jo.getString("qnaCategory"));
		qna.setQna_view(jo.getInt("qnaView"));
		qna.setQna_date(jo.getString("qnaDate"));
		qna.setUser_id(jo.getString("qnaUserId"));
	}

	public boolean adminAnswerInfo(BookClient bookClient, int qnaNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminAnswerInfo");
		jsonObject.put("data", new JSONObject().put("qnaNo", qnaNo));
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		answer = new AnswerDto();
		JSONObject joo = new JSONObject(json);
		JSONObject jo = joo.getJSONObject("data");
		answer.setAnswer_no(jo.getInt("answerNo"));
		if (answer.getAnswer_no() == 0) {
			return false;
		}
		answer.setAnswer_title(jo.getString("answerTitle"));
		answer.setAnswer_content(jo.getString("answerContent"));
		answer.setAnswer_date(jo.getString("answerDate"));
		answer.setQna_no(jo.getInt("qnaNo"));
		return true;
	}

	public int adminAnswerDelete(BookClient bookClient, int answerNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminAnswerDelete");
		JSONObject data = new JSONObject();
		data.put("answerNo", answerNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		return (new JSONObject(json).getInt("data"));

	}

}
