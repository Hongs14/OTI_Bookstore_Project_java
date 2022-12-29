package oti3.DTO;

import lombok.Data;

@Data
public class QnaDto {
	private int qna_no;
	private String qna_category;
	private String qna_title;
	private String qna_content;
	private String qna_date;
	private int qna_view;
	private String user_id;
}
