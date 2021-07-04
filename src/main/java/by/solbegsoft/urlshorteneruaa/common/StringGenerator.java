package by.solbegsoft.urlshorteneruaa.common;

import java.util.Random;

public class StringGenerator {
    private static final int LEFT = 48;
    private static final int RIGHT = 122;

    public static String generate(final int expectedLength) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < expectedLength; i++) {
            int dec = 0;
            while ((dec < LEFT) || (dec >= 58 && dec <= 64) || (dec >= 91 && dec <= 96)){
                dec = random.nextInt(RIGHT + 1);
            }
            char c = (char) dec;
            result.append(c);
        }
        return String.valueOf(result);
    }
}
