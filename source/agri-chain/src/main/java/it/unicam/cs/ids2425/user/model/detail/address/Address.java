package it.unicam.cs.ids2425.user.model.detail.address;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;
    private Double latitude;
    private Double longitude;
    private String notes;
    private String street;
    private String city;
    private String zipCode;
    private String country;
}
