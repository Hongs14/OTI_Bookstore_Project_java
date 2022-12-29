package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import oti3.DAO.AdminBookDao;
import oti3.DTO.AuthorBookDto;
import oti3.DTO.AuthorDto;
import oti3.DTO.BookDto;
import oti3.DTO.BookHashDto;
import oti3.DTO.PagerDto;
import oti3.DTO.WarehousingDto;

public class AdminBookService {
	// 상품관리
	// 책 제목 검색의 행 수
	public int adminBookListByBookNameCount(Connection conn, BookDto bookDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		int count = adminBookDao.adminBookListByBookNameCount(conn, bookDto);
		return count;
	}

	// 출간일 순 행 수
	public int adminBookListOrderByPublishDateCount(Connection conn) {
		AdminBookDao adminBookDao = new AdminBookDao();
		int count = adminBookDao.adminBookListOrderByPublishDateCount(conn);
		return count;
	}

	// 판매량 순 행 수
	public int adminBookListBySalesCount(Connection conn) {
		AdminBookDao adminBookDao = new AdminBookDao();
		int count = adminBookDao.adminBookListBySalesCount(conn);
		return count;
	}

	// 관리자 페이지 => 책 제목 검색
	public ArrayList<BookDto> adminBookListByBookName(Connection conn, PagerDto pagerDto, BookDto bookDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		ArrayList<BookDto> list = adminBookDao.adminBookListByBookName(conn, pagerDto, bookDto);
		return list;
	}

	// 전체 책 목록 검색(출간일 순)
	public ArrayList<BookDto> adminBookListOrderByPublishDate(Connection conn, PagerDto pagerDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		ArrayList<BookDto> list = adminBookDao.adminBookListOrderByPublishDate(conn, pagerDto);
		return list;
	}

	// 전체 책 목록 검색(판매량 순 - 주문 취소건은 합산x)
	public ArrayList<BookDto> adminBookListBySales(Connection conn, PagerDto pagerDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		ArrayList<BookDto> list = adminBookDao.adminBookListBySales(conn, pagerDto);
		return list;
	}

	// 책 상세정보
	public BookDto adminBookInfo(Connection conn, BookDto bookDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		BookDto book = adminBookDao.adminBookInfo(conn, bookDto);
		return book;
	}

	// 책의 저자 정보 조회
	public ArrayList<AuthorDto> adminBookAuthorInfo(Connection conn, BookDto bookDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		ArrayList<AuthorDto> list = adminBookDao.adminBookAuthorInfo(conn, bookDto);
		return list;
	}

	// 책의 해시태그 정보 조회
	public ArrayList<BookHashDto> adminBookHashInfo(Connection conn, BookDto bookDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		ArrayList<BookHashDto> list = adminBookDao.adminBookHashInfo(conn, bookDto);
		return list;
	}

	// 책 정보수정
	public int adminBookUpdate(Connection conn, BookDto bookDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		int rows = adminBookDao.adminBookUpdate(conn, bookDto);
		return rows;
	}

	// 책에 저자 추가
	public int adminBookAuthorAdd(Connection conn, AuthorBookDto authorBookDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		int rows = adminBookDao.adminBookAuthorAdd(conn, authorBookDto);
		return rows;
	}

	// 책의 저자 삭제
	public int adminBookAuthorPop(Connection conn, AuthorBookDto authorBookDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		int rows = adminBookDao.adminBookAuthorPop(conn, authorBookDto);
		return rows;
	}

	// 책 해시태그 추가
	public int adminHashTagAdd(Connection conn, BookHashDto bookHashDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		int rows = adminBookDao.adminHashTagAdd(conn, bookHashDto);
		return rows;
	}

	// 책 해시태그 삭제
	public int adminHashTagPop(Connection conn, BookHashDto bookHashDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		int rows = adminBookDao.adminHashTagPop(conn, bookHashDto);
		return rows;
	}

	// 책 삭제 -> rows = 0이면 실패 1이면 성공
	public int adminBookDelete(Connection conn, BookDto bookDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		int rows = adminBookDao.adminBookDelete(conn, bookDto);
		return rows;
	}

	// 책 추가
	public int adminBookCreate(Connection conn, BookDto bookDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		int rows = adminBookDao.adminBookCreate(conn, bookDto);
		return rows;
	}

	// 책 재고 추가 = warehousing 행 추가 + book_store 값 + => 트랜잭션 처리
	public boolean adminWareHousingAdd(Connection conn, WarehousingDto warehousingDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		boolean done = adminBookDao.adminWareHousingAdd(conn, warehousingDto);
		return done;
	}

	// 상품 입출고 이력 행 수
	public int adminWareHistorySearchCount(Connection conn, BookDto bookDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		int rows = adminBookDao.adminWareHistorySearchCount(conn, bookDto);
		return rows;
	}

	// 상품 입출고 이력 확인(입출고 날짜 최신 순)
	public ArrayList<WarehousingDto> adminWareHistorySearch(Connection conn, BookDto bookDto, PagerDto pagerDto) {
		AdminBookDao adminBookDao = new AdminBookDao();
		ArrayList<WarehousingDto> list = adminBookDao.adminWareHistorySearch(conn, bookDto, pagerDto);
		return list;
	}

}
