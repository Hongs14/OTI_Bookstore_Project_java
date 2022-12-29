package oti3.Controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.ConnectionProvider;
import oti3.DTO.OrderDetailBookDto;
import oti3.DTO.OrderDto;
import oti3.DTO.PagerDto;
import oti3.Service.MyOrderService;

public class MyOrderController {
	MyOrderService myOrderService = new MyOrderService();

	public String selectOrder(JSONObject data) {
		// 주문내역 보기
		// 1)JSON to DTO
		OrderDto orderDto = new OrderDto();
		orderDto.setUser_id(data.getString("userId"));
		int count = myOrderService.selectOrderCount(ConnectionProvider.getConnection(), orderDto);
		PagerDto pagerDto = new PagerDto(5, 5, count, data.getInt("pageNo"));
		// 2)Service -> 결과
		ArrayList<OrderDto> OrderList = myOrderService.selectOrder(ConnectionProvider.getConnection(), pagerDto,
				orderDto);

		// 3)DTO to JSON
		// {"command":"","data":{{}, {},{},[{}, {}]}
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "selectOrder");
		JSONObject sendData = new JSONObject();
		JSONArray dataArr = new JSONArray();
		for (OrderDto sd : OrderList) {
			JSONObject json = new JSONObject();
			json.put("orderNo", sd.getOrder_no());
			json.put("orderDate", sd.getOrder_date());
			json.put("userId", sd.getUser_id());
			json.put("orderReceivename", sd.getOrder_receivename());
			json.put("orderTel", sd.getOrder_tel());
			json.put("orderAddress", sd.getOrder_address());
			json.put("orderMemo", sd.getOrder_memo());
			json.put("orderStatus", sd.getOrder_status() + "");
			JSONArray odlist = new JSONArray();
			for (OrderDetailBookDto sod : sd.getOrderDetails()) {
				JSONObject jo = new JSONObject();
				jo.put("orderNo", sod.getOrder_no());
				jo.put("bookNo", sod.getBook_no());
				jo.put("odQty", sod.getOd_qty());
				jo.put("bookName", sod.getBook_name());
				odlist.put(jo);
			}
			json.put("detailList", odlist);
			dataArr.put(json);
		}

		sendData.put("orderDetail", dataArr);

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

		return sendJson.toString();
	}

	public String cancelOrder(JSONObject data) {
		// 주문취소
		// 1)JSON to DTO
		OrderDto orderDto = new OrderDto();
		orderDto.setOrder_no(data.getInt("orderNo"));
		// 2)Service -> 결과
		boolean done = myOrderService.cancelOrder(ConnectionProvider.getConnection(), orderDto);
		// 3)DTO to JSON
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "cancelOrder");
		sendJson.put("data", done);
		return sendJson.toString();
	}

}
