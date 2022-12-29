package oti3.View.mainPageManage;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookDto;
import oti3.DTO.UserDto;
import oti3.View.adminAuthorManage.AdminAuthorPageView;
import oti3.View.adminBookManage.AdminBookPageView;
import oti3.View.adminQnaManage.AdminQnaPageView;
import oti3.View.adminUserManage.AdminUserPageView;
import oti3.View.joinView.LoginView;
import oti3.View.myPageView.MyPageView;
import oti3.View.qnaView.QnaPageView;
import oti3.View.searchView.BookReadView;
import oti3.View.searchView.CategoryPageView;
import oti3.View.searchView.IntegrationPageView;

public class MainPageView {
	private ArrayList<BookDto> thisWeekBestseller;
	private ArrayList<BookDto> genderAgeBestseller;
	private ArrayList<BookDto> newBookBestseller;
	private static Scanner sc = new Scanner(System.in);

	public MainPageView(BookClient bookClient) {
		while (true) {
			try {
				mainPageBestSellerList(bookClient);
				if (bookClient.loginUser.getUser_id() != null) {
					mainPageGenderAgeList(bookClient, bookClient.loginUser.getUser_gender() + "",
							bookClient.loginUser.getUser_birth());
				}

				mainPageRecentBookList(bookClient);
				// 베스트셀러 세가지 목록 출력
				pageList(bookClient);

				// 로그인 or 로그아웃 상태에 따른 메뉴 출력
				if (bookClient.loginUser.getUser_id() != null) {
					System.out.print("| 1.로그아웃 ");
				} else {
					System.out.print("| 1.로그인/회원가입 ");
				}

				System.out.print("| 2.QNA게시판 | 3.마이페이지 | 4.통합검색 | 5.카테고리 검색 | 6.책 상세조회 | 7.관리자 페이지 |");
				System.out.println();
				System.out.print("-> ");
				int menu = Integer.parseInt(sc.nextLine());
				switch (menu) {
				case 1:
					if (bookClient.loginUser.getUser_id() != null) {
						bookClient.loginUser = new UserDto();
						System.out.println("로그아웃 되었습니다.");
					} else {
						new LoginView(bookClient);
					}
					break;

				case 2:
					new QnaPageView(bookClient);
					break;

				case 3:
					new MyPageView(bookClient);
					break;

				case 4:
					new IntegrationPageView(bookClient);
					break;

				case 5:
					new CategoryPageView(bookClient);
					break;

				case 6:
					System.out.println("상세 조회할 책 번호 입력-> ");
					int bookNo = Integer.parseInt(sc.nextLine());
					boolean find = false;
					for (int i = 0; i < thisWeekBestseller.size(); i++) {
						if (thisWeekBestseller.get(i).getBook_no() == bookNo) {
							find = true;
							break;
						}
					}
					if (genderAgeBestseller != null) {
						for (int i = 0; i < genderAgeBestseller.size(); i++) {
							if (genderAgeBestseller.get(i).getBook_no() == bookNo) {
								find = true;
								break;
							}
						}
					}

					for (int i = 0; i < newBookBestseller.size(); i++) {
						if (newBookBestseller.get(i).getBook_no() == bookNo) {
							find = true;
							break;
						}
					}
					if (find) {
						new BookReadView(bookClient, bookNo);
					} else {
						System.out.println("현재 페이지에 존재하지 않는 책입니다.");
					}
					break;

				case 7:
					if (bookClient.loginUser.getUser_id() != null
							&& bookClient.loginUser.getUser_id().equals(BookClient.adminId)) {
						System.out.println("| 1.상품 관리 | 2.회원 관리 | 3.문의 관리 | 4.저자 관리");
						int adminChoice = Integer.parseInt(sc.nextLine());
						switch (adminChoice) {
						case 1:
							new AdminBookPageView(bookClient);
							break;

						case 2:
							new AdminUserPageView(bookClient);
							break;

						case 3:
							new AdminQnaPageView(bookClient);
							break;

						case 4:
							new AdminAuthorPageView(bookClient);
							break;
						}
					} else {
						System.out.println("관리자가 아니거나 로그인 되어 있지 않습니다.");
					}
					break;

				}
			} catch (Exception e) {
				System.out.println("잘못된 입력입니다.");
			}

		}

	}

	// 금주 bestseller top-5
	public void mainPageBestSellerList(BookClient bookClient) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mainPageBestSellerList");
		JSONObject data = new JSONObject();
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONArray jsonArray = joo.getJSONArray("data");
		thisWeekBestseller = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			BookDto book = new BookDto();
			JSONObject jo = jsonArray.getJSONObject(i);
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
			thisWeekBestseller.add(book);
		}
	}

	// 로그인 유저 성별 나이대 bestseller top-5
	public void mainPageGenderAgeList(BookClient bookClient, String userGender, String userBirth) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mainPageGenderAgeList");
		JSONObject data = new JSONObject();
		data.put("userGender", userGender);
		data.put("userBirth", userBirth);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONArray jsonArray = joo.getJSONArray("data");
		genderAgeBestseller = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			BookDto book = new BookDto();
			JSONObject jo = jsonArray.getJSONObject(i);
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
			genderAgeBestseller.add(book);
		}
	}

	// 신간도서 bestseller top-5
	public void mainPageRecentBookList(BookClient bookClient) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mainPageRecentBookList");
		JSONObject data = new JSONObject();
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONArray jsonArray = joo.getJSONArray("data");
		newBookBestseller = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			BookDto book = new BookDto();
			JSONObject jo = jsonArray.getJSONObject(i);
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
			newBookBestseller.add(book);
		}
	}

	public void pageList(BookClient bookClient) {
		// 금주 bestseller top-5 출력
		System.out.println("------------------------------------");
		System.out.println("[Bestseller 목록]");
		System.out.println("------------------------------------");
		System.out.println("No.   |        제목        |   저자  |   츌판사   |  가격 |  페이지 수 | 출간일 | 서브 카테고리 | 카테고리");

		System.out.println("[ 금주 bestseller top-5 ]");
		for (BookDto bookDto : thisWeekBestseller) {
			System.out.print(bookDto.getBook_no() + " | ");
			System.out.print(bookDto.getBook_name() + " | ");
			System.out.print(
					bookDto.getAuthors().get(0).getAuthor_name() + " 외 " + (bookDto.getAuthors().size() - 1) + "명 | ");
			System.out.print(bookDto.getBook_publisher() + " | ");
			System.out.print(bookDto.getBook_price() + "원 | ");
			System.out.print(bookDto.getBook_page() + "쪽 | ");
			System.out.print(bookDto.getBook_date() + " | ");
			System.out.print(bookDto.getSub_name() + " | ");
			System.out.println(bookDto.getCategory_name());
		}
		// 로그인 유저 성별 나이대 bestseller top-5 출력
		if (bookClient.loginUser.getUser_id() != null) {
			System.out.println();
			System.out.println("[ 회원님 성별/연령대 bestseller top-5 ]");
			for (BookDto bookDto : genderAgeBestseller) {
				System.out.print(bookDto.getBook_no() + " | ");
				System.out.print(bookDto.getBook_name() + " | ");
				System.out.print(bookDto.getAuthors().get(0).getAuthor_name() + " 외 "
						+ (bookDto.getAuthors().size() - 1) + "명 | ");
				System.out.print(bookDto.getBook_publisher() + " | ");
				System.out.print(bookDto.getBook_price() + "원 | ");
				System.out.print(bookDto.getBook_page() + "쪽 | ");
				System.out.print(bookDto.getBook_date() + " | ");
				System.out.print(bookDto.getSub_name() + " | ");
				System.out.println(bookDto.getCategory_name());
			}
		}

		System.out.println();
		System.out.println("[ 신간도서 bestseller top-5 ]");
		// 신간도서 bestseller top-5 출력
		for (BookDto bookDto : newBookBestseller) {
			System.out.print(bookDto.getBook_no() + " | ");
			System.out.print(bookDto.getBook_name() + " | ");
			System.out.print(
					bookDto.getAuthors().get(0).getAuthor_name() + " 외 " + (bookDto.getAuthors().size() - 1) + "명 | ");
			System.out.print(bookDto.getBook_publisher() + " | ");
			System.out.print(bookDto.getBook_price() + "원 | ");
			System.out.print(bookDto.getBook_page() + "쪽 | ");
			System.out.print(bookDto.getBook_date() + " | ");
			System.out.print(bookDto.getSub_name() + " | ");
			System.out.println(bookDto.getCategory_name());
		}
	}

}
