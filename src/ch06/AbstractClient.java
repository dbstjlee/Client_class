package ch06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class AbstractClient {

	private String name;
	private Socket socket;
	private PrintWriter socketWriter;
	private BufferedReader socketReader;
	private BufferedReader keyboardReader;

	public AbstractClient(String name) {
		this.name = name;
	}

	// 외부에서 나의 멤버 변수(socket)에 참조변수(Socket)를 주입받을 수 있도록 setter 메서드 설계
	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	public final void run() {
		try {
			connectToServer(); // 서버와 연결된 소켓 객체 생성
			setupStreams(); // I/O 입력
			startService(); // join() 걸어둔 상태 -> 끝날때까지 finally로 안 내려감
			// 여기서 안 멈추고 main 쓰레드가 돌아가면 접속이 종료되면서 자원을 닫아버림
		} catch (IOException e) {
			System.out.println(">>>>> 접속 종료 <<<<< ");
		} finally {
			cleanup(); // 소켓 자원 닫음
		}

	}

	protected abstract void connectToServer() throws IOException; 
	// 상속 받는 클래스에서 서버와 연결하기 위해 setSocket() 메서드를 통해 소켓 객체를 생성하도록 설계

	private void setupStreams() throws IOException { // run() 메서드에게 예외처리 떠넘김
		socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		socketWriter = new PrintWriter(socket.getOutputStream(), true);
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));
	}

	private void startService() throws IOException {

		Thread readThread = createReadThread(); // 데이터 읽는 스레드를 readThread에 담음
		Thread writeThread = createWriteThread(); // 데이터 출력하는 스레드를 writeThread에 담음

		// 스레드 시작
		readThread.start(); // 데이터 읽는 스레드를 시작
		writeThread.start(); // 데이터 출력하는 스레드를 시작

		// 메인 스레드 대기 처리
		try {
			readThread.join();// readThread가 다 돌아갈때까지 메인 스레드 대기?
			writeThread.join();
		} catch (InterruptedException e) {
		}
	}

	// 데이터 출력하는 스레드
	private Thread createWriteThread() {
		return new Thread(() -> {

			try {
				String msg;
				while ((msg = keyboardReader.readLine()) != null) { // 키보드 입력 값을 한 줄로 읽어라
					socketWriter.println("[ " + name + " ] : " + msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	// 데이터를 읽어들이는 스레드
	private Thread createReadThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = socketReader.readLine()) != null) {
					System.out.println("방송 옴 : " + msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void cleanup() {
		if (socket != null) {
			try {
				socket.close(); // 소켓 자원 닫기
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}