package com.techx7.techstore.model.dto.user;

import com.techx7.techstore.validation.country.CountryName;
import com.techx7.techstore.validation.gender.GenderName;
import com.techx7.techstore.validation.multipart.MultiPartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.techx7.techstore.utils.FileUtils.manageImage;

public class UserProfileDTO {

    @MultiPartFile(contentTypes = {"image/png", "image/jpeg"})
    private MultipartFile image;

    private String imageUrl;

    @NotNull(message = "Please enter your first name")
    @Size(min = 1, max = 30, message = "First name should have from 1 to 30 characters")
    private String firstName;

    @NotNull(message = "Please enter your last name")
    @Size(min = 1, max = 30, message = "Last name should have from 1 to 30 characters")
    private String lastName;

    @GenderName
    @NotBlank(message = "Please choose your gender")
    private String gender;

    @NotNull(message = "Please enter your phone number")
    @Pattern(regexp = "08[789]\\d{7}", message = "Please enter a valid phone number (i.e. 0887654321)")
    private String phoneNumber;

    @NotNull(message = "Please enter your address")
    @Size(min = 1, max = 50, message = "Address should have from 1 to 50 characters")
    private String address;

    private String secondaryAddress;

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

    public UserProfileDTO() {}

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
        this.imageUrl = imageUrl == null ? imageUrl : imageUrl.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address.trim();
    }

    public String getSecondaryAddress() {
        return secondaryAddress;
    }

    public void setSecondaryAddress(String secondaryAddress) {
        this.secondaryAddress = secondaryAddress == null ? secondaryAddress : secondaryAddress.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? state : state.trim();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode.trim();
    }

}
