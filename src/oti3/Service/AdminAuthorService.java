package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import oti3.DAO.AdminAuthorDao;
import oti3.DTO.AuthorDto;
import oti3.DTO.PagerDto;

public class AdminAuthorService {

	// 저자 행 수
	public int adminAuthorListCount(Connection conn) {
		AdminAuthorDao adminAuthorDao = new AdminAuthorDao();
		int count = adminAuthorDao.adminAuthorListCount(conn);
		return count;
	}

	// 저자 목록 출력
	public ArrayList<AuthorDto> adminAuthorList(Connection conn, PagerDto pagerDto) {
		AdminAuthorDao adminAuthorDao = new AdminAuthorDao();
		ArrayList<AuthorDto> list = adminAuthorDao.adminAuthorList(conn, pagerDto);
		return list;
	}

	// 저자 이름 검색 행 수
	public int adminAuthorListByNameCount(Connection conn, AuthorDto authorDto) {
		AdminAuthorDao adminAuthorDao = new AdminAuthorDao();
		int count = adminAuthorDao.adminAuthorListByNameCount(conn, authorDto);
		return count;
	}

	// 저자 이름 검색 목록 출력
	public ArrayList<AuthorDto> adminAuthorListByName(Connection conn, AuthorDto authorDto, PagerDto pagerDto) {
		AdminAuthorDao adminAuthorDao = new AdminAuthorDao();
		ArrayList<AuthorDto> list = adminAuthorDao.adminAuthorListByName(conn, authorDto, pagerDto);
		return list;
	}

	// 저자 상세정보
	public AuthorDto adminAuthorInfo(Connection conn, AuthorDto authorDto) {
		AdminAuthorDao adminAuthorDao = new AdminAuthorDao();
		AuthorDto aa = adminAuthorDao.adminAuthorInfo(conn, authorDto);
		return aa;
	}

	// 저자 추가
	public int adminAuthorAdd(Connection conn, AuthorDto authorDto) {
		AdminAuthorDao adminAuthorDao = new AdminAuthorDao();
		int rows = adminAuthorDao.adminAuthorAdd(conn, authorDto);
		return rows;
	}

	// 저자 삭제
	public int adminAuthorPop(Connection conn, AuthorDto authorDto) {
		AdminAuthorDao adminAuthorDao = new AdminAuthorDao();
		int rows = adminAuthorDao.adminAuthorPop(conn, authorDto);
		return rows;
	}

	// 저자 정보 변경
	public int adminAuthorUpdate(Connection conn, AuthorDto authorDto) {
		AdminAuthorDao adminAuthorDao = new AdminAuthorDao();
		int rows = adminAuthorDao.adminAuthorUpdate(conn, authorDto);
		return rows;
	}
}
