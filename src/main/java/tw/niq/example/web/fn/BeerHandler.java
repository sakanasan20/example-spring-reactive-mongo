package tw.niq.example.web.fn;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.dto.BeerDto;
import tw.niq.example.service.BeerService;

@Component
@RequiredArgsConstructor
public class BeerHandler {

	private final BeerService beerService;
	private final Validator validator;
	
	private void validate(BeerDto beerDto) {
		Errors errors = new BeanPropertyBindingResult(beerDto, "beerDto");
		validator.validate(beerDto, errors);
		
		if (errors.hasErrors()) {
			throw new ServerWebInputException(errors.toString());
		}
	}
	
	public Mono<ServerResponse> listBeers(ServerRequest serverRequest) {
		
		Flux<BeerDto> beerFlux;
		
		if (serverRequest.queryParam("beerStyle").isPresent()) {
			beerFlux = beerService.findByBeerStyle(serverRequest.queryParam("beerStyle").get());
		} else {
			beerFlux = beerService.listBeers();
		}
		
		return ServerResponse.ok()
				.body(beerFlux, BeerDto.class);
	}
	
	public Mono<ServerResponse> getBeerById(ServerRequest serverRequest) {
		return ServerResponse
				.ok()
				.body(
						beerService
							.getBeerById(serverRequest.pathVariable("beerId"))
							.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))), 
						BeerDto.class);
	}
	
	public Mono<ServerResponse> createNewBeer(ServerRequest serverRequest) {
		return beerService.createNewBeer(serverRequest.bodyToMono(BeerDto.class).doOnNext(this::validate))
				.flatMap(createdBeerDto -> {
					URI location = UriComponentsBuilder.fromPath(BeerRouterConfig.BEER_PATH_ID).build(createdBeerDto.getId());
					return ServerResponse.created(location).build();
				});
	}
	
	public Mono<ServerResponse> updateBeer(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(BeerDto.class)
				.doOnNext(this::validate)
				.flatMap(beerDto -> beerService.updateBeer(serverRequest.pathVariable("beerId"), beerDto))
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
				.flatMap(updatedDto -> ServerResponse.noContent().build());
	}
	
	public Mono<ServerResponse> patchBeer(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(BeerDto.class)
				.doOnNext(this::validate)
				.flatMap(beerDto -> beerService.patchBeer(serverRequest.pathVariable("beerId"), beerDto))
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
				.flatMap(updatedDto -> ServerResponse.noContent().build());
	}
	
	public Mono<ServerResponse> deleteBeerById(ServerRequest serverRequest) {
		return beerService.getBeerById(serverRequest.pathVariable("beerId"))
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
				.flatMap(beerDto -> beerService.deleteBeerById(beerDto.getId()))
				.then(ServerResponse.noContent().build());
	}
	
}
