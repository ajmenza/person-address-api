package com.egt.challenge.controller;

import com.egt.challenge.dto.AddressDto;
import com.egt.challenge.dto.AddressMapper;
import com.egt.challenge.error.BadRequestException;
import com.egt.challenge.model.Address;
import com.egt.challenge.service.AddressService;
import com.egt.challenge.service.PersonService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(AddressController.BASE_URL)
@RequiredArgsConstructor
public class AddressController {
    public static final String BASE_URL = "/api/addresses";

    @NonNull
    private final AddressService addressService;
    @NonNull
    private final PersonService personService;
    @NonNull
    private final AddressMapper addressMapper;

    @GetMapping
    public List<AddressDto> fetchAddressList() {
        List<Address> addressList = addressService.fetchAddressList();
        List<AddressDto> addressDtoList = addressList.stream().map(addressMapper::toDtoAll).collect(Collectors.toList());
        return addressDtoList;
    }

    @GetMapping("/{id}")
    public AddressDto fetchAddressById(@PathVariable("id") Long addressId) throws BadRequestException {
        if (Objects.isNull(addressId)) throw new BadRequestException("No id provided");
        Address address = addressService.findAddressById(addressId);
        AddressDto addressDto = addressMapper.toDto(address);
        return addressDto;
    }

    @PostMapping
    public AddressDto saveAddress(@RequestBody AddressDto addressDto) throws BadRequestException {
        Address address = addressMapper.toEntity(addressDto);
        Address savedAddress = addressService.saveAddress(address);
        return addressMapper.toDto(savedAddress);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AddressDto updateAddress(@RequestBody AddressDto addressDto) throws BadRequestException {
        Address address = addressMapper.toEntity(addressDto);
        Address savedAddress = addressService.updateAddress(address);
        return addressMapper.toDto(savedAddress);
    }

    @DeleteMapping("/{id}")
    public String deleteAddressById(@PathVariable("id") Long addressId) throws BadRequestException {
        addressService.deleteAddressById(addressId);
        return "Address successfully deleted";
    }

    // TODO create the appropriate endpoints as outlined in the README


    // TODO create the appropriate endpoints as outlined in the README
}
