package oti3.View.myPageView;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;
import oti3.DTO.PagerDto;
import oti3.DTO.SelectDibDto;
import oti3.View.searchView.BookReadView;

public class MyDibPageView {
	Scanner sc = new Scanner(System.in);
	ArrayList<SelectDibDto> diblist;
	private PagerDto pagerDto;

	public MyDibPageView(BookClient bookClient) {
		while (true) {

			int pageNo = 1;
			try {
				System.out.println(bookClient.loginUser.getUser_id() + "님의 찜목록입니다.");
				
				System.out.println("----------------------------------------------");
				System.out.println("No. |           책 제목       |   저자명  ");

				selectDib(bookClient, pageNo);
				System.out.println("찜 개수 : " + diblist.size());
				for (int i = 0; i < diblist.size(); i++) {
					System.out.print(diblist.get(i).getBook_no() + " | ");
					System.out.print(diblist.get(i).getBook_name() + " | ");
					// 외 1명 표시
					System.out.print(diblist.get(i).getAuthorList().get(0).getAuthor_name() + "외 "
							+ diblist.get(i).getAuthorList().size() + "명");
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
				}

				System.out.println("| 1.페이지 이동 | 2.책 상세 보기 | 3.찜 삭제하기 | 4.홈으로 이동 | 5.뒤로 가기 |");
				System.out.print("-> ");
				String answer = sc.nextLine();
				if (answer.equals("1")) {
					System.out.print("페이지 입력-> ");
					String answer2 = sc.nextLine();
					if (answer.equals("처음")) {
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
				} else if (answer.equals("2")) {

					System.out.print("상세조회할 책번호 입력-> ");
					int bookNo = Integer.parseInt(sc.nextLine());
					Boolean flag = false;
					for (int i = 0; i < diblist.size(); i++) {
						// 리뷰의 번호가 DB에 있는 번호인지 유효성 검사
						if (diblist.get(i).getBook_no() == bookNo) {
							flag = true;
						}
					}
					if (flag == true) {
						new BookReadView(bookClient, bookNo);
					} else {
						System.out.println("번호를 다시 입력해주세요.");
					}

				} else if (answer.equals("3")) {
					//찜 삭제 기능
					System.out.print("삭제할 책 번호 입력-> ");
					int delbookno = Integer.parseInt(sc.nextLine());
					// 목록에 뜬 번호만 선택하기
					boolean flag = false;
					for (SelectDibDto selectdib : diblist) {
						if (selectdib.getBook_no() == delbookno) {
							flag = true;
							break;
						}
					}
				
					if (flag) {
						int row = deleteDib(bookClient, delbookno);
						if (row == 1) {
							System.out.println("성공적으로 삭제 되었습니다.");
						} else {
							System.out.println("삭제 실패하였습니다.");
						}
					} else {
						System.out.println("페이지에 없는 책입니다");
					}

				} else if (answer.equals("4")) {
					new MyPageView(bookClient);
				}
				else if(answer.equals("5")) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("잘못 입력하셨습니다.");
			}
		}
	}

	public void selectDib(BookClient bookClient, int pageNo) {
		// 찜 목록보기
		// JSON만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "selectDib");
		JSONObject data = new JSONObject();
		data.put("userId", bookClient.loginUser.getUser_id());
		data.put("pageNo", pageNo); // pageNo받아서 입력
		sendObject.put("data", data);

		// JSON보내기 & 받기
		bookClient.send(sendObject.toString());
		String json = bookClient.receive();
		// JSON파싱
		JSONObject jo = new JSONObject(json);
		JSONObject joo = jo.getJSONObject("data");

		JSONArray joa = joo.getJSONArray("diblist");
		
		diblist = new ArrayList();
		for (int i = 0; i < joa.length(); i++) {
			JSONObject joodata = joa.getJSONObject(i);
			SelectDibDto selectDib = new SelectDibDto();
			selectDib.setBook_no(joodata.getInt("bookNo"));
			selectDib.setBook_name(joodata.getString("bookName"));
			JSONArray joalist = joodata.getJSONArray("authorList");
			ArrayList<AuthorDto> authorList = new ArrayList<>();
			for (int j = 0; j < joalist.length(); j++) {
				JSONObject authorJo = joalist.getJSONObject(j);
				AuthorDto author = new AuthorDto();
				author.setAuthor_no(authorJo.getInt("authorNo"));
				author.setAuthor_name(authorJo.getString("authorName"));
				authorList.add(author);
			}

			selectDib.setAuthorList(authorList);
			diblist.add(selectDib);
		}

		JSONObject pager = joo.getJSONObject("pager");
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

	public int deleteDib(BookClient bookClient, int bookNo) {
		// 찜 삭제하기
		// JSON만들기
		JSONObject sendObject = new JSONObject();
		sendObject.put("command", "deleteDib");

		JSONObject data = new JSONObject();
		data.put("userId", bookClient.loginUser.getUser_id());
		data.put("bookNo", bookNo);
		sendObject.put("data", data);
		// JSON전송
		bookClient.send(sendObject.toString());
		// JSON받기
		String json = bookClient.receive();

		// json파싱하기
		JSONObject jo = new JSONObject(json);
		int row = jo.getInt("data");
		return row;
	}
}
