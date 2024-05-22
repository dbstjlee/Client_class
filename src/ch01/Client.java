package ch01;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

// 뭔가를 요청함
public class Client {

	public static void main(String[] args) {
		// 클라이언트 측 -- 소켓 통신을 하기 위해서 준비물
		// 1. 서버측 컴퓨터의 주소: 포트번호가 필요함.
		// 2. 서버측과 연결 될 기본 소켓이 필요함.

		// 생성자 매개변수에 서버측 (IP 주소, 포트번호)
		// "127.0.0.1" <- 자기 자신의 주소 
		// => 주소를 문자로 변환(localhost)
		try (Socket socket = new Socket("localhost", 5000)) {
			// new Socket("localhost", 5000) -> 객체 생성 시 서버측과 연결되어서
			// 스트림을 활용할 수 있다.
			// 5000은 포트 번호 -> 한번만 사용 가능하다. 

			// 클라이언트에서 정보를 보내기 때문에 OutputStream
			// 대상은 소켓이다!!
			OutputStream output = socket.getOutputStream();// 소켓에서 기반 스트림
			// 네트워크상에서 보낼때
			PrintWriter writer = new PrintWriter(output, true);// 기능 확장 - 보조 스트림
			// 기반 스트림인 output을 넣음
			writer.println("Hello, Server!"); // 데이터를 줄 단위로 한 번에 보냄
			// 서버가 데이터를 받음
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
