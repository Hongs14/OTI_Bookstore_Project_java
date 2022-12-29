package oti3.View.myPageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.OrderDetailBookDto;
import oti3.DTO.OrderDto;
import oti3.DTO.PagerDto;
import oti3.View.mainPageManage.MainPageView;

public class MyOrderPageView {
	private Scanner sc = new Scanner(System.in);
	private ArrayList<OrderDto> orders;
	private ArrayList<OrderDetailBookDto> orderDetails;
	private PagerDto pagerDto;

	public MyOrderPageView(BookClient bookClient) {
		int pageNo = 1;
		while (true) {
			try {
				selectOrder(bookClient, pageNo);
				System.out.println("------------------------------------");
				System.out.println("[" + bookClient.loginUser.getUser_id() + "님의 주문내역]");
				System.out.println("------------------------------------");

				for (OrderDto orderDto : orders) {
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

				}

				System.out.println("| 1.페이지 이동 | 2.상세 메뉴 | 3.홈으로이동 | 4.뒤로 가기 |");
				System.out.print("-> ");
				String answer = sc.nextLine();
				if (answer.equals("1")) {
					System.out.print("-> ");
					String answer2 = sc.nextLine();
					if (answer.equals("처음")) {
						pageNo = 1;
					} else if (answer2.equals("이전")) {
						if (pageNo - pagerDto.getPagesPerGroup() < 1) {
							System.out.println("더 뒤로 갈 수 없습니다.");
						} else {
							pageNo = pagerDto.getStartPageNo() - pagerDto.getPagesPerGroup();
						}
					} else if (answer2.equals("다음")) {
						if (pagerDto.getEndPageNo() == pagerDto.getTotalPageNo()) {
							System.out.println("다음 페이지가 없습니다.");
						} else {
							pageNo = pagerDto.getStartPageNo() + pagerDto.getPagesPerGroup();
						}
					} else if (answer2.equals("맨끝")) {
						pageNo = pagerDto.getTotalPageNo();
					} else {
						int answer3 = Integer.parseInt(answer2);
						if (answer3 >= pagerDto.getStartPageNo() && answer3 <= pagerDto.getEndPageNo()) {
							pageNo = answer3;
						} else {
							System.out.println("범위 안에 없는 값입니다.");
						}
					}
				} else if (answer.equals("2")) {
					while (true) {
						System.out.println("| 1.주문 취소하기 | 2.리뷰 작성하기 | 3.뒤로가기 | 4.홈으로 이동 |");
						System.out.print("->");
						String num = sc.nextLine();
						if (num.equals("3")) {
							break;
						}
						switch (num) {

						case "1":
							System.out.print("취소처리 할 주문번호를 입력하시오-> ");
							int orderdel = Integer.parseInt(sc.nextLine());
							// 목록에 뜬 번호만 선택하기
							boolean flag = false;
							for (OrderDto orlist : orders) {
								if (orlist.getOrder_no() == orderdel) {
									flag = true;
									break;
								}
							}
							if (flag) {
								boolean row = cancelOrder(bookClient, orderdel);
								if (row == true) {
									System.out.println("성공적으로 취소 되었습니다.");
								} else {
									System.out.println("취소 실패하였습니다.");
								}
							} else {
								System.out.println("페이지에 없는 번호입니다");
							}

							break;

						case "2":
							System.out.println("리뷰를 작성할 책의 번호를 입력해주세요-> ");

							int bookNo = Integer.parseInt(sc.nextLine());
							// 목록에 뜬 번호만 선택하기
							Set<Integer> bookNoSet = new HashSet<>();

							boolean flag1 = false;
							for (OrderDto orderDto : orders) {
								for (OrderDetailBookDto orderDetailBookDto : orderDto.getOrderDetails()) {
									bookNoSet.add(orderDetailBookDto.getBook_no());
								}
							}
							for (int no : bookNoSet) {
								if (no == bookNo) {
									flag1 = true;
									break;
								}
							}

							if (flag1) {
								new ReviewCreateView(bookClient, bookNo);
							} else {
								System.out.println("페이지에 없는 번호입니다");
							}
							break;

						case "4":
							new MainPageView(bookClient);
							break;
						}
						

					}

				} else if (answer.equals("3")) {
					new MainPageView(bookClient);
				}
				else if(answer.equals("4")) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("잘못 입력하셨습니다.");
			}
		}
	}

	public void selectOrder(BookClient bookClient, int pageNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "selectOrder");
		JSONObject data = new JSONObject();
		data.put("userId", bookClient.loginUser.getUser_id());
		data.put("pageNo", pageNo); // pageNo받아서 입력
		jsonObject.put("data", data);
		// json보내기
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		// [{"bookNo" : 값, "bookName" : 값}, {}, {}]

		// {"data" : [{"orderDetailList" : [{}, {}, {}]}, {}, {}]}
		JSONObject jo = new JSONObject(json);
		JSONObject parseData = jo.getJSONObject("data");
		JSONArray dataArr = parseData.getJSONArray("orderDetail");
		orders = new ArrayList<>();
		for (int i = 0; i < dataArr.length(); i++) {
			JSONObject joo = dataArr.getJSONObject(i);
			OrderDto orderDto = new OrderDto();
			orderDto.setOrder_no(joo.getInt("orderNo"));
			orderDto.setOrder_date(joo.getString("orderDate"));
			orderDto.setUser_id(joo.getString("userId"));
			orderDto.setOrder_receivename(joo.getString("orderReceivename"));
			orderDto.setOrder_tel(joo.getString("orderTel"));
			orderDto.setOrder_address(joo.getString("orderAddress"));
			orderDto.setOrder_memo(joo.getString("orderMemo"));
			orderDto.setOrder_status(joo.getString("orderStatus").charAt(0));
			JSONArray orderDetailList = joo.getJSONArray("detailList");
			orderDetails = new ArrayList<>();
			for (int j = 0; j < orderDetailList.length(); j++) {
				OrderDetailBookDto orderDetailBookDto = new OrderDetailBookDto();
				JSONObject od = orderDetailList.getJSONObject(j);
				orderDetailBookDto.setOrder_no(od.getInt("orderNo"));
				orderDetailBookDto.setBook_name(od.getString("bookName"));
				orderDetailBookDto.setBook_no(od.getInt("bookNo"));
				orderDetailBookDto.setOd_qty(od.getInt("odQty"));
				orderDetails.add(orderDetailBookDto);
			}
			orderDto.setOrderDetails(orderDetails);
			orders.add(orderDto);
		}
		JSONObject pager = parseData.getJSONObject("pager");
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

	public boolean cancelOrder(BookClient bookClient, int orderNo) {
		// 리뷰 삭제하기
		// json생성
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "cancelOrder");
		JSONObject data = new JSONObject();
		data.put("orderNo", orderNo);
		jsonObject.put("data", data);
		// json전송
		bookClient.send(jsonObject.toString());
		// json받기
		String json = bookClient.receive();
		// json파싱하기
		JSONObject jo = new JSONObject(json);
		boolean row = jo.getBoolean("data");
		return row;
	}
}
