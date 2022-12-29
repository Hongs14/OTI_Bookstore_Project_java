package oti3.View.searchView;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookHashDto;
import oti3.DTO.PagerDto;
import oti3.DTO.SearchDto;

public class IntegrationPageView {

	Scanner scanner = new Scanner(System.in);
	ArrayList<SearchDto> pagelist;
	PagerDto pagerDto;

	public IntegrationPageView(BookClient bookClient) {

		System.out.println("검색어를 입력해 주세요. ");
		System.out.print("-> ");
		String keyword = scanner.nextLine();
		IntegrationPage(bookClient, keyword, 1);
		int pageNo = 1;
		while (true) {
			try {
				System.out.println("------------------------------------");
				System.out.println("[검색 결과]");
				System.out.println("------------------------------------");
				System.out.println("No.   |       책제목        |   출판사  |   가격   | 평균별점 | 작가 | 해시태그");

				for (int i = 0; i < pagelist.size(); i++) {
					System.out.print(pagelist.get(i).getBook_no() + " | ");
					System.out.print(pagelist.get(i).getBook_name() + " | ");
					System.out.print(pagelist.get(i).getBook_publisher() + " | ");
					System.out.print(pagelist.get(i).getBook_price() + " | ");
					for (int j = 0; j < pagelist.get(i).getReviews_avg(); j++) {
						System.out.print("*");
					}
					for (int j = 0; j < pagelist.get(i).getAuthor_name().size(); j++) {
						if (j == pagelist.get(i).getAuthor_name().size() - 1) {
							System.out.print(pagelist.get(i).getAuthor_name().get(j).getAuthor_name() + " ");
						} else {
							System.out.print(pagelist.get(i).getAuthor_name().get(j).getAuthor_name() + ", ");
						}
					}
					for (int j = 0; j < pagelist.get(i).getHash_id().size(); j++) {
						if (j == pagelist.get(i).getHash_id().size() - 1) {
							System.out.print(pagelist.get(i).getHash_id().get(j).getHash_id() + " ");
						} else {
							System.out.print(pagelist.get(i).getHash_id().get(j).getHash_id() + ", ");
						}
					}
					System.out.println();
				}


				if (pagerDto.getPageNo() == 1) {
					System.out.print("[처음]");
					for (int i = pagerDto.getStartPageNo(); i <= pagerDto.getEndPageNo(); i++) {
						System.out.print("[" + i + "]");
						if (i == pagerDto.getTotalPageNo()) {
							break;
						}
					}
					System.out.print("[다음][맨끝]");
					System.out.println();
				} else if (pagerDto.getPageNo() == pagerDto.getTotalPageNo()) {
					System.out.print("[처음][이전]");
					for (int i = pagerDto.getStartPageNo(); i <= pagerDto.getEndPageNo(); i++) {
						System.out.print("[" + i + "]");
						if (i == pagerDto.getTotalPageNo()) {
							break;
						}
					}
					System.out.print("[맨끝]");
					System.out.println();
				} else {
					System.out.print("[처음][이전]");
					for (int i = pagerDto.getStartPageNo(); i <= pagerDto.getEndPageNo(); i++) {
						System.out.print("[" + i + "]");
						if (i == pagerDto.getTotalPageNo()) {
							break;
						}
					}
					System.out.print("[다음][맨끝]");
					System.out.println();
				}
				System.out.println("| 1. 페이지 이동 | 2. 상세 조회 | 3. 홈으로 이동 |");
				System.out.print("-> ");
				String answer = scanner.nextLine();
				if (answer.equals("1")) {
					System.out.print("페이지 입력-> ");
					String answer2 = scanner.nextLine();
					if (answer2.equals("처음")) {
						pageNo = 1;
					} else if (answer2.equals("이전")) {
						if (pageNo - pagerDto.getPagesPerGroup() < 1) {
							System.out.println("더 뒤로 갈 수 없습니다.");
						} else {
							pageNo = pagerDto.getStartPageNo() - pagerDto.getPagesPerGroup();
						}
					} else if (answer2.equals("다음")) {
						if (pagerDto.getEndPageNo() == pagerDto.getTotalPageNo()) {
							System.out.println("다음 페이지가 없습니다.");
						} else {
							pageNo = pagerDto.getStartPageNo() + pagerDto.getPagesPerGroup();
						}
					} else if (answer2.equals("맨끝")) {
						pageNo = pagerDto.getTotalPageNo();
					} else {
						int answer3 = Integer.parseInt(answer2);
						if (answer3 >= pagerDto.getStartPageNo() && answer3 <= pagerDto.getEndPageNo()) {
							pageNo = answer3;
						} else {
							System.out.println("범위 안에 없는 값입니다.");
						}
					}
					IntegrationPage(bookClient, keyword, pageNo);
				} else if (answer.equals("2")) {
					System.out.println("책 번호를 입력해주세요. ");
					System.out.print("-> ");
					int book_no = Integer.parseInt(scanner.nextLine());
					boolean find = false;
					for (SearchDto searchDto : pagelist) {
						if (searchDto.getBook_no() == book_no) {
							find = true;
							break;
						}
					}
					if (find) {
						new BookReadView(bookClient, book_no);
					} else {
						System.out.println("현재 보고 있는 페이지에 해당 책은 없습니다.");
					}

				} else if (answer.equals("3")) {
					break;
				}
			} catch (Exception e) {
				System.out.println("잘못 입력하셨습니다.");
			}
		}
	}

	public void IntegrationPage(BookClient bookClient, String keyword, int pageNo) {
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", "integration");
		JSONObject data = new JSONObject();
		data.put("search", keyword);
		data.put("pageNo", pageNo);
		sendJson.put("data", data);
		bookClient.send(sendJson.toString());

		// 응답
		String receiveJson = bookClient.receive();

		// 응답 json 파싱 후 필드에 넣기
		JSONObject jo = new JSONObject(receiveJson);
		JSONObject receiveData = jo.getJSONObject("data");
		pagelist = new ArrayList<>();

		JSONArray jarr = receiveData.getJSONArray("bookList");
		JSONObject pager = receiveData.getJSONObject("pager");

		for (int i = 0; i < jarr.length(); i++) {
			SearchDto sd = new SearchDto();
			JSONObject jo2 = jarr.getJSONObject(i);
			sd.setBook_no(jo2.getInt("book_no"));
			sd.setBook_name(jo2.getString("book_name"));
			sd.setBook_publisher(jo2.getString("book_publisher"));
			sd.setBook_price(jo2.getInt("book_price"));
			sd.setReviews_avg(jo2.getInt("review_avg"));

			JSONArray ja = jo2.getJSONArray("author_name");
			ArrayList<AuthorDto> alist = new ArrayList<AuthorDto>();

			for (int j = 0; j < ja.length(); j++) {
				AuthorDto ad = new AuthorDto();
				ad.setAuthor_name(ja.getJSONObject(j).getString("author_name"));
				alist.add(ad);
			}
			sd.setAuthor_name(alist);

			JSONArray ja2 = jo2.getJSONArray("hash_id");
			ArrayList<BookHashDto> hlist = new ArrayList<BookHashDto>();
			for (int j = 0; j < ja2.length(); j++) {
				BookHashDto bhd = new BookHashDto();
				bhd.setHash_id(ja2.getJSONObject(j).getString("hash_id"));
				hlist.add(bhd);
			}
			sd.setHash_id(hlist);
			pagelist.add(sd);
		}
		pagerDto = new PagerDto();

		pagerDto.setTotalRows(pager.getInt("totalRows"));
		pagerDto.setTotalPageNo(pager.getInt("totalPageNo"));
		pagerDto.setTotalGroupNo(pager.getInt("totalGroupNo"));
		pagerDto.setStartPageNo(pager.getInt("startPageNo"));
		pagerDto.setEndPageNo(pager.getInt("endPageNo"));
		pagerDto.setPageNo(pager.getInt("pageNo"));
		pagerDto.setPagesPerGroup(pager.getInt("pagesPerGroup"));
		pagerDto.setGroupNo(pager.getInt("groupNo"));
		pagerDto.setRowsPerPage(pager.getInt("rowsPerPage"));
		pagerDto.setStartRowNo(pager.getInt("startRowNo"));
		pagerDto.setStartRowIndex(pager.getInt("startRowIndex"));
		pagerDto.setEndRowNo(pager.getInt("endRowNo"));
		pagerDto.setEndRowIndex(pager.getInt("endRowIndex"));
	}
}
