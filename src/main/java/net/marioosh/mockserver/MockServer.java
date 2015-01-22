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
import org.mockserver.model.Parameter;

public class MockServer {

	public MockServer(int port) throws IOException {
		
		/**
		 * json
		 */
		InputStream categories = MockServer.class.getClassLoader().getResourceAsStream("categories.json");
		InputStream banners = MockServer.class.getClassLoader().getResourceAsStream("banners.json");
		InputStream image = MockServer.class.getClassLoader().getResourceAsStream("image.png");
		ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
		IOUtils.copy(image, imageBytes);
		
		new org.mockserver.mockserver.MockServer(port);
		MockServerClient s = new MockServerClient("localhost", port).dumpToLog();
		
		/**
		 * /
		 */
		s.when(request().withMethod("GET").withPath("/"))
		.respond(response().withStatusCode(200).withBody("Hello World"));

		/**
		 * /categories
		 */
		s.when(request().withMethod("GET").withPath("/categories"))
		.respond(response().withHeader(header("Content-type", "application/json"))
		.withBody(Utils.inputStreamtoString(categories)));
		
		/**
		 * /image/1
		 */
		s.when(request().withMethod("GET").withPath("/images/\\d+"))
		.respond(response()
				.withHeaders(header("Content-Type", "image/png"), header("Cache-Control", "max-age=10"))
				.withBody(new BinaryBody(imageBytes.toByteArray())));
		
		/**
		 * /users/1/banners?expand=image
		 */
		s.when(request().withMethod("GET").withPath("/users/\\d+/banners").withQueryStringParameter(new Parameter("expand", "image")))
		.respond(response().withHeader(header("Content-type", "application/json"))
		.withBody(Utils.inputStreamtoString(banners)));
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
