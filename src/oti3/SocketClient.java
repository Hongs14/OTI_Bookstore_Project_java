package oti3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.json.JSONObject;

import oti3.Controller.AdminAuthorController;
import oti3.Controller.AdminBookController;
import oti3.Controller.AdminQnaController;
import oti3.Controller.AdminUserController;
import oti3.Controller.BuyController;
import oti3.Controller.CartsController;
import oti3.Controller.MainPageController;
import oti3.Controller.MemberController;
import oti3.Controller.MyExtraController;
import oti3.Controller.MyOrderController;
import oti3.Controller.MyReviewUserController;
import oti3.Controller.QnaController;
import oti3.Controller.ReviewPlusController;
import oti3.Controller.SearchController;

public class SocketClient {
	BookServer bookServer;
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	// 연결된 고객ip
	String clientIp;

	public SocketClient(BookServer bookServer, Socket socket) {
		try {
			this.bookServer = bookServer;
			this.socket = socket;
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
			InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			this.clientIp = socketAddress.getHostName();
			receive();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	// 클라이언트 요청({"command": "", data = {}}
	public void receive() {
		bookServer.threadPool.execute(() -> {
			try {
				while (true) {
					String receiveJson = dis.readUTF();
					JSONObject jsonObject = new JSONObject(receiveJson);
					// {"command" : "글작성" , "data" : {기능수행하기위한 입력값}}
					String command = jsonObject.getString("command");
					JSONObject data = jsonObject.getJSONObject("data");

					switch (command) {

					// 장바구니 목록
					case "cartsBoard":
						send(new CartsController().cartsBoard(data));
						break;
					// 장바구니 추가
					case "cartsBoardPlus":
						send(new CartsController().cartsBoardPlus(data));
						break;

					// 장바구니 전체 삭제
					case "cartsBoardAllDelete":
						send(new CartsController().cartsBoardAllDelete(data));
						break;

					// 장바구니 부분 삭제
					case "cartsBoardDelete":
						send(new CartsController().cartsBoardDelete(data));
						break;

					// 장바구니 상품 수량 변경
					case "cartsBoardQty":
						send(new CartsController().cartsBoardQty(data));
						break;

//					 메인 카테고리
					case "category":
						send(new SearchController().category(data));
						break;

					// 서브 카테고리
					case "subCategory":
						send(new SearchController().subCategory(data));
						break;

					// 카테고리 목록
					case "categoryBoard":
						send(new SearchController().categoryBoard(data));
						break;

					// 통합 검색 목록
					case "integration":
						send(new SearchController().integration(data));
						// search 의 값은 제목, 출판사, 작가 이름을 통합적으로 검색하기 위한 String 값.
						break;

					// 책에 대한 상세 검색 정보
					case "board":
						send(new SearchController().board(data));
						break;

					case "selectCheckDibs":
						send(new SearchController().selectCheckDibs(data));
						break;

					// 책 찜한 결과 생성
					case "dibs":
						send(new SearchController().dibs(data));
						break;

					// 책 찜한 결과 삭제
					case "deleteDibs":
						send(new SearchController().deleteDibs(data));
						break;
					// 리뷰 작성
					case "reviewBoardPlus":
						send(new ReviewPlusController().reviewBoardPlus(data));
						break;

					// 리뷰가 있는지 확인 (리뷰 작성 관련)
					case "reviewCheck":
						send(new ReviewPlusController().reviewCheck(data));
						break;

					// 책 구매 한적이 있는지 확인 (리뷰 작성 관련)
					case "orderCheck":
						send(new ReviewPlusController().orderCheck(data));
						break;

					// 책 구매
					case "buy":
						send(new BuyController().buy(data));
						break;

					// 책을 구매하기 위한 책 재고 정보
					case "bookStore":
						send(new BuyController().bookStore(data));
						break;

					// 책을 구매하기 위한 유저 아이디에 맞는 잔고 정보
					case "userMoney":
						send(new BuyController().userMoney(data));
						break;
					// MyReviewUserController
					// 리뷰 보기
					case "selectReview":
						send(new MyReviewUserController().selectReview(data));
						break;
					// 리뷰 수정
					case "updateReview":
						send(new MyReviewUserController().updateReview(data));
						break;
					// 리뷰 삭제
					case "deleteReview":
						send(new MyReviewUserController().deleteReview(data));
						break;
					// 사용자 정보 수정
					case "updateUser":
						send(new MyReviewUserController().updateUser(data));
						break;
					// 사용자 탈퇴 요청
					case "leaveUser":
						send(new MyReviewUserController().leaveUser(data));
						break;

					// MyOrderController
					// 회원 주문 내역
					case "selectOrder":
						send(new MyOrderController().selectOrder(data));
						break;
					// 회원 주문 삭제
					case "cancelOrder":
						send(new MyOrderController().cancelOrder(data));
						break;

					// MyExtraController
					// 회원 돈 충전
					case "chargeMoney":
						send(new MyExtraController().chargeMoney(data));
						break;
					// 회원의 찜 목록
					case "selectDib":
						send(new MyExtraController().selectDib(data));
						break;

					// QNA목록 보기
					case "selectQna":
						send(new MyExtraController().selectQna(data));
						break;
					// Answer목록보기
					case "selectAnswer":
						send(new MyExtraController().selectAnswer(data));
						break;
					// 찜목록 삭제하기
					case "deleteDib":
						send(new MyExtraController().deleteDib(data));
						break;

					// 내가 쓴 리뷰들 책 번호 조회
					case "checkBookNumber":
						send(new MyExtraController().checkBookNumber(data));
						break;

					case "mainPageBestSellerList":
						send(new MainPageController().mainPageBestSellerList(data).toString());
						break;

					case "mainPageGenderAgeList":
						send(new MainPageController().mainPageGenderAgeList(data).toString());
						break;

					case "mainPageRecentBookList":
						send(new MainPageController().mainPageRecentBookList(data).toString());
						break;

					// 관리자 상품관리
					// {"command" : "adminBookListByBookName", "data" : {"bookName" : "책제목"} }
					case "adminBookListByBookName":
						send(new AdminBookController().adminBookListByBookName(data).toString());
						break;

					case "adminBookListOrderByPublishDate":
						send(new AdminBookController().adminBookListOrderByPublishDate(data).toString());
						break;

					case "adminBookListBySales":
						send(new AdminBookController().adminBookListBySales(data).toString());
						break;

					case "adminBookInfo":
						send(new AdminBookController().adminBookInfo(data).toString());
						break;

					case "adminBookAuthorInfo":
						send(new AdminBookController().adminBookAuthorInfo(data).toString());
						break;

					case "adminBookHashInfo":
						send(new AdminBookController().adminBookHashInfo(data).toString());
						break;

					case "adminBookUpdate":
						send(new AdminBookController().adminBookUpdate(data).toString());
						break;

					case "adminBookAuthorAdd":
						send(new AdminBookController().adminBookAuthorAdd(data).toString());
						break;

					case "adminBookAuthorPop":
						send(new AdminBookController().adminBookAuthorPop(data).toString());
						break;

					case "adminHashTagAdd":
						send(new AdminBookController().adminHashTagAdd(data).toString());
						break;

					case "adminHashTagPop":
						send(new AdminBookController().adminHashTagPop(data).toString());
						break;

					case "adminBookDelete":
						send(new AdminBookController().adminBookDelete(data).toString());
						break;

					case "adminBookCreate":
						send(new AdminBookController().adminBookCreate(data).toString());
						break;

					case "adminWareHousingAdd":
						send(new AdminBookController().adminWareHousingAdd(data).toString());
						break;

					case "adminWareHistorySearch":
						send(new AdminBookController().adminWareHistorySearch(data).toString());
						break;

					// 저자 관리
					case "adminAuthorList":
						send(new AdminAuthorController().adminAuthorList(data).toString());
						break;

					case "adminAuthorListByName":
						send(new AdminAuthorController().adminAuthorListByName(data).toString());
						break;

					case "adminAuthorInfo":
						send(new AdminAuthorController().adminAuthorInfo(data).toString());
						break;

					case "adminAuthorAdd":
						send(new AdminAuthorController().adminAuthorAdd(data).toString());
						break;

					case "adminAuthorPop":
						send(new AdminAuthorController().adminAuthorPop(data).toString());
						break;

					case "adminAuthorUpdate":
						send(new AdminAuthorController().adminAuthorUpdate(data).toString());
						break;

					// 관리자 회원관리

					case "adminUserList":
						send(new AdminUserController().adminUserList(data).toString());
						break;

					case "adminDeleteRequestUserList":
						send(new AdminUserController().adminDeleteRequestUserList(data).toString());
						break;

					case "adminUserListById":
						send(new AdminUserController().adminUserListById(data).toString());
						break;

					case "adminUserInfo":
						send(new AdminUserController().adminUserInfo(data).toString());
						break;

					case "adminCanDeleteUser":
						send(new AdminUserController().adminCanDeleteUser(data).toString());
						break;

					case "adminDeleteUser":
						send(new AdminUserController().adminDeleteUser(data).toString());
						break;

					case "adminUserOrderList":
						send(new AdminUserController().adminUserOrderList(data).toString());
						break;

					// 사용자 문의 관리
					case "adminQnaList":
						send(new AdminQnaController().adminQnaList(data).toString());
						break;

					case "adminQnaNoAnswerList":
						send(new AdminQnaController().adminQnaNoAnswerList(data).toString());
						break;

					case "adminQnaInfo":
						send(new AdminQnaController().adminQnaInfo(data).toString());
						break;

					case "adminAnswerInfo":
						send(new AdminQnaController().adminAnswerInfo(data).toString());
						break;

					case "adminAnswerCreate":
						send(new AdminQnaController().adminAnswerCreate(data).toString());
						break;

					case "adminAnswerDelete":
						send(new AdminQnaController().adminAnswerDelete(data).toString());
						break;

					// 은종
					// QNA 게시판 목록 조회
					case "selectQlist":
						send(new QnaController().selectQlist(data)); // Controller에서 toString 해줘서 여기선 안해줌
						break;

					// QNA 카테고리 목록 조회
					case "selectQcglist":
						send(new QnaController().selectQcglist(data));
						break;

					// QNA 게시판 새 글 작성
					case "insertQna":
						send(new QnaController().insertQna(data));
						break;

					// QNA 게시판 게시글 수정
					case "updateQna":
						send(new QnaController().updateQna(data));
						break;

					// QNA 게시판 게시글 삭제
					case "deleteQna":
						send(new QnaController().deleteQna(data));
						break;

					// QNA 게시판 글 조회
					case "selectQdetail":
						send(new QnaController().selectQdetail(data));
						break;

					// 게시글 수정/삭제/조회시 원글을 작성한 본인이 맞는지 확인
					case "selectQmatch":
						send(new QnaController().selectQmatch(data));
						break;

					// QNA 게시판 조회수 증가
					case "updateQviewcount":
						send(new QnaController().updateQviewcount(data));
						break;

					// 문의글 답변 읽기
					case "selectQanswer":
						send(new QnaController().selectQanswer(data));
						break;

					// 회원가입(Join)
					case "insertJoinUser":
						send(new MemberController().insertJoinUser(data));
						break;

					// 회원가입시 아이디 존재 여부 체크
					case "selectJuserId":
						send(new MemberController().selectJuserId(data));
						break;

					// 로그인
					case "selectLogin":
						send(new MemberController().selectLogin(data));
						break;

					// 아이디 찾기
					case "selectSearchId":
						send(new MemberController().selectSearchId(data));
						break;

					// 비밀번호 찾기
					case "selectSearchPw":
						send(new MemberController().selectSearchPw(data));
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void send(String json) {
		try {
			dos.writeUTF(json);
			dos.flush();
		} catch (IOException e) {
		}

	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
		}
	}
}
