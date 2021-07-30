package demo.codingnomads.co.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;


public class CookieHelper {

    public static Cookie generateCookie(String name, int length, int maxAge) {
        Random random = new Random();
        StringBuilder cookieValue = new StringBuilder();

        char charToAdd;
        for(int i = 0; i < length; i++) {

            do {
                charToAdd = (char) (48 + random.nextInt(42));
            }while (charToAdd == ';');

            cookieValue.append(charToAdd);
        }

       Cookie newCookie = new Cookie(name, cookieValue.toString());
        newCookie.setMaxAge(maxAge);
        return newCookie;
    }

    public static Cookie assignCookie(HttpServletResponse response, String cookieName, int length, int maxAge) {
        Cookie newCookie = generateCookie(cookieName, length, maxAge);
        response.addCookie(newCookie);
        return newCookie;
    }

    public static void removeCookie(HttpServletResponse response, String cookieName) {
        Cookie cookieToRemove = new Cookie(cookieName, "1");
        cookieToRemove.setMaxAge(0);
        response.addCookie(cookieToRemove);
    }

}
