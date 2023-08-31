package tw.niq.example.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.domain.Beer;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {
	
	Mono<Beer> findFirstByBeerName(String beerName);
	
	Flux<Beer> findByBeerStyle(String beerStyle);

}
