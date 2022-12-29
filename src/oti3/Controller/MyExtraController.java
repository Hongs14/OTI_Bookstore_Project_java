package oti3.Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.AuthorDto;
import oti3.DTO.DibDto;
import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;
import oti3.DTO.ReviewDto;
import oti3.DTO.SelectAnswerDto;
import oti3.DTO.SelectDibDto;
import oti3.DTO.UserDto;
import oti3.Service.MyExtraService;

public class MyExtraController {
	MyExtraService myExtraService = new MyExtraService();

	public String chargeMoney(JSONObject data) {
		// 사용자가 돈 충전하기
		// 1)JSON to DTO //클라이언트가 서버로 날린 값
		UserDto userDto = new UserDto();
		userDto.setUser_id(data.getString("userId"));
		int money = data.getInt("money");
		// 2)service -> 결과
		Boolean done = myExtraService.chargeMoney(ConnectionProvider.getConnection(), userDto, money);
		// 3)DTO to JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "chargeMoney");
		sendJson.put("data", done);

		return sendJson.toString();
	}

	public String selectDib(JSONObject data) {
		// 찜한 목록보기
		// 1)JSON to DTO

		SelectDibDto selectDibDto = new SelectDibDto();
		selectDibDto.setUser_id(data.getString("userId"));
		int count = myExtraService.selectDibCount(ConnectionProvider.getConnection(), selectDibDto);
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2)service -> 결과
		ArrayList<SelectDibDto> list = myExtraService.selectDib(ConnectionProvider.getConnection(), pagerDto,
				selectDibDto);
		// 3)DTO to JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "selectDib");
		JSONObject sendData = new JSONObject();

		JSONArray dataArr = new JSONArray();
		for (SelectDibDto sd : list) {
			JSONObject json = new JSONObject();
			json.put("bookNo", sd.getBook_no());
			json.put("bookName", sd.getBook_name());
			JSONArray authorList = new JSONArray();
			for (AuthorDto authorDto : sd.getAuthorList()) {
				JSONObject author = new JSONObject();
				author.put("authorNo", authorDto.getAuthor_no());
				author.put("authorName", authorDto.getAuthor_name());
				authorList.put(author);
			}
			json.put("authorList", authorList);
			dataArr.put(json);
		}

		sendData.put("diblist", dataArr);

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

	public String selectQna(JSONObject data) {
		// QNA목록 보기
		// 1)JSON to DTO
		QnaDto qnaDto = new QnaDto();
		qnaDto.setUser_id(data.getString("userId"));
		int count = myExtraService.selectQnaCount(ConnectionProvider.getConnection(), qnaDto);

		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2)service -> 결과
		ArrayList<QnaDto> list = myExtraService.selectQna(ConnectionProvider.getConnection(), pagerDto, qnaDto);
		// 3)DTO to JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "selectQna");
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();
		for (QnaDto sq : list) {
			JSONObject json = new JSONObject();
			json.put("qnaNo", sq.getQna_no());
			json.put("qnaTitle", sq.getQna_title());
			json.put("qnaCategory", sq.getQna_category());
			json.put("qnaDate", sq.getQna_date());
			json.put("qnaView", sq.getQna_view());
			dataArr.put(json);
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

		return sendJson.toString();

	}

	public String selectAnswer(JSONObject data) {
		// 답변 목록보기
		// 1)JSON to DTO
		SelectAnswerDto selectAnswerDto = new SelectAnswerDto();
		selectAnswerDto.setUser_id(data.getString("userId"));
		// 2)service -> 결과
		ArrayList<SelectAnswerDto> list = myExtraService.selectAnswer(ConnectionProvider.getConnection(),
				selectAnswerDto);
		// 3)DTO to JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "selectAnswer");
		JSONArray dataArr = new JSONArray();

		for (SelectAnswerDto sa : list) {
			JSONObject json = new JSONObject();
			json.put("answerNo", sa.getAnswer_no());
			json.put("qnaCategory", sa.getQna_category());
			json.put("answerTitle", sa.getAnswer_title());
			json.put("answerContent", sa.getAnswer_content());
			json.put("answerDate", sa.getAnswer_date());
			dataArr.put(json);
		}
		sendJson.put("data", dataArr);
		return sendJson.toString();
	}

	public String deleteDib(JSONObject data) {
		// 찜목록 삭제
		// 1)JSON to DTO
		DibDto dibDto = new DibDto();
		dibDto.setUser_id(data.getString("userId"));
		dibDto.setBook_no(data.getInt("bookNo"));

		// 2)Service ->결과
		int row = myExtraService.deleteDib(ConnectionProvider.getConnection(), dibDto);
		// 3)DTO to JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "deleteDib");
		sendJson.put("data", row);
		return (sendJson.toString());
	}

	public String checkBookNumber(JSONObject data) {
		// JSON to DTO
		ReviewDto review = new ReviewDto();
		review.setUser_id(data.getString("userId"));

		// Service->결과
		ArrayList<ReviewDto> list = myExtraService.checkBookNumber(ConnectionProvider.getConnection(), review);
		// DTO to JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "checkBookNumber");
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();
		for (ReviewDto reviewDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("bookNo", reviewDto.getBook_no());
			dataArr.put(jo);
		}

		sendJson.put("data", dataArr);

		return (sendJson.toString());
	}
}
