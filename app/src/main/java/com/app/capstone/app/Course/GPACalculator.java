package com.app.capstone.app.Course;

import java.text.DecimalFormat;

/**
 * Created by benjamin on 27/08/17.
 */

public class GPACalculator {
    private double goalGPA;
    private double curGPA;
    private int cpLeft;
    private int cpDone;
    private int unitsDone;
    private int unitsLeft;
    private int unitsTotal;

    public GPACalculator(double goalGPA, double gpa, double cpLeft, double cpDone){
        this.goalGPA = goalGPA;
        this.curGPA = gpa;
        this.cpLeft = (int) cpLeft;
        this.cpDone = (int)cpDone;
        calculateUnits();
    }

    public double calculate(){
        if(unitsLeft <= 0){
            return 0;
        }
        double g = ((goalGPA * unitsTotal) - (curGPA * unitsDone)) / unitsLeft;
        DecimalFormat df = new DecimalFormat("#.00");

        double r = 0;

        try {
            r = Double.parseDouble(df.format(g));
        } catch(Exception e){
            System.out.println(g);
            System.out.println("Err:" + e.toString());
        }

        return r;
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
