package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.DAO.MyOrderDao;
import oti3.DTO.OrderDto;
import oti3.DTO.PagerDto;
import oti3.DTO.ReviewDto;

public class MyOrderService {
	MyOrderDao myOrderDao = new MyOrderDao();
	
	public ArrayList<OrderDto> selectOrder(Connection conn, PagerDto pagerDto, OrderDto sorder) {
		// 자신이 주문한 내역 보기
		ArrayList<OrderDto> orderlist = myOrderDao.selectOrder(conn, pagerDto, sorder);
		return orderlist;
	}
	
	public int selectOrderCount(Connection conn, OrderDto order) {
		int count = myOrderDao.selectOrderCount(conn, order);
		return count;
	}
		
	public boolean cancelOrder(Connection conn, OrderDto corder) {
		// 주문취소
		boolean list = myOrderDao.cancelOrder(conn, corder);
		return list;
	}

}
