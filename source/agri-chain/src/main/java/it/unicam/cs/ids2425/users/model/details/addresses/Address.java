package it.unicam.cs.ids2425.users.model.details.addresses;

import it.unicam.cs.ids2425.core.identifiers.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@Data
@EqualsAndHashCode(of = {"latitude", "longitude"})
@ToString(of = {"latitude", "longitude"})
public class Address implements Identifiable<String> {
    private double latitude;
    private double longitude;
    private String notes;
    private String street;
    private String city;
    private String zipCode;
    private String country;


    @Override
    public String getId() {
        return toString();
    }
}
