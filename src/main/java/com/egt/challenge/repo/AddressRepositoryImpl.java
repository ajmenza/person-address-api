package com.egt.challenge.repo;

import com.egt.challenge.model.Address;
import com.egt.challenge.model.Person;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AddressRepositoryImpl implements AddressRepository {

    private final Map<Long, Address> repo = new HashMap<>();
    private Comparator<Address> compareById = Comparator.comparing(Address::getId);

    @Override
    public List<Address> findAll() {
        return new ArrayList<>(repo.values());
    }

    @Override
    public Optional<Address> findById(Long id) {
        return Optional.ofNullable(repo.get(id));
    }

    @Override
    public Address save(Address address) {
        if (address.getId() == null) {
            List<Address> addressList = findAll();
            if (addressList.isEmpty() || addressList == null)
                address.setId(1L);
            else {
                // Sort by id number from low to high
                addressList = addressList.stream().sorted(compareById).collect(Collectors.toList());
                Address lastElement = addressList.get(addressList.size() - 1);
                // Set id to id of last element (highest id) plus 1
                address.setId(lastElement.getId() + 1L);
            }
        }

        repo.put(address.getId(), address);
        return address;
    }

    @Override
    public void delete(Address address) {
        repo.remove(address.getId());
    }
}
