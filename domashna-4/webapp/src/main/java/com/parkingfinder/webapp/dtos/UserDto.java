package com.parkingfinder.webapp.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Simple data transfer object for user with included validation
 * */
@Data
public class UserDto {

    private int ID;

    @NotNull
    @Size(min = 5, message = "Името мора да содржи барем 5 знаци")
    private String name;

    @NotNull
    @Size(min = 8, message = "Лозинката мора да содржи барем 8 знаци")
    private String password;

    @Email(message = "Невалидна email адреса")
    @Size(min = 5)
    private String email;

}