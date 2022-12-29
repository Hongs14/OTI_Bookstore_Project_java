package oti3.Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.PagerDto;
import oti3.DTO.ReviewDto;
import oti3.DTO.UserDto;
import oti3.Service.MyReviewUserService;

public class MyReviewUserController {
	private MyReviewUserService myReviewUserService = new MyReviewUserService();

	public String selectReview(JSONObject data) {
		// 리뷰 목록보기
		// 1)JSON to DTO
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setUser_id(data.getString("userId"));
		int count = myReviewUserService.selectReviewCount(ConnectionProvider.getConnection(), reviewDto);
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2) Service 호출 후 결과 값 받기
		ArrayList<ReviewDto> list = myReviewUserService.selectReview(ConnectionProvider.getConnection(), pagerDto, reviewDto);
		// 3)DTO to json
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "selectReview");
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();
		
		for (ReviewDto review : list) {
			JSONObject json = new JSONObject();
			json.put("reviewNo", review.getReview_no());
			json.put("bookName", review.getBook_name());
			json.put("reviewDate", review.getReview_date());
			json.put("reviewContent", review.getReview_content());
			json.put("reviewScore", review.getReview_score());
			json.put("userId", review.getUser_id());
			dataArr.put(json);
		}

		sendData.put("reviewList", dataArr);

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

		// 4)JSON반환	
		return sendJson.toString();
	}

	public String updateReview(JSONObject data) {
		// 리뷰 업데이트 (1이면 수정, 0이면 수정X)
		// 1)JSON to DTO
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setReview_no(data.getInt("reviewNo"));
		reviewDto.setReview_content(data.getString("reviewContent"));
		reviewDto.setReview_score(data.getInt("reviewScore"));

		// 2)Service요청한 후 결과받기
		int row = myReviewUserService.updateReview(ConnectionProvider.getConnection(), reviewDto);
		// 3) DTO to JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "updateReview");
		sendJson.put("data", row);

		return sendJson.toString();
	}

	public String deleteReview(JSONObject data) {
		// 리뷰삭제 (1이면 삭제, 0이면 삭제X)
		// 1)JSON to DTO
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setReview_no(data.getInt("reviewNo"));
		// 2)Service에 요청한 후 결과 값
		int row = myReviewUserService.deleteReview(ConnectionProvider.getConnection(), reviewDto);
		// 3)DTO to JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "deleteReview");
		sendJson.put("data", row);
		return sendJson.toString();
	}

	public String updateUser(JSONObject data) {
		// 사용자 수정 (1이면 수정, 0이면 수정X)
		// 1)JSON to DTO
		UserDto userDto = new UserDto();
		userDto.setUser_password(data.getString("userPassword"));
		userDto.setUser_email(data.getString("userEmail"));
		userDto.setUser_birth(data.getString("userBirth"));
		userDto.setUser_tel(data.getString("userTel"));
		userDto.setUser_address(data.getString("userAddress"));
		userDto.setUser_id(data.getString("userId"));
		
		// 2)service에 요청한 후 결과 받기
		int row = myReviewUserService.updateUser(ConnectionProvider.getConnection(), userDto);
		System.out.println(row);
		// 3)DTO to JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "updateUser");
		sendJson.put("data", row);
		return sendJson.toString();

	}

	public String leaveUser(JSONObject data) {
		// 사용자 탈퇴 요청
		// 1)JSON to DTO
		UserDto userDto = new UserDto();
		userDto.setUser_id(data.getString("userId"));
		
		// 2)Service요청 -> 결과
		int row = myReviewUserService.leaveUser(ConnectionProvider.getConnection(), userDto);
		// 3)DTO to JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "leaveUser");
		sendJson.put("data", row);
		return sendJson.toString();
	}

}
