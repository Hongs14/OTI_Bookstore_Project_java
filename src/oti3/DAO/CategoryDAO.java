package oti3.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oti3.DTO.AuthorDto;
import oti3.DTO.BookHashDto;
import oti3.DTO.CategoryDto;
import oti3.DTO.PagerDto;
import oti3.DTO.SearchDto;
import oti3.DTO.SubCategoryDto;

public class CategoryDAO {
	
    // 카테고리 페이지 조회
    public ArrayList<CategoryDto> selectCategory(Connection conn) {
        ArrayList<CategoryDto> CategoryList = new ArrayList<CategoryDto>();
        try {
            String sql = "" +
                    "SELECT category_no, category_name " +
                    "FROM category " +
                    "ORDER BY category_no ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
            	CategoryDto cDto = new CategoryDto();
                cDto.setCategory_no(rs.getInt("category_no"));
                cDto.setCategory_name(rs.getString("category_name"));
                CategoryList.add(cDto);
            }
            rs.close();
            pstmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return CategoryList;
    }
    
    // 카테고리 상세 페이지 조회
    public ArrayList<SubCategoryDto> selectSubCategory(Connection conn, CategoryDto categorydto) {
        ArrayList<SubCategoryDto> SubCategoryList = new ArrayList<SubCategoryDto>();
        try {
            String sql = "" +
                    "SELECT sub_no, sub_name " +
                    "FROM subcategory s, category c " +
                    "WHERE s.category_no = c.category_no " +
                    "AND c.category_no = ? " +
                    "ORDER BY c.category_no ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categorydto.getCategory_no());
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
            	SubCategoryDto sDto = new SubCategoryDto();
                sDto.setSub_no(rs.getInt("sub_no"));
                sDto.setSub_name(rs.getString("sub_name"));
                SubCategoryList.add(sDto);
            }
            rs.close();
            pstmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
               e.printStackTrace();
            }
        }
        return SubCategoryList;
    }
    
    // 카테고리 검색 행 수
    public int SelectCategoryBoardCount(Connection conn, SubCategoryDto subcategorydto) {
    	int count = 0;
    	try {
    		String sql = "SELECT count(*) as count FROM books where sub_no = ? ";
    		PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subcategorydto.getSub_no());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
            	count = rs.getInt("count");
            }
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if (conn != null) {
    				conn.close();
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    	return count;
    }
    
    // 카테고리 내 책 간이 목록
    public ArrayList<SearchDto> SelectCategoryBoard(Connection conn, SubCategoryDto subcategorydto, PagerDto pagerDto) {
        ArrayList<SearchDto> CategoryBoardlist = new ArrayList<SearchDto>();
        try {
			String sql = "SELECT ff.*"
					+ " FROM (SELECT rownum rnum, f.*"
					+ " FROM (SELECT b.book_no, b.book_name, b.book_publisher, b.book_price, avg(review_score) review_avg"
					+ " FROM books b, reviews r"
					+ " WHERE r.book_no(+) = b.book_no"
					+ " AND b.sub_no = ?"
					+ " GROUP BY b.book_no, b.book_name, b.book_publisher, b.book_price) f"
					+ " WHERE rownum <= ?) ff"
					+ " WHERE rnum >= ?";
			
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subcategorydto.getSub_no());
            pstmt.setInt(2, pagerDto.getEndRowNo());
            pstmt.setInt(3, pagerDto.getStartRowNo());
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
            	SearchDto cbDto = new SearchDto();
				cbDto.setBook_no(rs.getInt("book_no"));
				cbDto.setBook_name(rs.getString("book_name"));
				cbDto.setBook_publisher(rs.getString("book_publisher"));
				cbDto.setBook_price(rs.getInt("book_price"));
				cbDto.setReviews_avg(rs.getInt("review_avg"));
				int book_no = cbDto.getBook_no();
				
				// 이 책 번호로 등록된 저자 정보
				ArrayList<AuthorDto> authorlist = new ArrayList<AuthorDto>();
				try {
					String sql2 = "SELECT author_name " +
							"FROM books b, author_book ab, authors a " +
							"WHERE b.book_no = ab.book_no " +
							"AND a.author_no = ab.author_no " +
							"AND b.book_no = ? ";
					pstmt = conn.prepareStatement(sql2);
					pstmt.setInt(1, book_no);
					
					ResultSet rs2 = pstmt.executeQuery();
					
					while(rs2.next()) {
						AuthorDto aDto = new AuthorDto();
						aDto.setAuthor_name(rs2.getString("author_name"));
						authorlist.add(aDto);
					}
					rs2.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 이 책 번호로 등록된 해시태그 정보
				ArrayList<BookHashDto> hashlist = new ArrayList<BookHashDto>();
				try {
					String sql3 = "SELECT hash_id " +
							"FROM books b, book_hash h " +
							"WHERE b.book_no = h.book_no " +
							"AND b.book_no = ? ";
					pstmt = conn.prepareStatement(sql3);
					pstmt.setInt(1, book_no);
					
					ResultSet rs3 = pstmt.executeQuery();
					while(rs3.next()) {
						BookHashDto hDto = new BookHashDto();
						hDto.setHash_id(rs3.getString("hash_id"));
						hashlist.add(hDto);
					}
					rs3.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				cbDto.setAuthor_name(authorlist);
				cbDto.setHash_id(hashlist);
				CategoryBoardlist.add(cbDto);
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return CategoryBoardlist;
	}
}
