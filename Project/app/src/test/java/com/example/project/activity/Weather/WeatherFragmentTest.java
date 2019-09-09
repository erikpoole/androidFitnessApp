package com.example.project.activity.Weather;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class WeatherFragmentTest {

    @Test
    public void roundStringWithMultiplier() {
        ArrayList<String> inputValues = new ArrayList<String>(
                Arrays.asList(
                        "5.1",
                        "0.0",
                        "5.9",
                        "10")
        );
        ArrayList<String> expectedValues = new ArrayList<String>(
                Arrays.asList(
                        "5",
                        "0",
                        "6",
                        "10")
        );
        for (int i = 0; i < inputValues.size(); i++) {
            assertEquals(WeatherFragment.roundStringWithMultiplier(inputValues.get(i), 1), expectedValues.get(i));
        }
    }
}