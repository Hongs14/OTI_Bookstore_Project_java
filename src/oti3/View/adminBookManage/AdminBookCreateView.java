package oti3.View.adminBookManage;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.CategoryDto;
import oti3.DTO.SubCategoryDto;

public class AdminBookCreateView {
	private static Scanner sc = new Scanner(System.in);
	private ArrayList<CategoryDto> categoryList;
	private ArrayList<SubCategoryDto> subcategoryList;

	public AdminBookCreateView(BookClient bookClient) {
		int rows = adminBookCreate(bookClient);
		if (rows == 1) {
			System.out.println("상품 등록 성공");
		} else {
			System.out.println("상품 등록 실패");
		}
	}

	// 책 추가
	public int adminBookCreate(BookClient bookClient) {
		// 1) 요청 json
		JSONObject data = new JSONObject();
		System.out.println("제목> ");
		data.put("bookName", sc.nextLine());

		System.out.println("출판사-> ");
		data.put("bookPublisher", sc.nextLine());

		System.out.println("소개글-> ");
		data.put("bookDetail", sc.nextLine());

		System.out.println("가격-> ");
		data.put("bookPrice", Integer.parseInt(sc.nextLine()));

		System.out.println("페이지 수-> ");
		data.put("bookPage", Integer.parseInt(sc.nextLine()));

		System.out.println("언어-> ");
		data.put("bookLang", sc.nextLine());

		// YYYY-MM-DD 형식 강제 - 정규표현식
		while (true) {
			System.out.println("출간일(4자리연도-2자리월-2자리일 형식으로 입력)> ");
			String regex = "(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])";
			String publishedDate = sc.nextLine();
			if (Pattern.matches(regex, publishedDate)) {
				data.put("bookDate", publishedDate);
				break;
			} else {
				System.out.println("형식에 맞지 않습니다.");
			}
		}

		category(bookClient);
		System.out.println("============================================================");
		for (CategoryDto categoryDTO : categoryList) {
			System.out.println(categoryDTO.getCategory_no() + "." + categoryDTO.getCategory_name());
		}
		System.out.println();
		System.out.println("============================================================");

		System.out.println("카테고리 선택-> ");
		int subC = Integer.parseInt(sc.nextLine());
		subcategory(bookClient, subC);
		System.out.println("============================================================");
		for (SubCategoryDto subCategoryDTO : subcategoryList) {
			System.out.println(subCategoryDTO.getSub_no() + "." + subCategoryDTO.getSub_name());
		}
		System.out.println();
		System.out.println("============================================================");
		System.out.println("서브 카테고리 선택-> ");
		data.put("subNo", Integer.parseInt(sc.nextLine()));
		System.out.println(data.getString("bookName"));
		System.out.println(data.getString("bookPublisher"));
		System.out.println(data.getString("bookDetail"));
		System.out.println(data.getInt("bookPrice"));
		System.out.println(data.getInt("bookPage"));
		System.out.println(data.getString("bookLang"));
		System.out.println(data.getString("bookDate"));
		System.out.println(data.getInt("subNo"));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminBookCreate");
		jsonObject.put("data", data);
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		return joo.getInt("data");
	}

	public void category(BookClient bookClient) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "category");
		jsonObject.put("data", new JSONObject());
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONArray dataArr = joo.getJSONArray("data");
		categoryList = new ArrayList<>();

		for (int i = 0; i < dataArr.length(); i++) {
			CategoryDto categoryDTO = new CategoryDto();
			JSONObject jo = dataArr.getJSONObject(i);
			categoryDTO.setCategory_no(jo.getInt("category_no"));
			categoryDTO.setCategory_name(jo.getString("category_name"));
			categoryList.add(categoryDTO);
		}
	}

	public void subcategory(BookClient bookClient, int category_no) {
		// 1) 요청 json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "subCategory");
		jsonObject.put("data", new JSONObject().put("category_no", category_no));
		bookClient.send(jsonObject.toString());

		// 2) 응답 json
		String json = bookClient.receive();

		// 3) 응답 json 파싱 후 필드에 파싱 결과값 넣기
		JSONObject joo = new JSONObject(json);
		JSONArray dataArr = joo.getJSONArray("data");
		subcategoryList = new ArrayList<>();

		for (int i = 0; i < dataArr.length(); i++) {
			SubCategoryDto subCategoryDTO = new SubCategoryDto();
			JSONObject jo = dataArr.getJSONObject(i);
			subCategoryDTO.setSub_no(jo.getInt("sub_no"));
			subCategoryDTO.setSub_name(jo.getString("sub_name"));
			subcategoryList.add(subCategoryDTO);
		}
	
	}
}
