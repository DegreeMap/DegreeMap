package com.degreemap.DegreeMap.utility;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoderUtil {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean matches(String candidate, String hashed) {
        return BCrypt.checkpw(candidate, hashed);
    }
}

