package net.marioosh.mockserver;

import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpCallback.callback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.marioosh.mockserver.callbacks.ProductCallback;

import org.apache.commons.io.IOUtils;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.BinaryBody;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
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
		InputStream productsF = MockServer.class.getClassLoader().getResourceAsStream("products-filtered.json");
		InputStream productsEan = MockServer.class.getClassLoader().getResourceAsStream("products-ean.json");
		InputStream user = MockServer.class.getClassLoader().getResourceAsStream("user.json");
		InputStream sizes = MockServer.class.getClassLoader().getResourceAsStream("sizes.json");
		InputStream colours = MockServer.class.getClassLoader().getResourceAsStream("colours.json");		
		String productsEanJson = Utils.inputStreamtoString(productsEan);		
	
		InputStream image2big = MockServer.class.getClassLoader().getResourceAsStream("image2.png");
		InputStream image2s = MockServer.class.getClassLoader().getResourceAsStream("image2s.jpg");
		ByteArrayOutputStream imageBytesBig = new ByteArrayOutputStream();
		IOUtils.copy(image2big, imageBytesBig);
		ByteArrayOutputStream imageBytesS = new ByteArrayOutputStream();
		IOUtils.copy(image2s, imageBytesS);
		
		new org.mockserver.mockserver.MockServer(port);
		MockServerClient s = new MockServerClient("localhost", port).dumpToLog();
		
		/**
		 * /
		 */
		s.when(request().withMethod("GET").withPath("/"))
		.respond(response().withStatusCode(200).withBody("obslugiwane:\n/categories\n/categories/{id}/subcategories\n/categories/{id}/sizes\n/categories/{id}/colours\n/products\n/products/{id}\n/images/{id}\n/users/{id}/banners?expand=image"));

		/**
		 * /hello1
		 */
		s.when(request().withMethod("GET").withPath("/hello1"))
		.respond(response()
				.withHeaders(header("Content-Type", "image/png"), header("Cache-Control", "max-age=10"))
				.withBody(new BinaryBody(imageBytesBig.toByteArray())));

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
		 * /categories/{id}/sizes
		 */
		s.when(request().withMethod("GET").withPath("/categories/\\d+/sizes"))
		.respond(response().withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
		.withBody(Utils.inputStreamtoString(sizes)));

		/**
		 * /categories/{id}/colours
		 */
		s.when(request().withMethod("GET").withPath("/categories/\\d+/colours"))
		.respond(response().withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
		.withBody(Utils.inputStreamtoString(colours)));
		
		/**
		 * /products?ean=...
		 */
		s.when(request().withMethod("GET").withPath("/products").withQueryStringParameter(new Parameter("ean", "\\d+")))
		.respond(response().withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
		.withBody(productsEanJson));				
		
		/**
		 * /categories/1/products
		 */
		s.when(request().withMethod("GET").withPath("/categories/\\d+/products").withQueryStringParameters(new Parameter("colors", ".*"), new Parameter("sizes", ".*")))
		.respond(response().withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
		.withBody(Utils.inputStreamtoString(productsF)));
		
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
		.callback(callback().withCallbackClass(ProductCallback.class.getName()));

		/**
		 * login
		 */
		s.when(request().withMethod("POST").withPath("/login"))
		.respond(response().withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
		.withBody(Utils.inputStreamtoString(user)));		
		
		/**
		 * /image/1
		 */
		s.when(request().withMethod("GET").withPath("/images/\\d+"))
		.respond(response()
				.withHeaders(header("Content-Type", "image/jpg"), header("Cache-Control", "max-age=60"))
				.withBody(new BinaryBody(imageBytesS.toByteArray())));
		
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
