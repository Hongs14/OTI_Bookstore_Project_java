package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import oti3.DAO.CartsDAO;
import oti3.DTO.CartBoardDto;
import oti3.DTO.CartDto;

public class CartsService {
    
    // 장바구니 목록 json 파일로 보내줌
    public ArrayList<CartBoardDto> cartsBoard(Connection conn, CartDto cartDto) {
    	CartsDAO cDao = new CartsDAO();
    	ArrayList<CartBoardDto> list = cDao.selectCartsBoard(conn, cartDto);
    	return list;
    }
    
    // 장바구니 추가 후 1 = 성공 JSON 포맷으로 전송
    public int cartsBoardPlus(Connection conn, CartDto cartdto) {
    	CartsDAO cDao = new CartsDAO();
    	int result = cDao.insertCartsBoardPlus(conn, cartdto);
    	return result;
    }
    
    // 장바구니 전체 삭제 후 1 = 성공 JSON 포맷으로 전송
    public int cartsBoardAllDelete(Connection conn, CartDto cartdto) {
    	CartsDAO cDao = new CartsDAO();
    	int result = cDao.deleteCartsBoardAllDelete(conn, cartdto);
    	return result;
    }
    
    // 장바구니 부분 삭제 후 1 = 성공 JSON 포맷으로 전송
    public int cartsBoardDelete(Connection conn, CartDto cartdto) {
    	CartsDAO cDao = new CartsDAO();
    	int result = cDao.deleteCartsBoardDelete(conn, cartdto);
    	return result;
    }
    
    // 장바구니 상품 수량 변경 후 1 = 성공 JSON 포맷 전송
    public int cartsBoardQty(Connection conn, CartDto cartdto) {
    	CartsDAO cDao = new CartsDAO();
    	int result = cDao.updateCartsBoardQty(conn, cartdto);
    	return result;
    }
    
}
