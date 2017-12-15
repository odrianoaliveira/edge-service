package oi.digital.edgeservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@SpringBootApplication
@RestController
public class EdgeserviceApplication {
	@Value("${remote.home}")
	private URI home;

	@GetMapping(path="/test", headers="x-host=png.abc.org")
	public ResponseEntity<Object> proxy(ProxyExchange<Object> proxy) throws Exception {
		return proxy.uri(home.toString() + "/image/png")
				.get(header("X-TestHeader", "foobar"));
	}

	@GetMapping("/test2")
	public ResponseEntity<Object> proxyFoos(ProxyExchange<Object> proxy) throws Exception {
		return proxy.uri(home.toString() + "/image/webp").get(header("X-AnotherHeader", "baz"));
	}

	private Function<ResponseEntity<Object>, ResponseEntity<Object>> header(String key,
																			String value) {
		return response -> ResponseEntity.status(response.getStatusCode())
				.headers(response.getHeaders()).header(key, value)
				.body(response.getBody());
	}

	public static void main(String[] args) {
		SpringApplication.run(EdgeserviceApplication.class, args);
	}
}
