package oti3.DTO;

import lombok.Data;

@Data
public class SelectAnswerDto {
	private int answer_no;
	private String user_id;
	private String qna_category;
	private String answer_title;
	private String answer_content;
	private String answer_date;
}
