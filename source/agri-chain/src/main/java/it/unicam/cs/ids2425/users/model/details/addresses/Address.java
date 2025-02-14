package it.unicam.cs.ids2425.users.model.details.addresses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode(of = {"latitude", "longitude"})
public class Address {
    private double latitude;
    private double longitude;
    private String notes;
    private String street;
    private String city;
    private String zipCode;
    private String country;
}
