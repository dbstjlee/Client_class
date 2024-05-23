package ch05_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// 2-1 상속을 활용한 구현 클래스 설계하기
public class MyClient extends AbstractClient {

	public static void main(String[] args) {
		MyClient myClient = new MyClient();
		myClient.run();
	}

	@Override
	protected void connection() throws IOException {
		super.setSocket(new Socket("localhost", 5001));
		// AbstractClient내의 setSocket을 호출하면 동시에 클라이언트의 소켓을 생성하고,
		// 소켓 내에 "localhost"와 포트 번호 입력
	}
}
