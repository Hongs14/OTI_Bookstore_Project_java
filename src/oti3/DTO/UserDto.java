package oti3.DTO;

import lombok.Data;

@Data
public class UserDto {
	private String user_id;
	private String user_password;
	private String user_name;
	private String user_email;
	private String user_birth;
	private char user_gender;
	private String user_tel;
	private String user_address;
	private int user_money;
	private int user_point;
	private String user_date;
	private char user_delete;
	private String user_dreq_date;
}