package oti3.View.adminBookManage;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import oti3.BookClient;
import oti3.DTO.BookDto;
import oti3.DTO.CategoryDto;
import oti3.DTO.SubCategoryDto;

public class AdminBookUpdateView {
	private static Scanner sc = new Scanner(System.in);
	private ArrayList<CategoryDto> categoryList;
	private ArrayList<SubCategoryDto> subcategoryList;

	public AdminBookUpdateView(BookClient bookClient, BookDto original) {
		int rows = adminBookUpdate(bookClient, original);
		if (rows == 1) {
			System.out.println("상품 수정 성공");
		} else {
			System.out.println("상품 수정 실패");
		}
	}

	public int adminBookUpdate(BookClient bookClient, BookDto original) {
		BookDto newBook = new BookDto();
		newBook.setBook_no(original.getBook_no());

		System.out.println("[제목]을 수정하시겠습니까?");
		System.out.println("| 1. 네  |  2. 아니오  |");
		System.out.println("->");
		String input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			System.out.println("변경할 제목-> ");
			String bookName = sc.nextLine();
			newBook.setBook_name(bookName);
		} else {
			newBook.setBook_name(original.getBook_name());
		}

		System.out.println("[출판사]를 수정하시겠습니까?");
		System.out.println("| 1. 네  |  2. 아니오  |");
		System.out.println("->");
		input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			System.out.println("변경할 출판사-> ");
			String bookPublisher = sc.nextLine();
			newBook.setBook_publisher(bookPublisher);
		} else {
			newBook.setBook_publisher(original.getBook_publisher());
		}

		System.out.println("[책 소개글]을 수정하시겠습니까?");
		System.out.println("| 1. 네  |  2. 아니오  |");
		System.out.println("->");
		input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			System.out.println("변경할 소개글(2000자 이하)-> ");
			String bookDetail = sc.nextLine();
			newBook.setBook_detail(bookDetail);
		} else {
			newBook.setBook_detail(original.getBook_detail());
		}

		System.out.println("[가격]을 수정하시겠습니까?");
		System.out.println("| 1. 네  |  2. 아니오  |");
		System.out.println("->");
		input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			System.out.println("변경할 가격-> ");
			String bookPrice = sc.nextLine();
			newBook.setBook_price(Integer.parseInt(bookPrice));
		} else {
			newBook.setBook_price(original.getBook_price());
		}

		System.out.println("[책 페이지 수]를 수정하시겠습니까?");
		System.out.println("| 1. 네  |  2. 아니오  |");
		System.out.println("->");
		input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			System.out.println("변경할 페이지 수-> ");
			String bookPage = sc.nextLine();
			newBook.setBook_page(Integer.parseInt(bookPage));
		} else {
			newBook.setBook_page(original.getBook_page());
		}

		System.out.println("[언어]를 수정하시겠습니까?");
		System.out.println("| 1. 네  |  2. 아니오  |");
		System.out.println("->");
		input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			System.out.println("변경할 언어-> ");
			String bookLang = sc.nextLine();
			newBook.setBook_lang(bookLang);
		} else {
			newBook.setBook_lang(original.getBook_lang());
		}

		// YYYY-MM-DD 형식 강제 - 정규표현식

		System.out.println("[출간일]를 수정하시겠습니까?");
		System.out.println("| 1. 네  |  2. 아니오  |");
		System.out.println("->");
		input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			while (true) {
				System.out.println("변경할 출간일(4자리연도-2자리월-2자리일 형식으로 입력)-> ");
				String regex = "(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])";
				String publishedDate = sc.nextLine();
				if (Pattern.matches(regex, publishedDate)) {
					newBook.setBook_date(publishedDate);
					break;
				} else {
					System.out.println("형식에 맞지 않습니다.");
				}
			}
		} else {
			newBook.setBook_date(original.getBook_date());
		}

		System.out.println("[서브 카테고리]를 수정하시겠습니까?");
		System.out.println("| 1. 네  |  2. 아니오  |");
		System.out.println("-> ");
		input = sc.nextLine();
		if (input.equals("1") || input.equals("네")) {
			int categoryNo = 0;
			int subCategoryNo = 0;
			category(bookClient);
			System.out.println("============================================================");
			for (int i = 0; i < categoryList.size(); i++) {
				System.out.print(
						categoryList.get(i).getCategory_no() + "." + categoryList.get(i).getCategory_name() + "   ");
			}
			System.out.println();
			System.out.println("============================================================");
			while (true) {
				System.out.println("메인 카테고리를 선택해 주세요 ");
				System.out.print("-> ");
				categoryNo = Integer.parseInt(sc.nextLine());
				boolean categoryFlag = false;
				for (CategoryDto categoryDto : categoryList) {
					if (categoryDto.getCategory_no() == categoryNo) {
						categoryFlag = true;
						break;
					}
				}
				if (categoryFlag) {
					break;
				} else {
					System.out.println("카테고리를 다시 선택해주세요.");
				}
			}

			subCategory(bookClient, categoryNo);
			System.out.println("============================================================");
			for (int i = 0; i < subcategoryList.size(); i++) {
				System.out
						.print(subcategoryList.get(i).getSub_no() + "." + subcategoryList.get(i).getSub_name() + "   ");
			}
			System.out.println();
			System.out.println("============================================================");
			while (true) {
				System.out.println("서브 카테고리를 선택해 주세요 ");
				System.out.print("-> ");
				subCategoryNo = Integer.parseInt(sc.nextLine());
				boolean categoryFlag = false;
				for (SubCategoryDto subCategoryDto : subcategoryList) {
					if (subCategoryDto.getSub_no() == subCategoryNo) {
						categoryFlag = true;
						break;
					}
				}
				if (categoryFlag) {
					break;
				} else {
					System.out.println("서브 카테고리를 다시 선택해주세요.");
				}
			}

			newBook.setSub_no(subCategoryNo);
		} else {
			newBook.setSub_no(original.getSub_no());
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "adminBookUpdate");
		JSONObject data = new JSONObject();
		data.put("bookNo", newBook.getBook_no());
		data.put("bookName", newBook.getBook_name());
		data.put("bookPublisher", newBook.getBook_publisher());
		data.put("bookDetail", newBook.getBook_detail());
		data.put("bookPrice", newBook.getBook_price());
		data.put("bookPage", newBook.getBook_page());
		data.put("bookLang", newBook.getBook_lang());
		data.put("bookDate", newBook.getBook_date());
		data.put("subNo", newBook.getSub_no());
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

	public void subCategory(BookClient bookClient, int category_no) {
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
