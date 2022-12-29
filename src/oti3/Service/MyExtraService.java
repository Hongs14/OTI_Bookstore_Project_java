package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import oti3.DAO.MyExtraDao;
import oti3.DTO.DibDto;
import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;
import oti3.DTO.ReviewDto;
import oti3.DTO.SelectAnswerDto;
import oti3.DTO.SelectDibDto;
import oti3.DTO.UserDto;

public class MyExtraService {
	
	public boolean chargeMoney(Connection conn, UserDto user, int money) {
		//회원 돈 충전 하기
		MyExtraDao myDao = new MyExtraDao();
		boolean done = myDao.chargeMoney(conn, user, money);
		return done;
	}	
	
	public ArrayList<SelectDibDto> selectDib(Connection conn, PagerDto pagerDto, SelectDibDto sdib) {
		//찜 목록 조회
		MyExtraDao myDao = new MyExtraDao();
		ArrayList<SelectDibDto> list = myDao.selectDib(conn, pagerDto, sdib);
		return list;
	}
	
	public int selectDibCount(Connection conn, SelectDibDto sdib) {
		//찜 목록 Count
		MyExtraDao myDao = new MyExtraDao();
		int count = myDao.selectDibCount(conn, sdib);
		return count;
	}
	
	
	public ArrayList<QnaDto> selectQna(Connection conn, PagerDto pagerDto, QnaDto qna) {
		//Qna조회
		MyExtraDao myDao = new MyExtraDao();
		ArrayList<QnaDto> list = myDao.selectQna(conn, pagerDto, qna);
		return list;
	}
	
	public int selectQnaCount(Connection conn, QnaDto qna) {
		//Qna조회 Count
		MyExtraDao myDao = new MyExtraDao();
		int count = myDao.selectQnaCount(conn, qna);
		return count;
	}
	
	
	public ArrayList<SelectAnswerDto> selectAnswer(Connection conn, SelectAnswerDto sanswer) {
		//answer답변 조회
		MyExtraDao myDao = new MyExtraDao();
		ArrayList<SelectAnswerDto> list = myDao.selectAnswer(conn, sanswer);
		return list;
	}
	
	public int deleteDib(Connection conn, DibDto deldib) {
		//찜 목록 삭제
		MyExtraDao myDao = new MyExtraDao();
		int row = myDao.deleteDib(conn, deldib);
		return row;
	}
	
	public ArrayList<ReviewDto> checkBookNumber(Connection conn, ReviewDto review){
		MyExtraDao myDao = new MyExtraDao();
		ArrayList<ReviewDto> list = myDao.checkBookNumber(conn, review);
		return list;
	}
}
