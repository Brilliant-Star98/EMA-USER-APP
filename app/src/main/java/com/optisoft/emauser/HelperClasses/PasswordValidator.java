package com.optisoft.emauser.HelperClasses;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by OptiSoft_A on 4/10/2018.
 */

public class PasswordValidator {
    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,16})";

    public PasswordValidator(){
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password){
        matcher = pattern.matcher(password);
        return matcher.matches();

    }
}
