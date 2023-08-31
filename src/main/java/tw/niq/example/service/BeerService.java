package tw.niq.example.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.dto.BeerDto;

public interface BeerService {

	Flux<BeerDto> listBeers();

	Mono<BeerDto> getBeerById(String beerId);

	Mono<BeerDto> createNewBeer(Mono<BeerDto> beerDto);

	Mono<BeerDto> updateBeer(String beerId, BeerDto beerDto);

	Mono<BeerDto> patchBeer(String beerId, BeerDto beerDto);

	Mono<Void> deleteBeerById(String beerId);
	
	Mono<BeerDto> findFirstByBeerName(String beerName);
	
	Flux<BeerDto> findByBeerStyle(String beerStyle);
	
}
