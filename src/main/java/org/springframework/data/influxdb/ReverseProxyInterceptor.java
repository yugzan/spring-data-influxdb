package org.springframework.data.influxdb;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author yongzan
 */
public class ReverseProxyInterceptor implements Interceptor {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String reversePrefix;

	public ReverseProxyInterceptor(InfluxDBProperties properties) {
		//default append an empty string.
		reversePrefix = Optional.of(properties.getReversePrefix()).orElse("");
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		HttpUrl.Builder fixedUrl = chain.request().url().newBuilder()
				.encodedPath(reversePrefix + chain.request().url().encodedPath());
//		logger.debug("After reverse Url: {}", fixedUrl);
		return
				(reversePrefix.isEmpty())?
					chain.proceed( chain.request() ):
					chain.proceed(chain.request().newBuilder().url( fixedUrl.build()).build());
	}
}
