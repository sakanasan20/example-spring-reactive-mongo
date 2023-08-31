package tw.niq.example.bootstrap;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.example.domain.Beer;
import tw.niq.example.repository.BeerRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
	
	private final BeerRepository beerRepository;

	@Override
	public void run(String... args) throws Exception {
		
		beerRepository.deleteAll()
			.doOnSuccess(success -> {
				loadBeers();
			})
			.subscribe();
	}

	private void loadBeers() {
		
		beerRepository.count().subscribe(count -> {
			if (count == 0) {
				Beer beer1 = Beer.builder()
						.beerName("Galaxy Cat")
						.beerStyle("Pale Ale")
						.upc("12356")
						.price(new BigDecimal("12.99"))
						.quantityOnHand(122)
						.build();

				Beer beer2 = Beer.builder()
						.beerName("Crank")
						.beerStyle("Pale Ale")
						.upc("12356222")
						.price(new BigDecimal("11.99"))
						.quantityOnHand(392)
						.build();

				Beer beer3 = Beer.builder()
						.beerName("Sunshine City")
						.beerStyle("IPA Ale")
						.upc("12356")
						.price(new BigDecimal("13.99"))
						.quantityOnHand(144)
						.build();
				
				beerRepository.save(beer1).subscribe(); // need subscribe to show actual count
				beerRepository.save(beer2).subscribe(); // need subscribe to show actual count
				beerRepository.save(beer3).subscribe(); // need subscribe to show actual count
			}
		});
	}

}
