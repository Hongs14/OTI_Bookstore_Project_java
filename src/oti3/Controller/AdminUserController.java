package oti3.Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.OrderDetailBookDto;
import oti3.DTO.OrderDto;
import oti3.DTO.PagerDto;
import oti3.DTO.UserDto;
import oti3.Service.AdminUserService;

public class AdminUserController {
	private AdminUserService adminUserService = new AdminUserService();
	/*
	 * 회원 관리
	 */

	// 회원 전체 검색
	public String adminUserList(JSONObject data) {
		// 1) JSON TO DTO
		int count = adminUserService.adminUserListCount(ConnectionProvider.getConnection());
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<UserDto> list = adminUserService.adminUserList(ConnectionProvider.getConnection(), pagerDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminUserList");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();

		for (UserDto userDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("userId", userDto.getUser_id());
			jo.put("userName", userDto.getUser_name());
			jo.put("userDate", userDto.getUser_date());
			jo.put("userDelete", userDto.getUser_delete() + "");
			dataArr.put(jo);
		}
		sendData.put("userList", dataArr);

		JSONObject pager = new JSONObject();
		pager.put("totalRows", pagerDto.getTotalRows());
		pager.put("totalPageNo", pagerDto.getTotalPageNo());
		pager.put("totalGroupNo", pagerDto.getTotalGroupNo());
		pager.put("startPageNo", pagerDto.getStartPageNo());
		pager.put("endPageNo", pagerDto.getEndPageNo());
		pager.put("pageNo", pagerDto.getPageNo());
		pager.put("pagesPerGroup", pagerDto.getPagesPerGroup());
		pager.put("groupNo", pagerDto.getGroupNo());
		pager.put("rowsPerPage", pagerDto.getRowsPerPage());
		pager.put("startRowNo", pagerDto.getStartRowNo());
		pager.put("startRowIndex", pagerDto.getStartRowIndex());
		pager.put("endRowNo", pagerDto.getEndRowNo());
		pager.put("endRowIndex", pagerDto.getEndRowIndex());

		sendData.put("pager", pager);

		sendJson.put("data", sendData);
		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 탈퇴 요청 회원의 목록
	public String adminDeleteRequestUserList(JSONObject data) {
		// 1) JSON TO DTO
		int count = adminUserService.adminDeleteRequestUserListCount(ConnectionProvider.getConnection());
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<UserDto> list = adminUserService.adminDeleteRequestUserList(ConnectionProvider.getConnection(),
				pagerDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminDeleteRequestUserList");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();

		for (UserDto userDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("userId", userDto.getUser_id());
			jo.put("userName", userDto.getUser_name());
			jo.put("userDate", userDto.getUser_date());
			jo.put("userDelete", userDto.getUser_delete()+ "");
			dataArr.put(jo);
		}
		sendData.put("userList", dataArr);

		JSONObject pager = new JSONObject();
		pager.put("totalRows", pagerDto.getTotalRows());
		pager.put("totalPageNo", pagerDto.getTotalPageNo());
		pager.put("totalGroupNo", pagerDto.getTotalGroupNo());
		pager.put("startPageNo", pagerDto.getStartPageNo());
		pager.put("endPageNo", pagerDto.getEndPageNo());
		pager.put("pageNo", pagerDto.getPageNo());
		pager.put("pagesPerGroup", pagerDto.getPagesPerGroup());
		pager.put("groupNo", pagerDto.getGroupNo());
		pager.put("rowsPerPage", pagerDto.getRowsPerPage());
		pager.put("startRowNo", pagerDto.getStartRowNo());
		pager.put("startRowIndex", pagerDto.getStartRowIndex());
		pager.put("endRowNo", pagerDto.getEndRowNo());
		pager.put("endRowIndex", pagerDto.getEndRowIndex());

		sendData.put("pager", pager);

		sendJson.put("data", sendData);
		// 4) RETURN JSON
		return sendJson.toString();

	}

	// 회원 id 검색
	public String adminUserListById(JSONObject data) {
		// 1) JSON TO DTO
		UserDto InputuserDto = new UserDto();
		InputuserDto.setUser_id(data.getString("userId"));
		int count = adminUserService.adminUserListByIdCount(ConnectionProvider.getConnection(), InputuserDto);
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));

		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<UserDto> list = adminUserService.adminUserListById(ConnectionProvider.getConnection(), pagerDto,
				InputuserDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminUserListById");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();

		for (UserDto userDto : list) {
			JSONObject jo = new JSONObject();
			jo.put("userId", userDto.getUser_id());
			jo.put("userName", userDto.getUser_name());
			jo.put("userDate", userDto.getUser_date());
			jo.put("userDelete", userDto.getUser_delete()+ "");
			dataArr.put(jo);
		}
		sendData.put("userList", dataArr);

		JSONObject pager = new JSONObject();
		pager.put("totalRows", pagerDto.getTotalRows());
		pager.put("totalPageNo", pagerDto.getTotalPageNo());
		pager.put("totalGroupNo", pagerDto.getTotalGroupNo());
		pager.put("startPageNo", pagerDto.getStartPageNo());
		pager.put("endPageNo", pagerDto.getEndPageNo());
		pager.put("pageNo", pagerDto.getPageNo());
		pager.put("pagesPerGroup", pagerDto.getPagesPerGroup());
		pager.put("groupNo", pagerDto.getGroupNo());
		pager.put("rowsPerPage", pagerDto.getRowsPerPage());
		pager.put("startRowNo", pagerDto.getStartRowNo());
		pager.put("startRowIndex", pagerDto.getStartRowIndex());
		pager.put("endRowNo", pagerDto.getEndRowNo());
		pager.put("endRowIndex", pagerDto.getEndRowIndex());

		sendData.put("pager", pager);

		sendJson.put("data", sendData);
		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 회원 상세정보
	public String adminUserInfo(JSONObject data) {
		// 1) JSON TO DTO
		UserDto userDto = new UserDto();
		userDto.setUser_id(data.getString("userId"));

		// 2) SERVICE 호출 후 결과값 받기
		UserDto user = adminUserService.adminUserInfo(ConnectionProvider.getConnection(), userDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminUserInfo");

		JSONObject jo = new JSONObject();
		jo.put("userId", user.getUser_id());
		jo.put("userName", user.getUser_name());
		jo.put("userPassword", user.getUser_password());
		jo.put("userEmail", user.getUser_email());
		jo.put("userBirth", user.getUser_birth());
		jo.put("userGender", user.getUser_gender() + "");
		jo.put("userTel", user.getUser_tel());
		jo.put("userAddress", user.getUser_address());
		jo.put("userMoney", user.getUser_money());
		jo.put("userPoint", user.getUser_point());
		jo.put("userDate", user.getUser_date());
		jo.put("userDelete", user.getUser_delete() + "");
		jo.put("userDreqDate", user.getUser_dreq_date());

		sendJson.put("data", jo);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 회원 탈퇴 가능 여부(user_dreq_date로부터 15일 지났는지 + user_delete가 'R'인지)
	public String adminCanDeleteUser(JSONObject data) {
		// 1) JSON TO DTO
		UserDto userDto = new UserDto();
		userDto.setUser_id(data.getString("userId"));

		// 2) SERVICE 호출 후 결과값 받기
		boolean done = adminUserService.adminCanDeleteUser(ConnectionProvider.getConnection(), userDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminCanDeleteUser");
		sendJson.put("data", done);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 회원 탈퇴 -> 1이면 탈퇴 성공, 0이면 탈퇴 실패
	public String adminDeleteUser(JSONObject data) {
		// 1) JSON TO DTO
		UserDto userDto = new UserDto();
		userDto.setUser_id(data.getString("userId"));

		// 2) SERVICE 호출 후 결과값 받기
		int rows = adminUserService.adminDeleteUser(ConnectionProvider.getConnection(), userDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminDeleteUser");
		sendJson.put("data", rows);

		// 4) RETURN JSON
		return sendJson.toString();
	}

	// 회원 주문 내역 조회
	public String adminUserOrderList(JSONObject data) {
		// 1) JSON TO DTO
		UserDto userDto = new UserDto();
		userDto.setUser_id(data.getString("userId"));

		int count = adminUserService.adminUserOrderListCount(ConnectionProvider.getConnection(), userDto);
		PagerDto pagerDto = new PagerDto(10, 5, count, data.getInt("pageNo"));
		// 2) SERVICE 호출 후 결과값 받기
		ArrayList<OrderDto> list = adminUserService.adminUserOrderList(ConnectionProvider.getConnection(), pagerDto,
				userDto);

		// 3) DTO TO JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "adminUserOrderList");
		// {"data" : {"bookList" : [{},{},{}]} , "pager" : {} } }
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();

		for (OrderDto orderDto : list) {
			JSONObject jo = new JSONObject();
			JSONArray ja = new JSONArray();
			for (OrderDetailBookDto orderDetailBookDto : orderDto.getOrderDetails()) {
				JSONObject jo2 = new JSONObject();
				jo2.put("orderNo", orderDetailBookDto.getOrder_no());
				jo2.put("bookName", orderDetailBookDto.getBook_name());
				jo2.put("bookNo", orderDetailBookDto.getBook_no());
				jo2.put("odQty", orderDetailBookDto.getOd_qty());
				ja.put(jo2);
			}
			jo.put("orderDetailList", ja);
			jo.put("orderNo", orderDto.getOrder_no());
			jo.put("orderDate", orderDto.getOrder_date());
			jo.put("orderReceivename", orderDto.getOrder_receivename());
			jo.put("orderTel", orderDto.getOrder_tel());
			jo.put("orderAddress", orderDto.getOrder_address());
			jo.put("orderMemo", orderDto.getOrder_memo());
			jo.put("orderStatus", orderDto.getOrder_status() + "");
			jo.put("userId", orderDto.getUser_id());
			dataArr.put(jo);
		}
		sendData.put("orderList", dataArr);

		JSONObject pager = new JSONObject();
		pager.put("totalRows", pagerDto.getTotalRows());
		pager.put("totalPageNo", pagerDto.getTotalPageNo());
		pager.put("totalGroupNo", pagerDto.getTotalGroupNo());
		pager.put("startPageNo", pagerDto.getStartPageNo());
		pager.put("endPageNo", pagerDto.getEndPageNo());
		pager.put("pageNo", pagerDto.getPageNo());
		pager.put("pagesPerGroup", pagerDto.getPagesPerGroup());
		pager.put("groupNo", pagerDto.getGroupNo());
		pager.put("rowsPerPage", pagerDto.getRowsPerPage());
		pager.put("startRowNo", pagerDto.getStartRowNo());
		pager.put("startRowIndex", pagerDto.getStartRowIndex());
		pager.put("endRowNo", pagerDto.getEndRowNo());
		pager.put("endRowIndex", pagerDto.getEndRowIndex());

		sendData.put("pager", pager);
		sendJson.put("data", sendData);
		// 4) RETURN JSON
		return sendJson.toString();
	}
}
