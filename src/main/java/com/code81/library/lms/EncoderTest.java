package com.code81.library.lms;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword1 = "adminpass";
        String rawPassword2 = "libpass";
        String rawPassword3 = "staffpass";

        String encodedPassword1 = encoder.encode(rawPassword1);
        String encodedPassword2 = encoder.encode(rawPassword2);
        String encodedPassword3 = encoder.encode(rawPassword3);

        System.out.println("adminpass hash: " + encodedPassword1);
        System.out.println("libpass hash: " + encodedPassword2);
        System.out.println("staffpass hash: " + encodedPassword3);

        // يمكنك أيضًا اختبار كلمة مرور:
        // System.out.println("Matches 'adminpass'? " + encoder.matches("adminpass", encodedPassword1));
    }
}