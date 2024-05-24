package ch04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiThreadClient {

	public static void main(String[] args) {

		System.out.println("### 클라이언트 실행 ###");

		try {
			Socket socket = new Socket("192.168.0.48", 5000); // "localhost" => IP 주소값 입력하기
			System.out.println("*** connected to the Server ***");

			// 1. 서버의 데이터를 받을 입력 스트림 필요
			// 2. 서버에게 데이터를 보낼 출력 스트림 필요
			// 클라이언트 측 - 키보드 입력을 받기 위한 입력 스트림 필요
			BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

			// 서버로부터 받은 데이터를 읽는 스레드 생성
			Thread readThread = new Thread(() -> {
				// while <--
				try {
					String serverMessage;
					while ((serverMessage = socketReader.readLine()) != null) {
						// 받은 데이터를 ServerMessage에 담고 한줄씩 읽는데 null이 아닐때까지 반복
						System.out.println("서버에서 온 MSG : " + serverMessage);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			// 서버에게 데이터를 보내는 스레드
			Thread writerThread = new Thread(() -> {
				try {
					String clientMessage;
					while ((clientMessage = keyboardReader.readLine()) != null) {
						// 1. 키보드에서 데이터를 응용프로그램 안으로 입력 받아서
						// 2. 서버측 소켓과 연결되어 있는 출력 스트림을 통해 데이터를 보낸다.
						socketWriter.println(clientMessage); // 키보드에게서 받은 clientMessage를 출력
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			});

			readThread.start();
			writerThread.start();

			// join(): 하나의 스레드가 종료될 때까지 기다리도록 하는 기능
			readThread.join();
			writerThread.join();

			System.out.println("클라이언트 측 프로그램 종료");
		} catch (Exception e) {
		}
	}// end of main
} // end of class