package com.techx7.techstore.model.entity;

import com.techx7.techstore.model.dto.user.UserProfileDTO;
import com.techx7.techstore.model.enums.GenderEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Locale;

@Entity
@Table(name = "user_info")
public class UserInfo extends BaseEntity {

    @Column
    private String imageUrl;

    @NotNull(message = "First name should not be empty")
    @Size(min = 1, max = 30, message = "First name should have from 1 to 30 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull(message = "Last name should not be empty")
    @Size(min = 1, max = 30, message = "Last name should have from 1 to 30 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column
    private GenderEnum gender;

    @NotBlank(message = "Phone number should not be empty")
    @Pattern(regexp = "08[789]\\d{7}", message = "Phone number should be valid")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotBlank(message = "Address should not be empty")
    @Size(min = 1, max = 50, message = "Address should have from 1 to 50 characters")
    @Column(nullable = false)
    private String address;

    @Column(name = "secondary_address")
    private String secondaryAddress;

    @ManyToOne(optional = false)
    private Country country;

    @NotBlank(message = "City should not be empty")
    @Size(min = 1, max = 35, message = "City should have from 1 to 35 characters")
    @Column(nullable = false)
    private String city;

    @Column
    private String state;

    @NotBlank(message = "ZIP code should not be empty")
    @Size(min = 1, max = 10, message = "ZIP code should have from 1 to 10 characters")
    @Column(nullable = false)
    private String zipCode;

    public UserInfo() {}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
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

    public String getSecondaryAddress() {
        return secondaryAddress;
    }

    public void setSecondaryAddress(String secondaryAddress) {
        this.secondaryAddress = secondaryAddress;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
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

    public void editUserProfile(UserProfileDTO userProfileDTO) {
        this.setFirstName(userProfileDTO.getFirstName());
        this.setLastName(userProfileDTO.getLastName());
        this.setGender(
                GenderEnum.valueOf(
                        userProfileDTO.getGender().toUpperCase(Locale.getDefault())));
        this.setPhoneNumber(userProfileDTO.getPhoneNumber());
        this.setAddress(userProfileDTO.getAddress());
        this.setSecondaryAddress(userProfileDTO.getSecondaryAddress());
        this.setCity(userProfileDTO.getCity());
        this.setState(userProfileDTO.getState());
        this.setZipCode(userProfileDTO.getZipCode());
    }

}
