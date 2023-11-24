package com.techx7.techstore.model.dto.user;

import com.techx7.techstore.validation.multipart.MultiPartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.techx7.techstore.util.FileUtils.manageImage;

public class UserProfileDTO {

    @NotNull(message = "UUID should not be empty")
    private UUID uuid;

    @MultiPartFile(contentTypes = {"image/png", "image/jpeg"})
    private MultipartFile image;

    private String imageUrl;

    @NotBlank(message = "Please enter your first name")
    private String firstName;

    @NotBlank(message = "Please enter your last name")
    private String lastName;

    @NotBlank(message = "Please choose your gender")
    private String gender;

    @NotBlank(message = "Please enter your phone number")
    private String phoneNumber;

    @NotBlank(message = "Please enter your address")
    private String address;

    private String secondaryAddress;

    @NotBlank(message = "Please choose your country")
    private String country;

    @NotBlank(message = "Please enter your city")
    private String city;

    private String state;

    @NotBlank(message = "Please enter your zip code")
    private String zipCode;

    public UserProfileDTO() {}

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) throws IOException {
        this.image = manageImage(image, imageUrl);
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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
