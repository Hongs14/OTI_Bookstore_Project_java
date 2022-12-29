package oti3.View.adminUserManage;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.PagerDto;
import oti3.DTO.UserDto;
import oti3.View.mainPageManage.MainPageView;

public class AdminUserPageView {
	private ArrayList<UserDto> userList;
	private static Scanner sc = new Scanner(System.in);
	private PagerDto pagerDto;

	public AdminUserPageView(BookClient bookClient) {
		int pageNo = 1;
		int searchType = 1;
		String searchId = "";
		adminUserList(bookClient, 1);
		while (true) {
			try {
				// 페이지에 해당되는 list 얻기 + list 출력
				System.out.println("------------------------------------");
				System.out.println("[회원관리]");
				System.out.println("------------------------------------");
				System.out.println("ID   |        이름        |   가입일  |   탈퇴여부   | ");

		
				for (UserDto userDto : userList) {
					System.out.print(userDto.getUser_id() + " | ");
					System.out.print(userDto.getUser_name() + " | ");
					System.out.print(userDto.getUser_date() + " | ");
					System.out.println(userDto.getUser_delete());
				}
		

				if (pagerDto.getPageNo() == 1) {
					System.out.print("[처음]");
					for (int i = pagerDto.getStartPageNo(); i <= pagerDto.getEndPageNo(); i++) {
						System.out.print("[" + i + "]");
						if (i == pagerDto.getTotalPageNo()) {
							break;
						}
					}
					System.out.print("[다음][맨끝]");
					System.out.println();
				} else if (pagerDto.getPageNo() == pagerDto.getTotalPageNo()) {
					System.out.print("[처음][이전]");
					for (int i = pagerDto.getStartPageNo(); i <= pagerDto.getEndPageNo(); i++) {
						System.out.print("[" + i + "]");
						if (i == pagerDto.getTotalPageNo()) {
							break;
						}
					}
					System.out.print("[맨끝]");
					System.out.println();
				} else {
					System.out.print("[처음][이전]");
					for (int i = pagerDto.getStartPageNo(); i <= pagerDto.getEndPageNo(); i++) {
						System.out.print("[" + i + "]");
						if (i == pagerDto.getTotalPageNo()) {
							break;
						}
					}
					System.out.print("[다음][맨끝]");
					System.out.println();
				}

				// 사용자 메뉴 선택
				System.out.println("| 1.페이지 이동 | 2.전체 회원 조회 | 3.탈퇴 요청 회원 조회 | 4.회원 ID 검색 | 5.회원정보 상세조회 | 6.뒤로 가기 | 7.홈으로 이동 |");
				System.out.println("입력 : ");
				int menu = Integer.parseInt(sc.nextLine());
				if (menu == 6)
					break;
				switch (menu) {
				case 1:
					System.out.println("보고 싶은 페이지 입력-> ");
					String answer = sc.nextLine();
					if (answer.equals("처음")) {
						pageNo = 1;
					} else if (answer.equals("이전")) {
						if (pageNo - pagerDto.getPagesPerGroup() < 1) {
							System.out.println("더 뒤로 갈 수 없습니다.");
						} else {
							pageNo = pagerDto.getStartPageNo() - pagerDto.getPagesPerGroup();
						}
					} else if (answer.equals("다음")) {
						if (pagerDto.getEndPageNo() == pagerDto.getTotalPageNo()) {
							System.out.println("다음 페이지가 없습니다.");
						} else {
							pageNo = pagerDto.getStartPageNo() + pagerDto.getPagesPerGroup();
						}
					} else if (answer.equals("맨끝")) {
						pageNo = pagerDto.getTotalPageNo();
					} else {
						int answer2 = Integer.parseInt(answer);
						if (answer2 >= pagerDto.getStartPageNo() && answer2 <= pagerDto.getEndPageNo()) {
							pageNo = answer2;
						} else {
							System.out.println("범위 안에 없는 값입니다.");
						}
					}
					if (searchType == 1) {
						adminUserList(bookClient, pageNo);
					} else if (searchType == 2) {
						adminDeleteRequestUserList(bookClient, pageNo);
					} else {
						adminUserListById(bookClient, searchId, pageNo);
					}
					break;

				case 2:
					searchType = 1;
					adminUserList(bookClient, 1);
					break;

				case 3:
					searchType = 2;
					adminDeleteRequestUserList(bookClient, 1);
					break;

				case 4:
					searchType = 3;
					System.out.println("검색 ID 입력-> ");
					searchId = sc.nextLine();
					adminUserListById(bookClient, searchId, 1);
					break;

				case 5:
					System.out.println("상세 조회할 회원 ID 입력-> ");
					String searchUserId = sc.nextLine();
					boolean find = false;
					for (UserDto userDto : userList) {
						if (userDto.getUser_id().equals(searchUserId)) {
							find = true;
						}
					}
					if (find) {
						new AdminUserReadView(bookClient, searchUserId);
					} else {
						System.out.println("없는 회원입니다.");
					}
					break;

				case 7:
					new MainPageView(bookClient);
					break;

				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("잘못 입력하셨습니다.");
			}
		}
	}

	// 유저 전체 조회
	public void adminUserList(BookClient bookClient, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminUserList");
		JSONObject data = new JSONObject();
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		userList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("userList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < list.length(); i++) {
			UserDto userDto = new UserDto();
			JSONObject jo = list.getJSONObject(i);
			userDto.setUser_id(jo.getString("userId"));
			userDto.setUser_name(jo.getString("userName"));
			userDto.setUser_date(jo.getString("userDate"));
			userDto.setUser_delete(jo.getString("userDelete").charAt(0));
			userList.add(userDto);
		}
		pagerDto = new PagerDto();
		pagerDto.setTotalRows(pager.getInt("totalRows"));
		pagerDto.setTotalPageNo(pager.getInt("totalPageNo"));
		pagerDto.setTotalGroupNo(pager.getInt("totalGroupNo"));
		pagerDto.setStartPageNo(pager.getInt("startPageNo"));
		pagerDto.setEndPageNo(pager.getInt("endPageNo"));
		pagerDto.setPageNo(pager.getInt("pageNo"));
		pagerDto.setPagesPerGroup(pager.getInt("pagesPerGroup"));
		pagerDto.setGroupNo(pager.getInt("groupNo"));
		pagerDto.setRowsPerPage(pager.getInt("rowsPerPage"));
		pagerDto.setStartRowNo(pager.getInt("startRowNo"));
		pagerDto.setStartRowIndex(pager.getInt("startRowIndex"));
		pagerDto.setEndRowNo(pager.getInt("endRowNo"));
		pagerDto.setEndRowIndex(pager.getInt("endRowIndex"));
	}

	// 탈퇴 요청 회원의 목록
	public void adminDeleteRequestUserList(BookClient bookClient, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminDeleteRequestUserList");
		JSONObject data = new JSONObject();
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		userList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("userList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < list.length(); i++) {
			UserDto userDto = new UserDto();
			JSONObject jo = list.getJSONObject(i);
			userDto.setUser_id(jo.getString("userId"));
			userDto.setUser_name(jo.getString("userName"));
			userDto.setUser_date(jo.getString("userDate"));
			userDto.setUser_delete(jo.getString("userDelete").charAt(0));
			userList.add(userDto);
		}
		pagerDto = new PagerDto();
		pagerDto.setTotalRows(pager.getInt("totalRows"));
		pagerDto.setTotalPageNo(pager.getInt("totalPageNo"));
		pagerDto.setTotalGroupNo(pager.getInt("totalGroupNo"));
		pagerDto.setStartPageNo(pager.getInt("startPageNo"));
		pagerDto.setEndPageNo(pager.getInt("endPageNo"));
		pagerDto.setPageNo(pager.getInt("pageNo"));
		pagerDto.setPagesPerGroup(pager.getInt("pagesPerGroup"));
		pagerDto.setGroupNo(pager.getInt("groupNo"));
		pagerDto.setRowsPerPage(pager.getInt("rowsPerPage"));
		pagerDto.setStartRowNo(pager.getInt("startRowNo"));
		pagerDto.setStartRowIndex(pager.getInt("startRowIndex"));
		pagerDto.setEndRowNo(pager.getInt("endRowNo"));
		pagerDto.setEndRowIndex(pager.getInt("endRowIndex"));
	}

	// 회원 id 검색
	public void adminUserListById(BookClient bookClient, String userId, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminUserListById");
		JSONObject data = new JSONObject();
		data.put("userId", userId);
		data.put("pageNo", pageNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONObject receiveData = joo.getJSONObject("data");
		userList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("userList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < list.length(); i++) {
			UserDto userDto = new UserDto();
			JSONObject jo = list.getJSONObject(i);
			userDto.setUser_id(jo.getString("userId"));
			userDto.setUser_name(jo.getString("userName"));
			userDto.setUser_date(jo.getString("userDate"));
			userDto.setUser_delete(jo.getString("userDelete").charAt(0));
			userList.add(userDto);
		}
		pagerDto = new PagerDto();
		pagerDto.setTotalRows(pager.getInt("totalRows"));
		pagerDto.setTotalPageNo(pager.getInt("totalPageNo"));
		pagerDto.setTotalGroupNo(pager.getInt("totalGroupNo"));
		pagerDto.setStartPageNo(pager.getInt("startPageNo"));
		pagerDto.setEndPageNo(pager.getInt("endPageNo"));
		pagerDto.setPageNo(pager.getInt("pageNo"));
		pagerDto.setPagesPerGroup(pager.getInt("pagesPerGroup"));
		pagerDto.setGroupNo(pager.getInt("groupNo"));
		pagerDto.setRowsPerPage(pager.getInt("rowsPerPage"));
		pagerDto.setStartRowNo(pager.getInt("startRowNo"));
		pagerDto.setStartRowIndex(pager.getInt("startRowIndex"));
		pagerDto.setEndRowNo(pager.getInt("endRowNo"));
		pagerDto.setEndRowIndex(pager.getInt("endRowIndex"));
	}
}
