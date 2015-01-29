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
		InputStream subcategories = MockServer.class.getClassLoader().getResourceAsStream("subcategories.json");
		InputStream banners = MockServer.class.getClassLoader().getResourceAsStream("banners.json");
		InputStream products = MockServer.class.getClassLoader().getResourceAsStream("products.json");
		InputStream product = MockServer.class.getClassLoader().getResourceAsStream("product.json");
		String productJson = Utils.inputStreamtoString(product);		
		
		InputStream image = MockServer.class.getClassLoader().getResourceAsStream("image.png");
		ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
		IOUtils.copy(image, imageBytes);
		
		new org.mockserver.mockserver.MockServer(port);
		MockServerClient s = new MockServerClient("localhost", port).dumpToLog();
		
		/**
		 * /
		 */
		s.when(request().withMethod("GET").withPath("/"))
		.respond(response().withStatusCode(200).withBody("obslugiwane:\n/categories\n/categories/{id}/subcategories\n/products\n/products/{id}\n/images/{id}\n/users/{id}/banners?expand=image"));

		/**
		 * /hello1
		 */
		s.when(request().withMethod("GET").withPath("/hello1"))
		.respond(response()
				.withHeaders(header("Content-Type", "image/png"), header("Cache-Control", "max-age=10"))
				.withBody(new BinaryBody(imageBytes.toByteArray())));

		/**
		 * /hello
		 */
		s.when(request().withMethod("GET").withPath("/hello"))
		.respond(response()
 		.withHeaders(header("Content-Type", "application/json"), header("Cache-Control", "max-age=10"))
		.withBody("{\"msg\":\"Cached response, 10s\"}"));

		/**
		 * /categories
		 */
		s.when(request().withMethod("GET").withPath("/categories"))
		.respond(response().withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
		.withBody(Utils.inputStreamtoString(categories)));

		/**
		 * /categories/{id}/subcategories
		 */
		s.when(request().withMethod("GET").withPath("/categories/\\d+/subcategories"))
		.respond(response().withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
		.withBody(Utils.inputStreamtoString(subcategories)));
		
		/**
		 * /products?ean=...
		 */
		s.when(request().withMethod("GET").withPath("/products").withQueryStringParameter(new Parameter("ean", "\\d+")))
		.respond(response().withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
		.withBody(productJson));				
		
		/**
		 * /products
		 */
		s.when(request().withMethod("GET").withPath("/products"))
		.respond(response().withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
		.withBody(Utils.inputStreamtoString(products)));		

		/**
		 * /products/{id}
		 */
		s.when(request().withMethod("GET").withPath("/products/\\d+"))
		.respond(response().withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
		.withBody(productJson));		
		
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
