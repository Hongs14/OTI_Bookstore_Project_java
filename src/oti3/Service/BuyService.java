package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import oti3.DAO.BuyDAO;
import oti3.DTO.BookDto;
import oti3.DTO.OrderDto;
import oti3.DTO.OrderdetailDto;
import oti3.DTO.UserDto;


public class BuyService {
    
    // 세부 페이지에서 구매를 눌렀을때 보내는 json 포맷
    public boolean buy(Connection conn, OrderDto orderDto, ArrayList<OrderdetailDto> list) {
    	BuyDAO bDao = new BuyDAO();
    	boolean result = bDao.insertBuy(conn, orderDto, list);
    	return result;
    }
    
    // 구매하기 위해 book_no 에 맞는 책 재고를 보내는 json 포맷
    public ArrayList<BookDto> bookStore(Connection conn, BookDto bookDto) {
    	BuyDAO bDao = new BuyDAO();
    	ArrayList<BookDto> list = bDao.selectBookStore(conn, bookDto);
    	return list;
    }
    
    // 구매하기 위해 user_id에 맞는 user_money 정보를 보내는 json 포맷
    public ArrayList<UserDto> userMoney(Connection conn, UserDto userDto) {
    	BuyDAO bDao = new BuyDAO();
    	ArrayList<UserDto> list = bDao.selectUserMoney(conn, userDto);
    	return list;
    }

    
    /* <buy 서비스를 요청할 때 클라이언트가 서버로 보내는 json형식>
	{"command" : "buy", "data" : {"userOrder" : {"orderNo : "주문번호값", "orderDate : "주문날짜값" .....}         
	,  "orderDetailList" : [{"orderNo : "주문번호값", "bookNo" : "책번호값", "odQty" :  "주문수량값"}, {}, {} ...]}}
    */

}
