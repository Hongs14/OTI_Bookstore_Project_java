package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.CartBoardDto;
import oti3.DTO.CartDto;

public class CartsDAO {
    
    // 장바구니 목록 출력
    public ArrayList<CartBoardDto> selectCartsBoard(Connection conn, CartDto cartdto) {
        ArrayList<CartBoardDto> CartsBoardlist = new ArrayList<CartBoardDto>();
        try {
            
            String sql = "" +
                    "SELECT b.book_no, book_name, book_publisher, book_price, book_store, cart_qty, (book_price * cart_qty) b_c " +
                    "FROM books b, carts c " +
                    "WHERE b.book_no = c.book_no(+) " +
                    "AND user_id = ? ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cartdto.getUser_id());
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
            	CartBoardDto cbDto = new CartBoardDto();
                cbDto.setBook_no(rs.getInt("book_no"));
                cbDto.setBook_name(rs.getString("book_name"));
                cbDto.setBook_publisher(rs.getString("book_publisher"));
                cbDto.setBook_price(rs.getInt("book_price"));
                cbDto.setBook_store(rs.getInt("book_store"));
                cbDto.setCart_qty(rs.getInt("cart_qty"));
                cbDto.setB_c(rs.getInt("b_c"));
                CartsBoardlist.add(cbDto);
            }
            rs.close();
            pstmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return CartsBoardlist;
    }
    
    // 장바구니에 추가
    public int insertCartsBoardPlus(Connection conn, CartDto cartdto) {
        int result = 0;
        try {
            String sql = "INSERT INTO carts " +
                    "VALUES (?, ?, ? ) ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cartdto.getUser_id());
            pstmt.setInt(2, cartdto.getBook_no());
            pstmt.setInt(3, cartdto.getCart_qty());
            result = pstmt.executeUpdate();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    // 장바구니 전체 삭제
    public int deleteCartsBoardAllDelete(Connection conn, CartDto cartdto) {
        int result = 0;
    	try {
            String sql = "" +
                    "DELETE FROM carts WHERE user_id = ? ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cartdto.getUser_id());
            result = pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    // 장바구니 부분 삭제
    public int deleteCartsBoardDelete(Connection conn, CartDto cartdto) {
        int result = 0;
    	try {
            String sql = "" +
                    "DELETE FROM carts WHERE user_id = ? AND book_no = ? ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cartdto.getUser_id());
            pstmt.setInt(2, cartdto.getBook_no());
            result = pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    // 장바구니 상품 수량 변경
    public int updateCartsBoardQty(Connection conn, CartDto cartdto) {
        int result = 0;
    	try {
            String sql = "" +
                    "UPDATE carts SET cart_qty = ? WHERE user_id = ? AND book_no = ? ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cartdto.getCart_qty());
            pstmt.setString(2, cartdto.getUser_id());
            pstmt.setInt(3, cartdto.getBook_no());
            result = pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
