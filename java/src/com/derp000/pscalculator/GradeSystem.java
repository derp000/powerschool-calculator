package com.derp000.pscalculator;

public class GradeSystem {
    private final PSFetcher p;
    private int mp;
    private double grade;

    public GradeSystem(PSFetcher p, int mp) {
        this.p = p;
        this.mp = mp;
    }

    public double calculate(int missingCourses) {
        double[][] grades = p.getGrades();
        grade = sum(grades, mp-1)/(grades.length-missingCourses);
        return grade;
    }

    protected static double sum(double[][] a, int mp) {
        double sum = 0;
        for (double[] b : a)
            sum += b[mp];
        return sum;
    }

    public PSFetcher getP() {
        return p;
    }

    public int getMP() {
        return mp;
    }

    public void setMP(int mp) {
        this.mp = mp;
    }

    public double getGrade() {
        return grade;
    }

    protected void setGrade(double grade) {
        this.grade = grade;
    }

    protected String getName() {
        return "Grade";
    }

    @Override
    public String toString() {
        return String.format("MP%d %s: %.2f\n", mp, getName(), grade);
    }
}
