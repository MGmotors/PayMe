package com.example.mscha.payme;

import com.example.mscha.payme.app.APIInteractor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    private APIInteractor apiInteractor;

    @Before
    public void before() {
        apiInteractor = new APIInteractor();
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}