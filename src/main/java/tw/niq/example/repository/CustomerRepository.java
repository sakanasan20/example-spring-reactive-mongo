package tw.niq.example.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import tw.niq.example.domain.Customer;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

}
