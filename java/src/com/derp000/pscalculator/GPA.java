package com.derp000.pscalculator;

import java.util.HashMap;

public class GPA extends GradeSystem {
    private final HashMap<Integer, Double> scale;

    public GPA(PSFetcher p, int mp) {
        super(p, mp);
        scale = new HashMap<>();
        scale.put(10, 4.0);
        scale.put(0, 0.0);
        for (int i = 0; i <= 4; i++)
            scale.put(9-i, 4.0-i);
    }

    public GPA(PSFetcher p, int mp, HashMap<Integer, Double> scale) {
        super(p, mp);
        this.scale = scale;
    }

    @Override
    public double calculate(int missingCourses) {
        double[][] grades = getP().getGrades();

        double sum = 0;
        Double courseGPA = null;
        for (double[] course : grades) {
            courseGPA = scale.get(format(course[getMP() - 1]));
            if (courseGPA != null) {
                sum += courseGPA;
            }
        }

        setGrade(sum/(grades.length-missingCourses));

        return getGrade();
    }

    private int format(double a) {
        int fmt = ((int) a - (int) a % 10) / 10;
        if (fmt >= 6)
            return fmt;
        return 0;
    }

    @Override
    protected String getName() {
        return "GPA";
    }
}
