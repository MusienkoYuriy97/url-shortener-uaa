package by.solbegsoft.urlshorteneruaa.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class WhiteListTest {
    @Test
    void get() {
        assertEquals(WhiteList.get().getClass(), String[].class);
    }
}