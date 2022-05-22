import ps_calc

def main():
    """
    home = {PowerSchool login page url}
    grades = {PowerSchool homepage url}
    """
    home = "https://www.example.com"
    grades = "https://www.example.com"

    """
    LUNCH_CO_0 = "frn={numbers}"
    LUNCH_CO_1 = "frn={numbers}"

    To find {numbers}, go to PowerSchool website and open Inspect Element. Use ctrl+shift+c to use the selector.
    In your lunch period, click on the [ i ] symbol if it shows up (if you see none, then just set the two variables
    to empty strings).
    This should be an <a> tag. In the href attribute, look for "...frn=...". Copy this as the string. Repeat for any 
    other instances of [ i ] ONLY found in Lunch and Co.
    """
    LUNCH_CO_0 = "frn=123456789"
    LUNCH_CO_1 = "frn=123456789"

    """
    class1 = [{mp1score}, {mp2score}, {mp3score}, {mp4score}]
    class2 = [{mp1score}, {mp2score}, {mp3score}, {mp4score}]

    Size of both lists depends on number of marking periods that have elapsed in the school year.
    """
    class1 = [100, 100, 100, 100]
    class2 = [100, 100, 100, 100]
    
    """
    As an example, you may
    CALL (some variation of) THIS:
    raw = ps_calc.get_data(home, grades, LUNCH_CO_0, LUNCH_CO_1, read_from_file=True, file="path/to/file.html")
    
    OR THIS:
    raw = ps_calc.get_data(home, grades, LUNCH_CO_0, LUNCH_CO_1, read_from_file=False)

    Refer to the docs for what you should call to suit your needs. Your actual params may be different than the example.
    Same goes for everything below.
    """
    raw = ps_calc.get_data(home, grades, LUNCH_CO_0, LUNCH_CO_1, read_from_file=False)

    ps_calc.make_csv(raw, 4, class1, class2)
    df = ps_calc.load_df()
    ps_calc.calc_qpa(df["MP1"], sci_period=[1, 2], missing=0)
    ps_calc.calc_qpa(df["MP2"], sci_period=[1, 2], missing=1)
    ps_calc.calc_qpa(df["MP3"], sci_period=[1, 2], missing=2)
    ps_calc.calc_qpa(df["MP4"], sci_period=[1, 2], missing=3)

if __name__ == "__main__":
    main()
