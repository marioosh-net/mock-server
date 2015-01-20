package net.marioosh.mockserver;

import java.io.IOException;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;


public class OkHttpMockServer {

	public OkHttpMockServer() throws IOException {
		MockWebServer server = new MockWebServer();
		server.setDispatcher(new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request)
					throws InterruptedException {
				if(request.getPath().equals("/")) {
					return new MockResponse().setBody("hello, world!");
				} else {
					return new MockResponse().setResponseCode(400).setBody("400: Bad Request");
				}
			}
		});
		server.play(3000);
	}
	
	public static void main(String[] args) {
		try {
			new OkHttpMockServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
