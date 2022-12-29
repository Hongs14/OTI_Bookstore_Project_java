package oti3.View.adminQnaManage;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;
import oti3.View.adminBookManage.AdminBookReadView;
import oti3.View.mainPageManage.MainPageView;

public class AdminQnaPageView {
	private static Scanner sc = new Scanner(System.in);
	private ArrayList<QnaDto> qnaList;
	private PagerDto pagerDto;

	public AdminQnaPageView(BookClient bookClient) {
		int pageNo = 1;
		int searchType = 1;
		adminQnaList(bookClient, pageNo);
		while (true) {
			try {
				// 페이지에 해당되는 list 얻기 + list 출력
				System.out.println("------------------------------------");
				System.out.println("[문의관리]");
				System.out.println("------------------------------------");
				System.out.println("No.   |      제목      |      문의유형      |   작성일  |   작성자   | ");

				for (QnaDto qnaDto : qnaList) {
					System.out.print(qnaDto.getQna_no()  + " | ");
					System.out.print(qnaDto.getQna_title() + " | ");
					System.out.print(qnaDto.getQna_category() + " | ");
					System.out.print(qnaDto.getQna_date() + " | ");
					System.out.println(qnaDto.getUser_id());
				}

				if (pagerDto.getPageNo() == 1) {
					System.out.print("[처음]");
					for (int i = pagerDto.getStartPageNo(); i <= pagerDto.getEndPageNo(); i++) {
						System.out.print("[" + i + "]");
						if (i == pagerDto.getTotalPageNo()) {
							break;
						}
					}
					System.out.print("[다음][맨끝]");
					System.out.println();
				} else if (pagerDto.getPageNo() == pagerDto.getTotalPageNo()) {
					System.out.print("[처음][이전]");
					for (int i = pagerDto.getStartPageNo(); i <= pagerDto.getEndPageNo(); i++) {
						System.out.print("[" + i + "]");
						if (i == pagerDto.getTotalPageNo()) {
							break;
						}
					}
					System.out.print("[맨끝]");
					System.out.println();
				} else {
					System.out.print("[처음][이전]");
					for (int i = pagerDto.getStartPageNo(); i <= pagerDto.getEndPageNo(); i++) {
						System.out.print("[" + i + "]");
						if (i == pagerDto.getTotalPageNo()) {
							break;
						}
					}
					System.out.print("[다음][맨끝]");
					System.out.println();
				}

				// 사용자 메뉴 선택
				System.out.println("| 1.페이지 이동 | 2.문의목록 출력 | 3.답변미완료 문의목록 출력 | 4.문의글 상세조회 | 5.뒤로 가기 | 6.홈으로 이동 |");
				System.out.println("-> ");
				int menu = Integer.parseInt(sc.nextLine());
				if (menu == 5)
					break;
				switch (menu) {
				case 1:
					System.out.println("보고 싶은 페이지 입력-> ");
					String answer = sc.nextLine();
					if (answer.equals("처음")) {
						pageNo = 1;
					} else if (answer.equals("이전")) {
						if (pageNo - pagerDto.getPagesPerGroup() < 1) {
							System.out.println("더 뒤로 갈 수 없습니다.");
						} else {
							pageNo = pagerDto.getStartPageNo() - pagerDto.getPagesPerGroup();
						}
					} else if (answer.equals("다음")) {
						if (pagerDto.getEndPageNo() == pagerDto.getTotalPageNo()) {
							System.out.println("다음 페이지가 없습니다.");
						} else {
							pageNo = pagerDto.getStartPageNo() + pagerDto.getPagesPerGroup();
						}
					} else if (answer.equals("맨끝")) {
						pageNo = pagerDto.getTotalPageNo();
					} else {
						int answer2 = Integer.parseInt(answer);
						if (answer2 >= pagerDto.getStartPageNo() && answer2 <= pagerDto.getEndPageNo()) {
							pageNo = answer2;
						} else {
							System.out.println("범위 안에 없는 값입니다.");
						}
					}
					if (searchType == 1) {
						adminQnaList(bookClient, pageNo);
					} else if (searchType == 2) {
						adminQnaNoAnswerList(bookClient, pageNo);
					}
					break;

				case 2:
					searchType = 1;
					adminQnaList(bookClient, 1);
					break;

				case 3:
					searchType = 2;
					adminQnaNoAnswerList(bookClient, 1);
					break;

				case 4:
					System.out.println("상세 조회할 문의글 번호 입력-> ");
					int qnaNo = Integer.parseInt(sc.nextLine());
					boolean find = false;
					for (QnaDto qnaDto : qnaList) {
						if (qnaDto.getQna_no() == qnaNo) {
							find = true;
						}
					}
					if (find) {
						new AdminQnaReadView(bookClient, qnaNo);
					} else {
						System.out.println("없는 문의글입니다.");
					}
					break;

				case 6:
					new MainPageView(bookClient);
					break;

				}

			} catch (Exception e) {
				System.out.println("잘못 입력하셨습니다.");
			}
		}
	}

	// qna게시글 전체 목록
	public void adminQnaList(BookClient bookClient, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminQnaList");
		JSONObject data = new JSONObject();
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		qnaList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("qnaList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < list.length(); i++) {
			QnaDto qnaDto = new QnaDto();
			JSONObject jo = list.getJSONObject(i);
			qnaDto.setQna_no(jo.getInt("qnaNo"));
			qnaDto.setQna_category(jo.getString("qnaCategory"));
			qnaDto.setQna_title(jo.getString("qnaTitle"));
			qnaDto.setQna_content(jo.getString("qnaContent"));
			qnaDto.setQna_date(jo.getString("qnaDate"));
			qnaDto.setQna_view(jo.getInt("qnaView"));
			qnaDto.setUser_id(jo.getString("userId"));
			qnaList.add(qnaDto);
		}
		pagerDto = new PagerDto();
		pagerDto.setTotalRows(pager.getInt("totalRows"));
		pagerDto.setTotalPageNo(pager.getInt("totalPageNo"));
		pagerDto.setTotalGroupNo(pager.getInt("totalGroupNo"));
		pagerDto.setStartPageNo(pager.getInt("startPageNo"));
		pagerDto.setEndPageNo(pager.getInt("endPageNo"));
		pagerDto.setPageNo(pager.getInt("pageNo"));
		pagerDto.setPagesPerGroup(pager.getInt("pagesPerGroup"));
		pagerDto.setGroupNo(pager.getInt("groupNo"));
		pagerDto.setRowsPerPage(pager.getInt("rowsPerPage"));
		pagerDto.setStartRowNo(pager.getInt("startRowNo"));
		pagerDto.setStartRowIndex(pager.getInt("startRowIndex"));
		pagerDto.setEndRowNo(pager.getInt("endRowNo"));
		pagerDto.setEndRowIndex(pager.getInt("endRowIndex"));
	}

	// 답변 안달린 qna게시글 전체 목록
	public void adminQnaNoAnswerList(BookClient bookClient, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminQnaNoAnswerList");
		JSONObject data = new JSONObject();
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		qnaList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("qnaList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < list.length(); i++) {
			QnaDto qnaDto = new QnaDto();
			JSONObject jo = list.getJSONObject(i);
			qnaDto.setQna_no(jo.getInt("qnaNo"));
			qnaDto.setQna_category(jo.getString("qnaCategory"));
			qnaDto.setQna_title(jo.getString("qnaTitle"));
			qnaDto.setQna_content(jo.getString("qnaContent"));
			qnaDto.setQna_date(jo.getString("qnaDate"));
			qnaDto.setQna_view(jo.getInt("qnaView"));
			qnaDto.setUser_id(jo.getString("userId"));
			qnaList.add(qnaDto);
		}
		pagerDto = new PagerDto();
		pagerDto.setTotalRows(pager.getInt("totalRows"));
		pagerDto.setTotalPageNo(pager.getInt("totalPageNo"));
		pagerDto.setTotalGroupNo(pager.getInt("totalGroupNo"));
		pagerDto.setStartPageNo(pager.getInt("startPageNo"));
		pagerDto.setEndPageNo(pager.getInt("endPageNo"));
		pagerDto.setPageNo(pager.getInt("pageNo"));
		pagerDto.setPagesPerGroup(pager.getInt("pagesPerGroup"));
		pagerDto.setGroupNo(pager.getInt("groupNo"));
		pagerDto.setRowsPerPage(pager.getInt("rowsPerPage"));
		pagerDto.setStartRowNo(pager.getInt("startRowNo"));
		pagerDto.setStartRowIndex(pager.getInt("startRowIndex"));
		pagerDto.setEndRowNo(pager.getInt("endRowNo"));
		pagerDto.setEndRowIndex(pager.getInt("endRowIndex"));
	}

}
