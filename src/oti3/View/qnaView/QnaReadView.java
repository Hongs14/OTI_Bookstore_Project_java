package oti3.View.qnaView;

import java.util.Scanner;

import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AnswerDto;
import oti3.DTO.QnaDto;
import oti3.View.mainPageManage.MainPageView;

public class QnaReadView {
	Scanner sc = new Scanner(System.in);
	QnaDto qdetail = new QnaDto();
	AnswerDto qanswer = new AnswerDto();

	public QnaReadView(BookClient bookClient, int qnaNo) {
		while (true) {
			selectQdetail(bookClient, qnaNo);

			// 조회수 올리기
			updateQviewcount(bookClient, qnaNo);
			System.out.println("=========================================================================");
			System.out.println("[문의글]");
			System.out.println("-------------------------------------------------------------------------");
			System.out.println("[글번호 : " + qdetail.getQna_no() + "]");
			System.out.println("[작성일 : " + qdetail.getQna_date() + "]");
			System.out.println("[조회수 : " + qdetail.getQna_view() + "]");
			System.out.println("[문의유형 : " + qdetail.getQna_category() + "]");
			System.out.println("[제목 : " + qdetail.getQna_title() + "]");
			System.out.println("[작성자 : " + qdetail.getUser_id() + "]");
			System.out.println("[내용 : \n" + qdetail.getQna_content() + "]");
			System.out.println("-------------------------------------------------------------------------");
			if (selectQanswer(bookClient, qnaNo) == true) {
				System.out.println("[문의 답변]");
				System.out.println("[답변 : " + qanswer.getAnswer_no() + "] ");
				System.out.println("[작성일 : " + qanswer.getAnswer_date() + "] ");
				System.out.println("[답변 제목: " + qanswer.getAnswer_title() + "] ");
				System.out.println("[답변 내용:\n" + qanswer.getAnswer_content() + "] ");
				System.out.println("-------------------------------------------------------------------------");
			}

			System.out.println("| 1. 문의 수정 | 2. 문의 삭제 | 3. 뒤로 가기 | 4. 홈으로 가기 |");
			System.out.print("-> ");
			String input = sc.nextLine();
			if (input.equals("3")) {
				break;
			}
			if (input.equals("2")) {
				if (selectQmatch(bookClient, qnaNo)) {
					int rows = deleteQna(bookClient, qnaNo);
					if (rows == 1) {
						System.out.println("삭제되었습니다.");
						new QnaPageView(bookClient);
					} else {
						System.out.println("다시 시도해주세요.");
					}
				} else {
					System.out.println("본인 글만 삭제하실 수 있습니다.");
				}
				break;

			}

			switch (input) {
			case "1":
				if (selectQmatch(bookClient, qnaNo) && selectQanswer(bookClient, qnaNo) == false) {
					new QnaUpdateView(bookClient, qdetail);
				} else if (selectQmatch(bookClient, qnaNo) && selectQanswer(bookClient, qnaNo) == true) {
					System.out.println("답변 완료된 글은 수정하실 수 없습니다.");
				} else {
					System.out.println("본인 글만 수정하실 수 있습니다.");
				}
				break;

			case "4":
				new MainPageView(bookClient);
				break;

			default:
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
				break;
			}
		}
	}

	// 조회수 올리기
	public void updateQviewcount(BookClient bookClient, int qnaNo) {
		// 1) 요청 json 만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "updateQviewcount");

		JSONObject data = new JSONObject();
		data.put("qnaNo", qnaNo);
		sendObject.put("data", data);

		// 2) 요청
		bookClient.send(sendObject.toString());

		// 3) 응답 json 파싱
		String receiveJson = bookClient.receive();
		JSONObject jo = new JSONObject(receiveJson);
		int rows = jo.getInt("data");
	}

	public boolean selectQmatch(BookClient bookClient, int qnaNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "selectQmatch");
		JSONObject data = new JSONObject();
		data.put("userId", bookClient.loginUser.getUser_id());
		data.put("qnaNo", qnaNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String receiveJson = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		return (new JSONObject(receiveJson).getBoolean("data"));
	}

	// 문의글 자세히 보기
	public void selectQdetail(BookClient bookClient, int qnaNo) {
		// 1) 요청 json 만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectQdetail");

		JSONObject data = new JSONObject();
		data.put("qnaNo", qnaNo);
		sendObject.put("data", data);

		// 2) 요청
		bookClient.send(sendObject.toString());

		// 3) 응답 json 파싱
		String receiveJson = bookClient.receive();
		JSONObject jo = new JSONObject(receiveJson);
		JSONObject jo2 = jo.getJSONObject("data"); // "data" 값들 파싱
		qdetail.setQna_no(jo2.getInt("qnaNo"));
		qdetail.setQna_category(jo2.getString("qnaCategory"));
		qdetail.setUser_id(jo2.getString("userId"));
		qdetail.setQna_title(jo2.getString("qnaTitle"));
		qdetail.setQna_content(jo2.getString("qnaContent"));
		qdetail.setQna_date(jo2.getString("qnaDate"));
		qdetail.setQna_view(jo2.getInt("qnaView"));
	}

	// 문의글 답변 보기
	public boolean selectQanswer(BookClient bookClient, int qnaNo) {
		// 1) 요청 json 만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectQanswer");

		JSONObject data = new JSONObject();
		data.put("qnaNo", qnaNo);
		sendObject.put("data", data);

		// 2) 요청
		bookClient.send(sendObject.toString());

		// 3) 응답 json 파싱
		String receiveJson = bookClient.receive();
		JSONObject jo = new JSONObject(receiveJson);
		JSONObject jo2 = jo.getJSONObject("data"); // "data" 값들 파싱
		qanswer.setAnswer_no(jo2.getInt("answerNo"));
		if (qanswer.getAnswer_no() == 0) {
			return false;
		}
		qanswer.setAnswer_title(jo2.getString("answerTitle"));
		qanswer.setAnswer_content(jo2.getString("answerContent"));
		qanswer.setAnswer_date(jo2.getString("answerDate"));
		return true;
	}

	// 문의글 삭제하기
	public int deleteQna(BookClient bookClient, int qnaNo) {
		// 1) 요청 json 만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "deleteQna");

		JSONObject data = new JSONObject();
		data.put("qnaNo", qnaNo);
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
