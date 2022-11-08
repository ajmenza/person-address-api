package com.egt.challenge.controller;

import com.egt.challenge.service.AddressService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AddressController.BASE_URL)
@RequiredArgsConstructor
public class AddressController {
    public static final String BASE_URL = "/api/addresses";

    @NonNull
    private final AddressService addressService;

    // TODO create the appropriate endpoints as outlined in the README
}
