package oti3.View.adminUserManage;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.OrderDetailBookDto;
import oti3.DTO.OrderDto;
import oti3.DTO.PagerDto;
import oti3.DTO.UserDto;
import oti3.View.mainPageManage.MainPageView;

public class AdminUserOrderPageView {
	private static Scanner sc = new Scanner(System.in);
	private ArrayList<OrderDto> orderList;
	private PagerDto pagerDto;

	public AdminUserOrderPageView(BookClient bookClient, UserDto userDto) {
		int pageNo = 1;
		adminUserOrderList(bookClient, userDto.getUser_id(), 1);
		while (true) {
			// 회원 정보 출력
			System.out.println("------------------------------------");
			System.out.println("[주문내역]");
			System.out.println("------------------------------------");

			for (OrderDto orderDto : orderList) {
				System.out.println("[주문정보]");
				System.out.println("[주문번호 : " + orderDto.getOrder_no() + "] ");
				System.out.println("[주문일 : " + orderDto.getOrder_date() + "] ");
				System.out.println("[주문아이디 : " + orderDto.getUser_id() + "] ");
				System.out.println("[수령인 : " + orderDto.getOrder_receivename() + "] ");
				System.out.println("[전화번호 : " + orderDto.getOrder_tel() + "] ");
				System.out.println("[배송주소 : " + orderDto.getOrder_address() + "] ");
				System.out.println("[배송메모 : " + orderDto.getOrder_memo() + "] ");
				System.out.println("[주문상태 : " + orderDto.getOrder_status() + "] ");
				System.out.println("[주문상세]");
				for (OrderDetailBookDto orderDetailBookDto : orderDto.getOrderDetails()) {
					System.out.print("[책번호 : " + orderDetailBookDto.getBook_no() + "] ");
					System.out.print("[책제목 : " + orderDetailBookDto.getBook_name() + " ] ");
					System.out.println("[구매 수량 : " + orderDetailBookDto.getOd_qty() + " ] ");
				}
				System.out.println();
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
			System.out.println("| 1.페이지 이동 | 2.뒤로가기 | 3.홈으로 이동 |");
			int menu = Integer.parseInt(sc.nextLine());
			if (menu == 2)
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
				adminUserOrderList(bookClient, userDto.getUser_id(), pageNo);
				break;

			case 3:
				new MainPageView(bookClient);
				break;
			}
		}

	}

	public void adminUserOrderList(BookClient bookClient, String userId, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminUserOrderList");
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
		orderList = new ArrayList<>();

		JSONArray list = receiveData.getJSONArray("orderList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < list.length(); i++) {
			OrderDto orderDto = new OrderDto();
			JSONObject jo = list.getJSONObject(i);
			JSONArray orderDetailList = jo.getJSONArray("orderDetailList");
			ArrayList<OrderDetailBookDto> orderDetailBookList = new ArrayList<>();
			for (int j = 0; j < orderDetailList.length(); j++) {
				OrderDetailBookDto orderDetailBookDto = new OrderDetailBookDto();
				orderDetailBookDto.setOrder_no(orderDetailList.getJSONObject(j).getInt("orderNo"));
				orderDetailBookDto.setBook_name(orderDetailList.getJSONObject(j).getString("bookName"));
				orderDetailBookDto.setBook_no(orderDetailList.getJSONObject(j).getInt("bookNo"));
				orderDetailBookDto.setOd_qty(orderDetailList.getJSONObject(j).getInt("odQty"));
				orderDetailBookList.add(orderDetailBookDto);
			}
			orderDto.setOrderDetails(orderDetailBookList);

			orderDto.setOrder_no(jo.getInt("orderNo"));
			orderDto.setOrder_date(jo.getString("orderDate"));
			orderDto.setOrder_receivename(jo.getString("orderReceivename"));
			orderDto.setOrder_tel(jo.getString("orderTel"));
			orderDto.setOrder_address(jo.getString("orderAddress"));
			orderDto.setOrder_memo(jo.getString("orderMemo"));
			orderDto.setOrder_status(jo.getString("orderStatus").charAt(0));
			orderDto.setUser_id(jo.getString("userId"));

			orderList.add(orderDto);
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
