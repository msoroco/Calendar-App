package ui;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.BorderFactory.createEmptyBorder;

/**
 * An AddTaskProcess prompts user for the necessary fields to construct a task and uses user input to instantiate a new
 * Task.
 * <p>
 * Note the addition of tags is not a part of this process as tags are not a required part of tasks. Tags can be added
 * through the EditTaskProcess from TaskListProcess.
 */

public class AddTaskProcess extends JDialog implements ActionListener {

    public Calendar calendar;
    public Year year;
    public Month month;
    public Day day;
    private Task task;

    private JTextField title = new JTextField(10);
    private JTextField startHour = new JTextField(5);
    private JTextField startMinute = new JTextField(5);
    private JTextField endHour = new JTextField(5);
    private JTextField endMinute = new JTextField(5);
    private JTextField description = new JTextField(100);
    private GridBagConstraints constraints = new GridBagConstraints();
    private Box box;

    //EFFECTS: Constructor; displays components and make the JDialogue window modal, visible and resizable.
    public AddTaskProcess(Year year, Month month, Day day) {
        this.year = year;
        this.month = month;
        this.day = day;

        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(YearProcess.WIDTH, YearProcess.HEIGHT));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new GridBagLayout());
        setResizable(true);
        initialize();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: layout prompt and JFields and enter JButton vertically on this Dialogue
    public void initialize() {
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(createBoxWithFieldAndLabel("Enter the title", title), constraints);
        constraints.gridy = 1;
        add(createBoxWithFieldAndLabel("Enter the start hour", startHour), constraints);
        constraints.gridy = 2;
        add(createBoxWithFieldAndLabel("Enter the start minute", startMinute), constraints);
        constraints.gridy = 3;
        add(createBoxWithFieldAndLabel("Enter the end hour", endHour), constraints);
        constraints.gridy = 4;
        add(createBoxWithFieldAndLabel("Enter the end minute", endMinute), constraints);
        constraints.gridy = 5;
        add(createBoxWithFieldAndLabel("Enter the description", description), constraints);
        JButton btn = new JButton("Enter");
        btn.setActionCommand("createTask");
        btn.addActionListener(this);
        constraints.gridy = 6;
        add(btn, constraints);
    }

    //MODIFIES: this
    //EFFECTS: creates a box with a JField for user input and the JLabel prompt.
    private Box createBoxWithFieldAndLabel(String command, JTextField field) {
        box = Box.createVerticalBox();
        JLabel label = new JLabel(command);
        box.add(label);
        box.add(field);
        box.setBorder(createEmptyBorder(13, 13, 13, 13));
        return box;
    }

    //EFFECTS: Returns this task
    public Task getTask() {
        return this.task;
    }


    //MODIFIES: this
    //EFFECTS: calls methods prompting using input. Instantiate new Task with null/0 values. Run validity check of user
    //          values with instantiated Task. modifies instantiated Task to user input if validity check passes and
    //          sets it to this, returns true. else return false to report fail.
    protected boolean processCommand(String title, int startHour, int startMinute, int endHour, int endMinute,
                                     String description) {
        Task t = new Task(title, 0, 0, 0, 0, description);
        if (t.validTimeFrame(startHour, startMinute, endHour, endMinute)) {
            t.changeTime(startHour, startMinute, endHour, endMinute);
            this.task = t;
            return true;
        } else {
            return false;
        }
    }

    //EFFECTS: return start hour as integer.
    private int getStartHour(String text) {
        return Integer.parseInt(text);
    }

    //EFFECTS: return start minute as integer.
    private int getStartMinute(String text) {
        return Integer.parseInt(text);
    }

    //EFFECTS: return end hour as integer.
    private int getEndHour(String text) {
        return Integer.parseInt(text);
    }

    //EFFECTS:  return end minute as integer.
    private int getEndMinute(String text) {
        return Integer.parseInt(text);
    }

    //EFFECTS: returns text in description JTextField
    private String getDescription() {
        return description.getText();
    }

    //MODIFIES: this
    //EFFECTS: If user input times are valid, create new task and close this JDialogue, else if invalid, display error
    //           popup, or catch NumberFormatException.
    @Override
    public void actionPerformed(ActionEvent e) {
        int startHour;
        int startMinute;
        int endHour;
        int endMinute;

        if (e.getActionCommand().equals("createTask")) {
            try {
                startHour = getStartHour(this.startHour.getText());
                startMinute = getStartMinute(this.startMinute.getText());
                endHour = getEndHour(this.endHour.getText());
                endMinute = getEndMinute(this.endMinute.getText());
                if (!processCommand(title.getText(), startHour, startMinute, endHour, endMinute, getDescription())) {
                    errorMessagePopup();
                } else {
                    successMessagePopup();
                    if (task != null) {
                        day.addTask(task);
                        dispose();
                    }
                }
            } catch (NumberFormatException nfe) {
                errorMessagePopup();
            }
        }
    }

    //EFFECTS: Creates JFrame and shows success message
    private void successMessagePopup() {
        JFrame frame = new JFrame("Task Added");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "Success!", "Task Added", JOptionPane.PLAIN_MESSAGE);
    }

    //EFFECTS: Creates JFrame and shows error message
    private void errorMessagePopup() {
        JFrame frame = new JFrame("Task Error");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "Invalid times were given.", "Task error",
                JOptionPane.ERROR_MESSAGE);
    }
}

