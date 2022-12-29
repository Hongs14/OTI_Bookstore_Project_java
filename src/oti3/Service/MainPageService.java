package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import oti3.DAO.MainPageDao;
import oti3.DTO.UserDto;
import oti3.DTO.BookDto;

public class MainPageService {
	// 금주 bestSeller top-5
	public ArrayList<BookDto> mainPageBestSellerList(Connection conn) {
		MainPageDao mainPageDao = new MainPageDao();
		return mainPageDao.mainPageBestSellerList(conn);
	}

	// 로그인한 사용자 나이대, 성별에서 가장 많이 팔린 top-5
	public ArrayList<BookDto> mainPageGenderAgeList(Connection conn, UserDto userDto) {
		MainPageDao mainPageDao = new MainPageDao();
		return mainPageDao.mainPageGenderAgeList(conn, userDto);
	}

	// 신간도서 bestSeller top-5
	public ArrayList<BookDto> mainPageRecentBookList(Connection conn) {
		MainPageDao mainPageDao = new MainPageDao();
		return mainPageDao.mainPageRecentBookList(conn);
	}
}
