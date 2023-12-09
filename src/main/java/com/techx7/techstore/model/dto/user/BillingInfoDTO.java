package com.techx7.techstore.model.dto.user;

import com.techx7.techstore.validation.country.CountryName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BillingInfoDTO {

    @NotNull(message = "Please enter your first name")
    @Size(min = 1, max = 30, message = "First name should have from 1 to 30 characters")
    private String firstName;

    @NotNull(message = "Please enter your last name")
    @Size(min = 1, max = 30, message = "Last name should have from 1 to 30 characters")
    private String lastName;

    @NotNull(message = "Please enter your phone number")
    @Pattern(regexp = "08[789]\\d{7}", message = "Please enter a valid phone number (i.e. 0887654321)")
    private String phoneNumber;

    @NotNull(message = "Please enter your address")
    @Size(min = 1, max = 50, message = "Address should have from 1 to 50 characters")
    private String address;

    @CountryName
    @NotBlank(message = "Please choose your country")
    private String country;

    @NotNull(message = "Please enter your city")
    @Size(min = 1, max = 35, message = "City should have from 1 to 35 characters")
    private String city;

    private String state;

    @NotNull(message = "Please enter your zip code")
    @Size(min = 1, max = 10, message = "ZIP code should have from 1 to 10 characters")
    private String zipCode;

    public BillingInfoDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

}
