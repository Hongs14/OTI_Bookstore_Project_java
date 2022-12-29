package oti3.View.searchView;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;
import oti3.DTO.BoardDto;
import oti3.DTO.CartBoardDto;
import oti3.DTO.OrderdetailDto;
import oti3.DTO.ReviewDto;
import oti3.View.joinView.LoginView;
import oti3.View.mainPageManage.MainPageView;

public class BookReadView {
	Scanner scanner = new Scanner(System.in);
	ArrayList<BoardDto> pagelist;
	ArrayList<CartBoardDto> CartsBoard;

	/*
	 * - 바로구매 - 찜 추가
	 */

	public BookReadView(BookClient bookClient, int bookNo) throws InterruptedException {
		// int BookNum으로 바꾸기
		a: while (true) {
			BookRead(bookClient, bookNo);

			System.out.println("[ 책 상세 정보 ]");
			System.out.println("[ 책 제목 : " + pagelist.get(0).getBook_name() + " ]");
			System.out.println("[ 책 가격 : " + pagelist.get(0).getBook_price() + "원 ]");
			System.out.println("[ 책 출판사 : " + pagelist.get(0).getBook_publisher() + " ]");
			System.out.println("[ 책 재고 : " + pagelist.get(0).getBook_store() + "권 ]");
			System.out.println("[ 책 페이지 수 : " + pagelist.get(0).getBook_page() + "쪽 ]");
			System.out.println("[ 책 언어 : " + pagelist.get(0).getBook_lang() + " ]");
			System.out.println("[ 책 출간일 : " + pagelist.get(0).getBook_date() + " ]");
			System.out.println();
			System.out.print("[ 책 소개글 ]");
			System.out.println();
			for (int i = 0; i < pagelist.get(0).getBook_detail().length(); i++) {
				if (i % 50 == 0 && i != 0) {
					System.out.println();
				}
				System.out.print(pagelist.get(0).getBook_detail().charAt(i));
			}
			System.out.println();
			System.out.println();
			System.out.println("[ 작가 정보 ]");
			for (int i = 0; i < pagelist.get(0).getAuthor().size(); i++) {
				System.out.print("[ 작가 : ");
				System.out.print(pagelist.get(0).getAuthor().get(i).getAuthor_name());
				System.out.print(" ]");
				System.out.println();
				System.out.print("[ 작가 소개글 ]");
				System.out.println();
				for (int j = 0; j < pagelist.get(0).getAuthor().get(i).getAuthor_detail().length(); j++) {
					if (j % 50 == 0 && j != 0) {
						System.out.println();
					}
					System.out.print(pagelist.get(0).getAuthor().get(i).getAuthor_detail().charAt(j));
				}
				System.out.println();
			}
			System.out.println();
			System.out.println("[리뷰]");
			for (int i = 0; i < pagelist.get(0).getReview().size(); i++) {
				System.out.println("[ 작성자 아이디 : " + pagelist.get(0).getReview().get(i).getUser_id() + " ]");
				System.out.println(" [작성일자 : " + pagelist.get(0).getReview().get(i).getReview_date() + "]");
				System.out.println();
				System.out.print("[ 리뷰내용 ]");
				System.out.println();
				for (int j = 0; j < pagelist.get(0).getReview().get(i).getReview_content().length(); j++) {
					if (j % 50 == 0 && j != 0) {
						System.out.println();
					}
					System.out.print(pagelist.get(0).getReview().get(i).getReview_content().charAt(j));
				}
				System.out.println(" [평점 : " + pagelist.get(0).getReview().get(i).getReview_score() + "점]");
				System.out.println();
			}
			int price = pagelist.get(0).getBook_price();
			// [ 2.장바구니 추가 ][ 3.찜 추가 ][ 4. 홈으로 가기 ][ 5. 뒤로 가기 ]");
			System.out.print("| 1.바로 구매 ");
			boolean cart = true;
			if (bookClient.loginUser.getUser_id() != null && CartsBoard(bookClient, bookNo)) {
				System.out.print("| 2.장바구니 변경 ");
				cart = false;
			} else {
				System.out.print("| 2.장바구니 추가 ");
			}
			boolean dib = true;
			if (bookClient.loginUser.getUser_id() != null
					&& CheckDibs(bookClient, bookNo, bookClient.loginUser.getUser_id())) {
				System.out.print("| 3.찜 삭제 ");
				dib = false;
			} else {
				System.out.print("| 3.찜 추가 ");
			}
			System.out.print("| 4.홈으로 가기 | 5. 뒤로 가기 |");
			System.out.println();
			System.out.print("-> ");
			String choice = scanner.nextLine();
			if (choice.equals("5"))
				break;
			switch (choice) {
			case "1":
				// 바로 구매
				if (bookClient.loginUser.getUser_id() != null) {
					System.out.print("책 수량-> ");
					int od_qty = Integer.parseInt(scanner.nextLine());
					int sum = price * od_qty;
					ArrayList<OrderdetailDto> list = new ArrayList<OrderdetailDto>();
					OrderdetailDto od = new OrderdetailDto();
					od.setBook_no(bookNo);
					od.setOd_qty(od_qty);
					list.add(od);
					new OrderCreateView(bookClient, sum, list);
				} else {
					System.out.println("로그인 후 사용 가능한 서비스입니다.");
					new LoginView(bookClient);
				}
				break;

			case "2":
				if (bookClient.loginUser.getUser_id() != null) {
					int cart_qty;
					if (cart) {
						// 장바구니 추가
						System.out.print("장바구니 안에 몇 권을 추가할까요? ");
						System.out.println("-> ");
						cart_qty = Integer.parseInt(scanner.nextLine());
						if (cart_qty <= 0) {
							System.out.println("0이거나 0보다 작은 숫자를 입력하셨습니다.");
						} else {
							CartsBoardPlus(bookClient, bookClient.loginUser.getUser_id(), bookNo, cart_qty);
						}
					} else {
						// 장바구니 변경
						System.out.print("장바구니 수량을 몇으로 변경할까요? ");
						System.out.println("-> ");
						cart_qty = Integer.parseInt(scanner.nextLine());
						if (cart_qty <= 0) {
							System.out.println("0이거나 0보다 작은 숫자를 입력하셨습니다.");
						} else {
							updateCartsBoardQty(bookClient, bookClient.loginUser.getUser_id(), bookNo, cart_qty);
						}
					}

					System.out.println();
					Thread.sleep(2000);
				} else {
					System.out.println("로그인 후 사용 가능한 서비스입니다.");
					new LoginView(bookClient);
				}
				break;
			case "3":
				if (bookClient.loginUser.getUser_id() != null) {
					if (dib) {
						// 찜 추가
						Dibs(bookClient, bookNo, bookClient.loginUser.getUser_id());
					} else {
						// 찜 삭제
						deleteDibs(bookClient, bookNo, bookClient.loginUser.getUser_id());
					}
					System.out.println();
					Thread.sleep(2000);
				} else {
					System.out.println("로그인 후 사용 가능한 서비스입니다.");
					new LoginView(bookClient);
				}
				break;
			case "4":
				// 홈으로 가기
				new MainPageView(bookClient);
				break;
			}
		}
	}

	// 책 상세 조회
	public void BookRead(BookClient bookClient, int book_no) {

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "board");
		sendJson.put("data", new JSONObject().put("book_no", book_no));

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);

		JSONArray jarr = jo.getJSONArray("data");
		pagelist = new ArrayList<>();
		for (int i = 0; i < jarr.length(); i++) {
			JSONObject jo2 = jarr.getJSONObject(i);
			BoardDto br = new BoardDto();
			br.setBook_name(jo2.getString("book_name"));
			br.setBook_detail(jo2.getString("book_detail"));
			br.setBook_publisher(jo2.getString("book_publisher"));
			br.setBook_price(jo2.getInt("book_price"));
			br.setBook_store(jo2.getInt("book_store"));
			br.setBook_page(jo2.getInt("book_page"));
			br.setBook_lang(jo2.getString("book_lang"));
			br.setBook_date(jo2.getString("book_date"));
			JSONArray ja = jo2.getJSONArray("author");
			ArrayList<AuthorDto> alist = new ArrayList<AuthorDto>();

			for (int j = 0; j < ja.length(); j++) {
				AuthorDto ad = new AuthorDto();
				ad.setAuthor_name(ja.getJSONObject(j).getString("author_name"));
				ad.setAuthor_detail(ja.getJSONObject(j).getString("author_detail"));
				alist.add(ad);
			}
			br.setAuthor(alist);

			JSONArray ja2 = jo2.getJSONArray("review");
			ArrayList<ReviewDto> rlist = new ArrayList<ReviewDto>();
			for (int j = 0; j < ja2.length(); j++) {
				ReviewDto rd = new ReviewDto();
				rd.setUser_id(ja2.getJSONObject(j).getString("user_id"));
				rd.setReview_date(ja2.getJSONObject(j).getString("review_date"));
				rd.setReview_content(ja2.getJSONObject(j).getString("review_content"));
				rd.setReview_score(ja2.getJSONObject(j).getInt("review_score"));
				rlist.add(rd);
			}
			br.setReview(rlist);
			pagelist.add(br);
		}
	}

	// 장바구니 목록 불러온 후 확인
	public boolean CartsBoard(BookClient bookClient, int book_no) {
		// json생성
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "cartsBoard");
		sendJson.put("data", new JSONObject().put("user_id", bookClient.loginUser.getUser_id()));
		// json보내기
		bookClient.send(sendJson.toString());
		String json = bookClient.receive();
		// json파싱
		JSONObject jo = new JSONObject(json);
		CartsBoard = new ArrayList<>();
		JSONArray joa = jo.getJSONArray("data");
		for (int i = 0; i < joa.length(); i++) {
			JSONObject joo = joa.getJSONObject(i);
			CartBoardDto carts = new CartBoardDto();
			carts.setBook_no(joo.getInt("book_no"));
			CartsBoard.add(carts);
		}
		boolean find = false;
		for (CartBoardDto cbd : CartsBoard) {
			if (cbd.getBook_no() == book_no) {
				find = true;
				break;
			}
		}
		return find;
	}

	// 장바구니 추가
	public void CartsBoardPlus(BookClient bookClient, String user_id, int book_no, int cart_qty) {
		JSONObject sendJson = new JSONObject();
		JSONObject sendDetailJson = new JSONObject();
		sendJson.put("command", "cartsBoardPlus");
		sendDetailJson.put("user_id", user_id);
		sendDetailJson.put("book_no", book_no);
		sendDetailJson.put("cart_qty", cart_qty);
		sendJson.put("data", sendDetailJson);

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);
		int rows = jo.getInt("data");
		if (rows == 1) {
			System.out.println("장바구니에 추가 되었습니다.");
		} else {
			System.out.println("실패!");
		}
	}

	// 장바구니 변경
	public void updateCartsBoardQty(BookClient bookClient, String user_id, int book_no, int cart_qty) {
		JSONObject sendJson = new JSONObject();
		JSONObject cartsjo = new JSONObject();
		sendJson.put("command", "cartsBoardQty");
		cartsjo.put("user_id", user_id);
		cartsjo.put("book_no", book_no);
		cartsjo.put("cart_qty", cart_qty);
		sendJson.put("data", cartsjo);

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);
		int rows = jo.getInt("data");
		if (rows == 1) {
			System.out.println("장바구니가 변경 되었습니다.");
		} else {
			System.out.println("실패!");
		}
	}
//	[송영훈 형] [오후 8:45] {"command" : "selectCheckDibs", "data" : {"book_no" : "값", "user_id" : "값"}}
//	[송영훈 형] [오후 8:45] 위에꺼 보내는 형식
//	[송영훈 형] [오후 8:45] {"data" : boolean값}
//	[송영훈 형] [오후 8:45] 받는 형식 

	// 찜 목록 불러온 후 확인
	public boolean CheckDibs(BookClient bookClient, int book_no, String user_id) {
		// json 생성
		JSONObject sendJson = new JSONObject();
		JSONObject sendDetailJson = new JSONObject();
		sendJson.put("command", "selectCheckDibs");
		sendDetailJson.put("book_no", book_no);
		sendDetailJson.put("user_id", user_id);
		sendJson.put("data", sendDetailJson);

		// json 보내기
		bookClient.send(sendJson.toString());
		String json = bookClient.receive();

		// json 파싱
		JSONObject jo = new JSONObject(json);
		boolean rows = jo.getBoolean("data");

		return rows;
	}

	// 찜 추가
	public void Dibs(BookClient bookClient, int book_no, String user_id) {
		JSONObject sendJson = new JSONObject();
		JSONObject sendDetailJson = new JSONObject();
		sendJson.put("command", "dibs");
		sendDetailJson.put("book_no", book_no);
		sendDetailJson.put("user_id", user_id);
		sendJson.put("data", sendDetailJson);

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);
		int rows = jo.getInt("data");

		if (rows == 1) {
			System.out.println("찜이 추가 되었습니다.");
		} else {
			System.out.println("실패!");
		}
	}

	// 찜 삭제
	public void deleteDibs(BookClient bookClient, int book_no, String user_id) {
		JSONObject sendJson = new JSONObject();
		JSONObject sendDetailJson = new JSONObject();
		sendJson.put("command", "deleteDibs");
		sendDetailJson.put("book_no", book_no);
		sendDetailJson.put("user_id", user_id);
		sendJson.put("data", sendDetailJson);

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);
		int rows = jo.getInt("data");

		if (rows == 1) {
			System.out.println("찜이 삭제 되었습니다.");
		} else {
			System.out.println("실패!");
		}
	}

}
