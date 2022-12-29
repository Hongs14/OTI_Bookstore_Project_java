package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;

public class QnaDao {
	// QNA 게시판 목록 행수 (for Pager)
	public int selectQlistCount(Connection conn) {
		int count = 0;
		try {
			String sql = "SELECT count(*)as COUNT FROM qnas";

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

	// QNA 게시판 목록 조회 (selectQlist()) - rownum
	public ArrayList<QnaDto> selectQlist(PagerDto pagerDto, Connection conn) {
		ArrayList<QnaDto> qlist = new ArrayList<>();
		try {
			// 글번호, 카테고리(문의유형), 아이디, 제목, 작성일, 조회수 띄우기
			String sql = "SELECT *"
					+ " FROM (SELECT rownum rnum, qna_no, qna_category, q.user_id, qna_title, to_char(qna_date, 'yyyy-mm-dd')as qna_date, qna_view"
					+ " FROM qnas q, users u" + " WHERE q.user_id = u.user_id and rownum <= ?"
					+ " ORDER BY qna_no DESC) f" + " WHERE rnum >= ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pagerDto.getEndRowNo());
			pstmt.setInt(2, pagerDto.getStartRowNo());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				QnaDto selQlist = new QnaDto();
				selQlist.setQna_no(rs.getInt("qna_no"));
				selQlist.setQna_category(rs.getString("qna_category"));
				selQlist.setUser_id(rs.getString("user_id"));
				selQlist.setQna_title(rs.getString("qna_title"));
				selQlist.setQna_date(rs.getString("qna_date"));
				selQlist.setQna_view(rs.getInt("qna_view"));
				qlist.add(selQlist);
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
		return qlist;
	}

	// QNA 게시판 새 글 작성 (QCreate()-QcreateDTO)
	public int insertQna(QnaDto insQna, Connection conn) {
		int rows = 0;
		try {
			// 글번호, 카테고리(문의유형), 아이디, 제목, 작성일, 조회수 입력
			String sql = "INSERT INTO qnas(qna_no, qna_category, user_id, qna_title, qna_content, qna_date, qna_view)"
					+ " VALUES(seq_qna_no.nextval, ?, ?, ?, ?, sysdate, 0)";

			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, insQna.getQna_category());
			pstmt.setString(2, insQna.getUser_id());
			pstmt.setString(3, insQna.getQna_title());
			pstmt.setString(4, insQna.getQna_content());

			rows = pstmt.executeUpdate();
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
		// 생성 완료되면 1
		return rows;
	}

	// QNA 게시판 게시글 수정(QUpdate()-QupdateDTO)
	public int updateQna(QnaDto upQna, Connection conn) {
		int rows = 0;
		try {
			// 카테고리, 제목, 내용 수정
			// 게시글을 수정하려는 사람이 해당 게시글을 작성한 작성자 본인인지 확인하기 위해 QmatchDAO 필요
			String sql = "UPDATE qnas SET qna_category = ?, qna_title = ?, qna_content = ?, qna_date = sysdate WHERE qna_no = ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, upQna.getQna_category());
			pstmt.setString(2, upQna.getQna_title());
			pstmt.setString(3, upQna.getQna_content());
			pstmt.setInt(4, upQna.getQna_no());

			rows = pstmt.executeUpdate();
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
		// 수정 완료되면 1
		return rows;
	}

	// QNA 게시판 게시글 삭제(QDelete()-QdeleteDTO)
	public int deleteQna(QnaDto delQna, Connection conn) {
		int rows = 0;
		// 게시글을 삭제하려는 사람이 해당 게시글을 작성한 작성자 본인인지 확인하기 위해 QmatchDAO 필요
		try {
			String sql = "DELETE FROM qnas WHERE qna_no=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, delQna.getQna_no());

			rows = pstmt.executeUpdate();
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
		// 삭제 완료되면 1
		return rows;
	}

	// QNA 게시판 글 조회(QRead()-QreadDTO)
	public QnaDto selectQdetail(QnaDto selQdetail, Connection conn) {
		QnaDto qdetail = new QnaDto();
		try {
			// 전체 게시글 중 글번호 입력해서 상세 내용 조회하기
			// 게시글을 조회하려는 사람이 해당 게시글을 작성한 작성자 본인인지 확인하기 위해 QmatchDAO 필요
			String sql = "SELECT qna_no, qna_title, qna_content, qna_category, qna_view, to_char(qna_date, 'yyyy-mm-dd') as qna_date, user_id  FROM qnas WHERE qna_no=?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, selQdetail.getQna_no());
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				qdetail.setQna_no(rs.getInt("qna_no"));
				qdetail.setQna_category(rs.getString("qna_category"));
				qdetail.setUser_id(rs.getString("user_id"));
				qdetail.setQna_title(rs.getString("qna_title"));
				qdetail.setQna_content(rs.getString("qna_content"));
				qdetail.setQna_date(rs.getString("qna_date"));
				qdetail.setQna_view(rs.getInt("qna_view"));
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
		return qdetail;
	}

	// QmatchDAO : 게시글 수정/삭제/조회시 원글을 작성한 본인이 맞는지 확인하는 메소드
	public boolean selectQmatch(QnaDto selQmatch, Connection conn) {
		boolean isMine = false;
		try {
			// user_id가 작성한 글 번호들 불러오기
			String sql = "SELECT qna_no FROM qnas WHERE user_id=?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selQmatch.getUser_id());
			ResultSet rs = pstmt.executeQuery();

			ArrayList<Integer> mlist = new ArrayList<Integer>();
			while (rs.next()) {
				mlist.add(rs.getInt("qna_no"));
			}

			// 조작하려는 글 번호 = user가 작성한 글 번호 라면 true
			for (int a : mlist) {
				if (selQmatch.getQna_no() == a) {
					isMine = true;
					break;
				}
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
		return isMine;
	}

	// QNA 게시판 조회수 카운트 (QViewcount()-QviewcountDTO)
	public int updateQviewcount(QnaDto upQviewcount, Connection conn) {
		int rows = 0;
		try {
			// 조회수 +1
			String sql = "UPDATE qnas SET qna_view = qna_view + 1 WHERE qna_no= ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, upQviewcount.getQna_no());

			rows = pstmt.executeUpdate();
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
		// 업데이트 되면 1
		return rows;
	}

	// QNA 게시판 카테고리 행수 (for Pager)
	public int selectQcglistCount(QnaDto selQcg, Connection conn) {
		int count = 0;
		try {
			String sql = "SELECT count(*) as COUNT FROM qnas WHERE qna_category = ? ";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selQcg.getQna_category());
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

	// QNA 게시판 카테고리별 조회 (selectQcglist()) - rownum
	public ArrayList<QnaDto> selectQcglist(QnaDto selQcg, PagerDto pagerDto, Connection conn) {
		ArrayList<QnaDto> qlist = new ArrayList<>();
		try {
			// 카테고리로 검색한 결과 띄우기
			String sql = "SELECT * FROM (SELECT rownum rnum, qna_no, qna_category, q.user_id, qna_title, to_char(qna_date, 'yyyy-mm-dd')as qna_date, qna_view FROM qnas q, users u WHERE q.user_id = u.user_id AND qna_category = ? AND rownum <= ? ORDER BY qna_no DESC)f WHERE rnum >= ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, selQcg.getQna_category());
			pstmt.setInt(2, pagerDto.getEndRowNo());
			pstmt.setInt(3, pagerDto.getStartRowNo());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				selQcg = new QnaDto();
				selQcg.setQna_no(rs.getInt("qna_no"));
				selQcg.setQna_category(rs.getString("qna_category"));
				selQcg.setUser_id(rs.getString("user_id"));
				selQcg.setQna_title(rs.getString("qna_title"));
				selQcg.setQna_date(rs.getString("qna_date"));
				selQcg.setQna_view(rs.getInt("qna_view"));
				qlist.add(selQcg);
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
		return qlist;
	}
}