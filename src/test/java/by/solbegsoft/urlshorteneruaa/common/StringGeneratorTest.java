package by.solbegsoft.urlshorteneruaa.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringGeneratorTest {
    private static final int LENGTH = 12;

    @Test
    void generate() {
        String generate = StringGenerator.generate(LENGTH);
        assertTrue(generate.matches("^[a-zA-Z0-9]*$")
                && (generate.length() == LENGTH));
    }
}