import javax.swing.*;
import java.awt.*;

public class ApplicationGUI extends JFrame
{
    private static JPanel mainPanel;
    private static String userName = "";
    private static String accessToken = "00112233-4455-6677-8899-aabbccddeeff";
    private static String accessLevel = "N";

    ApplicationGUI()
    {
        super("Whiteboard");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width / 3 , screenSize.height / 3));

        BuildGUI(getContentPane());

        pack();

        setLocationRelativeTo(null);

        setVisible(true);
    }

    public void BuildGUI(Container pane)
    {
        mainPanel = new JPanel(new CardLayout());

        Login loginInstance = new Login();
        JPanel loginPanel = loginInstance.BuildLoginPanel();

        Home homeInstance = new Home();

        UserCreation userCreationInstance = new UserCreation();
        CourseCreation courseCreationInstance = new CourseCreation();

        Course courseInstance = new Course();

        Gradebook gradebookInstance = new Gradebook();

        Assignments assignmentInstance = new Assignments();

        loginInstance.addListener((currentUser, comToken, comLevel) ->
        {
            SetSessionInformation(currentUser,comToken,comLevel);

            JPanel homePanel =  homeInstance.BuildHomePanel();
            mainPanel.add(homePanel, "homePanel");

            ChangePanel("homePanel");
        });

        homeInstance.addListener((panelName, courseName) ->
        {
            if(panelName.contains("userCreationPanel"))
            {
                JPanel userCreationPanel = userCreationInstance.BuildUserCreationPanel();
                mainPanel.add(userCreationPanel, "userCreationPanel");

                ChangePanel("userCreationPanel");
            }
            else if(panelName.contains("courseCreationPanel"))
            {
                JPanel courseCreationPanel = courseCreationInstance.BuildCourseCreationPanel();
                mainPanel.add(courseCreationPanel, "courseCreationPanel");

                ChangePanel("courseCreationPanel");
            }
            else if(panelName.contains("coursePanel"))
            {
                JPanel coursePanel = courseInstance.BuildCourse(courseName);
                mainPanel.add(coursePanel, "coursePanel");

                ChangePanel("coursePanel");
            }
        });

        courseInstance.addListener((panelName, courseName) ->
        {
            if(panelName.contains("assignmentPanel"))
            {
                JPanel assignmentPanel = assignmentInstance.BuildAssignmentsPanel(courseName);
                mainPanel.add(assignmentPanel, "assignmentPanel");

                ChangePanel("assignmentPanel");
            }
            else if(panelName.contains("gradebookPanel"))
            {
                JPanel gradebookPanel = gradebookInstance.BuildGradebookPanel(courseName);
                mainPanel.add(gradebookPanel, "gradebookPanel");

                ChangePanel("gradebookPanel");
            }
        });


        Settings settingsInstance = new Settings();
        JPanel settingsPanel = settingsInstance.BuildSettingsPanel();




        //Grades gradesInstance = new Grades();
        //JPanel gradesPanel = gradesInstance.BuildGradesPanel();


        mainPanel.add(loginPanel, "loginPanel");

        mainPanel.add(settingsPanel, "settingsPanel");


        pane.add(mainPanel, BorderLayout.CENTER);
    }

    public static void ChangePanel(String panelName)
    {
        CardLayout cardLayout = (CardLayout)(mainPanel.getLayout());
        cardLayout.show(mainPanel, panelName);
    }

    public static String[] GetSessionInformation()
    {
        return new String[] {userName, accessToken, accessLevel};
    }

    public static void SetSessionInformation(String currentUser, String comToken, String comLevel)
    {
        userName = currentUser;
        accessToken = comToken;
        accessLevel = comLevel;
    }
}
