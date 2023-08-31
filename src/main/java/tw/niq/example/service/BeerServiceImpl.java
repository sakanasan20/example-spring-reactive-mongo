package tw.niq.example.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.dto.BeerDto;
import tw.niq.example.mapper.BeerMapper;
import tw.niq.example.repository.BeerRepository;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

	private final BeerRepository beerRepository;
	
	private final BeerMapper beerMapper;
	
	@Override
	public Flux<BeerDto> listBeers() {
		return beerRepository.findAll()
				.map(beerMapper::beerToBeerDto);
	}

	@Override
	public Mono<BeerDto> getBeerById(String beerId) {
		return beerRepository.findById(beerId)
				.map(beerMapper::beerToBeerDto);
	}

	@Override
	public Mono<BeerDto> createNewBeer(Mono<BeerDto> beerDto) {
		return beerDto.map(beerMapper::beerDtoToBeer)
				.flatMap(beerRepository::save)
				.map(beerMapper::beerToBeerDto);
	}

	@Override
	public Mono<BeerDto> updateBeer(String beerId, BeerDto beerDto) {
		return beerRepository.findById(beerId)
				.map(foundBeer -> {
					foundBeer.setBeerName(beerDto.getBeerName());
					foundBeer.setBeerStyle(beerDto.getBeerStyle());
					foundBeer.setUpc(beerDto.getUpc());
					foundBeer.setQuantityOnHand(beerDto.getQuantityOnHand());
					foundBeer.setPrice(beerDto.getPrice());
					return foundBeer;
				})
				.flatMap(beerRepository::save)
				.map(beerMapper::beerToBeerDto);
	}

	@Override
	public Mono<BeerDto> patchBeer(String beerId, BeerDto beerDto) {
		return beerRepository.findById(beerId)
				.map(foundBeer -> {
					if (StringUtils.hasText(beerDto.getBeerName())) {
						foundBeer.setBeerName(beerDto.getBeerName());
					}
					if (StringUtils.hasText(beerDto.getBeerStyle())) {
						foundBeer.setBeerStyle(beerDto.getBeerStyle());
					}
					if (StringUtils.hasText(beerDto.getUpc())) {
						foundBeer.setUpc(beerDto.getUpc());
					}
					if (beerDto.getQuantityOnHand() != null) {
						foundBeer.setQuantityOnHand(beerDto.getQuantityOnHand());
					}
					if (beerDto.getPrice() != null) {
						foundBeer.setPrice(beerDto.getPrice());
					}
					return foundBeer;
				})
				.flatMap(beerRepository::save)
				.map(beerMapper::beerToBeerDto);
	}

	@Override
	public Mono<Void> deleteBeerById(String beerId) {
		return beerRepository.deleteById(beerId);
	}

	@Override
	public Mono<BeerDto> findFirstByBeerName(String beerName) {
		return beerRepository.findFirstByBeerName(beerName)
				.map(beerMapper::beerToBeerDto);
	}

	@Override
	public Flux<BeerDto> findByBeerStyle(String beerStyle) {
		return beerRepository.findByBeerStyle(beerStyle)
				.map(beerMapper::beerToBeerDto);
	}

}
