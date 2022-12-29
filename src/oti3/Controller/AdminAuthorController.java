package oti3.Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.AuthorDto;
import oti3.DTO.PagerDto;
import oti3.Service.AdminAuthorService;

public class AdminAuthorController {
	private AdminAuthorService adminAuthorService = new AdminAuthorService();

	// 저자 목록 출력
	public String adminAuthorList(JSONObject data) {
		// 1) JSON TO DTO
		int count = adminAuthorService.adminAuthorListCount(ConnectionProvider.getConnection());
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<AuthorDto> list = adminAuthorService.adminAuthorList(ConnectionProvider.getConnection(), pagerDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminAuthorList");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();

		for (AuthorDto author : list) {
			JSONObject jo = new JSONObject();
			jo.put("authorNo", author.getAuthor_no());
			jo.put("authorName", author.getAuthor_name());
			jo.put("authorDetail", author.getAuthor_detail());
			dataArr.put(jo);
		}
		sendData.put("authorList", dataArr);

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

	// 저자 이름 검색 목록 출력
	public String adminAuthorListByName(JSONObject data) {
		// 1) JSON TO DTO
		AuthorDto authorDto = new AuthorDto();
		authorDto.setAuthor_name(data.getString("authorName"));

		int count = adminAuthorService.adminAuthorListByNameCount(ConnectionProvider.getConnection(), authorDto);
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<AuthorDto> list = adminAuthorService.adminAuthorListByName(ConnectionProvider.getConnection(),
				authorDto, pagerDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminAuthorListByName");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();

		for (AuthorDto author : list) {
			JSONObject jo = new JSONObject();
			jo.put("authorNo", author.getAuthor_no());
			jo.put("authorName", author.getAuthor_name());
			jo.put("authorDetail", author.getAuthor_detail());
			dataArr.put(jo);
		}
		sendData.put("authorList", dataArr);

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

	// 저자 상세정보
	public String adminAuthorInfo(JSONObject data) {
		// 1) JSON TO DTO
		AuthorDto authorDto = new AuthorDto();
		authorDto.setAuthor_no(data.getInt("authorNo"));

		// 2) SERVICE 호출 후 결과값 받기
		AuthorDto author = adminAuthorService.adminAuthorInfo(ConnectionProvider.getConnection(), authorDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminAuthorInfo");
		JSONObject jo = new JSONObject();

		jo.put("authorNo", author.getAuthor_no());
		jo.put("authorName", author.getAuthor_name());
		jo.put("authorDetail", author.getAuthor_detail());

		sendJson.put("data", jo);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 저자 추가
	public String adminAuthorAdd(JSONObject data) {
		// 1) JSON TO DTO
		AuthorDto adminAuthor = new AuthorDto();
		adminAuthor.setAuthor_name(data.getString("authorName"));
		adminAuthor.setAuthor_detail(data.getString("authorDetail"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminAuthorService.adminAuthorAdd(ConnectionProvider.getConnection(), adminAuthor);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminAuthorAdd");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 저자 삭제
	public String adminAuthorPop(JSONObject data) {
		// 1) JSON TO DTO
		AuthorDto adminAuthor = new AuthorDto();
		adminAuthor.setAuthor_no(data.getInt("authorNo"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminAuthorService.adminAuthorPop(ConnectionProvider.getConnection(), adminAuthor);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminAuthorPop");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 저자 정보 변경
	public String adminAuthorUpdate(JSONObject data) {
		// 1) JSON TO DTO
		AuthorDto adminAuthor = new AuthorDto();
		adminAuthor.setAuthor_no(data.getInt("authorNo"));
		adminAuthor.setAuthor_name(data.getString("authorName"));
		adminAuthor.setAuthor_detail(data.getString("authorDetail"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminAuthorService.adminAuthorUpdate(ConnectionProvider.getConnection(), adminAuthor);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminAuthorPop");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}
}
