package com.erp.constant;

public class CommonConstants {

    public static final String AUTH_TOKEN = "AuthToken";
    public static final String PAGE_ID = "pageId";
    public static final String SEARCH_PARAMS = "searchParams";
    public static final String REGEX_PASSWORD_VALIDATION = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{8, 20}$";
    public static final String REGEX_EMAIL_VALIDATION = "\\S+@\\S+\\.\\S+";
    public static final String REGEX_URL_VALIDATION = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    public static final String REGEX_NIC1_VALIDATION = "^[0-9]{9}[VX]$";
    public static final String REGEX_NIC2_VALIDATION = "^[0-9]{12}$";
    public static final String REGEX_CHAR_VALIDATION = "^[\\\\p{L} .'-]+$";
    public static final String REGEX_MOBILE_VALIDATION = "^07[0-9]{8}$";
    public static final String REGEX_TELEPHONE_VALIDATION = "^011[0-9]{7}$";
}