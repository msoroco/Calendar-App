package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A calendar year with 12 months starting at January
 * <p>
 * This calendar only supports years in the 2000s.
 *
 * Once a year has been generated, months and days are not to be modified since the days and months of years
 * are defined. Setter methods have been modified to accommodate this.
 *
 * @author Mauricio
 */

public class Year {

    private int year;
    private List<Month> months;

    public Year(int year) {
        this.year = year;
        this.months = generateMonths();
    }

    //MODIFIES: this
    //EFFECTS: generates the 12 months in a year and adds it to this
    private List<Month> generateMonths() {
        months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Month m = new Month(i + 1, this);
            months.add(m);
        }
        return months;
    }

    //EFFECTS: returns the last two digits of the year.
    public int getYearCode() {
        String y = Integer.toString(year);
        y = y.substring(2, 4);
        return Integer.parseInt(y);
    }

    //EFFECTS: returns 1 if the year is a leap year, 0 otherwise.
    public int getLeapYearCode() {
        //stub
        if (0 == year % 4) {
            if (0 == year % 100) {
                if (0 == year % 400) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 1;
            }
        }
        return 0;
    }

    //REQUIRES: A number between 1 and 12
    //EFFECTS: gets that month from this year
    public Month getMonth(int i) {
        return months.get(i - 1);
    }

    //GETTER
    public int getYear() {
        return year;
    }

    //REQUIRES:
    //MODIFIES: this
    //EFFECTS: clears the months in this.
    // changes this year to given year. As a result all the months will be replaced with the appropriate months,
    //          and new days will be added to the new months as well.
    public void setYear(int year) {
        months.clear();
        this.year = year;
        this.months = generateMonths();
    }

    //GETTER:
    public List<Month> getMonths() {
        return months;
    }

    //EFFECTS: returns year as JSON object
    protected JSONObject yearToJson() {
        JSONObject json = new JSONObject();
        json.put("year", year);
        json.put("months", monthsToJson());
        return json;
    }

    // EFFECTS: returns months in this year as a JSON array
    private JSONArray monthsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Month m : months) {
            jsonArray.put(m.monthToJson());
        }
        return jsonArray;
    }
}
