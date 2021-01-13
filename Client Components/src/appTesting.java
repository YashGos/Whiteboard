import static org.junit.jupiter.api.Assertions.*;

/**
 * Package: PACKAGE_NAME
 * Author: Yash
 * Date: 11/11/2018
 */

class appTesting {

    @org.junit.jupiter.api.Test
    void loginTest() {
        Login test = new Login();
        String testUsername = "testUsername";
        test.setUsername(testUsername);

        assertEquals(testUsername, test.getUsername(), "Wrong username set");
        test.setUsername("");
        test.setPassword("");
        assertTrue(test.invalidLogin(), "Password and Username not provided but logs in anyways");

        test.setUsername("someUsername");
        test.setPassword("");
        assertTrue(test.invalidLogin(), "Password and Username not provided but logs in anyways");
    }

    @org.junit.jupiter.api.Test
    void calenderTest(){
        fail("not programmed yet");
    }

    @org.junit.jupiter.api.Test
    void gradebookTest(){

        //Testing helper methods
        GradesView test1 = new GradesView();
        String name = "Assignment 1";
        int pe = 98;
        int tp = 100;
        test1.setName(name);
        test1.setPe(pe);
        test1.setTp(tp);
        assertEquals(name, test1.getName(), "Set and getname not working");
        assertEquals(pe, test1.getPe(),"Set and getpe not working");
        assertEquals(tp, test1.getTp(),"Set and gettp not working");
        // assertEquals();


    }
    @org.junit.jupiter.api.Test
    void assignmentTest(){
        fail("not programmed yet");
    }


} 