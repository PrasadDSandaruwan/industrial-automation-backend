package com.industrialautomation.api.dto.response;

public enum ResponseStatus {
    OK,
    FORCE_PASSWORD_CHANGE_PENDING,
    INVALID_INPUTS,
    MISSING_INPUTS,
    EMAIL_NOT_FOUND,
    WRONG_CURRENT_PASSWORD,
    INVALID_REFRESH_TOKEN,
    INVALID_USER,
    DB_INSERT_FAILED,
    FAILED,
    WARNING,
    FILE_HANDLING_FAILED,
    INVALID_FILE_TYPE,
    VEHICLE_ALREADY_EXISTS,
    USER_ALREADY_EXISTS,
}
