package com.egt.challenge.service;

import com.egt.challenge.repo.AddressRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    @NonNull
    private final AddressRepository addressRepository;

    // TODO create methods to create, read, update, and delete Persons as outlined in the README
}
