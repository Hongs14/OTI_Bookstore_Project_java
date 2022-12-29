package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.DAO.MyReviewUserDAO;
import oti3.DTO.PagerDto;
import oti3.DTO.ReviewDto;
import oti3.DTO.UserDto;

public class MyReviewUserService {
	// 서버->클라이언트
	MyReviewUserDAO myReviewUserDao = new MyReviewUserDAO();

	public ArrayList<ReviewDto> selectReview(Connection conn, PagerDto pagerDto, ReviewDto selectReview) {
		// 리뷰조회
		// {"command":[]}

		ArrayList<ReviewDto> list = myReviewUserDao.selectReview(conn, pagerDto, selectReview);
		return list;
	}

	public int selectReviewCount(Connection conn, ReviewDto reviewDto) {
		int count = myReviewUserDao.selectReviewCount(conn, reviewDto);
		return count;
	}

	public int updateReview(Connection conn, ReviewDto updateReview) {
		// 리뷰수정
		int list = myReviewUserDao.updateReview(conn, updateReview);
		return list;
	}

	public int deleteReview(Connection conn, ReviewDto review) {
		// 리뷰 삭제
		int list = myReviewUserDao.deleteReview(conn, review);
		return list;
	}

	public int updateUser(Connection conn, UserDto updateUser) {
		// 사용자 정보 수정
		int list = myReviewUserDao.updateUser(conn, updateUser);
		return list;
	}

	public int leaveUser(Connection conn, UserDto leaveUser) {
		// 사용자 탈퇴 요청
		int list = myReviewUserDao.leaveUser(conn, leaveUser);
		return list;
	}

}
