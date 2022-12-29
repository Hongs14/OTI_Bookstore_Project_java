package oti3.Service;

import java.sql.Connection;
import java.util.ArrayList;

import oti3.DAO.BookboardDAO;
import oti3.DAO.CategoryDAO;
import oti3.DAO.IntegrationDAO;
import oti3.DTO.BoardDto;
import oti3.DTO.CategoryDto;
import oti3.DTO.DibDto;
import oti3.DTO.PagerDto;
import oti3.DTO.SearchDto;
import oti3.DTO.SubCategoryDto;

public class SearchService {
	/*
	 * // 확인 public static void main(String[] args) { SearchService s = new
	 * SearchService(); s.BookBoard(ConnectionProvider.getConnection(), 11); }
	 */

	// 클라이언트에게 보낼 mainCategory json 포맷
	public ArrayList<CategoryDto> category(Connection conn) {
		CategoryDAO cDao = new CategoryDAO();
		ArrayList<CategoryDto> list = cDao.selectCategory(conn);
		return list;
	}

	// 클라이언트에게 보낼 subCategory json 포맷
	public ArrayList<SubCategoryDto> subCategory(Connection conn, CategoryDto categoryDto) {
		CategoryDAO cDao = new CategoryDAO();
		ArrayList<SubCategoryDto> list = cDao.selectSubCategory(conn, categoryDto);
		return list;
	}

	// 카테고리 행 수
	public int categoryBoardCount(Connection conn, SubCategoryDto subCategoryDto) {
		CategoryDAO cDao = new CategoryDAO();
		int count = cDao.SelectCategoryBoardCount(conn, subCategoryDto);
		return count;
	}

	// 클라이언트에게 보낼 카테고리 간이 목록 json 포맷
	public ArrayList<SearchDto> categoryBoard(Connection conn, SubCategoryDto subCategoryDto, PagerDto pagerDto) {
		CategoryDAO cDao = new CategoryDAO();
		ArrayList<SearchDto> list = cDao.SelectCategoryBoard(conn, subCategoryDto, pagerDto);
		return list;
	}

	// 통합 검색 행수
	public int integrationCount(Connection conn, SearchDto searchDto) {
		IntegrationDAO iDao = new IntegrationDAO();
		int result = iDao.selectIntegrationCount(conn, searchDto);
		return result;
	}

	// 클라이언트에게 보낼 통합 검색 목록 json 포맷
	public ArrayList<SearchDto> integration(Connection conn, SearchDto searchDto, PagerDto pagerDto) {
		IntegrationDAO iDao = new IntegrationDAO();
		ArrayList<SearchDto> list = iDao.selectIntegration(conn, searchDto, pagerDto);
		return list;
	}

	// 책에 대한 상세검색 정보를 보낼 json 포맷
	public ArrayList<BoardDto> board(Connection conn, BoardDto boardDto) {
		BookboardDAO bDao = new BookboardDAO();
		ArrayList<BoardDto> list = bDao.selectBoard(conn, boardDto);
		return list;
	}

	// userId, bookNo 찜에 존재하는지 여부 조회
	public boolean selectCheckDibs(Connection conn, DibDto dibDto) {
		BookboardDAO bDao = new BookboardDAO();
		boolean result = bDao.selectCheckDibs(conn, dibDto);
		return result;
	}

	// 상세 페이지에서 책 찜한 후 결과 보내주기
	public int dibs(Connection conn, DibDto dibDto) {
		BookboardDAO bDao = new BookboardDAO();
		int result = bDao.insertDibs(conn, dibDto);
		return result;
	}

	public int deleteDibs(Connection conn, DibDto dibDto) {
		BookboardDAO bDao = new BookboardDAO();
		int result = bDao.deleteDibs(conn, dibDto);
		return result;
	}
}
