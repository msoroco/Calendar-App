package model;

import exceptions.UnsupportedYearException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of calendar years.
 */
public class Calendar implements Writable {

    private List<Year> calendar;

    public Calendar() {
        this.calendar = new ArrayList<>();
    }

    //MODIFIES: this
    //EFFECTS: if year exists in this, returns that year, else makes a new year
    public Year findYearInCalendar(int i) throws UnsupportedYearException {
        if (i < 2000 || i > 2999) {
            throw new UnsupportedYearException(i);
        } else {
            int index = containsYear(i);
            if (index != -1) {
                return getYear(index);
            } else {
                Year y = newYear(i);
                calendar.add(y);
                return y;
            }
        }
    }

    //EFFECTS: makes a new year
    private Year newYear(int i) {
        return new Year(i);
    }

    //EFFECTS: gets the year at the given index in this
    private Year getYear(int index) {
        return calendar.get(index);
    }

    //EFFECTS: finds year in this, returns the index of that year in this, else returns -1.
    protected int containsYear(int i) throws UnsupportedYearException {
        if (i < 2000 || i > 2999) {
            throw new UnsupportedYearException(i);
        } else {
            int k = -1;
            for (Year y : calendar) {
                if (y.getYear() == i) {
                    k = k + 1;
                    return k;
                }
                k = k + 1;
            }
            return -1;
        }
    }

    //EFFECTS: returns the number of years in this calendar
    public int getNumYears() {
        return calendar.size();
    }

    //EFFECTS: returns calendar as a Json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("years", calendarToJson());
        return json;
    }

    // EFFECTS: returns years in this calendar as a JSON array
    private JSONArray calendarToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Year y : calendar) {
            jsonArray.put(y.yearToJson());
        }
        return jsonArray;
    }
}
