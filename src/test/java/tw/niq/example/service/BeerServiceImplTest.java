package tw.niq.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Mono;
import tw.niq.example.domain.Beer;
import tw.niq.example.dto.BeerDto;
import tw.niq.example.mapper.BeerMapper;
import tw.niq.example.mapper.BeerMapperImpl;

@SpringBootTest
class BeerServiceImplTest {
	
	@Autowired
	BeerService beerService;
	
	@Autowired
	BeerMapper beerMapper;
	
	BeerDto beerDto;

	@BeforeEach
	void setUp() throws Exception {
		beerDto = beerMapper.beerToBeerDto(getTestBeer());
	}
	
	public BeerDto getSavedBeerDto() {
		return beerService.createNewBeer(Mono.just(getTestBeerDto())).block();
	}
	
	public static BeerDto getTestBeerDto() {
		return new BeerMapperImpl().beerToBeerDto(getTestBeer());
	}

	public static Beer getTestBeer() {
		return Beer.builder()
				.beerName("Space Dust")
				.beerStyle("IPA")
				.price(BigDecimal.TEN)
				.quantityOnHand(12)
				.upc("123456")
				.build();
	}

	@Test
	void testCreateNewBeer_withSubscribe() {
		
		AtomicBoolean atomicBoolean = new AtomicBoolean(false);	
		AtomicReference<BeerDto> atomicDto = new AtomicReference<>();
		
		Mono<BeerDto> savedMono = beerService.createNewBeer(Mono.just(beerDto));
		
		savedMono.subscribe(savedDto -> {
			atomicBoolean.set(true);
			atomicDto.set(savedDto);
		});
		
		await().untilTrue(atomicBoolean);
		
		BeerDto persistedDto = atomicDto.get();
		assertThat(persistedDto).isNotNull();
		assertThat(persistedDto.getId()).isNotNull();
	}
	
	@Test
	void testCreateNewBeer_withBlock() {
		BeerDto savedDto = beerService.createNewBeer(Mono.just(getTestBeerDto())).block();
		assertThat(savedDto).isNotNull();
		assertThat(savedDto.getId()).isNotNull();
	}
	
	@Test
	void testUpdateBeer_withStream() {
		
		final String newName = "newBeerName";
		
		AtomicReference<BeerDto> atomicDto = new AtomicReference<>();

		beerService.createNewBeer(Mono.just(getTestBeerDto()))
			.map(savedDto -> {
				savedDto.setBeerName(newName);
				return savedDto;
			})
			.flatMap(toUpdateDto -> beerService.updateBeer(toUpdateDto.getId(), toUpdateDto))
			.flatMap(updatedDto -> beerService.getBeerById(updatedDto.getId()))
			.subscribe(dtoFromDb -> {
				atomicDto.set(dtoFromDb);
			});
		
		await().until(() -> atomicDto.get() != null);
		assertThat(atomicDto.get().getBeerName()).isEqualTo(newName);
	}
	
	@Test
	void testUpdateBeer_withBlock() {
		final String newName = "newBeerName";
		BeerDto savedDto = getSavedBeerDto();
		savedDto.setBeerName(newName);
		
		BeerDto updatedDto = beerService.updateBeer(savedDto.getId(), savedDto).block();
		
		BeerDto fetchedDto = beerService.getBeerById(updatedDto.getId()).block();
		assertThat(fetchedDto.getBeerName()).isEqualTo(newName);
	}
	
	@Test
	void testDeleteBeerById() {
		
		BeerDto beerToDelete = getSavedBeerDto();
		
		beerService.deleteBeerById(beerToDelete.getId()).block();
		
		Mono<BeerDto> expectedEmptyBeerMono = beerService.getBeerById(beerToDelete.getId());
		
		BeerDto emptyBeer = expectedEmptyBeerMono.block();
		
		assertThat(emptyBeer).isNull();
	}

	@Test
	void testFindFirstByBeerName() {
		
		AtomicBoolean atomicBoolean = new AtomicBoolean(false);	
		AtomicReference<BeerDto> atomicDto = new AtomicReference<>();
		BeerDto beerDto = getSavedBeerDto();
		
		Mono<BeerDto> foundDto = beerService.findFirstByBeerName(beerDto.getBeerName());
		
		foundDto.subscribe(dto -> {
			atomicBoolean.set(true);
			atomicDto.set(dto);
		});
		
		await().untilTrue(atomicBoolean);
		
		BeerDto firstDto = atomicDto.get();
		assertThat(firstDto).isNotNull();
		assertThat(firstDto.getId()).isNotNull();
	}
	
	@Test
	void testFindByBeerStyle() {
		
		AtomicBoolean atomicBoolean = new AtomicBoolean(false);	
		
		BeerDto beerDto = getSavedBeerDto();
		
		beerService.findByBeerStyle(beerDto.getBeerStyle())
			.subscribe(dto -> {
				System.out.println(dto);
				atomicBoolean.set(true);
			});
		
		await().untilTrue(atomicBoolean);		
	}
	
}
