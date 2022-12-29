package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import oti3.DAO.AdminQnaDao;
import oti3.DTO.AnswerDto;
import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;

public class AdminQnaService {
	/*
	 * 사용자 문의 관리
	 */

	// qna게시글 전체 행 수
	public int adminQnaListCount(Connection conn) {
		AdminQnaDao adminQnaDao = new AdminQnaDao();
		int rows = adminQnaDao.adminQnaListCount(conn);
		return rows;
	}

	// 답변 안 달린 qna게시글 전체 행 수
	public int adminQnaNoAnswerListCount(Connection conn) {
		AdminQnaDao adminQnaDao = new AdminQnaDao();
		int rows = adminQnaDao.adminQnaNoAnswerListCount(conn);
		return rows;
	}

	// qna게시글 전체 목록
	public ArrayList<QnaDto> adminQnaList(Connection conn, PagerDto pagerDto) {
		AdminQnaDao adminQnaDao = new AdminQnaDao();
		ArrayList<QnaDto> list = adminQnaDao.adminQnaList(conn, pagerDto);
		return list;
	}

	// 답변 안달린 qna게시글 전체 목록
	public ArrayList<QnaDto> adminQnaNoAnswerList(Connection conn, PagerDto pagerDto) {
		AdminQnaDao adminQnaDao = new AdminQnaDao();
		ArrayList<QnaDto> list = adminQnaDao.adminQnaNoAnswerList(conn, pagerDto);
		return list;
	}

	// qna 상세정보 가져오기
	public QnaDto adminQnaInfo(Connection conn, QnaDto qnaDto) {
		AdminQnaDao adminQnaDao = new AdminQnaDao();
		QnaDto aq = adminQnaDao.adminQnaInfo(conn, qnaDto);
		return aq;
	}

	// qna글에 딸린 문의답변 가져오기
	public AnswerDto adminAnswerInfo(Connection conn, QnaDto qnaDto) {
		AdminQnaDao adminQnaDao = new AdminQnaDao();
		AnswerDto ad = adminQnaDao.adminAnswerInfo(conn, qnaDto);
		return ad;
	}

//	qna 문의답변 생성
	public int adminAnswerCreate(Connection conn, AnswerDto answerDto) {
		AdminQnaDao adminQnaDao = new AdminQnaDao();
		int rows = adminQnaDao.adminAnswerCreate(conn, answerDto);
		return rows;
	}

//	qna 문의답변 삭제
	public int adminAnswerDelete(Connection conn, AnswerDto answerDto) {
		AdminQnaDao adminQnaDao = new AdminQnaDao();
		int rows = adminQnaDao.adminAnswerDelete(conn, answerDto);
		return rows;
	}
}
