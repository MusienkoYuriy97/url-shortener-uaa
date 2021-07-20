package by.solbegsoft.urlshorteneruaa.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WhiteListTest {
    @Autowired
    private WhiteList whiteList;

    @Test
    void get() {
        assertEquals(whiteList.get().getClass(), String[].class);
    }
}