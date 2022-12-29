package oti3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import oti3.DTO.UserDto;
import oti3.View.mainPageManage.MainPageView;

public class BookClient {
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	public UserDto loginUser = new UserDto();
	public static final String adminId = "thddudgns79";
	
	public static void main(String[] args) {
		try {
			BookClient bookClient = new BookClient();
			bookClient.connect();
			Scanner sc = new Scanner(System.in);
			System.out.println("안녕하세요. OTI서점에 오신것을 환영합니다.");
			new MainPageView(bookClient);
			sc.close();
			bookClient.unConnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void connect() throws IOException {
		// 서버 주소
		socket = new Socket("localhost", 50001);
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
	}

	public void unConnect() throws IOException {
		socket.close();
	}

	// 서버로부터 데이터 받는 쓰레드 -> 서버가 날리는 json의 command에 따라 알아서 화면에 데이터 출력
	public String receive() {
		String receiveJson = null;
		try {
			receiveJson = dis.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return receiveJson;
		
	}

	public void send(String json) {
		try {
			dos.writeUTF(json);
			dos.flush();
		} catch (IOException e) {
		}
	}

}
