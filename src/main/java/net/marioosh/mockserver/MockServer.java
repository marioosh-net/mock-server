package net.marioosh.mockserver;

import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.BinaryBody;

public class MockServer {

	public MockServer(int port) throws IOException {
		
		/**
		 * json
		 */
		InputStream categories = MockServer.class.getClassLoader().getResourceAsStream("categories.json");
		InputStream image = MockServer.class.getClassLoader().getResourceAsStream("image.png");
		ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
		IOUtils.copy(image, imageBytes);
		
		new org.mockserver.mockserver.MockServer(port);
		MockServerClient s = new MockServerClient("localhost", port).dumpToLog();
		
		/**
		 * root
		 */
		s.when(request().withMethod("GET").withPath("/"))
		.respond(response().withStatusCode(200).withBody("Hello World"));

		/**
		 * categories
		 */
		s.when(request().withMethod("GET").withPath("/categories"))
		.respond(response().withHeader(header("Content-type", "application/json"))
		.withBody(Utils.inputStreamtoString(categories)));
		
		/**
		 * image
		 */
		s.when(request().withMethod("GET").withPath("/image"))
		.respond(response().withHeader(header("Content-Type", "image/png")).withBody(new BinaryBody(imageBytes.toByteArray())));
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new MockServer(Integer.parseInt(args[0]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
