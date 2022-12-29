package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.OrderDetailBookDto;
import oti3.DTO.OrderDto;
import oti3.DTO.OrderdetailDto;
import oti3.DTO.PagerDto;


public class MyOrderDao {
	
	public ArrayList<OrderDto> selectOrder(Connection conn, PagerDto pagerDto, OrderDto order){
		//주문내역 보기 - OrderDto
		  ArrayList<OrderDto> orderList = new ArrayList<>();
		
		try {
			String sql = "select * from( "
					+ "select rownum rnum, order_no, to_char(order_date, 'yyyy-mm-dd') as order_date, order_receivename, order_tel, order_address, order_memo, order_status "
					+ "from orders where user_id = ? and rownum <= ? ) "
					+ "where rnum >= ? ";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, order.getUser_id());
			pstmt.setInt(2, pagerDto.getEndRowNo());
			pstmt.setInt(3, pagerDto.getStartPageNo());
			
			ResultSet rs = pstmt.executeQuery();
			//사용자의 order레코드 가져오기
			while(rs.next()) {
				OrderDto od = new OrderDto();
	            od.setOrder_no(rs.getInt("order_no"));
	            od.setOrder_date(rs.getString("order_date"));
	            od.setUser_id(order.getUser_id());
	            od.setOrder_receivename(rs.getString("order_receivename"));
	            od.setOrder_tel(rs.getString("order_tel"));
	            od.setOrder_address(rs.getString("order_address"));
	            od.setOrder_memo(rs.getString("order_memo"));
	            od.setOrder_status(rs.getString("order_status").charAt(0));

	            sql = "select * from orderDetails o, books b where o.book_no = b.book_no and order_no = ?";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, rs.getInt("order_no"));
	            ResultSet rss = pstmt.executeQuery();
	            ArrayList<OrderDetailBookDto> orderDetails = new ArrayList<>();
	            while(rss.next()) {
	            	OrderDetailBookDto selorderdetail = new OrderDetailBookDto();
	            	selorderdetail.setOrder_no(rss.getInt("order_no"));
	            	selorderdetail.setBook_no(rss.getInt("book_no"));
	            	selorderdetail.setOd_qty(rss.getInt("od_qty"));
	            	selorderdetail.setBook_name(rss.getString("book_name"));
	            	orderDetails.add(selorderdetail);
	            }
	            
	            od.setOrderDetails(orderDetails);
	            orderList.add(od);
			}
	
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("DB연결 오류 발생");
		} finally {
			try {
				if(conn != null) {
					System.out.println("DB연결 해제");
					conn.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return orderList ;
	}
	
	public int selectOrderCount(Connection conn, OrderDto orderdto) {
		//주문내역 count
		int count = 0;
		try {
			String sql = "select count(*) as count from orders where user_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, orderdto.getUser_id());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt("count");
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB연결 오류 발생");
		} finally {
			try {
				if (conn != null) {
					System.out.println("DB연결 해제");
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return count;
	}
	
	public boolean cancelOrder(Connection conn, OrderDto order) {
		//주문 취소
		boolean done = true;
		int rows = 0;
		try {
			conn.setAutoCommit(false);
			//1.주문취소 -> order레코드에 order_status = 'N'
			String sql = "update orders set order_status = 'N' "
					+"where order_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, order.getOrder_no());
			rows = pstmt.executeUpdate();
			if(rows == 0) {
				done = false;
				throw new Exception();
			}
			
			//2. warehousing테이블 재입고 처리
			sql = "select od.book_no, od.od_qty from orders o, orderdetails od "
				+"where o.order_no = od.order_no and o.order_no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, order.getOrder_no());
			ArrayList<OrderdetailDto> list = new ArrayList<>();
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				OrderdetailDto orderdetail = new OrderdetailDto();
				orderdetail.setBook_no(rs.getInt("book_no"));
				orderdetail.setOd_qty(rs.getInt("od_qty"));
				list.add(orderdetail);
			}
			
			for (OrderdetailDto od: list) {
			     sql = "insert into warehousing values(seq_ware_no.nextval, sysdate, ?,  'IN', ?)";
			     pstmt = conn.prepareStatement(sql);
			     pstmt.setInt(2, od.getBook_no());
			     pstmt.setInt(1, od.getOd_qty());
			     rows = 0;
			     rows = pstmt.executeUpdate();
			     if (rows == 0) {
			    	 	done = false;
			        throw new Exception();
			     }
			     
			}
			
			//3.books 테이블 book_store값 변경처리
			for(OrderdetailDto od:list) {
				sql = "update books set book_store = book_store + ? "
						+ "where book_no = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, od.getOd_qty());
				pstmt.setInt(2, od.getBook_no());
				
				int sodRow = pstmt.executeUpdate();
				if(sodRow == 0) {
					done = false;
					throw new Exception();
				}
			}
			conn.commit();
			
		} catch (Exception e) {
	         System.out.println("요청이 제대로 처리되지 않았습니다.");
	         e.printStackTrace();
	         try {
	            conn.rollback();
	         } catch (SQLException e1) {
	            System.out.println("롤백이 정상적으로 처리되지 못했습니다.");
	            e1.printStackTrace();
	         }
	      } finally {
	         try {
	            if (conn != null) {
	               conn.setAutoCommit(true);
	               conn.close();
	            }
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }
	      }
		return done;

	}
}
