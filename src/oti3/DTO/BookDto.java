package oti3.DTO;

import java.util.ArrayList;

import lombok.Data;

//"select book_no, book_name, book_publisher, book_price, book_date, book_store, sc.sub_name, c.category_name "

@Data
public class BookDto{
	private int book_no;
	private String book_name;
	private ArrayList<AuthorDto> authors;
	private ArrayList<BookHashDto> hashtags;
	private String book_publisher;
	private String book_detail;
	private int book_price;
	private int book_page;
	private String book_lang;
	private String book_date;
	private int book_store;
	private int sub_no;
	private String sub_name;
	private String category_name;
	private int sales;
}
