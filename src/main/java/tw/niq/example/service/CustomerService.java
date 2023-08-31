package tw.niq.example.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.dto.CustomerDto;

public interface CustomerService {

	Flux<CustomerDto> listCustomers();

	Mono<CustomerDto> getCustomerById(String customerId);

	Mono<CustomerDto> createNewCustomer(CustomerDto customerDto);

	Mono<CustomerDto> updateCustomer(String customerId, CustomerDto customerDto);

	Mono<CustomerDto> patchCustomer(String customerId, CustomerDto customerDto);

	Mono<Void> deleteCustomerById(String customerId);
	
}
