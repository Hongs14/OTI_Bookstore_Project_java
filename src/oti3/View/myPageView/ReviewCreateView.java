package oti3.View.myPageView;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.ReviewDto;

public class ReviewCreateView {
	Scanner sc = new Scanner(System.in);
	ArrayList<ReviewDto> myBookNoList;

	public ReviewCreateView(BookClient bookClient, int bookNo) {
		// 1) 이미 내가 리뷰를 쓴 책인지 검증
		ReviewDto send = new ReviewDto();
		send.setUser_id(bookClient.loginUser.getUser_id());
		checkBookNumber(bookClient, send);
		for (ReviewDto reviewDto : myBookNoList) {
			if (reviewDto.getBook_no() == bookNo) {
				System.out.println("이미 리뷰를 작성한 책입니다.");
				return;
			}
		}

		ReviewDto newReview = new ReviewDto();
		newReview.setUser_id(bookClient.loginUser.getUser_id());
		newReview.setBook_no(bookNo);

		System.out.println("[리뷰 작성 페이지]");
		System.out.println("코멘트 입력-> ");
		String input = sc.nextLine();
		newReview.setReview_content(input);

		System.out.println("별점 입력-> ");
		int score = Integer.parseInt(sc.nextLine());
		newReview.setReview_score(score);

		int rows = ReviewPlus(bookClient, newReview);

		if (rows == 1) {
			System.out.println("리뷰가 성공적으로 등록되었습니다.");
//			System.out.println("| 1. 리뷰 게시판으로 가기 | 2. 홈으로 가기 |");
//			System.out.print("선택> ");
//			int num = sc.nextInt();
//			if (num == 1) {
//				new MyReviewPageView(bookClient);
//			} else if (num == 2) {
//				// new MainPageView(bookClient);
//			} else {
//				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
//			}
		} else {
			System.out.println("리뷰 등록에 실패했습니다.");
		}

	}

	public int ReviewPlus(BookClient bookClient, ReviewDto reviewDto) {
		// json만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "reviewBoardPlus");
		JSONObject data = new JSONObject();
		data.put("review_content", reviewDto.getReview_content());
		data.put("review_score", reviewDto.getReview_score());
		data.put("user_id", reviewDto.getUser_id());
		data.put("book_no", reviewDto.getBook_no());
		sendObject.put("data", data);

		bookClient.send(sendObject.toString());
		String json = bookClient.receive();

		JSONObject receiveJson = new JSONObject(json);

		return receiveJson.getInt("data");
	}

	public void checkBookNumber(BookClient bookClient, ReviewDto reviewDto) {
		// json만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "checkBookNumber");
		JSONObject data = new JSONObject();
		data.put("userId", reviewDto.getUser_id());
		sendObject.put("data", data);

		bookClient.send(sendObject.toString());
		String json = bookClient.receive();

		JSONObject receiveJson = new JSONObject(json);
		JSONArray arr = receiveJson.getJSONArray("data");
		myBookNoList = new ArrayList<>();
		for (int i = 0; i < arr.length(); i++) {
			JSONObject jo = arr.getJSONObject(i);
			ReviewDto reviewDto2 = new ReviewDto();
			reviewDto2.setBook_no(jo.getInt("bookNo"));
			myBookNoList.add(reviewDto2);
		}

	}

}
