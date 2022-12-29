package oti3.Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.BookDto;
import oti3.DTO.OrderDto;
import oti3.DTO.OrderdetailDto;
import oti3.DTO.UserDto;
import oti3.Service.BuyService;

public class BuyController {
	private BuyService buyService = new BuyService();

	// 바로구매 + 장바구니 구매 처리
	public String buy(JSONObject data) {

		OrderDto orderDto = new OrderDto();
		ArrayList<OrderdetailDto> list = new ArrayList<OrderdetailDto>();

		JSONObject orderJSONObject = data.getJSONObject("userOrder");

		orderDto.setUser_id(orderJSONObject.getString("user_id"));
		orderDto.setOrder_receivename(orderJSONObject.getString("order_receivename"));
		orderDto.setOrder_tel(orderJSONObject.getString("order_tel"));
		orderDto.setOrder_address(orderJSONObject.getString("order_address"));
		orderDto.setOrder_memo(orderJSONObject.getString("order_memo"));

		JSONArray jsonarray = data.getJSONArray("userOrderDetail");
		for (int i = 0; i < jsonarray.length(); i++) {
			JSONObject jo1 = jsonarray.getJSONObject(i);
			OrderdetailDto odd = new OrderdetailDto();
			odd.setBook_no(jo1.getInt("book_no"));
			odd.setOd_qty(jo1.getInt("od_qty"));
			list.add(odd);
		}

		boolean result = buyService.buy(ConnectionProvider.getConnection(), orderDto, list);

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "buy");
		sendJson.put("data", result);

		return sendJson.toString();
	}

	// 구매하기 위한 책 재고 조회
	public String bookStore(JSONObject data) {
		BookDto bookdto = new BookDto();

		bookdto.setBook_no(data.getInt("book_no"));

		ArrayList<BookDto> list = buyService.bookStore(ConnectionProvider.getConnection(), bookdto);

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "bookStore");
		JSONArray dataArr = new JSONArray();

		for (BookDto bDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("book_no", bDto.getBook_no());
			jo.put("book_store", bDto.getBook_store());
			dataArr.put(jo);
		}
		sendJson.put("data", dataArr);

		return sendJson.toString();
	}

	// 구매하기 위한 user_money 조회
	public String userMoney(JSONObject data) {
		UserDto userDto = new UserDto();

		userDto.setUser_id(data.getString("user_id"));

		ArrayList<UserDto> list = buyService.userMoney(ConnectionProvider.getConnection(), userDto);

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "userMoney");
		JSONArray dataArr = new JSONArray();

		for (UserDto uDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("user_id", uDto.getUser_id());
			jo.put("user_money", uDto.getUser_money());
			dataArr.put(jo);
		}
		sendJson.put("data", dataArr);

		return sendJson.toString();
	}
}
