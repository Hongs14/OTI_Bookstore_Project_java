package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.AuthorDto;
import oti3.DTO.DibDto;
import oti3.DTO.PagerDto;
import oti3.DTO.QnaDto;
import oti3.DTO.ReviewDto;
import oti3.DTO.SelectAnswerDto;
import oti3.DTO.SelectDibDto;
import oti3.DTO.UserDto;

public class MyExtraDao {

	public boolean chargeMoney(Connection conn, UserDto user, int money) {
		// 회원 돈 충전
		int rows = 0;
		boolean brow = false;
		try {
			String sql = "update users set user_money = user_money + ? " + "where user_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, money);
			pstmt.setString(2, user.getUser_id());
			rows = pstmt.executeUpdate();

			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB연결 오류 발생");
		} finally {
			try {
				if (conn != null) {
					System.out.println("DB연결 해제");
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rows > 0)
			brow = true;
		return brow;
	}

	public ArrayList<SelectDibDto> selectDib(Connection conn, PagerDto pagerDto, SelectDibDto sdib) {
		// 찜목록 조회 - hongjuDTO.SelectDibs
		ArrayList<SelectDibDto> list = new ArrayList<>();
		try {
			String sql = "select * "
					+ "from( select rownum rnum, d.book_no, b.book_name from dibs d, books b where d.book_no = b.book_no and user_id= ? and rownum <= ? ) "
					+ "where rnum > = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sdib.getUser_id());
			pstmt.setInt(2, pagerDto.getEndRowNo());
			pstmt.setInt(3, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				// arraylist작가
				SelectDibDto selectDib = new SelectDibDto();
				selectDib.setBook_no(rs.getInt("book_no"));
				selectDib.setBook_name(rs.getString("book_name"));
				sql = "select a.author_no, a.author_name from author_book ab, authors a, books b "
						+ "where b.book_no = ab.book_no and ab.author_no = a.author_no and b.book_no = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, rs.getInt("book_no"));
				ResultSet rss = pstmt.executeQuery();
				ArrayList<AuthorDto> authorList = new ArrayList<>();
				while (rss.next()) {
					AuthorDto authorDto = new AuthorDto();
					authorDto.setAuthor_no(rss.getInt("author_no"));
					authorDto.setAuthor_name(rss.getString("author_name"));
					authorList.add(authorDto);
				}
				selectDib.setAuthorList(authorList);
				list.add(selectDib);

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB연결 오류 발생");
		} finally {
			try {
				if (conn != null) {
					System.out.println("DB연결 해제");
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public int selectDibCount(Connection conn, SelectDibDto dibdto) {
		// 찜목록 세기
		int count = 0;
		try {
			String sql = "select count(*) as count from dibs where user_id = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dibdto.getUser_id());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB연결 오류 발생");
		} finally {
			try {
				if (conn != null) {
					System.out.println("DB연결 해제");
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}

	public ArrayList<QnaDto> selectQna(Connection conn, PagerDto pagerDto, QnaDto qna) {
		// qna목록조회 - hongjuDTO.SelectQna
		ArrayList<QnaDto> list = new ArrayList<>();
		try {
			String sql = "select * "
					+ " from( select rownum rnum, qna_no, qna_title, qna_category, qna_content, to_char(qna_date,'yyyy-mm-dd')as qna_date, qna_view "
					+ " from qnas where user_id = ? and rownum <= ?)  " 
					+ " where rnum >= ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, qna.getUser_id());
			pstmt.setInt(2, pagerDto.getEndRowNo());
			pstmt.setInt(3, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QnaDto selectQna = new QnaDto();
				selectQna.setQna_no(rs.getInt("qna_no"));
				selectQna.setQna_title(rs.getString("qna_title"));
				selectQna.setQna_content(rs.getString("qna_content"));
				selectQna.setQna_category(rs.getString("qna_category"));
				selectQna.setQna_date(rs.getString("qna_date"));
				selectQna.setQna_view(rs.getInt("qna_view"));
				list.add(selectQna);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB연결 오류 발생");
		} finally {
			try {
				if (conn != null) {
					System.out.println("DB연결 해제");
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public int selectQnaCount(Connection conn, QnaDto qnadto) {
		// 리뷰 게시판 조회 - ReviewDto
		int count = 0;
		try {
			String sql = "select count(*) as count from qnas where user_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, qnadto.getUser_id());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB연결 오류 발생");
		} finally {
			try {
				if (conn != null) {
					System.out.println("DB연결 해제");
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}

	public ArrayList<SelectAnswerDto> selectAnswer(Connection conn, SelectAnswerDto sanswer) {
		// qna답변목록 조회 - hongjuDTO.SelectQna
		ArrayList<SelectAnswerDto> list = new ArrayList<>();
		try {
			String sql = "select answer_no, qna_category, answer_title, answer_content, "
					+ "to_char(answer_date,'yyyy-mm-dd')as answer_date " + "from qnas q, answers a "
					+ "where q.qna_no = a.qna_no and user_id = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sanswer.getUser_id());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SelectAnswerDto selectAnswer = new SelectAnswerDto();
				selectAnswer.setAnswer_no(rs.getInt("answer_no"));
				selectAnswer.setQna_category(rs.getString("qna_category"));
				selectAnswer.setAnswer_title(rs.getString("answer_title"));
				selectAnswer.setAnswer_content(rs.getString("answer_content"));
				selectAnswer.setAnswer_date(rs.getString("answer_date"));
				list.add(selectAnswer);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB연결 오류 발생");
		} finally {
			try {
				if (conn != null) {
					System.out.println("DB연결 해제");
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public int deleteDib(Connection conn, DibDto dib) {
		// 리뷰 삭제
		int deleteR_row = 0;
		try {
			String sql = "DELETE FROM Dibs WHERE user_id= ? and book_no = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dib.getUser_id());
			pstmt.setInt(2, dib.getBook_no());
			deleteR_row = pstmt.executeUpdate();
			System.out.println("행삭제 성공");
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB연결 오류 발생");
		} finally {
			try {
				if (conn != null) {
					System.out.println("DB연결 해제");
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return deleteR_row;
	}
	
	public ArrayList<ReviewDto> checkBookNumber(Connection conn, ReviewDto review) {
		ArrayList<ReviewDto> list = new ArrayList<>();
		try {
			String sql = "select book_no from reviews where user_id= ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, review.getUser_id());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				ReviewDto checkbook = new ReviewDto();
				checkbook.setBook_no(rs.getInt("book_no"));
				list.add(checkbook);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB연결 오류 발생");
		} finally {
			try {
				if (conn != null) {
					System.out.println("DB연결 해제");
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
