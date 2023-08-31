package tw.niq.example.dto;

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
public class CustomerDto {

	private String id;
	
	@NotBlank
	@Size(min = 3, max = 255)
	private String customerName;

	private LocalDateTime createdDate;
	
	private LocalDateTime last_modifiedDate;
	
}
