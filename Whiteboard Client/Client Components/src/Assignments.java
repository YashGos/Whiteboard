import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXFormattedTextField;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class Assignments
{
    public JPanel BuildAssignmentsPanel(String courseName)
    {
        JPanel assignmentsPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;

        if((ApplicationGUI.GetSessionInformation()[2]).contains("S")) {
            DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Assignment Name", "Assignment Description", "Due Date"}, 0);

            JTable assignmentTable = new JTable(tableModel);

            try {
                byte[] responseBytes = COM.COM("GetAssociatedAssignments;" + ApplicationGUI.GetSessionInformation()[0] + ';' + ApplicationGUI.GetSessionInformation()[1] + ';' + courseName);
                String[] response = (new String(responseBytes, "UTF-8")).split(";");

                for (int i = 0; i < response.length; i++) {
                    DefaultTableModel currentModel = (DefaultTableModel) assignmentTable.getModel();
                    currentModel.addRow(new Object[]{response[i * 3], response[(i * 3) + 1], response[(i * 3) + 2]});
                }
            } catch (Exception e) {
                //
            }

            JScrollPane scrollPane = new JScrollPane(assignmentTable);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            scrollPane.setPreferredSize(new Dimension(screenSize.width / 4, screenSize.height / 4));

            assignmentsPanel.add(scrollPane);

            gridConstraints.gridx = 0;
            gridConstraints.gridy = 2;
        }
        else if ((ApplicationGUI.GetSessionInformation()[2]).contains("I"))
        {
            try
            {
                JLabel newAssignmentStart_label = new JLabel("Set Assignment Start Date:");
                gridConstraints.gridx = 1;
                gridConstraints.gridy = 4;
                assignmentsPanel.add(newAssignmentStart_label, gridConstraints);

                JLabel newAssignmentEnd_label = new JLabel("Set Assignment End Date:");
                gridConstraints.gridx = 1;
                gridConstraints.gridy = 5;
                assignmentsPanel.add(newAssignmentEnd_label, gridConstraints);

                JLabel newAssignmentName_label = new JLabel("Set Assignment Name");
                gridConstraints.gridx = 1;
                gridConstraints.gridy = 6;
                assignmentsPanel.add(newAssignmentName_label, gridConstraints);

                JLabel newAssignmentDes_label = new JLabel("Set Description:");
                gridConstraints.gridx = 1;
                gridConstraints.gridy = 8;
                assignmentsPanel.add(newAssignmentDes_label, gridConstraints);

                JLabel newAssignmentType_label = new JLabel("Set Assignment Type:");
                gridConstraints.gridx = 1;
                gridConstraints.gridy = 7;
                assignmentsPanel.add(newAssignmentType_label, gridConstraints);

                JXDatePicker newAssignmentStart = new JXDatePicker();
                newAssignmentStart.setPreferredSize(new Dimension(100,20));
                gridConstraints.gridx = 2;
                gridConstraints.gridy = 4;
                assignmentsPanel.add(newAssignmentStart, gridConstraints);

                JXDatePicker newAssignmentEnd = new JXDatePicker();
                gridConstraints.gridx = 2;
                gridConstraints.gridy = 5;
                assignmentsPanel.add(newAssignmentEnd, gridConstraints);

                JXFormattedTextField newAssignmentName = new JXFormattedTextField();
                gridConstraints.gridx = 2;
                gridConstraints.gridy = 6;
                assignmentsPanel.add(newAssignmentName, gridConstraints);

                JXFormattedTextField newAssignmentType = new JXFormattedTextField();
                gridConstraints.gridx = 2;
                gridConstraints.gridy = 7;
                assignmentsPanel.add(newAssignmentType, gridConstraints);

                JXFormattedTextField newAssignmentDes = new JXFormattedTextField();
                gridConstraints.gridx = 2;
                gridConstraints.gridy = 8;
                assignmentsPanel.add(newAssignmentDes, gridConstraints);

                try
                {
                    //
                }
                catch (Exception e)
                {
                    //
                }

                JButton submitChanges = new JButton("Create New Assignment");
                submitChanges.addActionListener((ActionEvent e) ->
                {
                    //
                });
                gridConstraints.gridx = 1;
                gridConstraints.gridy = 9;
                gridConstraints.gridwidth = 3;
                assignmentsPanel.add(submitChanges, gridConstraints);
            }
            catch (Exception e)
            {
                //
            }
        }

        JButton returnToPrevious = new JButton("Back");
        returnToPrevious.addActionListener((ActionEvent e) ->
        {
            assignmentsPanel.removeAll();
            ApplicationGUI.ChangePanel("coursePanel");
        });

        assignmentsPanel.add(returnToPrevious, gridConstraints);

        return assignmentsPanel;
    }
}