package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oti3.DTO.AnswerDto;

public class AnswerDao {
	// QNA 글에 대한 답변 조회 (QAnswer()-QAnswerDTO)
	public AnswerDto selectQanswer(AnswerDto selQanswer, Connection conn) {
		AnswerDto qanswer = new AnswerDto();
		try {
			// Qmatch()하고 들어간 qna글에서 답변 볼 수 있게 하기
			String sql = "select answer_no, answer_title, answer_content, to_char(answer_date, 'yyyy-mm-dd') as answer_date, qna_no from answers where qna_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, selQanswer.getQna_no());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				qanswer.setAnswer_no(rs.getInt("answer_no"));
				qanswer.setAnswer_title(rs.getString("answer_title"));
				qanswer.setAnswer_content(rs.getString("answer_content"));
				qanswer.setAnswer_date(rs.getString("answer_date"));
				qanswer.setQna_no(rs.getInt("qna_no"));
			}

			rs.close();
			pstmt.close();

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
		return qanswer;
	}
}
