package com.gmail.volodymyrdotsenko;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Volodymyr Dotsenko on 31.05.16.
 */
public class MainTest {

    @Test
    public void shouldReadFileToString() {
        String result = null;

        try {
            result = Main.readFileToString(getClass().getClassLoader().getResource("test.text").getPath());

            assertNotNull(result);
            assertEquals(12, result.length());
        } catch (IOException e) {
            fail("WHOOPS! " + e.getMessage());
        }
    }

    @Test
    public void wordProcessorTest() {
        WordProcessor wp = new WordProcessor("boats. alloo, aoooo");
        wp.process();
        wp.getResults().forEach((k, v) -> {
            assertEquals("({a, o}, 5)->3.3", v.toString());
        });
    }
}