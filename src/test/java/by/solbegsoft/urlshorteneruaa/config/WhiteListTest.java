package by.solbegsoft.urlshorteneruaa.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WhiteListTest {

    @Test
    void get() {
        assertEquals(WhiteList.get().getClass(), String[].class);
    }
}