package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// 1단계 - 함수로 분리해서 리팩토링 진행
public class MultiThreadClient {

	public static void main(String[] args) {

		try {
			// ServerSocket 필요 없음
			Socket socket = new Socket("localhost", 5000);
			System.out.println("서버와 연결 완료");
			BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

			// 스레드 시작 메서드 호출
			
			// 오답1: 메인 스레드가 밑의 메서드 2개 호출시키도록 안 함.

		} catch (Exception e) {
			e.printStackTrace();
		}

	}// end of main

	// 서버로부터 데이터 읽기
	private static void startReadThread(BufferedReader bufferedReader) {

		Thread readThread = new Thread(() -> {
			try {
				String ServerMessage;
				while ((ServerMessage = bufferedReader.readLine()) != null) {
					System.out.println("서버에서 온 MSG : " + ServerMessage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}); // run() 메서드 안
		readThread.start();
		

	}

	// 서버로 데이터 보내기
	private static void startWriteThread(PrintWriter printWriter, BufferedReader KeyboardReader) {
		Thread writeThread = new Thread(() -> {
			try {
				String ClientMessage;
				while ((ClientMessage = KeyboardReader.readLine()) != null) {
					printWriter.println(); // 오답 2: 여기에 클라이언트 메시지를 출력하도록 안 함.
					printWriter.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}); // run() 메서드 안
		writeThread.start();
		
		// 오답 3: 메인 쓰레드가 기다리도록 출력.join(); 을 안 함. + try - catch
	}

}