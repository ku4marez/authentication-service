package com.github.ku4marez.authenticationservice.constant;

public class UserRole {
    public static final String ADMIN = "ADMIN";
    public static final String DOCTOR = "DOCTOR";
    public static final String PATIENT = "PATIENT";

    public static String[] getAllRoles() {
        return new String[]{ADMIN, DOCTOR, PATIENT};
    }
}
