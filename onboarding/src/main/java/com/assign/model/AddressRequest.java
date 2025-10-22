package com.assign.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
    @NotNull String streetName,
    @NotNull String houseNumber,
    @NotNull String city,
    @NotNull String postalCode,
    @NotNull  String country) {
}
