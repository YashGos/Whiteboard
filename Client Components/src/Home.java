import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.List;


interface HomeListener
{
    void launchChildPanel(String panelName, String courseName);
}

public class Home
{
    private List<HomeListener> listeners = new ArrayList<>();

    public void addListener(HomeListener toAdd)
    {
        listeners.add(toAdd);
    }

    public JPanel BuildHomePanel()
    {
        JPanel homePanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;

        String[] sessionInfo = ApplicationGUI.GetSessionInformation();

        if(sessionInfo[2].contains("A"))
        {
            JButton manageCourses = new JButton("Manage Courses");
            gridConstraints.gridx = 1;
            gridConstraints.gridy = 1;

            manageCourses.addActionListener((ActionEvent e) ->
            {
                for (HomeListener hl : listeners)
                {
                    hl.launchChildPanel("courseCreationPanel", "N/A");
                }
            });

            homePanel.add(manageCourses, gridConstraints);

            JButton manageUsers = new JButton("Manage Users");
            gridConstraints.gridx = 1;
            gridConstraints.gridy = 2;

            manageUsers.addActionListener((ActionEvent e) ->
            {
                for (HomeListener hl : listeners)
                {
                    hl.launchChildPanel("userCreationPanel", "N/A");
                }
            });

            homePanel.add(manageUsers, gridConstraints);
        }

        else
        {
            try
            {
                byte[] responseBytes = COM.COM("GetAssociatedCourses;" + ApplicationGUI.GetSessionInformation()[0] + ';' + ApplicationGUI.GetSessionInformation()[1]);
                String[] response = (new String(responseBytes, "UTF-8")).split(";") ;

                for (int i = 0; i < response.length; i++)
                {
                    JButton selectCourse = new JButton(response[i]);
                    gridConstraints.gridx = 1;
                    gridConstraints.gridy = i + 1;

                    selectCourse.addActionListener((ActionEvent e) ->
                    {
                        for (HomeListener hl : listeners)
                        {
                            hl.launchChildPanel("coursePanel", selectCourse.getText());
                        }
                    });

                    homePanel.add(selectCourse, gridConstraints);

                    i++;
                }
            }
            catch(Exception e)
            {
                //
            }
        }

        JButton returnToPrevious = new JButton("Logout");
        returnToPrevious.addActionListener((ActionEvent e) ->
        {
            homePanel.removeAll();
            ApplicationGUI.SetSessionInformation("","00112233-4455-6677-8899-aabbccddeeff", "N");
            ApplicationGUI.ChangePanel("loginPanel");
        });

        gridConstraints.gridx = 1;
        gridConstraints.gridy = 3;

        homePanel.add(returnToPrevious, gridConstraints);

        return homePanel;
    }
}
