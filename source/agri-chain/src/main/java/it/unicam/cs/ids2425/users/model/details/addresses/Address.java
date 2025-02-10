package it.unicam.cs.ids2425.users.model.details.addresses;

import it.unicam.cs.ids2425.utilities.statuses.BaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Address {
    private double latitude;
    private double longitude;
    private BaseStatus status;
    private String notes;
    private String street;
    private String city;
    private String zipCode;
    private String country;
}
