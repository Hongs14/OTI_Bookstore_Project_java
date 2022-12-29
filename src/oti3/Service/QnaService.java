package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import oti3.DAO.AnswerDao;
import oti3.DAO.QnaDao;
import oti3.DTO.AnswerDto;
import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;

public class QnaService {
	/*
	 * public static void main(String args[]) {
	 * 
	 * // 값 검증해보기 // QMatch() 검증 - boolean 타입 QnaService qnaService = new
	 * QnaService(); JSONObject jo = qnaService.QMatch("eunjong", 100,
	 * ConnectionProvider.getConnection());
	 * System.out.println(jo.getBoolean("data"));
	 * 
	 * // QCreate() 데이터 값 보내고 검증 - 값 입력해야하는 타입 QcreateDTO qcreateDTO = new
	 * QcreateDTO(); qcreateDTO.setQna_category("결제");
	 * qcreateDTO.setUser_id("eunjong"); qcreateDTO.setQna_title("배송");
	 * qcreateDTO.setQna_content("배송");
	 * 
	 * QnaService qnaService = new QnaService(); JSONObject jo =
	 * qnaService.QCreate(qcreateDTO); System.out.println(jo.getInt("data"));
	 * 
	 * // QList() 검증 - 다중 행 타입 QnaService qnaService = new QnaService(); JSONObject
	 * jo = qnaService.QList(ConnectionProvider.getConnection()); JSONArray arr =
	 * jo.getJSONArray("data"); for (int i = 0; i < arr.length(); i++) {
	 * System.out.println(arr.get(i).toString()); }
	 * 
	 * // QRead() 검증 - 객체 타입 QnaService qnaService = new QnaService(); QreadDTO
	 * qread = new QreadDTO(); qread.setQna_no(5);
	 * 
	 * JSONObject jo = qnaService.QRead(qread, ConnectionProvider.getConnection());
	 * System.out.println(jo.get("data").toString()); }
	 */
	// QNA 게시판 목록 행 수
	public int selectQlistCount(Connection conn) {
		QnaDao qnasDAO = new QnaDao();
		int count = qnasDAO.selectQlistCount(conn);
		return count;
	}

	// QNA 게시판 목록 조회
	public ArrayList<QnaDto> selectQlist(PagerDto pagerDto, Connection conn) {
		QnaDao qnasDAO = new QnaDao();

		ArrayList<QnaDto> qlist = qnasDAO.selectQlist(pagerDto, conn);

		return qlist;
	}

	// Qna 카테고리 목록 행수
	public int selectQcglistCount(QnaDto selQcg, Connection conn) {
		QnaDao qnasDAO = new QnaDao();
		int count = qnasDAO.selectQcglistCount(selQcg, conn);
		return count;
	}

	// QNA 카테고리 목록 조회
	public ArrayList<QnaDto> selectQcglist(QnaDto selQcg, PagerDto pagerDto, Connection conn) {
		QnaDao qnasDAO = new QnaDao();

		ArrayList<QnaDto> qlist = qnasDAO.selectQcglist(selQcg, pagerDto, conn);

		return qlist;
	}

	// QNA 게시판 새 글 작성
	public int insertQna(QnaDto insQna, Connection conn) {
		QnaDao qnasDAO = new QnaDao();

		int rows = qnasDAO.insertQna(insQna, conn);

		return rows;
	}

	// QNA 게시판 게시글 수정
	public int updateQna(QnaDto upQna, Connection conn) {
		QnaDao qnasDAO = new QnaDao();

		int rows = qnasDAO.updateQna(upQna, conn);

		return rows;
	}

	// QNA 게시판 게시글 삭제
	public int deleteQna(QnaDto delQna, Connection conn) {
		QnaDao qnasDAO = new QnaDao();

		int rows = qnasDAO.deleteQna(delQna, conn);

		return rows;
	}

	// QNA 게시판 글 조회
	public QnaDto selectQdetail(QnaDto selQdetail, Connection conn) {
		QnaDao qnasDAO = new QnaDao();

		QnaDto qdetail = qnasDAO.selectQdetail(selQdetail, conn);

		return qdetail;
	}

	// 게시글 수정/삭제/조회시 원글을 작성한 본인이 맞는지 확인
	public boolean selectQmatch(QnaDto selQmatch, Connection conn) {
		QnaDao qnasDAO = new QnaDao();

		boolean isMine = qnasDAO.selectQmatch(selQmatch, conn);

		return isMine;
	}

	// QNA 게시판 조회수 카운트
	public int updateQviewcount(QnaDto upQviewcount, Connection conn) {
		QnaDao qnasDAO = new QnaDao();

		int rows = qnasDAO.updateQviewcount(upQviewcount, conn);

		return rows;
	}

	// Qna Answer : 문의글 답변 상세 읽기
	public AnswerDto selectQanswer(AnswerDto selQanswer, Connection conn) {
		AnswerDao answersDAO = new AnswerDao();

		AnswerDto qanswer = answersDAO.selectQanswer(selQanswer, conn);

		return qanswer;
	}
}