package tw.niq.example.mapper;

import org.mapstruct.Mapper;

import tw.niq.example.domain.Beer;
import tw.niq.example.dto.BeerDto;

@Mapper
public interface BeerMapper {

	Beer beerDtoToBeer(BeerDto beerDto);
	
	BeerDto beerToBeerDto(Beer beer);
	
}
