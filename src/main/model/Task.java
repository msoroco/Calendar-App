package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Task with a name, the range of time in a day, description, and tag for organization
 */

public class Task {

    private String title;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String description;
    private List<String> tags = new ArrayList<>();

    //CONSTRUCTOR
    public Task(String title, int hour1, int min1, int hour2, int min2, String description) {
        this.title = title;
        this.startHour = hour1;
        this.startMinute = min1;
        this.endHour = hour2;
        this.endMinute = min2;
        this.description = description;
    }


    //REQUIRES: A time between 00:00 and 23:59.
    //MODIFIES: this
    //EFFECTS: changes the time frame of this task to new time frame returns true if successful, else false
    public boolean changeTime(int h1, int m1, int h2, int m2) {
        if (validTimeFrame(h1, m1, h2, m2)) {
            setStartHour(h1);
            setStartMinute(m1);
            setEndHour(h2);
            setEndMinute(m2);
            return true;
        } else {
            return false;
        }
    }

    //EFFECTS: calculates the duration of the time frame in minutes.
    public double getDuration() {
        double mi = startMinute * 1.0 / 60;
        double mf = endMinute * 1.0 / 60;
        double hi = startHour;
        double hf = endHour;
        double i = hi + mi;
        double f = hf + mf;
        double diff = f - i;
        return diff * 60;
    }


    //MODIFIES: this
    //EFFECTS: removes the given tag from the task, return true if successful, else false.
    public boolean removeTag(String tag) {
        if (tags.contains(tag)) {
            tags.remove(tag);
            return true;
        } else {
            return false;
        }
    }

    //MODIFIES: this
    //EFFECTS: adds given tag to this
    public void addTag(String tag) {
        tags.add(tag);
    }

    //MODIFIES: this
    //EFFECTS: sets the title of this task to given title
    public void setTitle(String t) {
        title = t;
    }

    //REQUIRES: hours in [0, 23], minutes in [0, 59]
    //MODIFIES: this
    //EFFECTS: returns true if end time is after start time, hence valid time frame
    public boolean validTimeFrame(int h1, int m1, int h2, int m2) {
        if (h2 < h1 || h2 > 23 || h1 < 0) {
            return false;
        } else {
            return (h2 != h1 || m2 > m1) && m1 <= 59 && m1 >= 0 && m2 <= 59 && m2 >= 0;
        }
    }

    //GETTER
    public String getTitle() {
        return this.title;
    }

    //GETTER
    public List<String> getTags() {
        return tags;
    }

    //GETTER
    public String getDescription() {
        return description;
    }

    //SETTER
    public void setDescription(String description) {
        this.description = description;
    }

    //GETTER
    public int getStartHour() {
        return startHour;
    }

    //REQUIRES: A time between 00 and 23
    //SETTER
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    //GETTER
    public int getStartMinute() {
        return startMinute;
    }

    //REQUIRES: A time between 00 and 59
    //SETTER
    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    //GETTER
    public int getEndHour() {
        return endHour;
    }

    //REQUIRES: A time between 00 and 23
    //SETTER
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    //GETTER
    public int getEndMinute() {
        return endMinute;
    }

    //REQUIRES: A time between 00 and 59
    //SETTER
    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    //EFFECTS: returns task as a JSON object
    protected JSONObject taskToJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("startHour", startHour);
        json.put("startMinute", startMinute);
        json.put("endHour", endHour);
        json.put("endMinute", endMinute);
        json.put("description", description);
        json.put("tags", tags);
        return json;
    }
}
