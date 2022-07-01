package com.derp000.pscalculator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class PSFetcher {
    private static String driverFilePath;
    private static String loginPage;
    private static String homePage;
    private static char browserType = 'f';

    private static int COURSES = 8;
    private static int MP_NUM = 4;
    private final Document doc;
    private final double[][] grades;

    /**
     * Class constructor that uses a Selenium WebDriver to gather necessary html.
     * @param sleepTime delay between WebDriver visiting PowerSchool login page and homepage
     * @throws InterruptedException if current Thread is interrupted
     * @see Thread#sleep(long)
     * @see WebDriver
     */
    public PSFetcher(long sleepTime) throws InterruptedException {
        WebDriver driver;
        if (browserType == 'f') {
            System.setProperty("webdriver.gecko.driver", driverFilePath);
            System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "null");
            driver = new FirefoxDriver();
        }
        else if (browserType == 'c')
            driver = new ChromeDriver();
        else
            throw new IllegalArgumentException("Specified browserType is invalid.");
        driver.get(loginPage);
        Thread.sleep(sleepTime * 1000);
        driver.get(homePage);
        doc = Jsoup.parse(driver.getPageSource());
        driver.quit();

        grades = new double[COURSES][MP_NUM];
    }

    /**
     * Class constructor specifying PowerSchool html source File.
     * @param input html File object containing PowerSchool homepage source
     * @throws IOException
     */
    public PSFetcher(File input) throws IOException {
        doc = Jsoup.parse(input, "UTF-8", "");
        grades = new double[COURSES][MP_NUM];
    }

    /**
     * Parses source html.
     * @param removeKeys removes Lunch and Co periods
     * @param missingCoursesGrades grades not found on homepage. Refer to template.
     * @throws IllegalArgumentException if classes missing exceeds {@link #COURSES}
     * @see Template
     */
    public void formatData(ArrayList<String> removeKeys, int[][] missingCoursesGrades) throws IllegalArgumentException {
        if (removeKeys != null) cleanData(removeKeys);

        Elements table = doc.select("table[aria-describedby=tableDesc]");
        Elements rawGrades = table.select("a[href^=scores.html]");

        System.out.println(rawGrades.size());

        if (missingCoursesGrades != null && COURSES-missingCoursesGrades.length-rawGrades.size()/MP_NUM < 0)
            throw new IllegalArgumentException("Number of missingClasses exceeds allowed amount");

        String gradeString;
        int course = 0;
        for (int i = 0, n = rawGrades.size(); i < n; i++) {
            gradeString = rawGrades.get(i).text();
            if (!gradeString.equals("[ i ]"))
                grades[course][i%4] = Integer.parseInt(gradeString);
            if (i%4 == 3) course++;
        }

        if (missingCoursesGrades == null) return;
        else appendMissingCourses(missingCoursesGrades, course);
    }

    private void cleanData(ArrayList<String> al) {
        String regex = "a[href~=.*(";
        for (String key : al)
            regex += key + "|";
        regex = regex.substring(0, regex.length() - 1);
        regex += ")]";

        Elements remove = doc.select(regex);
        remove.remove();
        remove = doc.select("a[href~=.*fg=S]");
        remove.remove();
    }

    private void appendMissingCourses(int[][] missingCoursesGrades, int course) {
        int len;
        for (int[] missingCourse : missingCoursesGrades) {
            len = missingCourse.length;
            if (len != 4)
                throw new IllegalArgumentException("Column length must be 4");
            for (int i = 0; i < len; i++) {
                grades[course][i%4] = missingCourse[i];
                if (i%4 == 3) course++;
            }
        }
    }

    /**
     * @return formatted 2D array of grades.
     */
    public double[][] getGrades() {
        return copyArray(grades);
    }

    private double[][] copyArray(double[][] a) {
        double[][] copy = new double[a.length][];
        for(int i = 0; i < a.length; i++)
            copy[i] = a[i].clone();

        return copy;
    }

    /**
     * @param driverFilePath absolute or relative path of {@link WebDriver}
     */
    public static void setDriverFilePath(String driverFilePath) {
        PSFetcher.driverFilePath = driverFilePath;
    }

    /**
     * @param loginPage URL of PowerSchool login page
     */
    public static void setLoginPage(String loginPage) {
        PSFetcher.loginPage = loginPage;
    }

    /**
     * @param homePage URL of PowerSchool homepage
     */
    public static void setHomePage(String homePage) {
        PSFetcher.homePage = homePage;
    }

    /**
     * Defaults to <code>'f'</code> (Firefox geckodriver).
     * @param browserType <code>'c'</code> for Chrome and <code>'f'</code> for Firefox
     */
    public static void setBrowserType(char browserType) {
        PSFetcher.browserType = browserType;
    }

    /**
     * @return number of courses as specified
     * @see #setCourses(int)
     */
    public static int getCourses() {
        return COURSES;
    }

    /**
     * Default is 8 courses.
     * @param courses total number of courses as listed on PowerSchool
     */
    public static void setCourses(int courses) {
        COURSES = courses;
    }

    /**
     * @return number of marking periods as specified
     * @see #setMPNum(int)
     */
    public static int getMPNum() {
        return MP_NUM;
    }

    /**
     * Default is 4 marking periods.
     * @param mpNum number of elapsed marking periods
     */
    public static void setMPNum(int mpNum) {
        MP_NUM = mpNum;
    }

    /**
     * @return lists of grades formatted as multi-line String
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (double[] grade : grades)
            s.append(Arrays.toString(grade)).append('\n');
        return s.toString();
    }
}
