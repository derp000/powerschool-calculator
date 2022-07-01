package com.derp000.pscalculator;

public class QPA extends GradeSystem {
    private final int MP_NUM;
    private static int COURSES = 8;
    private static double FIVE_CREDIT = 1.25;
    private static double SIX_CREDIT = 0.25;
    private static double SIX_CREDIT_QP = 1.2;

    public QPA(PSFetcher p, int mp, int totalElapsedMPs) {
        super(p, mp);
        MP_NUM = totalElapsedMPs;
    }

    @Override
    public double calculate(int scienceCoursePeriod) {
        return calculate(scienceCoursePeriod, 0);
    }

    public double calculate(int scienceCoursePeriod, int missingCourses) {
        double[][] qpa = getP().getGrades();

        double credit = FIVE_CREDIT * (COURSES-missingCourses) + SIX_CREDIT;

        multiply(qpa, FIVE_CREDIT);
        for (int i = 0; i < MP_NUM; i++) {
            qpa[scienceCoursePeriod][i] *= SIX_CREDIT_QP;
        }

        setGrade(sum(qpa, getMP()-1)/credit);
        return getGrade();
    }

    public double calculate(int[] scienceCoursePeriods, int missingCourses) {
        double[][] qpa = getP().getGrades();

        double credit = FIVE_CREDIT * (COURSES-missingCourses) + SIX_CREDIT;

        multiply(qpa, FIVE_CREDIT);
        for (int p : scienceCoursePeriods) {
            for (int i = 0; i < MP_NUM; i++) {
                qpa[p][i] *= SIX_CREDIT_QP;
            }
        }

        setGrade(sum(qpa, getMP()-1)/credit);
        return getGrade();
    }

    private static void multiply(double[][] a, double factor) {
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[0].length; j++)
                a[i][j] *= factor;
    }

    public static void setCOURSES(int courses) {
        COURSES = courses;
    }

    public static void setFiveCredit(double fiveCredit) {
        FIVE_CREDIT = fiveCredit;
    }

    public static void setSixCredit(double sixCredit) {
        SIX_CREDIT = sixCredit;
    }

    public static void setSixCreditQP(double sixCreditQP) {
        SIX_CREDIT_QP = sixCreditQP;
    }

    @Override
    protected String getName() {
        return "QPA";
    }
}
