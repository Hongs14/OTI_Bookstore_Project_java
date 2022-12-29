package oti3.View.searchView;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookHashDto;
import oti3.DTO.CategoryDto;
import oti3.DTO.PagerDto;
import oti3.DTO.SearchDto;
import oti3.DTO.SubCategoryDto;
import oti3.View.mainPageManage.MainPageView;

public class CategoryPageView {
	int book_no;
	Scanner scanner = new Scanner(System.in);
	ArrayList<CategoryDto> maincategorylist;
	ArrayList<SubCategoryDto> subcategorylist;
	ArrayList<SearchDto> pagelist;
	PagerDto pagerDto;

	// 생성자
	public CategoryPageView(BookClient bookClient) {
		while (true) {
			MainCategory(bookClient);
			int category_no = 0;
			int sub_no = 0;
			while (true) {
				System.out.println("메인 카테고리를 선택해 주세요 ");
				System.out.print("-> ");
				category_no = Integer.parseInt(scanner.nextLine());
				boolean categoryFlag = false;
				for (CategoryDto categoryDto : maincategorylist) {
					if (categoryDto.getCategory_no() == category_no) {
						categoryFlag = true;
						break;
					}
				}
				if (categoryFlag) {
					break;
				} else {
					System.out.println("카테고리를 다시 선택해주세요.");
				}
			}

			SubCategory(bookClient, category_no);
			while (true) {
				System.out.println("서브 카테고리를 선택해 주세요 ");
				System.out.print("-> ");
				sub_no = Integer.parseInt(scanner.nextLine());
				boolean categoryFlag = false;
				for (SubCategoryDto subCategoryDto : subcategorylist) {
					if (subCategoryDto.getSub_no() == sub_no) {
						categoryFlag = true;
						break;
					}
				}
				if (categoryFlag) {
					break;
				} else {
					System.out.println("서브 카테고리를 다시 선택해주세요.");
				}
			}

			int pageNo = 1;
			CategoryBoard(bookClient, sub_no, 1);
			while (true) {
				try {
					System.out.println("------------------------------------");
					System.out.println("[검색 결과]");
					System.out.println("------------------------------------");
					System.out.println("No.   |       책제목        |   출판사  |   가격   | 평균별점 | 작가 | 해시태그");

					for (int i = 0; i < pagelist.size(); i++) {
						System.out.print(pagelist.get(i).getBook_no() + " | ");
						System.out.print(pagelist.get(i).getBook_name() + " | ");
						System.out.print(pagelist.get(i).getBook_publisher() + " | ");
						System.out.print(pagelist.get(i).getBook_price() + " | ");
						for (int j = 0; j < pagelist.get(i).getReviews_avg(); j++) {
							System.out.print("*");
						}
						for (int j = 0; j < pagelist.get(i).getAuthor_name().size(); j++) {
							if (j == pagelist.get(i).getAuthor_name().size() - 1) {
								System.out.print(pagelist.get(i).getAuthor_name().get(j).getAuthor_name() + " ");
							} else {
								System.out.print(pagelist.get(i).getAuthor_name().get(j).getAuthor_name() + ", ");
							}
						}
						for (int j = 0; j < pagelist.get(i).getHash_id().size(); j++) {
							if (j == pagelist.get(i).getHash_id().size() - 1) {
								System.out.print(pagelist.get(i).getHash_id().get(j).getHash_id() + " ");
							} else {
								System.out.print(pagelist.get(i).getHash_id().get(j).getHash_id() + ", ");
							}
						}
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
					System.out.println("| 1. 페이지 이동 | 2. 상세 조회 | 3. 홈으로 이동 |");
					System.out.print("-> ");
					String answer = scanner.nextLine();
					if (answer.equals("1")) {
						System.out.print("페이지 입력-> ");
						String answer2 = scanner.nextLine();
						if (answer2.equals("처음")) {
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
						CategoryBoard(bookClient, sub_no, pageNo);
					} else if (answer.equals("2")) {
						System.out.println("책 번호를 입력해주세요. ");
						System.out.print("-> ");
						int book_no = Integer.parseInt(scanner.nextLine());
						boolean find = false;
						for (SearchDto searchDto : pagelist) {
							if (searchDto.getBook_no() == book_no) {
								find = true;
								break;
							}
						}
						if (find) {
							new BookReadView(bookClient, book_no);
						} else {
							System.out.println("현재 보고 있는 페이지에 해당 책은 없습니다.");
						}
					} else if (answer.equals("3")) {
						new MainPageView(bookClient);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("잘못 입력하셨습니다.");
				}
			}
		}
	}

	// 메인 카테고리 뷰에 출력
	public void MainCategory(BookClient bookClient) {
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "category");
		sendJson.put("data", new JSONObject());

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);
		maincategorylist = new ArrayList<>();
		JSONArray jarr = jo.getJSONArray("data");
		for (int i = 0; i < jarr.length(); i++) {
			JSONObject jo2 = (JSONObject) jarr.get(i);
			CategoryDto cd = new CategoryDto();
			cd.setCategory_no(jo2.getInt("category_no"));
			cd.setCategory_name(jo2.getString("category_name"));
			maincategorylist.add(cd);
		}
		System.out.println("============================================================");
		for (int i = 0; i < maincategorylist.size(); i++) {
			System.out.print(maincategorylist.get(i).getCategory_no() + "." + maincategorylist.get(i).getCategory_name()
					+ "   ");
		}
		System.out.println();
		System.out.println("============================================================");
	}

	// 서브 카테고리 뷰에 출력

	public void SubCategory(BookClient bookClient, int category_no) {
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "subCategory");
		sendJson.put("data", new JSONObject().put("category_no", category_no));

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);
		subcategorylist = new ArrayList<>();
		JSONArray jarr = jo.getJSONArray("data");
		for (int i = 0; i < jarr.length(); i++) {
			JSONObject jo2 = (JSONObject) jarr.get(i);
			SubCategoryDto sc = new SubCategoryDto();
			sc.setSub_no(jo2.getInt("sub_no"));
			sc.setSub_name(jo2.getString("sub_name"));
			subcategorylist.add(sc);
		}
		System.out.println("============================================================");
		for (int i = 0; i < subcategorylist.size(); i++) {
			System.out.print(subcategorylist.get(i).getSub_no() + "." + subcategorylist.get(i).getSub_name() + "   ");
		}
		System.out.println();
		System.out.println("============================================================");

	}

	// 카테고리
	public void CategoryBoard(BookClient bookClient, int sub_no, int pageNo) {
		// json 요청
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "categoryBoard");
		JSONObject data = new JSONObject();
		data.put("sub_no", sub_no);
		data.put("pageNo", pageNo);
		sendJson.put("data", data);
		bookClient.send(sendJson.toString());

		// 응답
		String receiveJson = bookClient.receive();

		// 응답 json 파싱 후 필드에 넣기
		JSONObject jo = new JSONObject(receiveJson);
		JSONObject receiveData = jo.getJSONObject("data");
		pagelist = new ArrayList<>();

		JSONArray jarr = receiveData.getJSONArray("bookList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < jarr.length(); i++) {
			SearchDto cp = new SearchDto();
			JSONObject jo2 = jarr.getJSONObject(i);
			cp.setBook_no(jo2.getInt("book_no"));
			cp.setBook_name(jo2.getString("book_name"));
			cp.setBook_publisher(jo2.getString("book_publisher"));
			cp.setBook_price(jo2.getInt("book_price"));
			cp.setReviews_avg(jo2.getInt("review_avg"));

			JSONArray ja = jo2.getJSONArray("author_name");
			ArrayList<AuthorDto> alist = new ArrayList<AuthorDto>();
			for (int j = 0; j < ja.length(); j++) {
				AuthorDto an = new AuthorDto();
				an.setAuthor_name(ja.getJSONObject(j).getString("author_name"));
				alist.add(an);
			}
			cp.setAuthor_name(alist);

			JSONArray ja2 = jo2.getJSONArray("hash_id");
			ArrayList<BookHashDto> hlist = new ArrayList<BookHashDto>();
			for (int j = 0; j < ja2.length(); j++) {
				BookHashDto hi = new BookHashDto();
				hi.setHash_id(ja2.getJSONObject(j).getString("hash_id"));
				hlist.add(hi);
			}
			cp.setHash_id(hlist);
			pagelist.add(cp);
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
