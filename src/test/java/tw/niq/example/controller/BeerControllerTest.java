package tw.niq.example.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;
import tw.niq.example.domain.Beer;
import tw.niq.example.dto.BeerDto;
import tw.niq.example.service.BeerServiceImplTest;
import tw.niq.example.web.fn.BeerRouterConfig;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {
	
	@Autowired
	WebTestClient webTestClient;

	@BeforeEach
	void setUp() throws Exception {
	}
	
    public BeerDto getSavedTestBeer() {
    	webTestClient.mutateWith(mockOAuth2Login()).post().uri(BeerRouterConfig.BEER_PATH)
				.body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDto.class)
				.header("Content-Type", "application/json")
				.exchange()
				.returnResult(BeerDto.class);
    	
    	return webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerRouterConfig.BEER_PATH)
    			.exchange()
    			.returnResult(BeerDto.class)
    			.getResponseBody()
    			.blockFirst();
    }
	
	@Order(1)
	@Test
	void testListBeers() {
		webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerRouterConfig.BEER_PATH)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().valueEquals("Content-type", "application/json")
			.expectBody().jsonPath("$.size()").value(greaterThan(1));
	}

	@Order(2)
	@Test
	void testGetBeerById() {
		BeerDto beerDto = getSavedTestBeer();
		
		webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerRouterConfig.BEER_PATH_ID, beerDto.getId())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().valueEquals("Content-type", "application/json")
			.expectBody(BeerDto.class);
	}
	
	@Order(2)
	@Test
	void testGetBeerById_whenIdNotExist() {
		webTestClient.mutateWith(mockOAuth2Login()).get().uri(BeerRouterConfig.BEER_PATH_ID, 999)
			.exchange()
			.expectStatus().isNotFound();
	}

	@Order(3)
	@Test
	void testCreateNewBeer() {
		webTestClient.mutateWith(mockOAuth2Login()).post().uri(BeerRouterConfig.BEER_PATH)
			.body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDto.class)
			.header("Content-type", "application/json")
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().exists("location");
	}
	
	@Order(3)
	@Test
	void testCreateNewBeer_withInvalidBeer() {
		
		Beer invalidBeer = BeerServiceImplTest.getTestBeer();
		invalidBeer.setBeerName("");
		
		webTestClient.mutateWith(mockOAuth2Login()).post().uri(BeerRouterConfig.BEER_PATH)
			.body(Mono.just(invalidBeer), BeerDto.class)
			.header("Content-type", "application/json")
			.exchange()
			.expectStatus().isBadRequest();
	}

	@Order(4)
	@Test
	void testUpdateBeer() {
		BeerDto beerDto = getSavedTestBeer();
		
		webTestClient.mutateWith(mockOAuth2Login()).put().uri(BeerRouterConfig.BEER_PATH_ID, beerDto.getId())
			.body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDto.class)
			.exchange()
			.expectStatus().isNoContent();
	}
	
	@Order(4)
	@Test
	void testUpdateBeer_withInvalidBeer() {
		BeerDto beerDto = getSavedTestBeer();
		
		Beer invalidBeer = BeerServiceImplTest.getTestBeer();
		invalidBeer.setBeerName("");
		
		webTestClient.mutateWith(mockOAuth2Login()).put().uri(BeerRouterConfig.BEER_PATH_ID, beerDto.getId())
			.body(Mono.just(invalidBeer), BeerDto.class)
			.exchange()
			.expectStatus().isBadRequest();
	}
	
	@Order(4)
	@Test
	void testUpdateBeer_whenIdNotFound() {
		webTestClient.mutateWith(mockOAuth2Login()).put().uri(BeerRouterConfig.BEER_PATH_ID, 999)
		.body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDto.class)
		.exchange()
		.expectStatus().isNotFound();
	}

	@Order(5)
	@Test
	void testPatchBeer() {
		BeerDto beerDto = getSavedTestBeer();
		
		webTestClient.mutateWith(mockOAuth2Login()).patch().uri(BeerRouterConfig.BEER_PATH_ID, beerDto.getId())
			.body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDto.class)
			.exchange()
			.expectStatus().isNoContent();
	}
	
	@Order(5)
	@Test
	void testPatchBeer_withInvalidBeer() {
		BeerDto beerDto = getSavedTestBeer();
		
		Beer invalidBeer = BeerServiceImplTest.getTestBeer();
		invalidBeer.setBeerName("");
		
		webTestClient.mutateWith(mockOAuth2Login()).patch().uri(BeerRouterConfig.BEER_PATH_ID, beerDto.getId())
			.body(Mono.just(invalidBeer), BeerDto.class)
			.exchange()
			.expectStatus().isBadRequest();
	}
	
	@Order(5)
	@Test
	void testPatchBeer_whenIdNotFound() {
		webTestClient.mutateWith(mockOAuth2Login()).patch().uri(BeerRouterConfig.BEER_PATH_ID, 999)
			.body(Mono.just(BeerServiceImplTest.getTestBeer()), BeerDto.class)
			.exchange()
			.expectStatus().isNotFound();
	}

	@Order(999)
	@Test
	void testDeleteBeerById() {
		BeerDto beerDto = getSavedTestBeer();
		
		webTestClient.mutateWith(mockOAuth2Login()).delete().uri(BeerRouterConfig.BEER_PATH_ID, beerDto.getId())
			.exchange()
			.expectStatus().isNoContent();
	}
	
	@Order(999)
	@Test
	void testDeleteBeerById_whenIdNotFound() {
		webTestClient.mutateWith(mockOAuth2Login()).delete().uri(BeerRouterConfig.BEER_PATH_ID, 999)
			.exchange()
			.expectStatus().isNotFound();
	}
	
	@Test
	void testFindByBeerStyle() {
		final String BEER_STYLE = "TEST";
		
		BeerDto beerDto = getSavedTestBeer();
		beerDto.setBeerStyle(BEER_STYLE);
		
		webTestClient.mutateWith(mockOAuth2Login()).post().uri(BeerRouterConfig.BEER_PATH)
			.body(Mono.just(beerDto), BeerDto.class)
			.header("Content-type", "application/json")
			.exchange();
		
		webTestClient.mutateWith(mockOAuth2Login()).get().uri(UriComponentsBuilder.fromPath(BeerRouterConfig.BEER_PATH).queryParam("beerStyle", BEER_STYLE).build().toUri())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().valueEquals("Content-type", "application/json")
			.expectBody().jsonPath("$.size()").value(equalTo(1));
	}

}
