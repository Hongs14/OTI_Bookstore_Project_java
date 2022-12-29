package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.AnswerDto;
import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;

public class AdminQnaDao {

	/*
	 * 사용자 문의 관리
	 */

	// qna게시물 전체 행수
	public int adminQnaListCount(Connection conn) {
		int count = 0;
		try {
			String sql = "select count(*) as count from qnas";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;

	}

// qna게시글 전체 목록  
	public ArrayList<QnaDto> adminQnaList(Connection conn, PagerDto pagerDto) {
		ArrayList<QnaDto> list = new ArrayList<>();
		try {
			String sql = "select * from (select rownum as r, qna_no, qna_title, qna_content, qna_category, qna_view, to_char(qna_date, 'yyyy-mm-dd') as qna_date , user_id"
					+ " from qnas q where rownum <= ?) where r >= ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pagerDto.getEndRowNo());
			pstmt.setInt(2, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QnaDto qd = new QnaDto();
				qd.setQna_no(rs.getInt("qna_no"));
				qd.setQna_title(rs.getString("qna_title"));
				qd.setQna_content(rs.getString("qna_content"));
				qd.setQna_category(rs.getString("qna_category"));
				qd.setQna_view(rs.getInt("qna_view"));
				qd.setQna_date(rs.getString("qna_date"));
				qd.setUser_id(rs.getString("user_id"));
				list.add(qd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 답변 안 달린 qna게시물 전체 행수
	public int adminQnaNoAnswerListCount(Connection conn) {
		int count = 0;
		try {
			String sql = "select count(*) as count from qnas q, answers a where q.qna_no = a.qna_no(+) and a.qna_no is null";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;

	}

//	답변 안달린 qna게시글 전체 목록 
	public ArrayList<QnaDto> adminQnaNoAnswerList(Connection conn, PagerDto pagerDto) {
		ArrayList<QnaDto> list = new ArrayList<>();
		try {
			String sql = "select * from"
					+ " (select rownum as r, q.qna_no, qna_title, qna_content, qna_category, qna_view, to_char(qna_date, 'yyyy-mm-dd') as qna_date , user_id"
					+ " from qnas q, answers a where q.qna_no = a.qna_no(+) and a.qna_no is null and rownum <= ?) where r >= ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pagerDto.getEndRowNo());
			pstmt.setInt(2, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QnaDto qd = new QnaDto();
				qd.setQna_no(rs.getInt("qna_no"));
				qd.setQna_title(rs.getString("qna_title"));
				qd.setQna_content(rs.getString("qna_content"));
				qd.setQna_category(rs.getString("qna_category"));
				qd.setQna_view(rs.getInt("qna_view"));
				qd.setQna_date(rs.getString("qna_date"));
				qd.setUser_id(rs.getString("user_id"));
				list.add(qd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

//  qna 상세정보 가져오기 
	public QnaDto adminQnaInfo(Connection conn, QnaDto qnaDto) {
		QnaDto qd = new QnaDto();
		try {
			String sql = "select qna_no, qna_title, qna_content, qna_category, qna_view, to_char(qna_date, 'yyyy-mm-dd') as qna_date , user_id from qnas where qna_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qnaDto.getQna_no());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				qd.setQna_no(rs.getInt("qna_no"));
				qd.setQna_title(rs.getString("qna_title"));
				qd.setQna_content(rs.getString("qna_content"));
				qd.setQna_category(rs.getString("qna_category"));
				qd.setQna_view(rs.getInt("qna_view"));
				qd.setQna_date(rs.getString("qna_date"));
				qd.setUser_id(rs.getString("user_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return qd;
	}

	// qna글에 딸린 문의답변 가져오기
	public AnswerDto adminAnswerInfo(Connection conn, QnaDto qnaDto) {
		AnswerDto ad = new AnswerDto();
		try {
			String sql = "select answer_no, answer_title, answer_content, to_char(answer_date, 'yyyy-mm-dd') as answer_date, qna_no from answers where qna_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qnaDto.getQna_no());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				ad.setAnswer_no(rs.getInt("answer_no"));
				ad.setAnswer_title(rs.getString("answer_title"));
				ad.setAnswer_content(rs.getString("answer_content"));
				ad.setAnswer_date(rs.getString("answer_date"));
				ad.setQna_no(rs.getInt("qna_no"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ad;
	}

//	qna 문의답변 생성
	public int adminAnswerCreate(Connection conn, AnswerDto answerDto) {
		int rows = 0;
		try {
			String sql = "insert into answers values (seq_answer_no.nextval, ?, ?, sysdate, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, answerDto.getAnswer_title());
			pstmt.setString(2, answerDto.getAnswer_content());
			pstmt.setInt(3, answerDto.getQna_no());
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rows;
	}

//	qna 문의답변 삭제
	public int adminAnswerDelete(Connection conn, AnswerDto answerDto) {
		int rows = 0;
		try {
			String sql = "delete from answers where answer_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, answerDto.getAnswer_no());
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rows;
	}
}
