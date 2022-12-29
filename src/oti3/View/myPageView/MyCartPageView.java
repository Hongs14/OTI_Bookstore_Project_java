package oti3.View.myPageView;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.CartBoardDto;
import oti3.DTO.OrderdetailDto;
import oti3.DTO.PagerDto;
import oti3.View.mainPageManage.MainPageView;
import oti3.View.searchView.OrderCreateView;

public class MyCartPageView {
	Scanner scanner = new Scanner(System.in);
	ArrayList<CartBoardDto> CartsBoard = new ArrayList<>();
	PagerDto pagerDto = new PagerDto();

	public MyCartPageView(BookClient bookClient) {
		while (true) {
			CartsBoard(bookClient);
			System.out.println(bookClient.loginUser.getUser_id() + "님의 장바구니");
			System.out.println("--------------------------------------------");
			int sum = 0;
			for (int i = 0; i < CartsBoard.size(); i++) {
				System.out.print("[ 책 번호 : " + CartsBoard.get(i).getBook_no() + " ]");
				System.out.print("[ 책 이름 : " + CartsBoard.get(i).getBook_name() + " ]");
				System.out.print("[ 책 수량 : " + CartsBoard.get(i).getCart_qty() + " ]");
				System.out.print("[ 가격 : " + CartsBoard.get(i).getB_c() + " ]");
				sum += CartsBoard.get(i).getB_c();
				System.out.println();
			}
			System.out.println("[ 총합 가격 : " + sum + " ]");
			System.out.println();

			System.out.println("1.장바구니 상품 수량 변경 | 2.장바구니 전체 삭제 | 3. 장바구니 일부 삭제 | 4. 주문 페이지 이동 | 5. 홈으로 가기 | 6. 뒤로 가기 |");
			String num = scanner.nextLine();
			if (num.equals("6"))
				break;
			switch (num) {
			case "1":
				System.out.println("어떤 책 번호의 책의 수량을 변경하겠습니까? ");
				System.out.print("-> ");
				int book_no = Integer.parseInt(scanner.nextLine());
				System.out.println("얼마 만큼 변경하시겠습니까? ");
				System.out.print("-> ");
				int cart_qty = Integer.parseInt(scanner.nextLine());
				int answer = updateCartsBoardQty(bookClient.loginUser.getUser_id(), book_no, cart_qty, bookClient);
				if (answer == 1) {
					System.out.println("정상적으로 변경 되었습니다.");
				} else {
					System.out.println("변경되지 않았습니다.");
				}
				break;
			case "2":
				System.out.println("정말로 전체 삭제 하시겠습니까? ");
				System.out.println("1. 예 | 2. 아니오");
				System.out.print("-> ");
				int allDelete = Integer.parseInt(scanner.nextLine());
				if (allDelete == 1) {
					int answer2 = CartsBoardAllDelete(bookClient.loginUser.getUser_id(), bookClient);
					if (answer2 == 0) {
						System.out.println("실패 하였습니다. ");
					} else {
						System.out.println("정상적으로 삭제 되었습니다.");
					}
				} else if (allDelete == 2) {

				} else {
					System.out.println("잘못된 값을 입력하셨습니다.");
				}
				break;

			case "3":
				System.out.println("삭제할 책 번호를 골라 주세요 ");
				System.out.print("-> ");
				int book_no2 = Integer.parseInt(scanner.nextLine());
				int answer3 = CartsBoardDelete(bookClient.loginUser.getUser_id(), book_no2, bookClient);
				if (answer3 == 1) {
					System.out.println("정상적으로 삭제 되었습니다.");
				} else {
					System.out.println("실패");
				}
				break;

			case "4":
				ArrayList<OrderdetailDto> list = new ArrayList<>();
				for (CartBoardDto cartBoardDto : CartsBoard) {
					OrderdetailDto od = new OrderdetailDto();
					od.setBook_no(cartBoardDto.getBook_no());
					od.setOd_qty(cartBoardDto.getCart_qty());
					list.add(od);
				}
				new OrderCreateView(bookClient, sum, list);
				break;

			case "5":
				new MainPageView(bookClient);
				break;

			}
		}
	}

	public void CartsBoard(BookClient bookClient) {
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
			carts.setBook_name(joo.getString("book_name"));
			carts.setBook_publisher(joo.getString("book_publisher"));
			carts.setBook_price(joo.getInt("book_price"));
			carts.setBook_store(joo.getInt("book_store"));
			carts.setCart_qty(joo.getInt("cart_qty"));
			carts.setB_c(joo.getInt("b_c"));
			CartsBoard.add(carts);
		}

	}

	public int updateCartsBoardQty(String user_id, int book_no, int cart_qty, BookClient bookClient) {
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

		return rows;
	}

	public int CartsBoardAllDelete(String user_id, BookClient bookClient) {
		JSONObject sendJson = new JSONObject();
		JSONObject carts = new JSONObject();
		sendJson.put("command", "cartsBoardAllDelete");
		carts.put("user_id", user_id);
		sendJson.put("data", carts);

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);
		int rows = jo.getInt("data");

		return rows;
	}

	public int CartsBoardDelete(String user_id, int book_no, BookClient bookClient) {
		JSONObject sendJson = new JSONObject();
		JSONObject cartsjo = new JSONObject();
		sendJson.put("command", "cartsBoardDelete");
		cartsjo.put("user_id", user_id);
		cartsjo.put("book_no", book_no);
		sendJson.put("data", cartsjo);

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);
		int rows = jo.getInt("data");

		return rows;
	}
}