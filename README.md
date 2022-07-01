# powerschool-calculator

A Java/Python API to calculate grades from PowerSchool.

## Java Dependencies

**`Java`**
- `Jsoup`
- `Selenium`

Use provided `PowerSchoolCalculator.jar`. Dependencies used in `.jar` file and `src` directory are simply provided for reference.

## Java API Reference

TODO; API reference will likely be generated into html and hosted on GitHub.

Java API javadoc comments are incomplete.

## Python Dependencies

**`Python`**
- `bs4` and `html5lib`
- `selenium`
- `pandas`
- `pipenv`

Install all necessary python dependencies via `requirements.txt` using command `pipenv install -r requirements.txt`.

**`Selenium`**
- `geckodriver` (recommended v0.30.0) and/or `chromedriver` (recommended v98.0.4758.80)

Drivers are expected to be in the same directory as the `ps_calc` module, but an absolute path may be specified.

`geckodriver` is recommended since in python, its logs are outputted to a logfile rather than to the terminal by default. 

Refer to `format.py` as a rough template for how you should use this module.


## Python API Reference

### `get_data() -> bs4.element.ResultSet`

The following arguments are accepted:

**ps_url : *str, default `None` (required if `read_from_file = False`)***

&nbsp;&nbsp;&nbsp;&nbsp;PowerSchool login page for WebDriver to access.

**grades_url : *str, default `None` (required if `read_from_file = False`)***

&nbsp;&nbsp;&nbsp;&nbsp;Homepage of grades that WebDriver accesses.

**lunch_co_0, lunch_co_1 : _str, default `None` (always required)_**

&nbsp;&nbsp;&nbsp;&nbsp;`<a>` tag `href` attribute in format of `frn={numbers}`.

**read_from_file : _boolean, default `False`_**

&nbsp;&nbsp;&nbsp;&nbsp;Selects input method from direct file or via `selenium`.

**file : _str, default `"in.html"`_**

&nbsp;&nbsp;&nbsp;&nbsp;Selects file relative/absolute name if file input method is chosen.

**browser : *str, default `"f"`***

&nbsp;&nbsp;&nbsp;&nbsp;Selects browser for Selenium WebDriver to use. Set `browser="c"` for `chromedriver`.

**login_delay : *int, default `7`***

&nbsp;&nbsp;&nbsp;&nbsp;Time allocated for user to log in.

### `fmt_data() -> CSV file`

The `temp.csv` file deletion is **NOT** handled.

The following arguments are accepted:

**raw_grades : *bs4.element.ResultSet (always required)***

&nbsp;&nbsp;&nbsp;&nbsp;Returned by `get_data()`

**mp_amt : *int (always required)***

&nbsp;&nbsp;&nbsp;&nbsp;Marking periods elapsed in school year.

**\*missing_grades : *arbitrary number of int arrays***

&nbsp;&nbsp;&nbsp;&nbsp;Manually adds and formats data not visible on PowerSchool homepage.

### `load_df() -> pandas DataFrame`

Returns a pandas DataFrame using `temp.csv`. Requires no arguments.

### `calc_qpa() -> IO, float`

QPA is printed to IO as a decimal rounded to 2 places. QPA is also returned as a float.

The following arguments are accepted:

**df : *pandas DataFrame column (always required)***

&nbsp;&nbsp;&nbsp;&nbsp;DataFrame is returned by `load_df()`. Accepted columns are MP1, MP2, MP3, and MP4.

&nbsp;&nbsp;&nbsp;&nbsp;_Example: if `my_df` is the DataFrame object, `my_df["MP1"]` accesses MP1 column_

**sci_period : *[int], default `[0]`***

&nbsp;&nbsp;&nbsp;&nbsp;Class periods for 6-credit science courses. Find this index by counting top-down on Home page, ignoring period 5 and 6.

**missing : *int, default `0` (always required)***

&nbsp;&nbsp;&nbsp;&nbsp;Number of classes which have not given any grades for a particular marking period.

**fmt_print : *boolean, default `False`***

&nbsp;&nbsp;&nbsp;&nbsp;Number of classes which have not given any grades for a particular marking period.
