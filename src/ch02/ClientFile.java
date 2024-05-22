package ch02;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFile {

	public static void main(String[] args) {

		// 클라이언트 측 준비물
		// 1. 서버측 IP 주소와 포트 번호가 필요하다.
		// 2. 서버측 소켓과 연결된 소켓이 필요하다.

		Socket socket = null;
		try {
			socket = new Socket("localhost", 5001); // localhost에는 서버측 IP 주소 입력
			// 서버측 소켓과 연결된 소켓
			// PrintWriter는 autoflush 가능 , 데이터를 출력함.
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // true = auto flush
			writer.print("안녕 반가워~"); // PrintWriter가 print() 메서드 제공.
			// 줄바꿈 처리
//			writer.print("안녕 반가워~\n");
//			writer.println("안녕 반가워~"); => 자동으로 버퍼 플러시 -> 데이터 즉시 전송
			writer.flush(); // -> 강제로 데이터 출력

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
