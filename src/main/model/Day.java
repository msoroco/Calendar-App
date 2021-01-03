package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A Day with a list of tasks to be completed in that day.
 * The names of the days in the month are calculated using algorithm as per:
 * https://cs.uwaterloo.ca/~alopez-o/math-faq/node73.html
 * <p>
 * Days are not to be modified after they are generated as they are dependent on the month and the year.
 * As such the setter methods for Day are limited.
 */

public class Day {

    private List<Task> tasks;
    private int number;
    private String name;
    private Month month;
    private Year year;


    public Day(int num, Month month, Year year) {
        this.number = num;
        this.month = month;
        this.year = year;
        this.name = getDayName();
        tasks = new ArrayList<>();
    }

    //MODIFIES: this
    //EFFECTS: adds given task to this
    public void addTask(Task t) {
        tasks.add(t);
    }

    //REQUIRES: t to be contained in this
    //MODIFIES: this
    //EFFECTS: removes t from this
    public void removeTask(Task t) {
        tasks.remove(t);
    }

    //GETTER
    public List<Task> getTasks() {
        return tasks;
    }

    //EFFECTS: returns the name of the given day
    protected String getDayName() {
        int x = calculateDay();
        if (x == 1) {
            return "Sunday";
        }
        if (x == 2) {
            return "Monday";
        }
        if (x == 3) {
            return "Tuesday";
        }
        if (x == 4) {
            return "Wednesday";
        }
        if (x == 5) {
            return "Thursday";
        }
        if (x == 6) {
            return "Friday";
        } else {
            return "Saturday";
        }
    }

    //EFFECTS: calculates the code corresponding to the name of the day
    private int calculateDay() {
        int yy = year.getYearCode();
        int mm = month.getMonthCode();
        int lp = year.getLeapYearCode();
        int lm = month.applyLeapYearCode(mm, lp);
        int cc = 6;
        int i = yy / 4;
        return (i + number + lm + cc + yy) % 7;
    }

    //EFFECTS: returns a list of Task containing the given tag.
    public List<Task> findTaskWithTag(String tag) {
        List<Task> lot = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTags().contains(tag)) {
                lot.add(task);
            }
        }
        return lot;
    }

    //EFFECTS: returns a list of Task containing the given Title.
    public List<Task> findTaskWithTitle(String title) {
        List<Task> lot = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTitle().equals(title)) {
                lot.add(task);
            }
        }
        return lot;
    }

    //EFFECTS: returns a the total duration in minutes in this day of Task with the given tag.
    public double getTotalDurationWithTag(String tag) {
        List<Task> lot = findTaskWithTag(tag);
        double duration = 0.0;
        for (Task task : lot) {
            duration = duration + task.getDuration();
        }
        return duration;
    }

    //GETTER
    public int getNumber() {
        return number;
    }

    //GETTER
    public String getName() {
        return name;
    }

    //EFFECTS: returns day as a JSON object
    protected JSONObject dayToJson() {
        JSONObject json = new JSONObject();
        json.put("day", number);
        json.put("tasks", tasksToJson());
        return json;
    }

    // EFFECTS: returns tasks in this day as a JSON array
    private JSONArray tasksToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Task t : tasks) {
            jsonArray.put(t.taskToJson());
        }
        return jsonArray;
    }
}
