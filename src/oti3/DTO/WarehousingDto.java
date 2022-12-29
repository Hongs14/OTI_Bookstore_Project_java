package oti3.DTO;


import lombok.Data;

@Data
public class WarehousingDto {
	private int ware_no;
	private int book_no;
	private String ware_date;
	private int ware_amount;
	private String ware_status;
}
