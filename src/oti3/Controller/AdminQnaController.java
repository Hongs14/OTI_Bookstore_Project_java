package oti3.Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.AnswerDto;
import oti3.DTO.AuthorDto;
import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;
import oti3.Service.AdminQnaService;

public class AdminQnaController {
	AdminQnaService adminQnaService = new AdminQnaService();
	/*
	 * 사용자 문의 관리
	 */

	// qna게시글 전체 목록
	public String adminQnaList(JSONObject data) {
		// 1) JSON TO DTO
		int count = adminQnaService.adminQnaListCount(ConnectionProvider.getConnection());
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<QnaDto> list = adminQnaService.adminQnaList(ConnectionProvider.getConnection(), pagerDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminQnaList");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();

		for (QnaDto qnaDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("qnaNo", qnaDto.getQna_no());
			jo.put("qnaTitle", qnaDto.getQna_title());
			jo.put("qnaContent", qnaDto.getQna_content());
			jo.put("qnaCategory", qnaDto.getQna_category());
			jo.put("qnaView", qnaDto.getQna_view());
			jo.put("qnaDate", qnaDto.getQna_date());
			jo.put("userId", qnaDto.getUser_id());
			dataArr.put(jo);
		}
		sendData.put("qnaList", dataArr);

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

	// 답변 안달린 qna게시글 전체 목록
	public String adminQnaNoAnswerList(JSONObject data) {
		// 1) JSON TO DTO
		int count = adminQnaService.adminQnaNoAnswerListCount(ConnectionProvider.getConnection());
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<QnaDto> list = adminQnaService.adminQnaNoAnswerList(ConnectionProvider.getConnection(), pagerDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminQnaNoAnswerList");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();

		for (QnaDto qnaDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("qnaNo", qnaDto.getQna_no());
			jo.put("qnaTitle", qnaDto.getQna_title());
			jo.put("qnaContent", qnaDto.getQna_content());
			jo.put("qnaCategory", qnaDto.getQna_category());
			jo.put("qnaView", qnaDto.getQna_view());
			jo.put("qnaDate", qnaDto.getQna_date());
			jo.put("userId", qnaDto.getUser_id());
			dataArr.put(jo);
		}
		sendData.put("qnaList", dataArr);

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

	// qna 상세정보 가져오기
	public String adminQnaInfo(JSONObject data) {
		// 1) JSON TO DTO
		QnaDto qnaDto = new QnaDto();
		qnaDto.setQna_no(data.getInt("qnaNo"));

		// 2) SERVICE 호출 후 결과값 받기
		QnaDto qna = adminQnaService.adminQnaInfo(ConnectionProvider.getConnection(), qnaDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminQnaInfo");

		JSONObject jo = new JSONObject();
		jo.put("qnaNo", qna.getQna_no());
		jo.put("qnaTitle", qna.getQna_title());
		jo.put("qnaContent", qna.getQna_content());
		jo.put("qnaCategory", qna.getQna_category());
		jo.put("qnaView", qna.getQna_view());
		jo.put("qnaDate", qna.getQna_date());
		jo.put("qnaUserId", qna.getUser_id());

		sendJson.put("data", jo);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// qna글에 딸린 문의답변 가져오기
	public String adminAnswerInfo(JSONObject data) {
		// 1) JSON TO DTO
		QnaDto qnaDto = new QnaDto();
		qnaDto.setQna_no(data.getInt("qnaNo"));

		// 2) SERVICE 호출 후 결과값 받기
		AnswerDto answerDto = adminQnaService.adminAnswerInfo(ConnectionProvider.getConnection(), qnaDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminAnswerInfo");

		JSONObject jo = new JSONObject();
		jo.put("answerNo", answerDto.getAnswer_no());
		jo.put("answerTitle", answerDto.getAnswer_title());
		jo.put("answerContent", answerDto.getAnswer_content());
		jo.put("answerDate", answerDto.getAnswer_date());
		jo.put("qnaNo", answerDto.getQna_no());

		sendJson.put("data", jo);

		// 4) RETURN JSON
		return sendJson.toString();

	}

//	qna 문의답변 생성
	public String adminAnswerCreate(JSONObject data) {
		// 1) JSON TO DTO
		AnswerDto answerDto = new AnswerDto();
		answerDto.setAnswer_title(data.getString("answerTitle"));
		answerDto.setAnswer_content(data.getString("answerContent"));
		answerDto.setQna_no(data.getInt("qnaNo"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminQnaService.adminAnswerCreate(ConnectionProvider.getConnection(), answerDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminAnswerCreate");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}

//	qna 문의답변 삭제
	public String adminAnswerDelete(JSONObject data) {
		// 1) JSON TO DTO
		AnswerDto answerDto = new AnswerDto();
		answerDto.setAnswer_no(data.getInt("answerNo"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminQnaService.adminAnswerDelete(ConnectionProvider.getConnection(), answerDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminAnswerDelete");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}
}
