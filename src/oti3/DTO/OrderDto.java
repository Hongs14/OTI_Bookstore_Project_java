package oti3.DTO;

import java.util.ArrayList;

import lombok.Data;

@Data
public class OrderDto {
	private int order_no;
	private String order_date;
	private String user_id;
	private String order_receivename;
	private String order_tel;
	private String order_address;
	private String order_memo;
	private ArrayList<OrderDetailBookDto> orderDetails;
	private char order_status;
}
