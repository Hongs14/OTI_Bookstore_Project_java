package oti3.View.qnaView;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;
import oti3.DTO.SearchDto;
import oti3.View.mainPageManage.MainPageView;
import oti3.View.searchView.BookReadView;

//문의 게시판 전체 목록 뷰
public class QnaPageView {
	ArrayList<QnaDto> qlist;
	Scanner sc = new Scanner(System.in);
	private PagerDto pagerDto = new PagerDto();

	public QnaPageView(BookClient bookClient) {
		int pageNo = 1;
		int searchType = 1;
		String qnaCategory = null;
		selectQlist(1, bookClient);
		while (true) {
			try {
				// 1) qna 게시물 목록 출력
				System.out.println("------------------------------------");
				System.out.println("[QNA 게시판]");
				System.out.println("------------------------------------");
				System.out.println("No.   |   카테고리   |      제목      |   작성자   |   날짜   |  조회수 ");
				
				for (int i = 0; i < qlist.size(); i++) {
					System.out.print(qlist.get(i).getQna_no() + " | ");
					System.out.print(qlist.get(i).getQna_category() + " | ");
					System.out.print(qlist.get(i).getQna_title() + " | ");
					System.out.print(qlist.get(i).getUser_id() + " | ");
					System.out.print(qlist.get(i).getQna_date() + " | ");
					System.out.println(qlist.get(i).getQna_view());
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

				// 2 ) 사용자 입력
				System.out.println();
				System.out.println("| 1. 전체목록 조회 | 2. 문의유형 검색 | 3. 페이지 이동 | 4. 문의글 보기 | 5. 1:1 문의 접수 | 6. 홈으로 가기 |");
				System.out.print("-> ");

				String menuNo = sc.nextLine();
				if (menuNo.equals("1")) {
					searchType = 1;
					selectQlist(1, bookClient);

				} else if (menuNo.equals("2")) {
					searchType = 2;
					System.out.println(
							"----------------------------------------------------------------------------------");
					System.out.println("| 1. 배송  |  2. 주문/결제  |  3. 도서/상품정보  |  4. 반품/교환/환불  |");
					System.out.println("| 5. 회원정보서비스 | 6. 웹사이트 이용 관련 | 7. 시스템 불편사항 | 8. 기타 |");
					System.out.println(
							"----------------------------------------------------------------------------------");
					System.out.print("검색하실 문의 유형 입력-> ");
					qnaCategory = sc.nextLine();
					switch (qnaCategory) {
					case "1":
						qnaCategory = "[배송]";
						break;
					case "2":
						qnaCategory = "[주문/결제]";
						break;
					case "3":
						qnaCategory = "[도서/상품정보]";
						break;
					case "4":
						qnaCategory = "[반품/교환/환불]";
						break;
					case "5":
						qnaCategory = "[회원정보서비스]";
						break;
					case "6":
						qnaCategory = "[웹사이트 이용 관련]";
						break;
					case "7":
						qnaCategory = "[시스템 불편사항]";
						break;
					case "8":
						qnaCategory = "[기타]";
						break;
					default:
						System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
					}
					selectQcglist(qnaCategory, 1, bookClient);
				}

				else if (menuNo.equals("3")) {
					System.out.print("페이지 입력-> ");
					String answer = sc.nextLine();

					if (answer.equals("처음")) {
						pageNo = 1;
					} else if (answer.equals("이전")) {
						if (pageNo - pagerDto.getPagesPerGroup() < 1) {
							System.out.println("이전 페이지가 없습니다.");
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
						selectQlist(pageNo, bookClient);
					} else if (searchType == 2) {
						selectQcglist(qnaCategory, pageNo, bookClient);
					}

				} else if (menuNo.equals("4")) {
					System.out.print("상세조회할 글 번호-> ");
					int qnaNo = Integer.parseInt(sc.nextLine());
					if (bookClient.loginUser.getUser_id() != null) {
						boolean find = false;
						for (QnaDto qnaDto : qlist) {
							if (qnaDto.getQna_no() == qnaNo) {
								find = true;
								break;
							}
						}
						if (find) {
							new QnaReadView(bookClient, qnaNo);
						} else {
							System.out.println("현재 보고 있는 페이지에 해당 게시물은 없습니다.");
						}
					} else {
						System.out.println("로그인이 필요한 서비스입니다.. 홈으로 이동합니다.");
						new MainPageView(bookClient);
					}
				} else if (menuNo.equals("5")) {
					if (bookClient.loginUser.getUser_id() != null) {
						new QnaCreateView(bookClient);
					} else {
						System.out.println("로그인이 필요한 서비스입니다.. 홈으로 이동합니다.");
						new MainPageView(bookClient);
					}
				} else if (menuNo.equals("6")) {
					new MainPageView(bookClient);
				} else {
					System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");

				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
				System.out.println();
				System.out
						.println("==================================================================================");
				System.out.println("[문의 게시판]");

			}
		}
	}

	public void selectQlist(int pageNo, BookClient bookClient) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "selectQlist");

		JSONObject data = new JSONObject();
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String receiveJson = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject jo = new JSONObject(receiveJson);
		JSONObject dt = jo.getJSONObject("data");
		qlist = new ArrayList<>();

		JSONArray jarr = dt.getJSONArray("qboardlist");
		JSONObject pager = dt.getJSONObject("pager");

		for (int i = 0; i < jarr.length(); i++) {
			JSONObject jo2 = jarr.getJSONObject(i);
			QnaDto qnas = new QnaDto();
			qnas.setQna_no(jo2.getInt("qnaNo"));
			qnas.setQna_category(jo2.getString("qnaCategory"));
			qnas.setUser_id(jo2.getString("userId"));
			qnas.setQna_title(jo2.getString("qnaTitle"));
			qnas.setQna_date(jo2.getString("qnaDate"));
			qnas.setQna_view(jo2.getInt("qnaView"));
			qlist.add(qnas);
		}
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

	public void selectQcglist(String qnaCategory, int pageNo, BookClient bookClient) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "selectQcglist");

		JSONObject data = new JSONObject();
		data.put("qnaCategory", qnaCategory);
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String receiveJson = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject jo = new JSONObject(receiveJson);
		JSONObject dt = jo.getJSONObject("data");
		qlist = new ArrayList<>();

		JSONArray jarr = dt.getJSONArray("qboardlist");
		JSONObject pager = dt.getJSONObject("pager");
		for (int i = 0; i < jarr.length(); i++) {
			JSONObject jo2 = jarr.getJSONObject(i);
			QnaDto selQcg = new QnaDto();
			selQcg.setQna_no(jo2.getInt("qnaNo"));
			selQcg.setQna_category(jo2.getString("qnaCategory"));
			selQcg.setUser_id(jo2.getString("userId"));
			selQcg.setQna_title(jo2.getString("qnaTitle"));
			selQcg.setQna_date(jo2.getString("qnaDate"));
			selQcg.setQna_view(jo2.getInt("qnaView"));
			qlist.add(selQcg);
		}
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