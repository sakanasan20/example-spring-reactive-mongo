package tw.niq.example.web.fn;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BeerRouterConfig {

	public static final String BEER_PATH = "/api/v3/beers";
	public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
	
	private final BeerHandler beerHandler;
	
	@Bean
	public RouterFunction<ServerResponse> beerRoutes() {
		return route()
				.GET(BEER_PATH, accept(APPLICATION_JSON), beerHandler::listBeers)
				.GET(BEER_PATH_ID, accept(APPLICATION_JSON), beerHandler::getBeerById)
				.POST(BEER_PATH, accept(APPLICATION_JSON), beerHandler::createNewBeer)
				.PUT(BEER_PATH_ID, accept(APPLICATION_JSON), beerHandler::updateBeer)
				.PATCH(BEER_PATH_ID, accept(APPLICATION_JSON), beerHandler::updateBeer)
				.DELETE(BEER_PATH_ID, accept(APPLICATION_JSON), beerHandler::deleteBeerById)
				.build();
	}
	
}
