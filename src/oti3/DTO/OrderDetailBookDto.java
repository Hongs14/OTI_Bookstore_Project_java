package oti3.DTO;

import lombok.Data;

@Data
public class OrderDetailBookDto {
	private int order_no;
	private int book_no;
	private int od_qty;
	private String book_name;
}
