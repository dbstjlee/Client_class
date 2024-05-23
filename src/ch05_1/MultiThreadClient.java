package ch05_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

// 1단계 - 함수로 분리해서 리팩토링 진행
public class MultiThreadClient {

	public static void main(String[] args) {
		System.out.println("### 클라이언트 실행 ###");

		// ServerSocket 필요 없음
		try (Socket socket = new Socket("localhost", 5001)) {
			System.out.println("connected to the server!!");

			// 서버와 통신을 위한 스트림 초기화
			// 메인 스레드가 밑에 3개를 생성함.
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

			// 메인 스레드가 2개 호출
			startReadThread(bufferedReader); // 워커1
			startWriteThread(printWriter, keyboardReader); //워커2
			// 메인 스레드 기다려는 어디있지? 가독성이 떨어짐. 
			// startWriteThread() <--- 내부에 있음

		} catch (Exception e) {
			e.printStackTrace();
		}

	}// end of main

	// 1. 서버로부터 데이터를 읽는 스레드 시작 메서드 생성
	private static void startReadThread(BufferedReader reader) {

		Thread readThread = new Thread(() -> {
			try {
				String msg;
				while ((msg = reader.readLine()) != null) {
					System.out.println("server에서 온 msg :" + msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		readThread.start();
	}

	// 2. 키보드에서 입력을 받아 서버측으로 데이터를 전송하는 스레드
	private static void startWriteThread(PrintWriter writer, BufferedReader keyboardReader) {

		Thread writeThread = new Thread(() -> {
			try {
				String msg;
				while ((msg = keyboardReader.readLine()) != null) {
					// 전송
					writer.println(msg);
					writer.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		writeThread.start();
		try {
			writeThread.join(); // 메인 스레드야 기다려!
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}

}