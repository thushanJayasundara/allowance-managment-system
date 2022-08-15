package com.erp.utils;



import com.erp.constant.CommonConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonValidation {

    public static boolean stringNullValidation(String inputString) {
        return inputString == null || inputString.isEmpty();
    }

    public static String sinceDateValidation(String since) {
        if (since.trim().equals("")) {
        } else {
            String sinceDate = since.trim();
            if (sinceDate.length() != 10) {
            } else {
                int delimeterCount = 0;
                char ch = '-';
                for (int x = 0; x < sinceDate.length(); x++) {
                    if (sinceDate.charAt(x) == ch) {
                        delimeterCount++;
                    }
                }
                if (delimeterCount != 2) {
                }
            }
        }
        return "";
    }

    public static boolean integerIsNull(Integer intVal) {
        return intVal == null;
    }

    public static boolean isNegative(Integer value) {
        return value < 1;
    }

    /**
     * this method use to validate url
     *
     * @param urlStr
     * @return
     */
    public static boolean urlValidation(String urlStr) {
        Pattern p = Pattern.compile(CommonConstants.REGEX_URL_VALIDATION);
        Matcher matcher = p.matcher(urlStr);
        return matcher.find();
    }

    /**
     * this method use to validate password
     *
     * @param passwordStr
     * @return
     */
    public static boolean passwordValidation(String passwordStr) {
        Pattern p = Pattern.compile(CommonConstants.REGEX_PASSWORD_VALIDATION);
        Matcher matcher = p.matcher(passwordStr);
        return matcher.find();
    }

    /**
     * this method use to email url
     *
     * @param emailStr
     * @return
     */
    public static boolean emailValidation(String emailStr) {
        Pattern p = Pattern.compile(CommonConstants.REGEX_EMAIL_VALIDATION);
        Matcher matcher = p.matcher(emailStr);
        return matcher.find();
    }

    /**
     * this method use to email url
     *
     * @param nic
     * @return
     */
    public static boolean validNic(String nic){
        if(nic.matches(CommonConstants.REGEX_NIC1_VALIDATION) || nic.matches(CommonConstants.REGEX_NIC2_VALIDATION))
            return false;
        return true;
    }

    /**
     * this method use to validate input numbers
     *
     * @param strNum
     * @return
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isValidMobile(String mobileNum){
        final String MOBILE = "^07[0-9]{8}$";
        final String TELEPHONE = "^011[0-9]{7}$";
        if(mobileNum.matches(CommonConstants.REGEX_MOBILE_VALIDATION)) {
            return false;
        }
        else if (mobileNum.matches(CommonConstants.REGEX_TELEPHONE_VALIDATION)){
            return false;
        }
        return true;
    }

    /**
     * this method use to validate char
     * @param strChar
     * @return
     */
    public static boolean isCharOnly(String strChar){
        Pattern p = Pattern.compile(CommonConstants.REGEX_CHAR_VALIDATION);
        Matcher matcher = p.matcher(strChar);
        return matcher.find();
    }

    public static boolean isValidOneDate(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(DateTimeUtil.FORMAT_DATE.toString());
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidDate(String fromDate, String toDate) {
        LocalDateTime firstDate = null;
        LocalDateTime secondDate = null;
        if (!CommonValidation.stringNullValidation(fromDate)) {
            firstDate = DateTimeUtil.getFormattedDateTime(fromDate);
        } else {
            firstDate = DateTimeUtil.getSriLankaTime();
        }
        if (!CommonValidation.stringNullValidation(toDate)) {
            secondDate = DateTimeUtil.getFormattedDateTime(toDate);
        }
        return secondDate.isAfter(firstDate);
    }

}
