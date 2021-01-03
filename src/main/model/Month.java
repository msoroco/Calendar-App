package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A month with a collection of days in order in that month.
 * <p>
 * Months are predetermined by the year its in and which month it is. As such there are limited and restricted
 * setter methods for months in the year. Months are not to be modified after they are generated.
 * <p>
 * Month codes are special number used in the arithmetic to determine the name of the days in the month (see Day Class).
 * As per https://cs.uwaterloo.ca/~alopez-o/math-faq/node73.html
 * <p>
 * Methods are included to calculate the number of days a month should have depending on the month and the year. As per
 * https://docs.microsoft.com/en-us/office/troubleshoot/excel/determine-a-leap-year
 */


public class Month {

    private static final int JAN_OCT_CODE = 1;
    private static final int FEB_MAR_NOV_CODE = 4;
    private static final int APR_JUL_CODE = 0;
    private static final int MAYCODE = 2;
    private static final int JUNCODE = 5;
    private static final int AUGCODE = 3;
    private static final int SEP_DEC_CODE = 6;

    private final Year year;
    private final int month;
    private final String name;
    private final List<Day> days;

    //REQUIRES: a number between 1 and 12.
    //CONSTRUCTOR
    public Month(int month, Year year) {
        this.month = month;
        this.year = year;
        this.days = new ArrayList<>();
        this.name = generateName(month);
        generateDays(year.getLeapYearCode());
    }

    //REQUIRES: a number between 1 and 12.
    //EFFECTS: returns the name of the month corresponding to that number.
    public String generateName(int month) {
        if (month <= 6) {
            return generateNameFirstHalf(month);
        } else {
            return generateNameSecondHalf(month);
        }
    }

    //REQUIRES: a number between 7 and 12.
    //EFFECTS: returns the name of the month corresponding to that number.
    private String generateNameSecondHalf(int month) {
        if (month == 7) {
            return "July";
        } else if (month == 8) {
            return "August";
        } else if (month == 9) {
            return "September";
        } else if (month == 10) {
            return "October";
        } else if (month == 11) {
            return "November";
        } else {
            return "December";
        }
    }

    //REQUIRES: a number between 1 and 6.
    //EFFECTS: returns the name of the month corresponding to that number.
    private String generateNameFirstHalf(int month) {
        if (month == 1) {
            return "January";
        } else if (month == 2) {
            return "February";
        } else if (month == 3) {
            return "March";
        } else if (month == 4) {
            return "April";
        } else if (month == 5) {
            return "May";
        } else {
            return "June";
        }
    }

    //EFFECTS: gets the code corresponding to this month
    public int getMonthCode() {
        if (month == 1 || month == 10) {
            return JAN_OCT_CODE;
        } else if (month == 2 || month == 3 || month == 11) {
            return FEB_MAR_NOV_CODE;
        } else if (month == 4 || month == 7) {
            return APR_JUL_CODE;
        } else if (month == 5) {
            return MAYCODE;
        } else if (month == 6) {
            return JUNCODE;
        } else if (month == 8) {
            return AUGCODE;
        } else {
            return SEP_DEC_CODE;
        }
    }

    //GETTER
    public List<Day> getDays() {
        return days;
    }

    //REQUIRES: The given number must be a day in the month
    //EFFECTS: returns the given day in this month.
    public Day getDay(int i) {
        return days.get(i - 1);
    }


    //MODIFIES: this
    //EFFECTS: depending on the month adds 31 days, 30 days, 28 days, or 29 days for february of leap year.
    public void generateDays(int c) {
        if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)) {
            for (int i = 1; i <= 31; i++) {
                Day d = new Day(i, this, this.year);
                days.add(d);
            }
        } else if ((month == 4 || month == 6 || month == 9 || month == 11)) {
            for (int i = 1; i <= 30; i++) {
                Day d = new Day(i, this, this.year);
                days.add(d);
            }
        } else if (c == 1) {
            for (int i = 1; i <= 29; i++) {
                Day d = new Day(i, this, this.year);
                days.add(d);
            }
        } else {
            for (int i = 1; i <= 28; i++) {
                Day d = new Day(i, this, this.year);
                days.add(d);
            }
        }
    }

    //EFFECTS: returns modified month code if month is January or February of leap year.
    public int applyLeapYearCode(int monthCode, int leapCode) {
        if (month == 1 || month == 2) {
            return monthCode - leapCode;
        } else {
            return monthCode;
        }
    }

    //EFFECTS: returns a the total duration in minutes in this month of Task with the given tag.
    public double getTotalDurationWithTag(String tag) {
        double duration = 0.0;
        for (Day d : days) {
            duration = duration + d.getTotalDurationWithTag(tag);
        }
        return duration;
    }

    //GETTER
    public String getName() {
        return name;
    }

    //EFFECTS: returns month as a JSON object
    protected JSONObject monthToJson() {
        JSONObject json = new JSONObject();
        json.put("month", month);
        json.put("days", daysToJson());
        return json;
    }

    // EFFECTS: returns days in this month as a JSON array
    private JSONArray daysToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Day d : days) {
            jsonArray.put(d.dayToJson());
        }
        return jsonArray;
    }
}
