package ui;

import com.sun.scenario.effect.ColorAdjust;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

/**
 * The user is shown a JPanel with buttons related to adding/removing/editing tasks during the day.
 * Additionally a user can ask for the total time allotted for tasks with a certain tag in this day.
 * The user can ask for all tasks (and their details) to be printed.
 * <p>
 * To remove or edit a task, the user must know the title of the task. This can be easily found by using the print tasks
 * button to see all the task information.
 * NOTE: If tasks in this day have identical titles, the all tasks with that title will be passed to the appropriate
 * Task Process.
 */

public class TaskListProcess extends JPanel implements ActionListener {

    public Calendar calendar;
    public Year year;
    public Month month;
    public Day day;
    private Set<String> tags;

    private JTextPane taskInfo = new JTextPane();
    private JPanel buttonsPanel = new JPanel();
    private JLabel durationInfo = new JLabel();

    protected TaskListProcess(Year year, Month month, Day day, Set<String> tags) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.tags = tags;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        displayOptions();
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        add(buttonsPanel, c);
        c.gridx = 0;
        c.gridy = 1;
        JScrollPane p = new JScrollPane(taskInfo);
        p.setPreferredSize(new Dimension(450, 200));
        add(p, c);
        c.gridx = 0;
        c.gridy = 2;
        add(durationInfo, c);
    }

    //EFFECTS: makes buttons for user commands
    public void displayOptions() {
        makeJButton("Add task", "addTask");
        makeJButton("Remove tasks", "removeTask");
        makeJButton("Get task duration", "taskDuration");
        makeJButton("Edit task", "editTask");
        makeJButton("Print all task information", "printInfo");
    }

    //MODIFIES: this
    //EFFECTS: makes a single button with the given label, and actionCommand to recognize button action
    public void makeJButton(String label, String actionCommand) {
        JButton btn = new JButton(label);
        btn.setActionCommand(actionCommand);
        btn.addActionListener(this);
        buttonsPanel.add(btn);
    }

    //EFFECTS: prompt title, instantiates EditTaskProcess for tasks with that title. If none, display error popup.
    private void editTaskProcess() {
        String command = promptUserPopup("Edit Tasks", "Enter the title of task(s) to edit");
        if (command == null) {
            return;
        }
        List<Task> list = selectTask(command);
        if (!list.isEmpty()) {
            new EditTaskProcess(year, month, day, list, tags);
        } else {
            errorMessagePopup("Task Error", "No tasks found with that title");
        }
    }

    //EFFECTS: returns list of tasks with that title.
    private List<Task> selectTask(String command) {
        return day.findTaskWithTitle(command);
    }

    //EFFECTS: Creates frame and shows given error message
    private void errorMessagePopup(String title, String message) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    //MODIFIES: this
    //EFFECTS: instantiates TaskProcess to create a new task. Adds created task to this day if task is not null.
    private void addTaskProcess() {
        new AddTaskProcess(year, month, day);
    }

    //MODIFIES: this
    //EFFECTS: prints the title, time frame, and description for each task in this day. If none, print none message.
    private void printTasks() {
        StringBuilder str = new StringBuilder();
        if (day.getTasks().isEmpty()) {
            str.append("No tasks in this day.");
        } else {
            for (Task t : day.getTasks()) {
                str.append(" " + t.getTitle() + " from " + makePrettyAddZero(t.getStartHour()) + ":"
                        + makePrettyAddZero(t.getStartMinute()) + " to "
                        + makePrettyAddZero(t.getEndHour()) + ":" + makePrettyAddZero(t.getEndMinute()) + ".\n");
                str.append("\t Description: " + t.getDescription());
                str.append("\n\t Tags: " + t.getTags() + "\n\n");
            }
        }
        taskInfo.setText(str.toString());
        taskInfo.setCaretPosition(0);
    }

    //EFFECTS: for single digit numbers, adds a leading 0 to make like a clock display. returns processed number.
    private String makePrettyAddZero(int t) {
        String s = String.valueOf(t);
        if (t < 10) {
            return ("0" + s);
        } else {
            return s;
        }
    }

    //MODIFIES: this
    //EFFECTS: prompts user for a tag. prints sum of total time duration of tasks with that tag.
    private void getTaskDuration() {
        JFrame frame = new JFrame("get Task Duration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String in = JOptionPane.showInputDialog(frame,
                "Enter a tag to search for total duration of tasks with that tag",
                "get Task Duration",
                JOptionPane.QUESTION_MESSAGE);
        if (in != null) {
            durationInfo.setText("Total duration of time with the tag " + in + " is: "
                    + day.getTotalDurationWithTag(in) + " minutes.");
        }
    }

    //MODIFIES: this
    //EFFECTS: prompts user for a task title. Removes all tasks with that title from this
    private void removeTask() {
        String command = promptUserPopup("Remove Tasks", "Enter the title of task(s) to remove.");
        if (command == null) {
            return;
        }
        int k = 0;
        for (Task t : day.findTaskWithTitle(command)) {
            day.removeTask(t);
            k = k + 1;
        }
        removalSuccessPopup(k);
    }

    //EFFECTS: calls appropriate method based on user button press.
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "addTask":
                addTaskProcess();
                break;
            case "editTask":
                editTaskProcess();
                break;
            case "removeTask":
                removeTask();
                break;
            case "printInfo":
                printTasks();
                break;
            case "taskDuration":
                getTaskDuration();
                break;
        }
    }

    //EFFECTS: Creates JFrame and prompts user with message
    private String promptUserPopup(String title, String message) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return JOptionPane.showInputDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    //EFFECTS: Creates JFrame and shows success message
    private void removalSuccessPopup(int numTasks) {
        JFrame frame = new JFrame("Removed Tasks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "Removed " + numTasks + " tasks.", "Removed Tasks",
                JOptionPane.PLAIN_MESSAGE);
    }
}
