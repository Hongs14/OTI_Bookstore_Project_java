package oti3.View.adminBookManage;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookHashDto;
import oti3.View.mainPageManage.MainPageView;

public class AdminBookHashUpdateView {
	private static Scanner sc = new Scanner(System.in);
	private ArrayList<BookHashDto> hashList;

	public AdminBookHashUpdateView(BookClient bookClient, int bookNo) {

		while (true) {
			System.out.println("| 1.해시태그 추가 | 2.해시태그 삭제 | 3.뒤로 가기 | 4.홈으로 이동 |");
			int menu = Integer.parseInt(sc.nextLine());
			String hash = null;
			// 날 호출한 view로 돌아감
			if (menu == 3) {
				break;
			}
			switch (menu) {
			case 1:
				// 현재 책의 해시태그 정보 출력
				adminBookHashInfo(bookClient, bookNo);
				System.out
						.println("----------------------------------------------------------------------------------");
				System.out.println("[책의 해시태그]");
				System.out
						.println("----------------------------------------------------------------------------------");
				for (BookHashDto bh : hashList) {
					System.out.print(bh.getHash_id() + " ");
				}
				System.out.println();
				System.out.println("책에 추가할 해시태그를 입력해주세요.");
				System.out.println("-> ");
				hash = sc.nextLine();
				boolean find = false;
				for (BookHashDto bookHashDto : hashList) {
					if (bookHashDto.getHash_id() == hash) {
						find = true;
					}
				}
				if (find) {
					System.out.println("이미 책에 존재하는 해시태그입니다.");
				} else {
					int rows = adminHashTagAdd(bookClient, bookNo, hash);
					if (rows == 1) {
						System.out.println("해시태그 추가 성공");
					} else {
						System.out.println("해시태그 추가 실패");
					}
				}
				break;

			case 2:
				// 현재 책의 해시태그 정보 출력
				adminBookHashInfo(bookClient, bookNo);
				System.out
						.println("----------------------------------------------------------------------------------");
				System.out.println("[책의 해시태그]");
				System.out
						.println("----------------------------------------------------------------------------------");
				for (BookHashDto bh : hashList) {
					System.out.print(bh.getHash_id() + " ");
				}
				System.out.println();
				System.out.println("책에서 삭제할 해시태그를 입력해주세요.");
				System.out.println("-> ");
				hash = sc.nextLine();
				boolean find2 = false;
				for (BookHashDto bookHashDto : hashList) {
					if (bookHashDto.getHash_id() == hash) {
						find2 = true;
					}
				}
				if (!find2) {
					int rows = adminHashTagPop(bookClient, bookNo, hash);
					if (rows == 1) {
						System.out.println("해시태그 삭제 성공");
					} else {
						System.out.println("해시태그 삭제 실패");
					}
				} else {
					System.out.println("책에 해당 해시태그가 없으므로 삭제가 불가능합니다.");
				}
				break;

			case 4:
				new MainPageView(bookClient);
				break;
			}
		}
	}

	// 책 해시태그 추가
	public int adminHashTagAdd(BookClient bookClient, int bookNo, String hashId) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminHashTagAdd");
		JSONObject data = new JSONObject();
		data.put("bookNo", bookNo);
		data.put("hashId", hashId);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		return (new JSONObject(json).getInt("data"));
	}

	// 책 해시태그 삭제
	public int adminHashTagPop(BookClient bookClient, int bookNo, String hashId) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminHashTagPop");
		JSONObject data = new JSONObject();
		data.put("bookNo", bookNo);
		data.put("hashId", hashId);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		return (new JSONObject(json).getInt("data"));
	}

	// 현재 책의 저자 정보 보기
	public void adminBookHashInfo(BookClient bookClient, int bookNo) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminBookHashInfo");
		JSONObject data = new JSONObject();
		data.put("bookNo", bookNo);
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3)
		JSONObject joo = new JSONObject(json);
		hashList = new ArrayList<>();

		JSONArray list = joo.getJSONArray("data");

		for (int i = 0; i < list.length(); i++) {
			BookHashDto bookHashDto = new BookHashDto();
			JSONObject jo = list.getJSONObject(i);
			bookHashDto.setBook_no(jo.getInt("bookNo"));
			bookHashDto.setHash_id(jo.getString("hashId"));
			hashList.add(bookHashDto);
		}
	}
}
