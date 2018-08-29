package net.pvytykac.scrape.client;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UserAgentInterceptor implements Interceptor {

	private static final String API_VERSION = "1.0.0";

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request()
				.newBuilder()
				.addHeader("User-Agent", "std-client " + API_VERSION)
				.build();

		return chain.proceed(request);
	}
}
