package net.marioosh.mockserver.callbacks;

import static org.mockserver.model.Header.header;
import net.marioosh.mockserver.MockServer;
import net.marioosh.mockserver.Utils;

import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

public class ProductCallback implements org.mockserver.mock.action.ExpectationCallback {
	String productJson = Utils.inputStreamtoString(MockServer.class.getClassLoader().getResourceAsStream("product.json"));
	
	public ProductCallback() {
		// TODO Auto-generated constructor stub
	}
	public HttpResponse handle(HttpRequest req) {
		String idProd = req.getPath().replaceAll("\\/products\\/", "");
		return new HttpResponse()
		.withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
		.withBody(productJson.replaceAll("#id#", idProd));
	}		
}

