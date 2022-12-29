package oti3.View.searchView;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.BookDto;
import oti3.DTO.OrderDto;
import oti3.DTO.OrderdetailDto;
import oti3.DTO.UserDto;
import oti3.View.mainPageManage.MainPageView;

public class OrderCreateView {
	Scanner scanner = new Scanner(System.in);
	ArrayList<BookDto> bookstorelist;
	ArrayList<UserDto> usermoneylist;

	public OrderCreateView(BookClient bookClient, int sum, ArrayList<OrderdetailDto> list) {
		System.out.println("받는 사람의 이름은 무엇입니까? ");
		System.out.print("-> ");
		String order_receivename = scanner.nextLine();
		String order_tel;
		while (true) {
			System.out.println("전화번호를 입력해 주세요. ");
			System.out.print("-> ");
			String regex = "(010|02|0[3-7][1-5])-\\d{3,4}-\\d{4}"; // 집 전화번호 or 핸드폰 번호
			order_tel = scanner.nextLine();
			boolean result1 = Pattern.matches(regex, order_tel);
			if (result1) {
				break;
			} else {
				System.out.println("**전화번호 형식을 다시 확인해주세요.**");
			}
		}

		System.out.println("받는 주소를 입력해 주세요. ");
		System.out.print("-> ");
		String order_address = scanner.nextLine();
		System.out.print("배송 시 주의사항 적어주세요 . ");
		System.out.print("-> ");
		String order_memo = scanner.nextLine();

		OrderDto od = new OrderDto();
		od.setUser_id(bookClient.loginUser.getUser_id());
		od.setOrder_receivename(order_receivename);
		od.setOrder_tel(order_tel);
		od.setOrder_address(order_address);
		od.setOrder_memo(order_memo);
		boolean storeFlag = true;
		boolean moneyFlag = true;
		for (OrderdetailDto odd : list) {
			int book_no = odd.getBook_no();
			int amount = BookStore(book_no, bookClient);
			if (amount < odd.getOd_qty()) {
				storeFlag = false;
			}
		}
		int user_money = UserMoney(bookClient.loginUser.getUser_id(), bookClient);
		if (sum > user_money) {
			moneyFlag = false;
		}

		if (storeFlag && moneyFlag) {
			boolean Buysuccess = Buy(od, list, bookClient);
			if (Buysuccess) {
				System.out.println("구매에 성공하였습니다.");
				bookClient.loginUser.setUser_money(bookClient.loginUser.getUser_money() - sum);
			} else {
				System.out.println("구매 실패");
			}
		} else if (!storeFlag) {
			System.out.println("재고가 부족합니다.");
		} else if (!moneyFlag) {
			System.out.println("돈이 부족합니다.");
		}
		// 메인으로 넘어가기
		new MainPageView(bookClient);
	}

	public int BookStore(int book_no, BookClient bookClient) {
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "bookStore");
		sendJson.put("data", new JSONObject().put("book_no", book_no));

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);
		JSONArray jarr = jo.getJSONArray("data");

		bookstorelist = new ArrayList<>();

		for (int i = 0; i < jarr.length(); i++) {
			JSONObject jo2 = jarr.getJSONObject(i);
			BookDto bDto = new BookDto();
			bDto.setBook_store(jo2.getInt("book_store"));
			bookstorelist.add(bDto);
		}
		int answer = 0;
		for (int i = 0; i < bookstorelist.size(); i++) {
			answer = bookstorelist.get(i).getBook_store();
		}

		return answer;

	}

	public int UserMoney(String user_id, BookClient bookClient) {
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "userMoney");
		sendJson.put("data", new JSONObject().put("user_id", user_id));

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();

		JSONObject jo = new JSONObject(receiveJson);
		JSONArray jarr = jo.getJSONArray("data");

		usermoneylist = new ArrayList<>();

		for (int i = 0; i < jarr.length(); i++) {
			JSONObject jo2 = (JSONObject) jarr.get(i);
			UserDto ud = new UserDto();
			ud.setUser_money(jo2.getInt("user_money"));
			usermoneylist.add(ud);
		}
		int answer = 0;
		for (int i = 0; i < usermoneylist.size(); i++) {
			answer = usermoneylist.get(i).getUser_money();
		}
		return answer;
	}

	// {"command" : "buy", "data" : {"order_no" : "no" ... } {
	public boolean Buy(OrderDto orderDto, ArrayList<OrderdetailDto> list, BookClient bookClient) {
		// {"command" : "buy", "data" : {}}
		JSONObject sendJson = new JSONObject();
		JSONObject userorderJson = new JSONObject();
		JSONObject userorderJson2 = new JSONObject();

		// {"data" : {"userOrder" : {} , "userOrderDetail" : [{}, {}, {}]} }
		sendJson.put("command", "buy");

		userorderJson.put("user_id", orderDto.getUser_id());
		userorderJson.put("order_receivename", orderDto.getOrder_receivename());
		userorderJson.put("order_tel", orderDto.getOrder_tel());
		userorderJson.put("order_address", orderDto.getOrder_address());
		userorderJson.put("order_memo", orderDto.getOrder_memo());
		userorderJson2.put("userOrder", userorderJson);
		JSONArray jonarray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject userorderdetailJson = new JSONObject();
			userorderdetailJson.put("book_no", list.get(i).getBook_no());
			userorderdetailJson.put("od_qty", list.get(i).getOd_qty());
			jonarray.put(userorderdetailJson);
		}
		userorderJson2.put("userOrderDetail", jonarray);
		sendJson.put("data", userorderJson2);

		bookClient.send(sendJson.toString());
		String receiveJson = bookClient.receive();
		JSONObject jo = new JSONObject(receiveJson);
		boolean rows = jo.getBoolean("data");
		return rows;
	}
}
