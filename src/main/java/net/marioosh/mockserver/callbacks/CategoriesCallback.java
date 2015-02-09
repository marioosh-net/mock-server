package net.marioosh.mockserver.callbacks;

import static org.mockserver.model.Header.header;
import net.marioosh.mockserver.MockServer;
import net.marioosh.mockserver.Utils;

import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

public class CategoriesCallback implements org.mockserver.mock.action.ExpectationCallback {
	String json = Utils.inputStreamtoString(MockServer.class.getClassLoader().getResourceAsStream("subcategories.json"));
	
	public CategoriesCallback() {
		// TODO Auto-generated constructor stub
	}
	public HttpResponse handle(HttpRequest req) {
		String idProd = req.getPath().replaceAll("\\/products\\/", "");
		if(req.getPath().endsWith("/20/subcategories")) {
			return new HttpResponse()
			.withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
			.withBody(json.replaceAll("true", "false")
					.replaceAll("(\\d+)", "$103")
					);			
		} else {
			return new HttpResponse()
			.withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=10"))
			.withBody(json);
		}
	}		
}

