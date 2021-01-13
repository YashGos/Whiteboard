import org.jdesktop.swingx.JXFormattedTextField;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CourseCreation
{
    public JPanel BuildCourseCreationPanel()
    {
        JPanel courseCreationPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel newCourseDisplayName_label = new JLabel("Set Course's Display Name:");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 4;
        courseCreationPanel.add(newCourseDisplayName_label, gridConstraints);

        JLabel newCourseStudents_label = new JLabel("Set Students:");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 5;
        courseCreationPanel.add(newCourseStudents_label, gridConstraints);

        JLabel newCourseInstructor_label = new JLabel("Set Instructor:");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 6;
        courseCreationPanel.add(newCourseInstructor_label, gridConstraints);

        JLabel newCourseAssignmentTypes_label = new JLabel("Set Gradebook Sections:");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 7;
        courseCreationPanel.add(newCourseAssignmentTypes_label, gridConstraints);

        JLabel newCourseTerm_label = new JLabel("Set Term:");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 8;
        courseCreationPanel.add(newCourseTerm_label, gridConstraints);

        JXFormattedTextField newCourseDisplayName = new JXFormattedTextField();
        newCourseDisplayName.setPreferredSize(new Dimension(100,20));
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 4;
        courseCreationPanel.add(newCourseDisplayName, gridConstraints);

        JXFormattedTextField newCourseStudents = new JXFormattedTextField();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 5;
        courseCreationPanel.add(newCourseStudents, gridConstraints);

        JXFormattedTextField newCourseInstructor = new JXFormattedTextField();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 6;
        courseCreationPanel.add(newCourseInstructor, gridConstraints);

        JXFormattedTextField newCourseAssignmentTypes = new JXFormattedTextField();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 7;
        courseCreationPanel.add(newCourseAssignmentTypes, gridConstraints);

        JXFormattedTextField newCourseTerm = new JXFormattedTextField();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 8;
        courseCreationPanel.add(newCourseTerm, gridConstraints);

        try
        {
            byte[] responseBytes = COM.COM("GetCurrentCourses;" + ApplicationGUI.GetSessionInformation()[0] + ';' + ApplicationGUI.GetSessionInformation()[1]);
            String[] response = (new String(responseBytes, "UTF-8")).split(";") ;

            JList<String> existingCourses = new JList<>(response);
            gridConstraints.gridx = 1;
            gridConstraints.gridy = 1;
            gridConstraints.gridwidth = 2;

            courseCreationPanel.add(existingCourses, gridConstraints);
        }
        catch (Exception e)
        {
            //
        }

        JButton submitChanges = new JButton("Create New Course");
        submitChanges.addActionListener((ActionEvent e) ->
        {
            if(newCourseDisplayName.isValid() && newCourseInstructor.isValid() && newCourseStudents.isValid() && newCourseAssignmentTypes.isValid() && newCourseTerm.isValid())
            {
                new Thread(() ->
                {
                    CreateCourse(newCourseDisplayName.getText(), newCourseInstructor.getText(), newCourseStudents.getText(), newCourseAssignmentTypes.getText(), newCourseTerm.getText());
                }
                ).start();
            }
        });
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 9;
        gridConstraints.gridwidth = 3;
        courseCreationPanel.add(submitChanges, gridConstraints);

        JButton returnToPrevious = new JButton("Back");
        returnToPrevious.addActionListener((ActionEvent e) ->
        {
            courseCreationPanel.removeAll();
            ApplicationGUI.ChangePanel("homePanel");
        });
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 12;
        gridConstraints.gridwidth = 3;
        courseCreationPanel.add(returnToPrevious, gridConstraints);

        return courseCreationPanel;
    }

    public void CreateCourse(String newCourseDisplayName, String newCourseInstructor, String newCourseStudents, String newCourseAssignmentTypes, String newCourseTerm)
    {
        String request = "CreateCourse;" + ApplicationGUI.GetSessionInformation()[0] + ";" + ApplicationGUI.GetSessionInformation()[1] + ";" + newCourseDisplayName + ";" + newCourseInstructor + ";" + newCourseStudents + ";" + newCourseAssignmentTypes + ";" + newCourseTerm;

        try
        {
            byte[] result = COM.COM(request);
        }
        catch (Exception e)
        {
            //
        }
    }
}
