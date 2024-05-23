package ch05;

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
	protected void setupServer() throws IOException {
		super.setServerSocket(new ServerSocket(5001));
		
	}


}
