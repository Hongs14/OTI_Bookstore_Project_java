package oti3.Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.AuthorDto;
import oti3.DTO.BoardDto;
import oti3.DTO.BookHashDto;
import oti3.DTO.CategoryDto;
import oti3.DTO.DibDto;
import oti3.DTO.PagerDto;
import oti3.DTO.ReviewDto;
import oti3.DTO.SearchDto;
import oti3.DTO.SubCategoryDto;
import oti3.Service.SearchService;

public class SearchController {
	private SearchService searchService = new SearchService();

	// 메인 카테고리
	public String category(JSONObject data) {

		ArrayList<CategoryDto> list = searchService.category(ConnectionProvider.getConnection());

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "category");
		JSONArray dataArr = new JSONArray();

		for (CategoryDto cDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("category_no", cDto.getCategory_no());
			jo.put("category_name", cDto.getCategory_name());
			dataArr.put(jo);
		}
		sendJson.put("data", dataArr);

		return sendJson.toString();
	}

	// 서브 카테고리
	public String subCategory(JSONObject data) {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setCategory_no(data.getInt("category_no"));

		ArrayList<SubCategoryDto> list = searchService.subCategory(ConnectionProvider.getConnection(), categoryDto);

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "subCategory");
		JSONArray dataArr = new JSONArray();

		for (SubCategoryDto scDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("sub_no", scDto.getSub_no());
			jo.put("sub_name", scDto.getSub_name());
			dataArr.put(jo);
		}
		sendJson.put("data", dataArr);

		return sendJson.toString();
	}

	// 카테고리 검색 후 목록
	public String categoryBoard(JSONObject data) {
		// JSON TO DTO
		SubCategoryDto subCategoryDto = new SubCategoryDto();
		subCategoryDto.setSub_no(data.getInt("sub_no"));

		int count = searchService.categoryBoardCount(ConnectionProvider.getConnection(), subCategoryDto);
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));

		ArrayList<SearchDto> list = searchService.categoryBoard(ConnectionProvider.getConnection(), subCategoryDto,
				pagerDto);

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "categoryBoard");
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();

		for (SearchDto sDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("book_no", sDto.getBook_no());
			jo.put("book_name", sDto.getBook_name());
			jo.put("book_publisher", sDto.getBook_publisher());
			jo.put("book_price", sDto.getBook_price());
			jo.put("review_avg", sDto.getReviews_avg());
			JSONArray ja2 = new JSONArray();
			for (AuthorDto aDto : sDto.getAuthor_name()) {
				JSONObject jo2 = new JSONObject();
				jo2.put("author_name", aDto.getAuthor_name());
				ja2.put(jo2);
			}
			jo.put("author_name", ja2);
			JSONArray ja3 = new JSONArray();
			for (BookHashDto bhDto : sDto.getHash_id()) {
				JSONObject jo3 = new JSONObject();
				jo3.put("hash_id", bhDto.getHash_id());
				ja3.put(jo3);
			}
			jo.put("hash_id", ja3);
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

		return sendJson.toString();
	}

	// 통합 검색 후 목록
	public String integration(JSONObject data) {
		SearchDto searchDto = new SearchDto();
		searchDto.setSearch(data.getString("search"));

		int count = searchService.integrationCount(ConnectionProvider.getConnection(), searchDto);
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));

		ArrayList<SearchDto> list = searchService.integration(ConnectionProvider.getConnection(), searchDto, pagerDto);

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "integration");
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();

		for (SearchDto sDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("book_no", sDto.getBook_no());
			jo.put("book_name", sDto.getBook_name());
			jo.put("book_publisher", sDto.getBook_publisher());
			jo.put("book_price", sDto.getBook_price());
			jo.put("review_avg", sDto.getReviews_avg());
			JSONArray ja2 = new JSONArray();
			for (AuthorDto aDto : sDto.getAuthor_name()) {
				JSONObject jo2 = new JSONObject();
				jo2.put("author_name", aDto.getAuthor_name());
				ja2.put(jo2);
			}
			jo.put("author_name", ja2);
			JSONArray ja3 = new JSONArray();
			for (BookHashDto bhDto : sDto.getHash_id()) {
				JSONObject jo3 = new JSONObject();
				jo3.put("hash_id", bhDto.getHash_id());
				ja3.put(jo3);
			}
			jo.put("hash_id", ja3);
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

		return sendJson.toString();
	}

	// 책에 대한 상세정보 목록
	public String board(JSONObject data) {
		BoardDto boardDto = new BoardDto();
		boardDto.setBook_no(data.getInt("book_no"));

		ArrayList<BoardDto> list = searchService.board(ConnectionProvider.getConnection(), boardDto);

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "board");
		JSONArray dataArr = new JSONArray();

		for (BoardDto bDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("book_name", bDto.getBook_name());
			jo.put("book_detail", bDto.getBook_detail());
			jo.put("book_publisher", bDto.getBook_publisher());
			jo.put("book_price", bDto.getBook_price());
			jo.put("book_store", bDto.getBook_store());
			jo.put("book_page", bDto.getBook_page());
			jo.put("book_lang", bDto.getBook_lang());
			jo.put("book_date", bDto.getBook_date());
			JSONArray ja2 = new JSONArray();
			for (AuthorDto aDto : bDto.getAuthor()) {
				JSONObject jo2 = new JSONObject();
				jo2.put("author_name", aDto.getAuthor_name());
				jo2.put("author_detail", aDto.getAuthor_detail());
				ja2.put(jo2);
			}
			jo.put("author", ja2);
			JSONArray ja3 = new JSONArray();
			for (ReviewDto rDto : bDto.getReview()) {
				JSONObject jo3 = new JSONObject();
				jo3.put("user_id", rDto.getUser_id());
				jo3.put("review_date", rDto.getReview_date());
				jo3.put("review_content", rDto.getReview_content());
				jo3.put("review_score", rDto.getReview_score());
				ja3.put(jo3);
			}
			jo.put("review", ja3);
			dataArr.put(jo);
		}
		sendJson.put("data", dataArr);

		return sendJson.toString();
	}

	// 책 찜 추가
	public String dibs(JSONObject data) {
		DibDto dibDto = new DibDto();
		dibDto.setBook_no(data.getInt("book_no"));
		dibDto.setUser_id(data.getString("user_id"));

		int result = searchService.dibs(ConnectionProvider.getConnection(), dibDto);

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "dibs");
		sendJson.put("data", result);

		return sendJson.toString();
	}

	// userId, bookNo 찜에 존재하는지 여부 조회
	public String selectCheckDibs(JSONObject data) {
		DibDto dibDto = new DibDto();
		dibDto.setBook_no(data.getInt("book_no"));
		dibDto.setUser_id(data.getString("user_id"));

		boolean result = searchService.selectCheckDibs(ConnectionProvider.getConnection(), dibDto);

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "deleteDibs");
		sendJson.put("data", result);

		return sendJson.toString();
	}

	// 책 찜 삭제
	public String deleteDibs(JSONObject data) {
		DibDto dibDto = new DibDto();
		dibDto.setBook_no(data.getInt("book_no"));
		dibDto.setUser_id(data.getString("user_id"));

		int result = searchService.deleteDibs(ConnectionProvider.getConnection(), dibDto);

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "deleteDibs");
		sendJson.put("data", result);

		return sendJson.toString();
	}
}
