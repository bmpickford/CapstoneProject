package com.app.capstone.app.Course;

/**
 * Created by root on 27/08/17.
 */

public class GPACalculator {
    private double goalGPA;
    private double curGPA;
    private int cpLeft;
    private int cpDone;
    private int unitsDone;
    private int unitsLeft;
    private int unitsTotal;

    public GPACalculator(double goalGPA, double gpa, int cpLeft, int cpDone){
        this.goalGPA = goalGPA;
        this.curGPA = gpa;
        this.cpLeft = cpLeft;
        this.cpDone = cpDone;
        calculateUnits();
    }

    public double calculate(){
        double g = ((goalGPA * unitsTotal) - (curGPA * unitsDone)) / unitsLeft;
        return g;
    }

    private void calculateUnits(){
        this.unitsLeft = cpLeft / 12;
        this.unitsDone = cpDone / 12;
        this.unitsTotal = this.unitsLeft + this.unitsDone;
    }

    public double getGoalGPA(){
        return goalGPA;
    }
}
