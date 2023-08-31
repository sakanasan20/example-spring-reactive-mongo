package tw.niq.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {

	private String id;
	
	@NotBlank
	@Size(min = 3, max = 255)
	private String beerName;

	@Size(min = 1, max = 255)
	private String beerStyle;
	
	@Size(max = 25)
	private String upc;
	
	private Integer quantityOnHand;
	
	private BigDecimal price;

	private LocalDateTime createdDate;
	
	private LocalDateTime last_modifiedDate;
	
}
