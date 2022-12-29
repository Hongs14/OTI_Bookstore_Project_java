package oti3.Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.CartBoardDto;
import oti3.DTO.CartDto;
import oti3.Service.CartsService;

public class CartsController {
	private CartsService cartsService = new CartsService();
	
	// 장바구니 목록
	public String cartsBoard(JSONObject data) {
		
		CartDto cartDto = new CartDto();
		cartDto.setUser_id(data.getString("user_id"));
		
		ArrayList<CartBoardDto> list = cartsService.cartsBoard(ConnectionProvider.getConnection(), cartDto);
		
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "cartsBoard");
		JSONArray dataArr = new JSONArray();
		
		for(CartBoardDto cbDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("book_no", cbDto.getBook_no());
			jo.put("book_name", cbDto.getBook_name());
			jo.put("book_publisher", cbDto.getBook_publisher());
			jo.put("book_price", cbDto.getBook_price());
			jo.put("book_store", cbDto.getBook_store());
			jo.put("cart_qty", cbDto.getCart_qty());
			jo.put("b_c", cbDto.getB_c());
			dataArr.put(jo);
		}
		sendJson.put("data", dataArr);
		
		return sendJson.toString();
	}
	
	// 장바구니 추가
	public String cartsBoardPlus(JSONObject data) {
		
		CartDto cartDto = new CartDto();
		cartDto.setUser_id(data.getString("user_id"));
		cartDto.setBook_no(data.getInt("book_no"));
		cartDto.setCart_qty(data.getInt("cart_qty"));
		
		int result = cartsService.cartsBoardPlus(ConnectionProvider.getConnection(), cartDto);
		
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "cartsBoardPlus");
		sendJson.put("data", result);
		
		return sendJson.toString();
	}
	
	// 장바구니 전체 삭제
	public String cartsBoardAllDelete(JSONObject data) {
		
		CartDto cartDto = new CartDto();
		cartDto.setUser_id(data.getString("user_id"));
		
		int result = cartsService.cartsBoardAllDelete(ConnectionProvider.getConnection(), cartDto);
		
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "cartsBoardAllDelete");
		sendJson.put("data", result);
		
		return sendJson.toString();
	}
	
	// 장바구니 부분 삭제
	public String cartsBoardDelete(JSONObject data) {
		
		CartDto cartDto = new CartDto();
		cartDto.setUser_id(data.getString("user_id"));
		cartDto.setBook_no(data.getInt("book_no"));
		
		int result = cartsService.cartsBoardDelete(ConnectionProvider.getConnection(), cartDto);
		
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "cartsBoardDelete");
		sendJson.put("data", result);
		
		return sendJson.toString();
	}
	
	public String cartsBoardQty(JSONObject data) {
		
		CartDto cartDto = new CartDto();
		cartDto.setCart_qty(data.getInt("cart_qty"));
		cartDto.setUser_id(data.getString("user_id"));
		cartDto.setBook_no(data.getInt("book_no"));
		
		int result = cartsService.cartsBoardQty(ConnectionProvider.getConnection(), cartDto);
		
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "cartsBoardQty");
		sendJson.put("data", result);
		
		return sendJson.toString();
	}
}
