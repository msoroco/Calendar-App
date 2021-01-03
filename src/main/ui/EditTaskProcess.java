package ui;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

import static javax.swing.BorderFactory.createEmptyBorder;

/**
 * An EditTaskProcess prompts user for the fields of Task they want to change, and changes it.
 * <p>
 * Users can change the title, time, description, add tags, or remove tags.
 * If multiple tasks in this day had the same title, they will all be edited with the same change.
 */

public class EditTaskProcess extends JDialog implements ActionListener {

    public Calendar calendar;
    public Year year;
    public Month month;
    public Day day;
    private List<Task> tasks;
    private Set<String> tags;

    private JPanel buttonsPanel = new JPanel();
    private Box box;

    private JTextField startHour = new JTextField(5);
    private JTextField startMinute = new JTextField(5);
    private JTextField endHour = new JTextField(5);
    private JTextField endMinute = new JTextField(5);
    private GridBagConstraints constraints = new GridBagConstraints();

    private JDialog editTimes = new JDialog();


    public EditTaskProcess(Year year, Month month, Day day, List<Task> tasks, Set<String> tags) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.tasks = tasks;
        this.tags = tags;

        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(YearProcess.WIDTH, YearProcess.HEIGHT));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        displayOptions();
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        add(buttonsPanel, c);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //EFFECTS: makes buttons for user commands
    public void displayOptions() {
        makeJButton("Change title");
        makeJButton("Change time");
        makeJButton("Change description");
        makeJButton("Add tag");
        makeJButton("Remove tag");
    }

    //MODIFIES: this
    //EFFECTS: makes a button & action command with the given label
    public void makeJButton(String label) {
        JButton btn = new JButton(label);
        btn.setActionCommand(label);
        btn.addActionListener(this);
        buttonsPanel.add(btn);
    }

    //MODIFIES: this
    //EFFECTS: prompts user for tag. removes tag from all tasks in this. prints number of tags removed.
    private void removeTag() {
        String command = inputPopup("Remove Tag", "Enter the tag to remove (removes all duplicates).");
        int k = 0;
        for (Task t : tasks) {
            if (t.removeTag(command)) {
                k = k + 1;
            }
        }
        changeSuccessfulPopup("Removed Tags", "Removed " + k + " tag(s)");
    }

    //EFFECTS: Creates JFrame and prompts input
    private String inputPopup(String title, String message) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return JOptionPane.showInputDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    //EFFECTS: Creates JFrame and shows success message
    private void changeSuccessfulPopup(String title, String message) {
        JFrame frame = new JFrame("Removed Tags");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame,
                message, title, JOptionPane.PLAIN_MESSAGE);
    }

    //MODIFIES: this
    //EFFECTS: prompts user for tag. adds tag to all tasks in this. prints number of tags added. Adds tags list in
    //         this so that month can get tags without having to update by instantiating new monthProcess.
    private void addTag() {
        int k = 0;
        String command = inputPopup("Enter Tag", "Enter the tag to add");
        for (Task t : tasks) {
            t.addTag(command);
            k = k + 1;
        }
        this.tags.add(command);
        changeSuccessfulPopup("Tag Added", "added tag " + command + " to " + k + " task(s).");
    }

    //MODIFIES: this
    //EFFECTS: prompts user for description. adds input to all tasks in this. prints number of descriptions added.
    private void changeDescription() {
        int k = 0;
        String command = inputPopup("Enter Description", "Enter the new description");
        for (Task t : tasks) {
            t.setDescription(command);
            k = k + 1;
        }
        changeSuccessfulPopup("Description Changed", k + " task(s) changed description to: " + command);
    }

    //MODIFIES: this
    //EFFECTS: prompts user for new title. changes title of all tasks in this. Prints number of titles changed.
    private void editTitle() {
        int k = 0;
        String command = inputPopup("Enter Title", "Enter the new title");
        for (Task t : tasks) {
            t.setTitle(command);
            k = k + 1;
        }
        changeSuccessfulPopup("Title Changed", k + " task(s) changed title to: " + command);
    }

    //MODIFIES: this
    //EFFECTS: prompts user for times. changes time of all tasks in this if valid, then disposes this JDialogue.
    private void changeTime() {
        String startHour = getStartHour();
        int sh = checkValidInput(startHour);
        String startMinute = getStartMinute();
        int sm = checkValidInput(startMinute);
        String endHour = getEndHour();
        int eh = checkValidInput(endHour);
        String endMinute = getEndMinute();
        int em = checkValidInput(endMinute);
        changeTimeHelper(sh, sm, eh, em);
        dispose();
    }

    //EFFECTS: get JField user input
    public String getStartHour() {
        return startHour.getText();
    }

    //EFFECTS: get JField user input
    public String getStartMinute() {
        return startMinute.getText();
    }

    //EFFECTS: get JField user input
    public String getEndHour() {
        return endHour.getText();
    }

    //EFFECTS: get JField user input
    public String getEndMinute() {
        return endMinute.getText();
    }

    //MODIFIES: this
    //EFFECTS: make JDialogue for user to input new times for editing time.
    private void makeDialogue() {
        editTimes.setModal(true);
        editTimes.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        editTimes.setPreferredSize(new Dimension(YearProcess.WIDTH, YearProcess.HEIGHT));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        editTimes.setLayout(new GridBagLayout());
        editTimes.setResizable(true);
        initialize();
        editTimes.pack();
        editTimes.setLocationRelativeTo(null);
        editTimes.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: add JFields and corresponding JLabel prompting user for input, add JButton for user to enter input
    public void initialize() {
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        editTimes.add(createBoxWithFieldAndLabel("Enter the start hour", startHour), constraints);
        constraints.gridy = 1;
        editTimes.add(createBoxWithFieldAndLabel("Enter the start minute", startMinute), constraints);
        constraints.gridy = 2;
        editTimes.add(createBoxWithFieldAndLabel("Enter the end hour", endHour), constraints);
        constraints.gridy = 3;
        editTimes.add(createBoxWithFieldAndLabel("Enter the end minute", endMinute), constraints);
        constraints.gridy = 4;
        JButton btn = new JButton("Enter");
        btn.setActionCommand("enterTimes");
        btn.addActionListener(this);
        editTimes.add(btn, constraints);
    }

    //MODIFIES: this
    //EFFECTS: creates a box with a JFields and the corresponding JLabel prompting user for input
    private Box createBoxWithFieldAndLabel(String command, JTextField field) {
        box = Box.createVerticalBox();
        JLabel label = new JLabel(command);
        box.add(label);
        box.add(field);
        box.setBorder(createEmptyBorder(13, 13, 13, 13));
        return box;
    }

    //EFFECTS: checks that parameter can be converted to an int, return int. if not, return -1.
    private int checkValidInput(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return -1;
        }
        return Integer.parseInt(s);
    }

    //MODIFIES: this
    //EFFECTS: changes times of tasks in this, if valid. returns number of tasks with times changed.
    private void changeTimeHelper(int sh, int sm, int eh, int em) {
        Task checkerTask = new Task("", 0, 0, 0, 0, "");
        if (!checkerTask.validTimeFrame(sh, sh, eh, em)) {
            errorMessagePopup();
        }
        int k = 0;
        for (Task t : tasks) {
            if (t.changeTime(sh, sm, eh, em)) {
                k = k + 1;
            }
        }
        changeSuccessfulPopup("Changed Times", k + " task(s) changed time");
    }

    //EFFECTS: Creates JFrame and shows error message
    private void errorMessagePopup() {
        JFrame frame = new JFrame("Invalid Times");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, "Invalid times were given.", "Invalid Times",
                JOptionPane.ERROR_MESSAGE);
    }

    //EFFECTS: runs appropriate method based on user button press.
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Change title":
                editTitle();
                break;
            case "Change time":
                makeDialogue();
                break;
            case "Change description":
                changeDescription();
                break;
            case "Add tag":
                addTag();
                break;
            case "Remove tag":
                removeTag();
                break;
            case "enterTimes":
                changeTime();
        }
    }
}
