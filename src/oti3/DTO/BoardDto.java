package oti3.DTO;

import java.util.ArrayList;

import lombok.Data;
@Data
public class BoardDto {
	private int book_no;
    private String book_name;
    private String book_detail;
    private String book_publisher;
    private int book_price;
    private int book_store;
    private int book_page;
    private String book_lang;
    private String book_date;
    private ArrayList<AuthorDto> author;
    private ArrayList<ReviewDto> review;
}
