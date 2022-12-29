package oti3.DTO;

import java.util.ArrayList;

import lombok.Data;

@Data
public class SearchDto {
	private String search;
    private int book_no;
    private String book_name;
    private String book_publisher;
    private int book_price;
    private int reviews_avg;
    private ArrayList<AuthorDto> author_name;
    private ArrayList<BookHashDto> hash_id;
}
