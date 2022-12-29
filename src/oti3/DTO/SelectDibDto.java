package oti3.DTO;

import java.util.ArrayList;

import lombok.Data;


@Data
public class SelectDibDto {
	private int book_no;
	private String book_name;
	private ArrayList<AuthorDto> authorList;
	private String user_id;
}

