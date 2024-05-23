package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// 2단계 - 상속 활용 리팩토링 단계
public abstract class AbstractClient {

	private ServerSocket serverSocket;
	private Socket socket; 
	private BufferedReader readerStream;
	private PrintWriter writerStream;
	private BufferedReader keyboardReader;

	protected void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	// 서버셋팅 - 포트 번호 할당
	public final void run() {
		try {
			setupServer();
			setupStream();
			startService();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void setupServer() throws IOException;

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
