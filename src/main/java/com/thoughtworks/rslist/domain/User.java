package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.validation.constraints.*;

public class User {
    @NotNull
    @Size(max = 8)
    private String name;
    @NotNull
    private String gender;
    @Email
    private String email;
    @Pattern(regexp = "1\\d{10}")
    private String phone;
    @Max(100)
    @Min(18)
    private int age;
    private int voteNum = 10;

    public User(String name, String gender, String email, String phone, int age) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.age = age;
    }
    @JsonProperty("user_name")
    public String getName() {
        return name;
    }
    @JsonProperty("user_name")
    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty("user_gender")
    public String getGender() {
        return gender;
    }
    @JsonProperty("user_gender")
    public void setGender(String gender) {
        this.gender = gender;
    }
    @JsonProperty("user_email")
    public String getEmail() {
        return email;
    }
    @JsonProperty("user_email")
    public void setEmail(String email) {
        this.email = email;
    }
    @JsonProperty("user_phone")
    public String getPhone() {
        return phone;
    }
    @JsonProperty("user_phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }
    @JsonProperty("user_age")
    public int getAge() {
        return age;
    }
    @JsonProperty("user_age")
    public void setAge(int age) {
        this.age = age;
    }

    public User() {
    }

    @JsonProperty("user_voteNum")
    public int getVoteNum() {
        return voteNum;
    }
    @JsonProperty("user_voteNum")
    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

}
