package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.AuthorDto;
import oti3.DTO.PagerDto;

public class AdminAuthorDao {
	/*
	 * 저자 관리
	 */

	// 저자 행 수
	public int adminAuthorListCount(Connection conn) {
		int count = 0;
		try {
			String sql = "select count(*) as count from authors";
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

	// 저자 목록 출력
	public ArrayList<AuthorDto> adminAuthorList(Connection conn, PagerDto pagerDto) {
		ArrayList<AuthorDto> list = new ArrayList<>();
		try {
			String sql = "select author_no, author_name, nvl(author_detail, '저자 정보가 없습니다') as author_detail from (select rownum r, author_no, author_name, author_detail from authors where rownum <= ?) where r >= ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pagerDto.getEndRowNo());
			pstmt.setInt(2, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AuthorDto adminAuthor = new AuthorDto();
				adminAuthor.setAuthor_no(rs.getInt("author_no"));
				adminAuthor.setAuthor_name(rs.getString("author_name"));
				adminAuthor.setAuthor_detail(rs.getString("author_detail"));
				list.add(adminAuthor);
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

	// 저자 이름 검색 행 수
	public int adminAuthorListByNameCount(Connection conn, AuthorDto authorDto) {
		int count = 0;
		try {
			String sql = "select count(*) as count from authors where author_name like ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + authorDto.getAuthor_name() + "%");
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

	// 저자 이름 검색 목록 출력
	public ArrayList<AuthorDto> adminAuthorListByName(Connection conn, AuthorDto authorDto, PagerDto pagerDto) {
		ArrayList<AuthorDto> list = new ArrayList<>();
		try {
			String sql = "select author_no, author_name, nvl(author_detail, '저자 정보가 없습니다') as author_detail from (select rownum r, author_no, author_name, author_detail "
					+ "from authors where author_name like ? and rownum <= ?) where r >= ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + authorDto.getAuthor_name() + "%");
			pstmt.setInt(2, pagerDto.getEndRowNo());
			pstmt.setInt(3, pagerDto.getStartRowNo());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AuthorDto adminAuthor = new AuthorDto();
				adminAuthor.setAuthor_no(rs.getInt("author_no"));
				adminAuthor.setAuthor_name(rs.getString("author_name"));
				adminAuthor.setAuthor_detail(rs.getString("author_detail"));
				list.add(adminAuthor);
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

	// 저자 상세정보
	public AuthorDto adminAuthorInfo(Connection conn, AuthorDto authorDto) {
		AuthorDto adminAuthor = new AuthorDto();
		try {
			String sql = "select author_no, author_name, nvl(author_detail, '저자 정보가 없습니다') as author_detail from authors where author_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, authorDto.getAuthor_no());
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				adminAuthor.setAuthor_no(rs.getInt("author_no"));
				adminAuthor.setAuthor_name(rs.getString("author_name"));
				adminAuthor.setAuthor_detail(rs.getString("author_detail"));
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
		return adminAuthor;
	}

	// 저자 추가
	public int adminAuthorAdd(Connection conn, AuthorDto authorDto) {
		int rows = 0;
		try {
			String sql = "insert into authors values (seq_author_no.nextval, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, authorDto.getAuthor_name());
			pstmt.setString(2, authorDto.getAuthor_detail());
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

	// 저자 삭제
	public int adminAuthorPop(Connection conn, AuthorDto authorDto) {
		int rows = 0;
		try {
			String sql = "delete from authors where author_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, authorDto.getAuthor_no());
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

	// 저자 정보 변경
	public int adminAuthorUpdate(Connection conn, AuthorDto authorDto) {
		int rows = 0;
		try {
			String sql = "update authors set author_name = ?, author_detail = ? where author_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, authorDto.getAuthor_name());
			pstmt.setString(2, authorDto.getAuthor_detail());
			pstmt.setInt(3, authorDto.getAuthor_no());
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
