import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

interface CourseListener
{
    void launchChildPanel(String panelName, String courseName);
}

public class Course
{
    private List<CourseListener> listeners = new ArrayList<>();

    public void addListener(CourseListener toAdd)
    {
        listeners.add(toAdd);
    }

    public JPanel BuildCourse(String courseID)
    {
        JPanel coursePanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;

        String[] sessionInfo = ApplicationGUI.GetSessionInformation();

        JButton viewAssignments = new JButton("Assignments");

        gridConstraints.gridx = 1;
        gridConstraints.gridy = 1;

        viewAssignments.addActionListener((ActionEvent e) ->
        {
            for (CourseListener hl : listeners)
            {
                hl.launchChildPanel("assignmentPanel", courseID);
            }
        });

        coursePanel.add(viewAssignments, gridConstraints);

        JButton viewGradebook = new JButton("Gradebook");

        gridConstraints.gridx = 1;
        gridConstraints.gridy = 2;

        viewGradebook.addActionListener((ActionEvent e) ->
        {
            for (CourseListener hl : listeners)
            {
                hl.launchChildPanel("gradebookPanel", courseID);
            }
        });

        coursePanel.add(viewGradebook, gridConstraints);

        JButton returnToPrevious = new JButton("Back");
        returnToPrevious.addActionListener((ActionEvent e) ->
        {
            coursePanel.removeAll();
            ApplicationGUI.ChangePanel("homePanel");
        });

        gridConstraints.gridx = 1;
        gridConstraints.gridy = 3;

        coursePanel.add(returnToPrevious, gridConstraints);

        return coursePanel;
    }
}