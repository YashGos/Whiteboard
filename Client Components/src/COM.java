import java.io.*;
import java.net.*;

public class COM
{
    private static String hostName = "eecslab-18.EECS.cwru.edu";
    private static int portNumber = 49001;

    public static byte[] COM(String request)
    {
        try
        {
            System.out.println(request);

            Socket tcpSocket = new Socket(hostName, portNumber);

            DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream());
            DataInputStream  in = new DataInputStream(tcpSocket.getInputStream());

            out.write(request.getBytes("UTF8"));

            byte[] data = new byte[4096];
            int count = in.read(data);

            tcpSocket.close();

            return data;
        }
        catch(Exception tcpException)
        {
            //
            return new byte[0];
        }
    }
}
