package tw.niq.example.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.niq.example.dto.CustomerDto;
import tw.niq.example.mapper.CustomerMapper;
import tw.niq.example.repository.CustomerRepository;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
	
	private final CustomerMapper customerMapper;
	
	@Override
	public Flux<CustomerDto> listCustomers() {
		return customerRepository.findAll()
				.map(customerMapper::customerToCustomerDto);
	}

	@Override
	public Mono<CustomerDto> getCustomerById(String customerId) {
		return customerRepository.findById(customerId)
				.map(customerMapper::customerToCustomerDto);
	}

	@Override
	public Mono<CustomerDto> createNewCustomer(CustomerDto customerDto) {
		return customerRepository.save(customerMapper.customerDtoToCustomer(customerDto))
				.map(customerMapper::customerToCustomerDto);
	}

	@Override
	public Mono<CustomerDto> updateCustomer(String customerId, CustomerDto customerDto) {
		return customerRepository.findById(customerId)
				.map(foundCustomer -> {
					foundCustomer.setCustomerName(customerDto.getCustomerName());
					return foundCustomer;
				})
				.flatMap(customerRepository::save)
				.map(customerMapper::customerToCustomerDto);
	}

	@Override
	public Mono<CustomerDto> patchCustomer(String customerId, CustomerDto customerDto) {
		return customerRepository.findById(customerId)
				.map(foundCustomer -> {
					if (StringUtils.hasText(customerDto.getCustomerName())) {
						foundCustomer.setCustomerName(customerDto.getCustomerName());
					}
					return foundCustomer;
				})
				.flatMap(customerRepository::save)
				.map(customerMapper::customerToCustomerDto);
	}

	@Override
	public Mono<Void> deleteCustomerById(String customerId) {
		return customerRepository.deleteById(customerId);
	}

}
