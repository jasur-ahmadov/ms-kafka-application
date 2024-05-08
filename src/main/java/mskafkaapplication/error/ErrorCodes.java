package mskafkaapplication.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {

    STUDENT_NOT_FOUND("STUDENT_NOT_FOUND");

    private final String code;
}