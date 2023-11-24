package com.techx7.techstore.model.entity;

import com.techx7.techstore.model.dto.user.UserProfileDTO;
import com.techx7.techstore.model.enums.GenderEnum;
import jakarta.persistence.*;

import java.util.Locale;

@Entity
@Table(name = "user_info")
public class UserInfo extends BaseEntity {

    @Column
    private String imageUrl;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column
    private GenderEnum gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private String address;

    @Column(name = "secondary_address")
    private String secondaryAddress;

    @ManyToOne
    private Country country;

    @Column
    private String city;

    @Column
    private String state;

    @Column
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
        this.setImageUrl(userProfileDTO.getImageUrl());
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
