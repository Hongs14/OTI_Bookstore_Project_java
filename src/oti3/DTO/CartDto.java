package oti3.DTO;

import lombok.Data;

@Data
public class CartDto {
	private String user_id;
	private int book_no; 
	private int cart_qty;
}
