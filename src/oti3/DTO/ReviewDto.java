package oti3.DTO;


import lombok.Data;

@Data
public class ReviewDto {
	private int review_no;
	private String book_name;
	private String review_date;
	private String review_content;
	private int review_score;
	private String user_id;
	private int book_no;
}
