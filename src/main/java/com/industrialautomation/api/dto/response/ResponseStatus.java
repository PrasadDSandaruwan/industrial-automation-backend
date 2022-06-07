package com.industrialautomation.api.dto.response;

public enum ResponseStatus {
    OK,
    FORCE_PASSWORD_CHANGE_PENDING,
    INVALID_INPUTS,
    MISSING_INPUTS,
    INVALID_USER,

    FAILED,

    USER_ALREADY_EXISTS,
    VERIFIED_USER,

    ALREADY_EXISTS

}
