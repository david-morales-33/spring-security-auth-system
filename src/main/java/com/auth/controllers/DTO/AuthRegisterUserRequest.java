package com.auth.controllers.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthRegisterUserRequest(
    @NotBlank String username,
    @NotBlank String password,
    @Valid AuthRegisterRoleRequest roleList
) {

}
