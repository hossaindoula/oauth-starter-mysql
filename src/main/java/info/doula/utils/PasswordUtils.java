package info.doula.utils;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by saad on 10/1/2016.
 */
public class PasswordUtils {
    private static final Random RANDOM = new SecureRandom();
    private static final int PASSWORD_LENGTH = 8;

    private static final String ALPHA_NUMERIC_PASS_POlICY_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,32})";

    private PasswordUtils() {
    }
 
    /**
     * Generate a random String suitable for use as a temporary password.
     *
     * @return String suitable for use as a temporary password
     */
    public static String generateRandomPassword() {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String pw = randomizePassword();
        if (!passwordCompliantWithPolicy(pw)) {
            pw = generateRandomPassword();
        }
        return pw;
    }

    private static String randomizePassword() {
        String pw = "";
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789@" + 1 + Math.random();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            pw += letters.substring(index, index + 1);
        }
        return pw;
    }

    public static boolean passwordCompliantWithPolicy(final String password) {
        Pattern pattern = Pattern.compile(ALPHA_NUMERIC_PASS_POlICY_PATTERN);
        Matcher matcher = pattern.matcher(password);

        boolean isAlphaNumeric = matcher.matches();
        boolean isConsecutive = isCharacterConsecutive(password);
        boolean isRepeated = isCharacterRepeated(password);

        return (isAlphaNumeric && !isConsecutive && !isRepeated);
    }

    private static boolean isCharacterConsecutive(String password) {
        Pattern pattern = Pattern.compile(".*([a-zA-Z0-9])\\1{2,}.*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }

    private static Boolean isCharacterRepeated(String password) {
        char[] characters = password.toCharArray();
        // build HashMap with character and number of times they appear in String
        Map<Character, Integer> charMap = new HashMap<Character, Integer>();
        for (Character ch : characters) {
            if (charMap.containsKey(ch)) {
                charMap.put(ch, charMap.get(ch) + 1);
            } else {
                charMap.put(ch, 1);
            }
        }
        Set<Map.Entry<Character, Integer>> entrySet = charMap.entrySet();
        for (Map.Entry<Character, Integer> entry : entrySet)
        //Check No character should be repeated thrice in a password
        {
            if (entry.getValue() > 2) {
                return true;
            }
        }
        return false; //If you reach here that means there are no character repeated thrice in a password therefore return false.
    }
}
