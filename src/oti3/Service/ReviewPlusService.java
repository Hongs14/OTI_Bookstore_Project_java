package oti3.Service;

import java.sql.Connection;

import oti3.DAO.ReviewPlusDAO;
import oti3.DTO.ReviewDto;

public class ReviewPlusService {
    // 클라이언트에게 보낼 리뷰 작성 성공했다는 json 포맷
    public int reviewBoardPlus(Connection conn, ReviewDto reviewdto) {
    	ReviewPlusDAO rDao = new ReviewPlusDAO();
    	int result = rDao.insertReviewBoardPlus(conn, reviewdto);
    	return result;
    }
    
    // 리뷰가 있는지 확인후 json 포맷으로 확인 값 리턴
    public boolean reviewCheck(Connection conn, ReviewDto reviewdto) {
    	ReviewPlusDAO rDao = new ReviewPlusDAO();
    	boolean result = rDao.selectReviewCheck(conn, reviewdto);
    	return result;
    }
    
    // 책을 구매헀는지 확인후 json 포맷으로 확인 값 리턴
    public boolean orderCheck(Connection conn, ReviewDto reviewdto) {
    	ReviewPlusDAO rDao = new ReviewPlusDAO();
    	boolean result = rDao.selectOrderCheck(conn, reviewdto);
    	return result;
    }

}
