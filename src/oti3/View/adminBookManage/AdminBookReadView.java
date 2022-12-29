package oti3.View.adminBookManage;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookDto;
import oti3.DTO.BookHashDto;
import oti3.DTO.PagerDto;
import oti3.DTO.WarehousingDto;
import oti3.Service.MainPageService;
import oti3.View.mainPageManage.MainPageView;

public class AdminBookReadView {
	private BookDto book;
	private static Scanner sc = new Scanner(System.in);
	private ArrayList<WarehousingDto> wareList;
	private PagerDto pagerDto;

	public AdminBookReadView(BookClient bookClient, int bookNo) {
		while (true) {
			adminBookInfo(bookClient, bookNo);
			// book 책 정보 출력

			System.out.println("[ 책 상세 정보 ]");
			System.out.println("[ 책 제목 : " + book.getBook_name() + " ]");
			System.out.println("[ 책 가격 : " + book.getBook_price() + "원 ]");
			System.out.println("[ 책 출판사 : " + book.getBook_publisher() + " ]");
			System.out.println("[ 책 재고 : " + book.getBook_store() + "권 ]");
			System.out.println("[ 책 페이지 수 : " + book.getBook_page() + "쪽 ]");
			System.out.println("[ 책 언어 : " + book.getBook_lang() + " ]");
			System.out.println("[ 책 출간일 : " + book.getBook_date() + " ]");
			System.out.println("[ 책 카테고리 : " + book.getCategory_name() + " ]");
			System.out.println("[ 책 서브카테고리 : " + book.getSub_name() + " ]");
			System.out.println();
			System.out.print("[ 책 소개글 ]");
			System.out.println();
			for (int i = 0; i < book.getBook_detail().length(); i++) {
				if (i % 50 == 0 && i != 0) {
					System.out.println();
				}
				System.out.print(book.getBook_detail().charAt(i));
			}
			System.out.println();
			System.out.println("[ 작가 정보 ]");
			for (int i = 0; i < book.getAuthors().size(); i++) {
				System.out.print("[ 작가 : ");
				System.out.print(book.getAuthors().get(i).getAuthor_name());
				System.out.print(" ]");
				System.out.println();
				System.out.print("[ 작가 소개글 ]");
				for (int j = 0; j < book.getAuthors().get(i).getAuthor_detail().length(); j++) {
					if (j % 50 == 0 && j != 0) {
						System.out.println();
					}
					System.out.print(book.getAuthors().get(i).getAuthor_detail().charAt(j));
				}
				System.out.println();
			}
			System.out.println();
			System.out.println("[해시태그]");
			for (int i = 0; i < book.getHashtags().size(); i++) {
				System.out.print(book.getHashtags().get(i).getHash_id() + " ");
			}
			System.out.println();
			System.out.println("| 1.책삭제 | 2.저자 추가/삭제 | 3.해시태그 추가/삭제 | 4.재고 추가 | 5.입.출고 이력 조회 | 6.책 정보 수정 | 7.뒤로가기 | 8. 홈으로 이동 |");
			int menu = Integer.parseInt(sc.nextLine());
			if (menu == 7)
				break;

			switch (menu) {
			case 1:
				int rows = adminBookDelete(bookClient, bookNo);
				if (rows == 1) {
					System.out.println("상품 삭제 성공");
					new AdminBookPageView(bookClient);
				} else {
					System.out.println("상품 삭제 실패");
				}
				break;

			case 2:
				// 저자 추가/삭제 뷰 이동
				new AdminBookAuthorUpdateView(bookClient, bookNo);
				break;

			case 3:
				// 해시태그 추가/삭제 뷰 이동
				new AdminBookHashUpdateView(bookClient, bookNo);
				break;

			case 4:
				System.out.println("몇 권을 추가하시겠습니까?");
				System.out.println("->");
				int amount = Integer.parseInt(sc.nextLine());
				boolean done = adminWareHousingAdd(bookClient, bookNo, amount);
				if (done) {
					System.out.println("재고 추가 성공");
				} else {
					System.out.println("재고 추가 실패");
				}
				break;

			// wareHistory도 pager조회 추가해야함
			case 5:
				int pageNo = 1;
				while (true) {
					try {
						adminWareHistorySearch(bookClient, bookNo, pageNo);
						// 페이지에 해당되는 list 얻기 + list 출력
						System.out.println("------------------------------------");
						System.out.println("[입출고 이력]");
						System.out.println("------------------------------------");
						System.out.println("No. | 수량 |    날짜    | 입고/출고 | ");

						for (WarehousingDto warehousingDto : wareList) {
							System.out.print(warehousingDto.getWare_no() + " | ");
							System.out.print(warehousingDto.getWare_amount() + " | ");
							System.out.print(warehousingDto.getWare_date() + " | ");
							System.out.println(warehousingDto.getWare_status());
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
						System.out.println("| 1.페이지 이동 | 2.뒤로가기 | 3.홈으로 이동 |");
						System.out.println("-> ");
						int wareMenu = Integer.parseInt(sc.nextLine());
						if (wareMenu == 1) {
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
						} else if (wareMenu == 2) {
							break;
						} else {
							new MainPageView(bookClient);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;

			case 6:
				new AdminBookUpdateView(bookClient, book);
				break;
				
			case 8:
				new MainPageView(bookClient);
				break;

			}
		}

	}

	public void adminBookInfo(BookClient bookClient, int bookNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminBookInfo");
		jsonObject.put("data", new JSONObject().put("bookNo", bookNo));
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);

		JSONObject jo = joo.getJSONObject("data");

		JSONArray authors = jo.getJSONArray("authors");
		book = new BookDto();
		ArrayList<AuthorDto> authorList = new ArrayList<>();
		for (int i = 0; i < authors.length(); i++) {
			AuthorDto authorDto = new AuthorDto();
			authorDto.setAuthor_no(authors.getJSONObject(i).getInt("authorNo"));
			authorDto.setAuthor_name(authors.getJSONObject(i).getString("authorName"));
			authorDto.setAuthor_detail(authors.getJSONObject(i).getString("authorDetail"));
			authorList.add(authorDto);
		}
		book.setAuthors(authorList);

		JSONArray hashtags = jo.getJSONArray("hashtags");
		ArrayList<BookHashDto> hashtagList = new ArrayList<>();
		for (int i = 0; i < hashtags.length(); i++) {
			BookHashDto bookHashDto = new BookHashDto();
			bookHashDto.setBook_no(hashtags.getJSONObject(i).getInt("bookNo"));
			bookHashDto.setHash_id(hashtags.getJSONObject(i).getString("hashId"));
			hashtagList.add(bookHashDto);
		}
		book.setHashtags(hashtagList);

		book.setBook_no(jo.getInt("bookNo"));
		book.setBook_name(jo.getString("bookName"));
		book.setBook_publisher(jo.getString("bookPublisher"));
		book.setBook_detail(jo.getString("bookDetail"));
		book.setBook_price(jo.getInt("bookPrice"));
		book.setBook_page(jo.getInt("bookPage"));
		book.setBook_lang(jo.getString("bookLang"));
		book.setBook_date(jo.getString("bookDate"));
		book.setBook_store(jo.getInt("bookStore"));
		book.setSub_no(jo.getInt("subNo"));
		book.setSub_name(jo.getString("subName"));
		book.setCategory_name(jo.getString("categoryName"));

	}

	// 책 삭제
	public int adminBookDelete(BookClient bookClient, int bookNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminBookDelete");
		JSONObject data = new JSONObject();
		data.put("bookNo", bookNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		return (new JSONObject(json).getInt("data"));

	}

	// 책 재고 추가 = warehousing 행 추가 + book_store 값 + => 트랜잭션 처리
	public boolean adminWareHousingAdd(BookClient bookClient, int bookNo, int amount) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminWareHousingAdd");
		JSONObject data = new JSONObject();
		data.put("bookNo", bookNo);
		data.put("amount", amount);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		return (new JSONObject(json).getBoolean("data"));
	}

	// 상품 입출고 이력 확인(입출고 날짜 최신 순)
	public void adminWareHistorySearch(BookClient bookClient, int bookNo, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminWareHistorySearch");
		JSONObject data = new JSONObject();
		data.put("bookNo", bookNo);
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		wareList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("wareList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < list.length(); i++) {
			WarehousingDto warehousingDto = new WarehousingDto();
			JSONObject jo = list.getJSONObject(i);
			warehousingDto.setWare_no(jo.getInt("wareNo"));
			warehousingDto.setBook_no(jo.getInt("bookNo"));
			warehousingDto.setWare_amount(jo.getInt("wareAmount"));
			warehousingDto.setWare_date(jo.getString("wareDate"));
			warehousingDto.setWare_status(jo.getString("wareStatus"));
			wareList.add(warehousingDto);
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
