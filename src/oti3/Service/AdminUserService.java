package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import oti3.DAO.AdminUserDao;
import oti3.DTO.OrderDto;
import oti3.DTO.PagerDto;
import oti3.DTO.UserDto;

public class AdminUserService {
	/*
	 * 회원 관리
	 */

	// 회원 전체 검색 행 수
	public int adminUserListCount(Connection conn) {
		AdminUserDao adminUserDao = new AdminUserDao();
		int rows = adminUserDao.adminUserListCount(conn);
		return rows;
	}

	// 탈퇴 요청 회원의 목록 행 수
	public int adminDeleteRequestUserListCount(Connection conn) {
		AdminUserDao adminUserDao = new AdminUserDao();
		int rows = adminUserDao.adminDeleteRequestUserListCount(conn);
		return rows;

	}

	// 회원 id 검색 행 수
	public int adminUserListByIdCount(Connection conn, UserDto userDto) {
		AdminUserDao adminUserDao = new AdminUserDao();
		int rows = adminUserDao.adminUserListByIdCount(conn, userDto);
		return rows;
	}

	// 회원 전체 검색
	public ArrayList<UserDto> adminUserList(Connection conn, PagerDto pagerDto) {
		AdminUserDao adminUserDao = new AdminUserDao();
		ArrayList<UserDto> list = adminUserDao.adminUserList(conn, pagerDto);
		return list;
	}

	// 탈퇴 요청 회원의 목록
	public ArrayList<UserDto> adminDeleteRequestUserList(Connection conn, PagerDto pagerDto) {
		AdminUserDao adminUserDao = new AdminUserDao();
		ArrayList<UserDto> list = adminUserDao.adminDeleteRequestUserList(conn, pagerDto);
		return list;

	}

	// 회원 id 검색
	public ArrayList<UserDto> adminUserListById(Connection conn, PagerDto pagerDto, UserDto userDto) {
		AdminUserDao adminUserDao = new AdminUserDao();
		ArrayList<UserDto> list = adminUserDao.adminUserListById(conn, pagerDto, userDto);
		return list;
	}

	// 회원 상세정보
	public UserDto adminUserInfo(Connection conn, UserDto userDto) {
		AdminUserDao adminUserDao = new AdminUserDao();
		UserDto user = adminUserDao.adminUserInfo(conn, userDto);
		return user;
	}

	// 회원 탈퇴 가능 여부(user_dreq_date로부터 15일 지났는지 + user_delete가 'R'인지)
	public boolean adminCanDeleteUser(Connection conn, UserDto userDto) {
		AdminUserDao adminUserDao = new AdminUserDao();
		boolean done = adminUserDao.adminCanDeleteUser(conn, userDto);
		return done;
	}

	// 회원 탈퇴 -> 1이면 탈퇴 성공, 0이면 탈퇴 실패
	public int adminDeleteUser(Connection conn, UserDto userDto) {
		AdminUserDao adminUserDao = new AdminUserDao();
		int rows = adminUserDao.adminDeleteUser(conn, userDto);
		return rows;
	}

	// 회원 주문 내역 조회 행 수
	public int adminUserOrderListCount(Connection conn, UserDto userDto) {
		AdminUserDao adminUserDao = new AdminUserDao();
		int rows = adminUserDao.adminUserOrderListCount(conn, userDto);
		return rows;
	}

	// 회원 주문 내역 조회
	public ArrayList<OrderDto> adminUserOrderList(Connection conn, PagerDto pagerDto, UserDto userDto) {
		AdminUserDao adminUserDao = new AdminUserDao();
		ArrayList<OrderDto> orderList = adminUserDao.adminUserOrderList(conn, pagerDto, userDto);
		return orderList;
	}

}
