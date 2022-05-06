from bs4 import BeautifulSoup
from selenium import webdriver
import csv
import pandas as pd
import re
import time

def get_data(
    ps_url: str = None,
    grades_url: str = None,
    lunch_co_0: str = None,
    lunch_co_1: str = None,
    read_from_file = False,
    file = "in.html",
    browser = "f",
    login_delay = 7,
) -> list:
    soup = None
    if read_from_file:
        fp = open(file, "r")
        soup = BeautifulSoup(fp, "html5lib")
        fp.close()
    else:
        if browser == "f":
            driver = webdriver.Firefox()
        elif browser == "c":
            driver = webdriver.Chrome()
        driver.get(ps_url)
        time.sleep(login_delay)
        driver.get(grades_url)
        soup = BeautifulSoup(driver.page_source, "html5lib")
        driver.close()
    
    # regex go brrr
    try:
        return soup.find("table", class_="linkDescList grid").tbody.find_all(
            href=lambda _href : _href and (
                re.compile("fg=M1").search(_href) or
                re.compile("fg=M2").search(_href) or
                re.compile("fg=M3").search(_href) or
                re.compile("fg=M4").search(_href) and not (
                    re.compile(lunch_co_0).search(_href) or
                    re.compile(lunch_co_1).search(_href)
                )
            )
        )
    except AttributeError:
        raise AttributeError("get_data() likely failed and returned None")

def make_csv(raw_grades: list, mp_amt: int, *missing_grades) -> None:
        with open("python/temp.csv", "w", encoding="UTF8", newline="") as fp:
            writer = csv.writer(fp)
            writer.writerow(["MP1", "MP2", "MP3", "MP4"])
            
            # classes is a 2D list, mp_grades is temp list for each course
            classes = []
            mp_grades = []
            for grade in raw_grades:
                grade_txt = grade.get_text(strip=True)
                mp_grades.append("0" if grade_txt == "[ i ]" else grade_txt)
                if len(mp_grades) == mp_amt:
                    _ = mp_amt
                    while _ != 4:
                        mp_grades.append("0")
                        _ += 1
                    classes.append(mp_grades)
                    mp_grades = []
            for grade in missing_grades:
                classes.append(grade)
            writer.writerows(classes)

def load_df() -> pd.DataFrame:
    return pd.read_csv("python/temp.csv")

def calc_qpa(df: pd.DataFrame, sci_period=[0], missing=0) -> float:
    credit = (len(df)-missing) * 1.25
    for _ in sci_period:
        credit += 0.25

    df *= 1.25
    for p in sci_period:
        df.loc[p] *= 1.2

    qpa = df.sum()/credit
    print(f"{df.name} QPA: {round(qpa, 2)}")
    return qpa
