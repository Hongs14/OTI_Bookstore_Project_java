package oti3.View.myPageView;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.PagerDto;
import oti3.DTO.ReviewDto;
import oti3.View.mainPageManage.MainPageView;

public class MyReviewPageView {
	private Scanner sc = new Scanner(System.in);
	private ArrayList<ReviewDto> selectReview;
	private PagerDto pagerDto;

	public MyReviewPageView(BookClient bookClient) {
		int pageNo = 1;
		while (true) {

			System.out.println("----------------------------------------------------------");
			System.out.println(bookClient.loginUser.getUser_id() + "님이 작성한 리뷰");
			System.out.println("----------------------------------------------------------");
			System.out.println("No.   |    책이름    |         리뷰 내용       | 별점 |  작성날짜 ");

			try {
				selectReview(bookClient, pageNo);

				for (int i = 0; i < selectReview.size(); i++) {
					System.out.print(selectReview.get(i).getReview_no() + " | ");
					System.out.print(selectReview.get(i).getBook_name() + " | ");
					System.out.print(selectReview.get(i).getReview_content() + " | ");
					System.out.print(selectReview.get(i).getReview_score() + " | ");
					System.out.print(selectReview.get(i).getReview_date());
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

				System.out.println("|1.페이지 이동 | 2.상세 메뉴 | 3.홈으로 이동 | 4.뒤로가기 |");
				System.out.print("선택 : -> ");
				String answer = sc.nextLine();
				if (answer.equals("1")) {
					System.out.print("-> ");
					String answer2 = sc.nextLine();

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
					// 현재 페이지를 유지하기 위해
					selectReview(bookClient, pageNo);
				} else if (answer.equals("2")) {
					while (true) {
						System.out.println("| 1.리뷰 삭제 | 2.리뷰 수정 | 3.뒤로가기 | 4.홈으로 이동 |");
						System.out.print("-> ");

						int num = Integer.parseInt(sc.nextLine());
						if (num == 3) {
							break;
						}
						switch (num) {
						case 1:
							System.out.print("삭제할 리뷰의 번호를 입력하시오-> ");
							int dreviewNo = Integer.parseInt(sc.nextLine());
							// 목록에 뜬 번호만 선택하기
							boolean flag = false;
							for (ReviewDto selreview : selectReview) {
								if (selreview.getReview_no() == dreviewNo) {
									flag = true;
									break;
								}
							}
							if (flag) {
								int row = deleteReview(bookClient, dreviewNo);
								if (row == 1) {
									System.out.println("성공적으로 삭제 되었습니다.");
								} else {
									System.out.println("삭제 실패하였습니다.");
								}
							} else {
								System.out.println("페이지에 없는 리뷰입니다");
							}
							break;

						case 2:
							System.out.print("수정할 리뷰의 번호를 입력하시오-> ");
							int ureviewNo = Integer.parseInt(sc.nextLine());
							// 목록에 뜬 번호만 선택하기
							boolean flag2 = false;
							for (ReviewDto selreview : selectReview) {
								if (selreview.getReview_no() == ureviewNo) {
									flag2 = true;
									break;
								}
							}
							if (flag2) {
								int row = updateReview(bookClient, ureviewNo);
								if (row == 1) {
									System.out.println("성공적으로 수정 되었습니다.");
								} else {
									System.out.println("수정 실패하였습니다.");
								}
							} else {
								System.out.println("페이지에 없는 리뷰입니다");
							}
							break;
						case 3:
							new MyReviewPageView(bookClient);
							break;

						case 4:
							new MainPageView(bookClient);
							break;
						}

					}

				} else if (answer.equals("3")) {
					new MainPageView(bookClient);
					break;
				} else if (answer.equals("4")) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("잘못 입력하셨습니다.");
			}
		}
	}

	// select book_no from reviews where user_id = ?;
	public void selectReview(BookClient bookClient, int pageNo) {
		// 리뷰보기
		// json생성
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "selectReview");
		JSONObject data = new JSONObject();
		data.put("userId", bookClient.loginUser.getUser_id());
		data.put("pageNo", pageNo); // pageNo받아서 입력
		jsonObject.put("data", data);
		// json보내기
		bookClient.send(jsonObject.toString());
		// json받기
		String json = bookClient.receive();
		// 파싱하기
		JSONObject jo = new JSONObject(json);
		JSONObject parsedata = jo.getJSONObject("data");
		JSONArray joa = parsedata.getJSONArray("reviewList");

		selectReview = new ArrayList<>();
		for (int i = 0; i < joa.length(); i++) {
			JSONObject reviewdata = joa.getJSONObject(i);
			ReviewDto sr = new ReviewDto();
			sr.setReview_no(reviewdata.getInt("reviewNo"));
			sr.setBook_name(reviewdata.getString("bookName"));
			sr.setReview_date(reviewdata.getString("reviewDate"));
			sr.setReview_content(reviewdata.getString("reviewContent"));
			sr.setReview_score(reviewdata.getInt("reviewScore"));
			sr.setUser_id(reviewdata.getString("userId"));
			selectReview.add(sr);
		}

		JSONObject pager = parsedata.getJSONObject("pager");
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

	public int deleteReview(BookClient bookClient, int reviewNo) {
		// 리뷰 삭제하기
		// json생성
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "deleteReview");
		JSONObject data = new JSONObject();
		data.put("reviewNo", reviewNo);
		jsonObject.put("data", data);
		// json전송
		bookClient.send(jsonObject.toString());
		// json받기
		String json = bookClient.receive();
		// json파싱하기
		JSONObject jo = new JSONObject(json);
		int row = jo.getInt("data");
		return row;
	}

	public int updateReview(BookClient bookClient, int reviewNo) {
		// 리뷰 수정하기
		// json생성
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "updateReview");
		JSONObject data = new JSONObject();
		// 갱신할 내용을 담기 위한 새 객체 생성
		ReviewDto original = new ReviewDto();
		ReviewDto review = new ReviewDto();

		boolean flag = false;
		for (int i = 0; i < selectReview.size(); i++) {
			// selectReview에서 reviewNo로 객체를 찾아 original객체에 저장
			if (selectReview.get(i).getReview_no() == reviewNo) {
				original = selectReview.get(i);
			}
		}

		String input = "";
		System.out.print("[리뷰내용]을 수정하시겠습니까? ");
		System.out.println("| 1.네  | 2.아니오 |");
		input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			System.out.print("리뷰내용 입력: ");
			input = sc.nextLine();
			review.setReview_content(input);
		} else {
			review.setReview_content(original.getReview_content());
		}
		System.out.print("[별점]을 수정하시겠습니까? ");
		System.out.println("| 1.네  | 2.아니오 |");
		input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			System.out.print("수정할 별점을 입력: ");
			input = sc.nextLine();
			review.setReview_score(Integer.parseInt(input));
		} else {
			review.setReview_score(original.getReview_score());
		}

		data.put("reviewNo", reviewNo);
		data.put("reviewContent", review.getReview_content());
		data.put("reviewScore", review.getReview_score());
		jsonObject.put("data", data);
		// json전송
		bookClient.send(jsonObject.toString());
		String json = bookClient.receive();
		// 파싱
		JSONObject jo = new JSONObject(json);
		int row = jo.getInt("data");
		return row;
	}
}
