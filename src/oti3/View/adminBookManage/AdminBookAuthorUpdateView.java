package oti3.View.adminBookManage;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookDto;
import oti3.DTO.PagerDto;
import oti3.View.adminAuthorManage.AdminAuthorCreateView;
import oti3.View.adminAuthorManage.AdminAuthorReadView;
import oti3.View.mainPageManage.MainPageView;

public class AdminBookAuthorUpdateView {
	private static Scanner sc = new Scanner(System.in);
	private ArrayList<AuthorDto> thisBookAuthorList;
	private ArrayList<AuthorDto> authorList;
	private PagerDto pagerDto;

	public AdminBookAuthorUpdateView(BookClient bookClient, int bookNo) {
		while (true) {
			System.out.println("| 1.저자 추가 | 2.저자 삭제 | 3.뒤로 가기 | 4.홈으로 이동 |");
			System.out.println("-> ");
			int menu1 = Integer.parseInt(sc.nextLine());
			if (menu1 == 3)
				break;
			switch (menu1) {
			case 1:
				// 책의 저자들 출력
				adminBookAuthorInfo(bookClient, bookNo);
				System.out.println("------------------------------------");
				System.out.println("[책의 저자 목록]");
				System.out.println("------------------------------------");
				System.out.println("No. |  저자명 ");
				for (AuthorDto authorDto : thisBookAuthorList) {
					System.out.print(authorDto.getAuthor_no() + " | ");
					System.out.println(authorDto.getAuthor_name());
					System.out.println("[저자소개] : " + authorDto.getAuthor_detail());
				}

				System.out.println("검색할 작가명 입력-> ");
				int pageNo = 1;
				String authorName = sc.nextLine();
				adminAuthorListByName(bookClient, authorName, 1);
				System.out.println();
				while (true) {
					try {
						// 페이지에 해당되는 list 얻기 + list 출력
						System.out.println("------------------------------------");
						System.out.println("[전체 저자 조회]");
						System.out.println("------------------------------------");
						System.out.println("No. |  저자명 ");
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
						System.out.println("| 1.페이지 이동 | 2.추가할 저자 선택 | 3.저자 이름 재입력 | 4.뒤로가기 | 5.홈으로 이동 |");
						System.out.println("->");
						int menu = Integer.parseInt(sc.nextLine());
						if (menu == 4)
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
								adminAuthorListByName(bookClient, authorName, pageNo);
							}
							break;

						case 2:
							System.out.println("추가할 저자 번호 입력-> ");
							int authorNo = Integer.parseInt(sc.nextLine());
							boolean find = false;
							for (AuthorDto authorDto : authorList) {
								if (authorDto.getAuthor_no() == authorNo) {
									find = true;
									break;
								}
							}
							if (find) {
								boolean find2 = false;
								for (AuthorDto authorDto : thisBookAuthorList) {
									if (authorDto.getAuthor_no() == authorNo) {
										find2 = true;
									}
								}
								if (find2) {
									System.out.println("이미 책에 존재하는 저자입니다.");
								} else {
									int rows = adminBookAuthorAdd(bookClient, bookNo, authorNo);
									if (rows == 1) {
										System.out.println("저자 추가 성공");
									} else {
										System.out.println("저자 추가 실패");
									}
								}
							} else {
								System.out.println("현재 페이지에 해당 저자가 없습니다.");
							}
							break;

						case 3:
							System.out.println("검색할 작가명 입력-> ");
							authorName = sc.nextLine();
							adminAuthorListByName(bookClient, authorName, 1);
							break;

						case 5:
							new MainPageView(bookClient);
							break;

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;

			case 2:
				// 책의 저자들 출력
				adminBookAuthorInfo(bookClient, bookNo);
				System.out.println("------------------------------------");
				System.out.println("[책의 저자 목록]");
				System.out.println("------------------------------------");
				System.out.println("No. |  저자명 ");
				for (AuthorDto authorDto : thisBookAuthorList) {
					System.out.print(authorDto.getAuthor_no() + " | ");
					System.out.println(authorDto.getAuthor_name());
					System.out.println("[저자소개] : " + authorDto.getAuthor_detail());
				}

				System.out.println("삭제할 저자 번호 입력-> ");
				int authorNo = Integer.parseInt(sc.nextLine());
				boolean find2 = false;
				for (AuthorDto authorDto : thisBookAuthorList) {
					if (authorDto.getAuthor_no() == authorNo) {
						find2 = true;
					}
				}
				if (find2) {
					int rows = adminBookAuthorPop(bookClient, bookNo, authorNo);
					if (rows == 1) {
						System.out.println("저자 삭제 성공");
					} else {
						System.out.println("저자 삭제 실패");
					}
				} else {
					System.out.println("책에 존재하지 않는 저자입니다.");
				}

				break;

			case 4:
				new MainPageView(bookClient);
				break;

			}
		}

	}

	// 책에 저자 추가
	public int adminBookAuthorAdd(BookClient bookClient, int bookNo, int authorNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminBookAuthorAdd");
		JSONObject data = new JSONObject();
		data.put("bookNo", bookNo);
		data.put("authorNo", authorNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		return (new JSONObject(json).getInt("data"));
	}

	// 책의 저자 삭제
	public int adminBookAuthorPop(BookClient bookClient, int bookNo, int authorNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminBookAuthorPop");
		JSONObject data = new JSONObject();
		data.put("bookNo", bookNo);
		data.put("authorNo", authorNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		return (new JSONObject(json).getInt("data"));
	}

	// 현재 책의 저자 정보 보기
	public void adminBookAuthorInfo(BookClient bookClient, int bookNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminBookAuthorInfo");
		JSONObject data = new JSONObject();
		data.put("bookNo", bookNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3)
		JSONObject joo = new JSONObject(json);
		thisBookAuthorList = new ArrayList<>();

		JSONArray list = joo.getJSONArray("data");

		for (int i = 0; i < list.length(); i++) {
			AuthorDto authorDto = new AuthorDto();
			JSONObject jo = list.getJSONObject(i);
			authorDto.setAuthor_no(jo.getInt("authorNo"));
			authorDto.setAuthor_name(jo.getString("authorName"));
			authorDto.setAuthor_detail(jo.getString("authorDetail"));
			thisBookAuthorList.add(authorDto);
		}
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
