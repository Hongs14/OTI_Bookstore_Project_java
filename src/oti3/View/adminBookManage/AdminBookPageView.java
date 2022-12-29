package oti3.View.adminBookManage;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookDto;
import oti3.DTO.PagerDto;
import oti3.View.mainPageManage.MainPageView;

public class AdminBookPageView {
	private ArrayList<BookDto> bookList;
	private static Scanner sc = new Scanner(System.in);
	private PagerDto pagerDto;

	public AdminBookPageView(BookClient bookClient) {
		int pageNo = 1;
		int searchType = 1;
		String searchId = "";
		adminBookListOrderByPublishDate(bookClient, 1);

		while (true) {
			try {
				// 페이지에 해당되는 list 얻기 + list 출력
				System.out.println("------------------------------------");
				System.out.println("[상품관리]");
				System.out.println("------------------------------------");
				System.out.println("No.   | 책제목 |   저자  |  출판사 |  가격  |  출간일  |  카테고리  | 서브카테고리 | 판매량 ");
				
				for (BookDto bookDto : bookList) {
					System.out.print(bookDto.getBook_no() + " |");
					System.out.print(bookDto.getBook_name() + " |");
					if (bookDto.getAuthors().size() != 0) {
						System.out.print("| " + bookDto.getAuthors().get(0).getAuthor_name() + " 외 "
								+ (bookDto.getAuthors().size() - 1) + "명 | ");
					}
					System.out.print(bookDto.getBook_publisher() + " |");
					System.out.print(bookDto.getBook_price() + " |");
					System.out.print(bookDto.getBook_date() + " |");
					System.out.print(bookDto.getCategory_name() + " |");
					System.out.print(bookDto.getSub_name() + " |");
					if(searchType == 2) {
						System.out.print(bookDto.getSales() + "개");
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
				// 사용자 메뉴 선택
				System.out.println("| 1.페이지 이동 | 2.판매량 순 목록 출력 | 3.출간일 순 목록 출력 | 4.제목 검색 | 5.책 추가 | 6.책 상세 조회 | 7.뒤로 가기 | 8.홈으로 이동 |");
				System.out.println("->");
				int menu = Integer.parseInt(sc.nextLine());
				if (menu == 7)
					break;
				switch (menu) {
				case 1:
					System.out.println("페이지 입력-> ");
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
						adminBookListOrderByPublishDate(bookClient, pageNo);
					} else if (searchType == 2) {
						adminBookListBySales(bookClient, pageNo);
					} else if (searchType == 3) {
						adminBookListByBookName(bookClient, searchId, pageNo);
					}
					break;

				case 2:
					searchType = 2;
					adminBookListBySales(bookClient, 1);
					break;

				case 3:
					searchType = 1;
					adminBookListOrderByPublishDate(bookClient, 1);
					break;

				case 4:
					searchType = 3;
					System.out.println("책 이름 입력-> ");
					searchId = sc.nextLine();
					System.out.println(searchId);
					adminBookListByBookName(bookClient, searchId, 1);
					break;

				case 5:
					new AdminBookCreateView(bookClient);
					break;

				case 6:
					System.out.println("상세 조회할 책 번호 입력-> ");
					int bookNo = Integer.parseInt(sc.nextLine());
					boolean find = false;
					for (BookDto book : bookList) {
						if (book.getBook_no() == bookNo) {
							find = true;
							break;
						}
					}
					if (find) {
						new AdminBookReadView(bookClient, bookNo);
					} else {
						System.out.println("현재 페이지에 존재하지 않는 책입니다.");
					}
					break;

				case 8:
					new MainPageView(bookClient);
					break;

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 책 리스트 조회(제목 검색)
	public void adminBookListByBookName(BookClient bookClient, String bookName, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminBookListByBookName");
		JSONObject data = new JSONObject();
		data.put("bookName", bookName);
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		bookList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("bookList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < list.length(); i++) {
			BookDto book = new BookDto();
			JSONObject jo = list.getJSONObject(i);
			JSONArray authors = jo.getJSONArray("authors");
			ArrayList<AuthorDto> authorList = new ArrayList<>();
			for (int j = 0; j < authors.length(); j++) {
				AuthorDto author = new AuthorDto();
				author.setAuthor_no(authors.getJSONObject(j).getInt("authorNo"));
				author.setAuthor_name(authors.getJSONObject(j).getString("authorName"));
				author.setAuthor_detail(authors.getJSONObject(j).getString("authorDetail"));
				authorList.add(author);
			}
			book.setAuthors(authorList);

			book.setBook_no(jo.getInt("bookNo"));
			book.setBook_name(jo.getString("bookName"));
			book.setBook_publisher(jo.getString("bookPublisher"));
			book.setBook_price(jo.getInt("bookPrice"));
			book.setBook_page(jo.getInt("bookPage"));
			book.setBook_date(jo.getString("bookDate"));
			book.setSub_name(jo.getString("subName"));
			book.setCategory_name(jo.getString("categoryName"));
			bookList.add(book);
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

	// 책 리스트 조회(출간일 순)
	public void adminBookListOrderByPublishDate(BookClient bookClient, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminBookListOrderByPublishDate");
		JSONObject data = new JSONObject();
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		bookList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("bookList");
		JSONObject pager = receiveData.getJSONObject("pager");
		for (int i = 0; i < list.length(); i++) {
			BookDto book = new BookDto();
			JSONObject jo = list.getJSONObject(i);
			JSONArray authors = jo.getJSONArray("authors");
			ArrayList<AuthorDto> authorList = new ArrayList<>();
			for (int j = 0; j < authors.length(); j++) {
				AuthorDto author = new AuthorDto();
				author.setAuthor_no(authors.getJSONObject(j).getInt("authorNo"));
				author.setAuthor_name(authors.getJSONObject(j).getString("authorName"));
				author.setAuthor_detail(authors.getJSONObject(j).getString("authorDetail"));
				authorList.add(author);
			}
			book.setAuthors(authorList);

			book.setBook_no(jo.getInt("bookNo"));
			book.setBook_name(jo.getString("bookName"));
			book.setBook_publisher(jo.getString("bookPublisher"));
			book.setBook_price(jo.getInt("bookPrice"));
			book.setBook_page(jo.getInt("bookPage"));
			book.setBook_date(jo.getString("bookDate"));
			book.setSub_name(jo.getString("subName"));
			book.setCategory_name(jo.getString("categoryName"));
			bookList.add(book);
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

	// 책 리스트 조회(판매량 순)
	public void adminBookListBySales(BookClient bookClient, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminBookListBySales");
		JSONObject data = new JSONObject();
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		bookList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("bookList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < list.length(); i++) {
			BookDto book = new BookDto();
			JSONObject jo = list.getJSONObject(i);
			JSONArray authors = jo.getJSONArray("authors");
			ArrayList<AuthorDto> authorList = new ArrayList<>();
			for (int j = 0; j < authors.length(); j++) {
				AuthorDto author = new AuthorDto();
				author.setAuthor_no(authors.getJSONObject(j).getInt("authorNo"));
				author.setAuthor_name(authors.getJSONObject(j).getString("authorName"));
				author.setAuthor_detail(authors.getJSONObject(j).getString("authorDetail"));
				authorList.add(author);
			}
			book.setAuthors(authorList);

			book.setBook_no(jo.getInt("bookNo"));
			book.setBook_name(jo.getString("bookName"));
			book.setBook_publisher(jo.getString("bookPublisher"));
			book.setBook_price(jo.getInt("bookPrice"));
			book.setBook_page(jo.getInt("bookPage"));
			book.setBook_date(jo.getString("bookDate"));
			book.setSales(jo.getInt("bookSales"));
			book.setSub_name(jo.getString("subName"));
			book.setCategory_name(jo.getString("categoryName"));
			bookList.add(book);
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
