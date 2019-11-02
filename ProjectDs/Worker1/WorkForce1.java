package Worker1;
import java.net.*;
import java.io.*;
public class WorkForce1
{
    private Socket requestSocket=null;
    private ObjectOutputStream out=null;
    private void sendRam()
    {

        try
        {
            requestSocket = new Socket(IP.ipMaster, IP.portMaster);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            System.out.println("Sending First WorkForce");
            sendAvailableRam(out);
            System.out.println("Sent!");
        }
        catch (IOException e) { e.printStackTrace(); }
        finally {close(out,requestSocket);}
    }
    private void sendAvailableRam(ObjectOutputStream out)
    {
        try
        {
            out.reset();
            int sum =(Runtime.getRuntime().availableProcessors())+((int) ((Runtime.getRuntime().freeMemory()/1048576)*(0.1)));
            out.writeInt(sum);
            out.flush();
        }
        catch (UnknownHostException unknownHost) { System.err.println("unknown host!"); }
        catch (IOException ioException) { ioException.printStackTrace(); }
    }
    private void close(ObjectOutputStream out, Socket requestSocket)
    {
        try {out.close();requestSocket.close(); System.out.println("WorkForce 1 closed it's connection.");}
        catch (IOException ioException) {ioException.printStackTrace();}
        catch (NullPointerException e){e.printStackTrace();}
    }
    public static void main(String[] args)
    {
        new WorkForce1().sendRam();
        new Client1().start();
    }
}

