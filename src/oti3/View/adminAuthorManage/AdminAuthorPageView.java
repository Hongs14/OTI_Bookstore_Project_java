package oti3.View.adminAuthorManage;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;
import oti3.DTO.PagerDto;
import oti3.View.mainPageManage.MainPageView;

public class AdminAuthorPageView {
	private static Scanner sc = new Scanner(System.in);
	private ArrayList<AuthorDto> authorList;
	private PagerDto pagerDto;

	public AdminAuthorPageView(BookClient bookClient) {
		int pageNo = 1;
		int searchType = 1;
		String searchId = "";
		adminAuthorList(bookClient, 1);
		while (true) {
			try {
				// 페이지에 해당되는 list 얻기 + list 출력
				System.out.println("------------------------------------");
				System.out.println("[저자관리]");
				System.out.println("------------------------------------");
				System.out.println("No.   |   저자명  ");
				for (AuthorDto authorDto : authorList) {
					System.out.print(authorDto.getAuthor_no() + " | ");
					System.out.println(authorDto.getAuthor_name());
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
				System.out.println("| 1.페이지 이동 | 2.전체 저자 조회 | 3.저자명 검색 | 4.저자 상세조회 | 5.저자 추가 | 6.뒤로 가기 | 7.홈으로 이동 |");
				System.out.println("입력 : ");
				int menu = Integer.parseInt(sc.nextLine());
				if (menu == 6)
					break;
				switch (menu) {
				case 1:
					System.out.println("보고 싶은 페이지 입력 : -> ");
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
						adminAuthorList(bookClient, pageNo);
					} else if (searchType == 2) {
						adminAuthorListByName(bookClient, searchId, pageNo);
					}
					break;

				case 2:
					searchType = 1;
					adminAuthorList(bookClient, 1);
					break;

				case 3:
					searchType = 2;
					System.out.println("저자 이름 입력-> ");
					searchId = sc.nextLine();
					adminAuthorListByName(bookClient, searchId, 1);
					break;

				case 4:
					System.out.println("상세 조회할 저자 번호를 입력->");
					int authorNo = Integer.parseInt(sc.nextLine());
					boolean find = false;
					for (AuthorDto authorDto : authorList) {
						if (authorDto.getAuthor_no() == authorNo) {
							find = true;
						}
					}
					if (find) {
						new AdminAuthorReadView(bookClient, authorNo);
					} else {
						System.out.println("없는 저자입니다.");
					}
					break;

				case 5:
					new AdminAuthorCreateView(bookClient);
					break;

				case 7:
					new MainPageView(bookClient);
					break;

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 저자 목록 출력
	public void adminAuthorList(BookClient bookClient, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminAuthorList");
		JSONObject data = new JSONObject();
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		authorList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("authorList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < list.length(); i++) {
			AuthorDto authorDto = new AuthorDto();
			JSONObject jo = list.getJSONObject(i);
			authorDto.setAuthor_no(jo.getInt("authorNo"));
			authorDto.setAuthor_name(jo.getString("authorName"));
			authorDto.setAuthor_detail(jo.getString("authorDetail"));
			authorList.add(authorDto);
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

	// 저자 이름 검색 목록 출력
	public void adminAuthorListByName(BookClient bookClient, String authorName, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminAuthorListByName");
		JSONObject data = new JSONObject();
		data.put("authorName", authorName);
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		authorList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("authorList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < list.length(); i++) {
			AuthorDto authorDto = new AuthorDto();
			JSONObject jo = list.getJSONObject(i);
			authorDto.setAuthor_no(jo.getInt("authorNo"));
			authorDto.setAuthor_name(jo.getString("authorName"));
			authorDto.setAuthor_detail(jo.getString("authorDetail"));
			authorList.add(authorDto);
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
