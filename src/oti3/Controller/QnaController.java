package oti3.Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.AnswerDto;
import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;
import oti3.Service.QnaService;

public class QnaController {

	private QnaService QnaService = new QnaService();

	// QNA 게시판 목록 조회
	public String selectQlist(JSONObject data) {
		// 1) JSON TO DTO
		// 주는 data 없음
		// PagerDto
		int count = QnaService.selectQlistCount(ConnectionProvider.getConnection());
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));

		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<QnaDto> qlist = QnaService.selectQlist(pagerDto, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectQlist");

		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();

		JSONArray dataArr = new JSONArray();
		for (QnaDto qboard : qlist) {
			JSONObject jo = new JSONObject();
			jo.put("qnaNo", qboard.getQna_no());
			jo.put("qnaCategory", qboard.getQna_category());
			jo.put("userId", qboard.getUser_id());
			jo.put("qnaTitle", qboard.getQna_title());
			jo.put("qnaDate", qboard.getQna_date());
			jo.put("qnaView", qboard.getQna_view());
			dataArr.put(jo);
		}
		sendData.put("qboardlist", dataArr);

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

		sendObject.put("data", sendData);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// QNA 게시판 새 글 작성
	public String insertQna(JSONObject data) {
		// 1) JSON TO DTO
		QnaDto insQna = new QnaDto();
		insQna.setQna_category(data.getString("qnaCategory"));
		insQna.setUser_id(data.getString("userId"));
		insQna.setQna_title(data.getString("qnaTitle"));
		insQna.setQna_content(data.getString("qnaContent"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = QnaService.insertQna(insQna, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "insertQna");
		sendObject.put("data", rows);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// QNA 게시판 게시글 수정
	public String updateQna(JSONObject data) {
		// 1) JSON TO DTO
		QnaDto upQna = new QnaDto();
		upQna.setQna_category(data.getString("qnaCategory"));
		upQna.setQna_title(data.getString("qnaTitle"));
		upQna.setQna_content(data.getString("qnaContent"));
		upQna.setQna_no(data.getInt("qnaNo"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = QnaService.updateQna(upQna, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "updateQna");
		sendObject.put("data", rows);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// QNA 게시판 게시글 삭제
	public String deleteQna(JSONObject data) {
		// 1) JSON TO DTO
		QnaDto delQna = new QnaDto();
		delQna.setQna_no(data.getInt("qnaNo"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = QnaService.deleteQna(delQna, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "deleteQna");
		sendObject.put("data", rows);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// QNA 게시판 글 조회
	public String selectQdetail(JSONObject data1) {
		// 1) JSON TO DTO
		QnaDto selQdetail = new QnaDto();
		selQdetail.setQna_no(data1.getInt("qnaNo"));

		// 2) SERVICE 호출 후 결과값 받기
		QnaDto qdetail = QnaService.selectQdetail(selQdetail, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectQdetail");
		JSONObject data = new JSONObject();
		data.put("qnaNo", qdetail.getQna_no());
		data.put("qnaCategory", qdetail.getQna_category());
		data.put("userId", qdetail.getUser_id());
		data.put("qnaTitle", qdetail.getQna_title());
		data.put("qnaContent", qdetail.getQna_content());
		data.put("qnaDate", qdetail.getQna_date());
		data.put("qnaView", qdetail.getQna_view());
		sendObject.put("data", data);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// 게시글 수정/삭제/조회시 원글을 작성한 본인이 맞는지 확인
	public String selectQmatch(JSONObject data) {
		// 1) JSON TO DTO
		QnaDto selQmatch = new QnaDto();
		selQmatch.setUser_id(data.getString("userId"));
		selQmatch.setQna_no(data.getInt("qnaNo"));

		// 2) SERVICE 호출 후 결과값 받기
		boolean isMine = QnaService.selectQmatch(selQmatch, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectQmatch");
		sendObject.put("data", isMine);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// QNA 게시판 조회수 증가
	public String updateQviewcount(JSONObject data) {
		// 1) JSON TO DTO
		QnaDto upQviewcount = new QnaDto();
		upQviewcount.setQna_no(data.getInt("qnaNo"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = QnaService.updateQviewcount(upQviewcount, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "updateQviewcount");
		sendObject.put("data", rows);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// Qna Answer : 문의글 답변 상세 읽기
	public String selectQanswer(JSONObject data1) {
		// 1) JSON TO DTO
		AnswerDto selQanswer = new AnswerDto();
		selQanswer.setQna_no(data1.getInt("qnaNo"));
		// 2) SERVICE 호출 후 결과값 받기
		AnswerDto qanswer = QnaService.selectQanswer(selQanswer, ConnectionProvider.getConnection());

		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectQanswer");
		JSONObject data = new JSONObject();
		data.put("answerNo", qanswer.getAnswer_no());
		data.put("answerTitle", qanswer.getAnswer_title());
		data.put("answerContent", qanswer.getAnswer_content());
		data.put("answerDate", qanswer.getAnswer_date());
		sendObject.put("data", data);

		// 4) RETURN JSON
		return sendObject.toString();
	}

	// QNA 게시판 카테고리별 조회 (selectQcglist()) -페이징
	public String selectQcglist(JSONObject data) {
		// 1) JSON TO DTO
		QnaDto selQcg = new QnaDto();
		selQcg.setQna_category(data.getString("qnaCategory"));

		// PagerDto
		int count = QnaService.selectQcglistCount(selQcg, ConnectionProvider.getConnection());
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));

		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<QnaDto> qlist = QnaService.selectQcglist(selQcg, pagerDto, ConnectionProvider.getConnection());
		
		// 3) DTO TO JSON
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectQcglist");

		JSONObject sendData = new JSONObject();

		JSONArray dataArr = new JSONArray();
		for (QnaDto qboard : qlist) {
			JSONObject jo = new JSONObject();
			jo.put("qnaNo", qboard.getQna_no());
			jo.put("qnaCategory", qboard.getQna_category());
			jo.put("userId", qboard.getUser_id());
			jo.put("qnaTitle", qboard.getQna_title());
			jo.put("qnaDate", qboard.getQna_date());
			jo.put("qnaView", qboard.getQna_view());
			dataArr.put(jo);
		}
		sendData.put("qboardlist", dataArr);

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

		sendObject.put("data", sendData);
				
		// 4) RETURN JSON
		return sendObject.toString();

	}
}
