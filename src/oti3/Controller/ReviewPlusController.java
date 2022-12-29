package oti3.Controller;

import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.ReviewDto;
import oti3.Service.ReviewPlusService;

public class ReviewPlusController {
	 private ReviewPlusService reviewplusService = new ReviewPlusService();
	 
	 public String reviewBoardPlus(JSONObject data) {
		 // 1) JSON TO DTO
		 ReviewDto reviewDto = new ReviewDto();
		 reviewDto.setReview_content(data.getString("review_content"));
		 reviewDto.setReview_score(data.getInt("review_score"));
		 reviewDto.setUser_id(data.getString("user_id"));
		 reviewDto.setBook_no(data.getInt("book_no"));
		 
		 // 2) SERVICE 호출 후 결과값
		 int result = reviewplusService.reviewBoardPlus(ConnectionProvider.getConnection(), reviewDto);
		 
		 // 3) DTO TO JSON
		 JSONObject sendJson = new JSONObject();
		 sendJson.put("command", "ReviewBoardPlus");
		 sendJson.put("data", result);
		 
		 // 4) RETURN JSON
		 return sendJson.toString();
		 
	 }
	 
	 public String reviewCheck (JSONObject data) {
		 ReviewDto reviewDto = new ReviewDto();
		 reviewDto.setUser_id(data.getString("user_id"));
		 reviewDto.setBook_no(data.getInt("book_no"));
		 
		 boolean result = reviewplusService.reviewCheck(ConnectionProvider.getConnection(), reviewDto);
		 
		 JSONObject sendJson = new JSONObject();
		 sendJson.put("command", "ReviewCheck");
		 sendJson.put("data", result);
		 
		 return sendJson.toString();
	 }
	 
	 public String orderCheck (JSONObject data) {
		 ReviewDto reviewDto = new ReviewDto();
		 reviewDto.setUser_id(data.getString("user_id"));
		 reviewDto.setBook_no(data.getInt("book_no"));
		 
		 boolean result = reviewplusService.orderCheck(ConnectionProvider.getConnection(), reviewDto);
		 
		 JSONObject sendJson = new JSONObject();
		 sendJson.put("command", "OrderCheck");
		 sendJson.put("data", result);
		 
		 return sendJson.toString();
	 }
}
