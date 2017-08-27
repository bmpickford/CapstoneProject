package com.app.capstone.app;

import com.app.capstone.app.Course.GPACalculator;

import org.junit.Test;

import static org.junit.Assert.*;


public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void gpa_calc_constructor_exists() throws Exception {
        GPACalculator gpaCalc = new GPACalculator(5.5, 5, 24, 24);
        assertEquals(5.5, gpaCalc.getGoalGPA(), 0.01);
    }

    @Test
    public void gpa_calc_constructor_correct() throws Exception {
        GPACalculator gpaCalc = new GPACalculator(5, 5, 48, 48);
        assertEquals(5, gpaCalc.calculate(), 0.01);
    }

    @Test
    public void gpa_calc_constructor_correct2() throws Exception {
        GPACalculator gpaCalc = new GPACalculator(5.5, 5, 48, 48);
        assertEquals(6, gpaCalc.calculate(), 0.01);
    }
}