package ch05_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// 2단계 - 상속 활용 리팩토링 단계
public abstract class AbstractClient {

	private Socket socket;
	private BufferedReader readerStream;
	private PrintWriter writerStream;
	private BufferedReader keyboardReader;

	// 서버셋팅 - 포트 번호 할당
	public final void run() {
		try {
			connection(); // 연결된다는 가정하에 밑의 메서드로 넘어감.
			setupStream();
			startService();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// setter
	protected void setSocket(Socket socket) {
		this.socket = socket;
	} // 클라이어트 소켓 값을 넣어주기 위해, MyClient에서 호출할 수 있도록 setter

	protected abstract void connection() throws IOException; // 연결 확인을 위해 메서드 오버라이드하도록
	// MyClient에서 소켓(ip 주소, 포트번호) 입력할 수 있도록 setSocket 호출하고 
	// 호출과 동시에 소켓의 객체 생성 및 소켓 내의 (ip 주소, 포트번호) 입력

	// 스트림 초기화
	private void setupStream() throws IOException {
		readerStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writerStream = new PrintWriter(socket.getOutputStream(), true);
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));
	}

	// 서비스 시작
	private void startService() {
		Thread readThread = createReadThread();
		Thread writeThread = createWriteThread();
		readThread.start();
		writeThread.start();
		try {
			readThread.join();
			writeThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 캡슐화
	private Thread createReadThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = readerStream.readLine()) != null) {
					System.out.println("서버측 msg : " + msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private Thread createWriteThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = keyboardReader.readLine()) != null) {
					writerStream.println(msg);
					writerStream.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
