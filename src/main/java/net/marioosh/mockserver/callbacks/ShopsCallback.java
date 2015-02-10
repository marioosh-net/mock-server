package net.marioosh.mockserver.callbacks;

import static org.mockserver.model.Header.header;
import net.marioosh.mockserver.MockServer;
import net.marioosh.mockserver.Utils;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;


public class ShopsCallback implements org.mockserver.mock.action.ExpectationCallback {

	String json = Utils.inputStreamtoString(MockServer.class.getClassLoader().getResourceAsStream("shops.json"));
	
	public ShopsCallback() {
		// TODO Auto-generated constructor stub
	}

	public HttpResponse handle(HttpRequest req) {
		return new HttpResponse()
		.withHeaders(header("Content-type", "application/json"),header("Cache-Control", "max-age=30"))
		.withBody(json);
	}
}
