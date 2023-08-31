package tw.niq.example.mapper;

import org.mapstruct.Mapper;

import tw.niq.example.domain.Customer;
import tw.niq.example.dto.CustomerDto;

@Mapper
public interface CustomerMapper {

	Customer customerDtoToCustomer(CustomerDto customerDto);
	
	CustomerDto customerToCustomerDto(Customer customer);
	
}
