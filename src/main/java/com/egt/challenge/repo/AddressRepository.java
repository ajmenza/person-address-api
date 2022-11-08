package com.egt.challenge.repo;

import com.egt.challenge.model.Address;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository {
    List<Address> findAll();

    Optional<Address> findById(Long id);

    Address save(Address address);

    void delete(Address address);
}
