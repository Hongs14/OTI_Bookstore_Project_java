package oti3.Controller;

import java.sql.Connection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;

import oti3.DTO.AuthorBookDto;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookDto;
import oti3.DTO.BookHashDto;
import oti3.DTO.PagerDto;
import oti3.DTO.WarehousingDto;
import oti3.Service.AdminBookService;

public class AdminBookController {
	private AdminBookService adminBookService = new AdminBookService();

	public String adminBookListByBookName(JSONObject data) {
		// 1) JSON TO DTO
		BookDto bookDto = new BookDto();
		bookDto.setBook_name(data.getString("bookName"));
		int count = adminBookService.adminBookListByBookNameCount(ConnectionProvider.getConnection(), bookDto);
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<BookDto> list = adminBookService.adminBookListByBookName(ConnectionProvider.getConnection(), pagerDto,
				bookDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminBookListByBookName");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
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
		sendData.put("bookList", dataArr);

		JSONObject pager = new JSONObject();
		pager.put("totalRows", pagerDto.getTotalRows());
		pager.put("totalPageNo", pagerDto.getTotalPageNo());
		pager.put("totalGroupNo", pagerDto.getTotalGroupNo());
		pager.put("startPageNo", pagerDto.getStartPageNo());
		pager.put("endPageNo", pagerDto.getEndPageNo());
		pager.put("pageNo", pagerDto.getPageNo());
		pager.put("pagesPerGroup", pagerDto.getPagesPerGroup());
		pager.put("groupNo", pagerDto.getGroupNo());
		pager.put("rowsPerPage", pagerDto.getRowsPerPage());
		pager.put("startRowNo", pagerDto.getStartRowNo());
		pager.put("startRowIndex", pagerDto.getStartRowIndex());
		pager.put("endRowNo", pagerDto.getEndRowNo());
		pager.put("endRowIndex", pagerDto.getEndRowIndex());

		sendData.put("pager", pager);

		sendJson.put("data", sendData);
		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 전체 책 목록 검색(출간일 순)
	public String adminBookListOrderByPublishDate(JSONObject data) {
		// 1) JSON TO DTO
		int count = adminBookService.adminBookListOrderByPublishDateCount(ConnectionProvider.getConnection());
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<BookDto> list = adminBookService.adminBookListOrderByPublishDate(ConnectionProvider.getConnection(),
				pagerDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminBookListOrderByPublishDate");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
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
		sendData.put("bookList", dataArr);

		JSONObject pager = new JSONObject();
		pager.put("totalRows", pagerDto.getTotalRows());
		pager.put("totalPageNo", pagerDto.getTotalPageNo());
		pager.put("totalGroupNo", pagerDto.getTotalGroupNo());
		pager.put("startPageNo", pagerDto.getStartPageNo());
		pager.put("endPageNo", pagerDto.getEndPageNo());
		pager.put("pageNo", pagerDto.getPageNo());
		pager.put("pagesPerGroup", pagerDto.getPagesPerGroup());
		pager.put("groupNo", pagerDto.getGroupNo());
		pager.put("rowsPerPage", pagerDto.getRowsPerPage());
		pager.put("startRowNo", pagerDto.getStartRowNo());
		pager.put("startRowIndex", pagerDto.getStartRowIndex());
		pager.put("endRowNo", pagerDto.getEndRowNo());
		pager.put("endRowIndex", pagerDto.getEndRowIndex());

		sendData.put("pager", pager);

		sendJson.put("data", sendData);
		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 전체 책 목록 검색(판매량 순 - 주문 취소건은 합산x)
	public String adminBookListBySales(JSONObject data) {
		// 1) JSON TO DTO
		int count = adminBookService.adminBookListBySalesCount(ConnectionProvider.getConnection());
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2) SERVICE 호출 후 결과값 받기s
		ArrayList<BookDto> list = adminBookService.adminBookListBySales(ConnectionProvider.getConnection(), pagerDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminBookListBySales");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
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
			jo.put("bookSales", book.getSales());
			jo.put("subName", book.getSub_name());
			jo.put("categoryName", book.getCategory_name());
			dataArr.put(jo);
		}
		sendData.put("bookList", dataArr);

		JSONObject pager = new JSONObject();
		pager.put("totalRows", pagerDto.getTotalRows());
		pager.put("totalPageNo", pagerDto.getTotalPageNo());
		pager.put("totalGroupNo", pagerDto.getTotalGroupNo());
		pager.put("startPageNo", pagerDto.getStartPageNo());
		pager.put("endPageNo", pagerDto.getEndPageNo());
		pager.put("pageNo", pagerDto.getPageNo());
		pager.put("pagesPerGroup", pagerDto.getPagesPerGroup());
		pager.put("groupNo", pagerDto.getGroupNo());
		pager.put("rowsPerPage", pagerDto.getRowsPerPage());
		pager.put("startRowNo", pagerDto.getStartRowNo());
		pager.put("startRowIndex", pagerDto.getStartRowIndex());
		pager.put("endRowNo", pagerDto.getEndRowNo());
		pager.put("endRowIndex", pagerDto.getEndRowIndex());

		sendData.put("pager", pager);

		sendJson.put("data", sendData);
		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 책 상세정보
	public String adminBookInfo(JSONObject data) {
		// 1) JSON TO DTO
		BookDto bookDto = new BookDto();
		bookDto.setBook_no(data.getInt("bookNo"));

		// 2) SERVICE 호출 후 결과값 받기
		BookDto book = adminBookService.adminBookInfo(ConnectionProvider.getConnection(), bookDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminBookInfo");

		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		for (AuthorDto aa : book.getAuthors()) {
			JSONObject jo2 = new JSONObject();
			jo2.put("authorNo", aa.getAuthor_no());
			jo2.put("authorName", aa.getAuthor_name());
			jo2.put("authorDetail", aa.getAuthor_detail());
			ja.put(jo2);
		}
		jo.put("authors", ja);

		ja = new JSONArray();
		for (BookHashDto bookHashDto : book.getHashtags()) {
			JSONObject jo2 = new JSONObject();
			jo2.put("bookNo", bookHashDto.getBook_no());
			jo2.put("hashId", bookHashDto.getHash_id());
			ja.put(jo2);
		}
		jo.put("hashtags", ja);

		jo.put("bookNo", book.getBook_no());
		jo.put("bookName", book.getBook_name());
		jo.put("bookPublisher", book.getBook_publisher());
		jo.put("bookDetail", book.getBook_detail());
		jo.put("bookPrice", book.getBook_price());
		jo.put("bookPage", book.getBook_page());
		jo.put("bookLang", book.getBook_lang());
		jo.put("bookDate", book.getBook_date());
		jo.put("bookStore", book.getBook_store());
		jo.put("subNo", book.getSub_no());
		jo.put("subName", book.getSub_name());
		jo.put("categoryName", book.getCategory_name());

		sendJson.put("data", jo);

		// 4) RETURN JSON
		return sendJson.toString();

	}

	// 책 저자 정보 조회
	public String adminBookAuthorInfo(JSONObject data) {
		// 1) JSON TO DTO
		BookDto bookDto = new BookDto();
		bookDto.setBook_no(data.getInt("bookNo"));

		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<AuthorDto> authorList = adminBookService.adminBookAuthorInfo(ConnectionProvider.getConnection(),
				bookDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminBookAuthorInfo");

		JSONArray ja = new JSONArray();
		for (AuthorDto aa : authorList) {
			JSONObject jo = new JSONObject();
			jo.put("authorNo", aa.getAuthor_no());
			jo.put("authorName", aa.getAuthor_name());
			jo.put("authorDetail", aa.getAuthor_detail());
			ja.put(jo);
		}

		sendJson.put("data", ja);

		// 4) RETURN JSON
		return sendJson.toString();

	}

	// 책 해시태그 정보 조회 
	public String adminBookHashInfo(JSONObject data) {
		// 1) JSON TO DTO
		BookDto bookDto = new BookDto();
		bookDto.setBook_no(data.getInt("bookNo"));

		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<BookHashDto> hashList = adminBookService.adminBookHashInfo(ConnectionProvider.getConnection(),
				bookDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminBookHashInfo");

		JSONArray ja = new JSONArray();
		for (BookHashDto bhd : hashList) {
			JSONObject jo = new JSONObject();
			jo.put("bookNo", bhd.getBook_no());
			jo.put("hashId", bhd.getHash_id());
			ja.put(jo);
		}

		sendJson.put("data", ja);

		// 4) RETURN JSON
		return sendJson.toString();

	}

	// 책 정보수정
	public String adminBookUpdate(JSONObject data) {
		// 1) JSON TO DTO
		BookDto book = new BookDto();
		book.setBook_no(data.getInt("bookNo"));
		book.setBook_name(data.getString("bookName"));
		book.setBook_publisher(data.getString("bookPublisher"));
		book.setBook_detail(data.getString("bookDetail"));
		book.setBook_price(data.getInt("bookPrice"));
		book.setBook_page(data.getInt("bookPage"));
		book.setBook_lang(data.getString("bookLang"));
		book.setBook_date(data.getString("bookDate"));
		book.setSub_no(data.getInt("subNo"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminBookService.adminBookUpdate(ConnectionProvider.getConnection(), book);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminBookUpdate");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 책에 저자 추가
	public String adminBookAuthorAdd(JSONObject data) {
		// 1) JSON TO DTO
		AuthorBookDto authorBookDto = new AuthorBookDto();
		authorBookDto.setAuthor_no(data.getInt("authorNo"));
		authorBookDto.setBook_no(data.getInt("bookNo"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminBookService.adminBookAuthorAdd(ConnectionProvider.getConnection(), authorBookDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminBookAuthorAdd");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 책의 저자 삭제
	public String adminBookAuthorPop(JSONObject data) {
		// 1) JSON TO DTO
		AuthorBookDto authorBookDto = new AuthorBookDto();
		authorBookDto.setAuthor_no(data.getInt("authorNo"));
		authorBookDto.setBook_no(data.getInt("bookNo"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminBookService.adminBookAuthorPop(ConnectionProvider.getConnection(), authorBookDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminBookAuthorPop");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 책 해시태그 추가
	public String adminHashTagAdd(JSONObject data) {
		// 1) JSON TO DTO
		BookHashDto bookHashDto = new BookHashDto();
		bookHashDto.setBook_no(data.getInt("bookNo"));
		bookHashDto.setHash_id(data.getString("hashId"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminBookService.adminHashTagAdd(ConnectionProvider.getConnection(), bookHashDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminHashTagAdd");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 책 해시태그 삭제
	public String adminHashTagPop(JSONObject data) {
		// 1) JSON TO DTO
		BookHashDto bookHashDto = new BookHashDto();
		bookHashDto.setBook_no(data.getInt("bookNo"));
		bookHashDto.setHash_id(data.getString("hashId"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminBookService.adminHashTagPop(ConnectionProvider.getConnection(), bookHashDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminHashTagPop");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 책 삭제 -> rows = 0이면 실패 1이면 성공
	public String adminBookDelete(JSONObject data) {
		// 1) JSON TO DTO
		BookDto bookDto = new BookDto();
		bookDto.setBook_no(data.getInt("bookNo"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminBookService.adminBookDelete(ConnectionProvider.getConnection(), bookDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminBookDelete");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 책 추가
	public String adminBookCreate(JSONObject data) {
		// 1) JSON TO DTO
		BookDto bookDto = new BookDto();
		bookDto.setBook_name(data.getString("bookName"));
		bookDto.setBook_publisher(data.getString("bookPublisher"));
		bookDto.setBook_detail(data.getString("bookDetail"));
		bookDto.setBook_price(data.getInt("bookPrice"));
		bookDto.setBook_page(data.getInt("bookPage"));
		bookDto.setBook_lang(data.getString("bookLang"));
		bookDto.setBook_date(data.getString("bookDate"));
		bookDto.setSub_no(data.getInt("subNo"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminBookService.adminBookCreate(ConnectionProvider.getConnection(), bookDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminBookCreate");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 책 재고 추가 = warehousing 행 추가 + book_store 값 + => 트랜잭션 처리
	public String adminWareHousingAdd(JSONObject data) {
		// 1) JSON TO DTO
		WarehousingDto warehousingDto = new WarehousingDto();
		warehousingDto.setBook_no(data.getInt("bookNo"));
		warehousingDto.setWare_amount(data.getInt("amount"));

		// 2) SERVICE 호출 후 결과값 받기
		boolean done = adminBookService.adminWareHousingAdd(ConnectionProvider.getConnection(), warehousingDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminWareHousingAdd");
		sendJson.put("data", done);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 상품 입출고 이력 확인(입출고 날짜 최신 순)
	public String adminWareHistorySearch(JSONObject data) {
		// 1) JSON TO DTO
		BookDto bookDto = new BookDto();
		bookDto.setBook_no(data.getInt("bookNo"));
		int count = adminBookService.adminWareHistorySearchCount(ConnectionProvider.getConnection(), bookDto);
		PagerDto pagerDto = new PagerDto(10, 5, count, data.getInt("pageNo"));
		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<WarehousingDto> list = adminBookService.adminWareHistorySearch(ConnectionProvider.getConnection(),
				bookDto, pagerDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminWareHistorySearch");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();

		for (WarehousingDto warehousingDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("wareNo", warehousingDto.getWare_no());
			jo.put("bookNo", warehousingDto.getBook_no());
			jo.put("wareDate", warehousingDto.getWare_date());
			jo.put("wareAmount", warehousingDto.getWare_amount());
			jo.put("wareStatus", warehousingDto.getWare_status() + "");
			dataArr.put(jo);
		}
		sendData.put("wareList", dataArr);

		JSONObject pager = new JSONObject();
		pager.put("totalRows", pagerDto.getTotalRows());
		pager.put("totalPageNo", pagerDto.getTotalPageNo());
		pager.put("totalGroupNo", pagerDto.getTotalGroupNo());
		pager.put("startPageNo", pagerDto.getStartPageNo());
		pager.put("endPageNo", pagerDto.getEndPageNo());
		pager.put("pageNo", pagerDto.getPageNo());
		pager.put("pagesPerGroup", pagerDto.getPagesPerGroup());
		pager.put("groupNo", pagerDto.getGroupNo());
		pager.put("rowsPerPage", pagerDto.getRowsPerPage());
		pager.put("startRowNo", pagerDto.getStartRowNo());
		pager.put("startRowIndex", pagerDto.getStartRowIndex());
		pager.put("endRowNo", pagerDto.getEndRowNo());
		pager.put("endRowIndex", pagerDto.getEndRowIndex());

		sendData.put("pager", pager);

		sendJson.put("data", sendData);
		// 4) RETURN JSON
		return sendJson.toString();
	}
}
