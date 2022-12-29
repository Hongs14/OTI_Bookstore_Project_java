package oti3.View.myPageView;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DAO.MainPageDao;
import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;
import oti3.View.mainPageManage.MainPageView;
import oti3.View.qnaView.QnaReadView;

public class MyQnaPageView {
	private Scanner sc = new Scanner(System.in);
	private ArrayList<QnaDto> selectQna;
	private PagerDto pagerDto;

	public MyQnaPageView(BookClient bookClient) {
		int pageNo = 1;
		while (true) {
			System.out.println("------------------------------------");
			System.out.println("[" + bookClient.loginUser.getUser_id() + "님이 작성한 QNA목록]");
			System.out.println("------------------------------------");
			System.out.println("No.   |        제목        |   카테고리  |   날짜   |  조회수 ");

			try {
				selectQna(bookClient, pageNo);
				for (int i = 0; i < selectQna.size(); i++) {
					System.out.print(selectQna.get(i).getQna_no() + " | ");
					System.out.print(selectQna.get(i).getQna_title() + " | ");
					System.out.print(selectQna.get(i).getQna_category() + " | ");
					System.out.print(selectQna.get(i).getQna_date() + " | ");
					System.out.print(selectQna.get(i).getQna_view());
					System.out.println();
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

				System.out.println("| 1.페이지 이동 | 2.Qna자세히 보기 | 3.뒤로가기 | 4.홈으로 |");
				System.out.print("-> ");
				String answer = sc.nextLine();
				if (answer.equals("1")) {
					System.out.print("페이지 선택-> ");
					String answer2 = sc.nextLine();
					if (answer.equals("처음")) {
						pageNo = 1;
					} else if (answer2.equals("이전")) {
						if (pageNo - pagerDto.getPagesPerGroup() < 1) {
							System.out.println("더 뒤로 갈 수 없습니다.");
						} else {
							pageNo = pagerDto.getStartPageNo() - pagerDto.getPagesPerGroup();
						}
					} else if (answer2.equals("다음")) {
						if (pagerDto.getEndPageNo() == pagerDto.getTotalPageNo()) {
							System.out.println("다음 페이지가 없습니다.");
						} else {
							pageNo = pagerDto.getStartPageNo() + pagerDto.getPagesPerGroup();
						}
					} else if (answer2.equals("맨끝")) {
						pageNo = pagerDto.getTotalPageNo();
					} else {
						int answer3 = Integer.parseInt(answer2);
						if (answer3 >= pagerDto.getStartPageNo() && answer3 <= pagerDto.getEndPageNo()) {
							pageNo = answer3;
						} else {
							System.out.println("범위 안에 없는 값입니다.");
						}
					}
					selectQna(bookClient, pageNo);
				} else if (answer.equals("2")) {
					System.out.print("상세조회할 QNA번호롤 고르시오-> ");
					int qnano = Integer.parseInt(sc.nextLine());
					boolean flag2 = false;
					for (QnaDto qnadto : selectQna) {
						if (qnadto.getQna_no() == qnano) {
							flag2 = true;
							break;
						}
					}
					if (flag2) {
						new QnaReadView(bookClient, qnano);
					} else {
						System.out.println("페이지에 없는 문의입니다");
					}
				} else if (answer.equals("3")) {
					break;
				} else if (answer.equals("4")) {
					new MainPageView(bookClient);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("잘못 입력하셨습니다.");
			}
		}
	}

	public void selectQna(BookClient bookClient, int pageNo) {
		// json생성
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "selectQna");
		JSONObject data = new JSONObject();
		data.put("userId", bookClient.loginUser.getUser_id());
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);

		// json보내기
		bookClient.send(jsonObject.toString());
		// json받기
		String json = bookClient.receive();

		// json파싱
		JSONObject jo = new JSONObject(json);
		JSONObject parseData = jo.getJSONObject("data");
		JSONArray joa = parseData.getJSONArray("qnaList");
		selectQna = new ArrayList<>();
		for (int i = 0; i < joa.length(); i++) {
			// array를 또 파싱
			JSONObject joo = joa.getJSONObject(i);
			// 빈객체 생성
			QnaDto selectqna = new QnaDto();
			selectqna.setQna_no(joo.getInt("qnaNo"));
			selectqna.setQna_title(joo.getString("qnaTitle"));
			selectqna.setQna_category(joo.getString("qnaCategory"));
			selectqna.setQna_date(joo.getString("qnaDate"));
			selectqna.setQna_view(joo.getInt("qnaView"));
			selectQna.add(selectqna);
		}

		JSONObject pager = parseData.getJSONObject("pager");
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
