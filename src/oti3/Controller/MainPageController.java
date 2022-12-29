package oti3.Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;

import oti3.DTO.UserDto;
import oti3.Service.MainPageService;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookDto;

public class MainPageController {
	private MainPageService mainPageService = new MainPageService();

	public String mainPageBestSellerList(JSONObject data) {
		// 1) JSON TO DTO
		// 금주의 베스트셀러는 기능 수행을 위해 클라이언트가 주는 data가 없음

		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<BookDto> list = mainPageService.mainPageBestSellerList(ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "mainPageBestSellerList");
		JSONArray dataArr = new JSONArray();

		for (BookDto book : list) {
			JSONObject jo = new JSONObject();
			JSONArray ja = new JSONArray();
			for (AuthorDto authorDto : book.getAuthors()) {
				JSONObject jo2 = new JSONObject();
				jo2.put("authorNo", authorDto.getAuthor_no());
				jo2.put("authorName", authorDto.getAuthor_name());
				jo2.put("authorDetail", authorDto.getAuthor_detail());
				ja.put(jo2);
			}
			jo.put("authors", ja);
			jo.put("bookNo", book.getBook_no());
			jo.put("bookName", book.getBook_name());
			jo.put("bookPublisher", book.getBook_publisher());
			jo.put("bookPrice", book.getBook_price());
			jo.put("bookPage", book.getBook_page());
			jo.put("bookDate", book.getBook_date());
			jo.put("subName", book.getSub_name());
			jo.put("categoryName", book.getCategory_name());
			dataArr.put(jo);
		}
		sendJson.put("data", dataArr);
		// 4) RETURN JSON
		return sendJson.toString();
	}

	public String mainPageGenderAgeList(JSONObject data) {
		// 1) JSON TO DTO
		UserDto userDto = new UserDto();
		userDto.setUser_gender(data.getString("userGender").charAt(0));
		userDto.setUser_birth(data.getString("userBirth"));

		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<BookDto> list = mainPageService.mainPageGenderAgeList(ConnectionProvider.getConnection(), userDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "mainPageGenderAgeList");
		JSONArray dataArr = new JSONArray();

		for (BookDto book : list) {
			JSONObject jo = new JSONObject();
			JSONArray ja = new JSONArray();
			for (AuthorDto authorDto : book.getAuthors()) {
				JSONObject jo2 = new JSONObject();
				jo2.put("authorNo", authorDto.getAuthor_no());
				jo2.put("authorName", authorDto.getAuthor_name());
				jo2.put("authorDetail", authorDto.getAuthor_detail());
				ja.put(jo2);
			}
			jo.put("authors", ja);
			jo.put("bookNo", book.getBook_no());
			jo.put("bookName", book.getBook_name());
			jo.put("bookPublisher", book.getBook_publisher());
			jo.put("bookPrice", book.getBook_price());
			jo.put("bookPage", book.getBook_page());
			jo.put("bookDate", book.getBook_date());
			jo.put("subName", book.getSub_name());
			jo.put("categoryName", book.getCategory_name());
			dataArr.put(jo);
		}
		sendJson.put("data", dataArr);
		// 4) RETURN JSON
		return sendJson.toString();
	}

	public String mainPageRecentBookList(JSONObject data) {
		// 1) JSON TO DTO
		// 신간도서 베스트셀러는 기능 수행을 위해 클라이언트가 주는 data가 없음

		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<BookDto> list = mainPageService.mainPageRecentBookList(ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "mainPageRecentBookList");
		JSONArray dataArr = new JSONArray();

		for (BookDto book : list) {
			JSONObject jo = new JSONObject();
			JSONArray ja = new JSONArray();
			for (AuthorDto authorDto : book.getAuthors()) {
				JSONObject jo2 = new JSONObject();
				jo2.put("authorNo", authorDto.getAuthor_no());
				jo2.put("authorName", authorDto.getAuthor_name());
				jo2.put("authorDetail", authorDto.getAuthor_detail());
				ja.put(jo2);
			}
			jo.put("authors", ja);
			jo.put("bookNo", book.getBook_no());
			jo.put("bookName", book.getBook_name());
			jo.put("bookPublisher", book.getBook_publisher());
			jo.put("bookPrice", book.getBook_price());
			jo.put("bookPage", book.getBook_page());
			jo.put("bookDate", book.getBook_date());
			jo.put("subName", book.getSub_name());
			jo.put("categoryName", book.getCategory_name());
			dataArr.put(jo);
		}
		sendJson.put("data", dataArr);
		// 4) RETURN JSON
		return sendJson.toString();
	}
}
