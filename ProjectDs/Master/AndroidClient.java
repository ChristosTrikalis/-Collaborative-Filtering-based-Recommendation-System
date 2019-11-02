package Master;
import java.net.*;
import java.io.*;
public class AndroidClient extends Thread
{

    private DataInputStream in=null;
    private DataOutputStream out = null;
    private  ServerSocket socket;
    private Socket connection ;
    public void run()
    {
        int k,ID;
        try
        {
            OpenConnection();
            ID = in.readInt();
            k = in.readInt();
            new Pois().fillPOISarray();
            out.writeBoolean(true);
            int i=0;
            double[] array = InitializeData.recomendationMatrix.getRow(ID);
            int sum=0;
           while(i<InitializeData.columns && sum<k)
           {

              if(array[i]!=0.0) {
                   out.writeDouble(Pois.latitudes[i]);
                   out.flush();
                   out.writeDouble(Pois.longidudes[i]);
                   out.flush();
                   out.writeUTF(Pois.category[i]);
                   out.flush();
                   out.writeUTF(Pois.name[i]);
                   out.flush();
                   sum++;
               }
               i++;
               if(i==k)
               {
                   out.writeBoolean(false);
               }
               else
               {
                   out.writeBoolean(true);
               }
           }
            System.out.println("Sent");
            CloseConnection();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void OpenConnection()
    {
        try
        {
            socket = new ServerSocket(9004, 10);
            connection = socket.accept();
            System.out.println("Connected");
            out = new DataOutputStream(connection.getOutputStream());
            in = new DataInputStream(connection.getInputStream());
        }
        catch (IOException e){e.printStackTrace();}
    }
    private void CloseConnection()
    {
        try
        {
            out.close();
            in.close();
            socket.close();
        }catch (IOException e){e.printStackTrace();}
    }

}