package Worker1;
import org.apache.commons.math3.linear.*;
import java.io.*;
import java.net.*;
public class Client1 extends Thread
{
    private RealMatrix C;
    private Worker1 worker1 = new Worker1();
    private ObjectInputStream in=null;
    private ObjectOutputStream out=null;
    private ServerSocket providerSocket = null;
    public void run()
    {
        try
        {
           openClient();

           int K = in.readInt();
           int startX = in.readInt();int limitX= in.readInt();
           int startY = in.readInt(); int limitY = in.readInt();


           RealMatrix P,X,Y;
           C = getMatrixFromMaster(in);
           P = getMatrixFromMaster(in);
           worker1.initialize(P,K);

           //Υπολόγισε τον Χ με τη βοήθεια του Y και στειλ'τον
            X = getMatrixFromMaster(in);
            Y = getMatrixFromMaster(in);
            calculateNewX(Y,X,startX,limitX,worker1);
            sendMatrixToMaster(X,out);


            //Υπολόγισε τον Υ με τη βοήθεια του νέου Χ και στείλ'τον
            X = getMatrixFromMaster(in);
            calculateNewY(X,Y,startY,limitY,worker1);
            sendMatrixToMaster(Y,out);


                boolean flag = in.readBoolean();
                while(flag)
                {
                    X = getMatrixFromMaster(in);
                    Y = getMatrixFromMaster(in);
                    calculateNewX(Y,X,startX,limitX,worker1);
                    sendMatrixToMaster(X,out);

                    X = getMatrixFromMaster(in);
                    calculateNewY(X,Y,startY,limitY,worker1);
                    sendMatrixToMaster(Y,out);
                    flag = in.readBoolean();
                }
            System.out.println("Client 1 Ready");

        }

        catch (IOException ioException) {ioException.printStackTrace();}
        finally {closeClient();}
    }
    private RealMatrix getMatrixFromMaster(ObjectInputStream in)
    {
        RealMatrix matrix = null;
        try
        {
            matrix = (RealMatrix) in.readObject();
        }
        catch (IOException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}
        return matrix;
    }
    private void sendMatrixToMaster(RealMatrix matrix,ObjectOutputStream out)
    {
        try {
            out.reset();
            out.writeObject(matrix);
            out.flush();

        }catch (IOException e){e.printStackTrace();}
    }
    private void calculateNewX(RealMatrix matrixY,RealMatrix matrixX,int start, int end,Worker1 w1)
    {
        double[] c;
        for (int i = start; i< end; i++)
        {
            c = C.getRow(i);
            RealMatrix D = w1.Diagonial(c);
            w1.calculateXu(matrixY, matrixX, D, i);
        }
        System.out.println("Finished X");

    }
    private void calculateNewY(RealMatrix matrixX,RealMatrix matrixY,int start, int end,Worker1 w1)
    {
        double[] c;
        for (int i = start; i < end; i++)
        {
            c = C.getColumn(i);
            RealMatrix D = w1.Diagonial(c);
            w1.calculateYi(matrixX, matrixY, D, i);
        }
        System.out.println("Finished Y");
    }
    private void closeClient()
    {
        try {in.close();out.close();providerSocket.close(); System.out.println("Connection closed..");}
        catch (IOException ioException) {ioException.printStackTrace();}
    }
    private void openClient()
    {
        try
        {
            providerSocket = new ServerSocket(IP.portWorker1);
            Socket connection = providerSocket.accept();
            System.out.print("Client 1 is up and waiting....");
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
            System.out.print("Connection established.");
        }catch (IOException e){e.printStackTrace();}
    }

}

