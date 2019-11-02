package Master;
import org.apache.commons.math3.linear.*;
import java.net.*;
import java.io.*;
import java.util.*;
public class Master
{
    private RealMatrix matrixX,matrixY;
    private ServerSocket providerSocket;
    private ObjectInputStream in = null;
    private Socket connection = null;
    private ArrayList<Integer> WorkersForce = new ArrayList<Integer>();
    private int sumRam;
    //Initializing All tools
    private Master()
    {
        openServerForWorkForce();
        InitializeData cache = new InitializeData("input_matrix_non_zeros.csv");
        matrixX = cache.getMatrixX();
        matrixY = cache.getMatrixY();
    }
    private void openServerForWorkForce()
    {
        int i=0;
        try
        {
            providerSocket = new ServerSocket(IP.portMaster, IP.numberOfWorkers);
            //we need all the workers.Δεν καταφέραμε να κάνουμε το σύστημα Rebalance
            while(i<IP.numberOfWorkers)
            {
                connection = providerSocket.accept();
                System.out.println("Connection with Client"+(i+1)+" established.");
                in = new ObjectInputStream(connection.getInputStream());

                int ram = in.readInt();
                WorkersForce.add(ram);
                sumRam = sumRam + ram;
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

    }
    //Cost Function
    double calculateError()
    {
        double sumCost =0.0;
        double Normalization = InitializeData.LCost * (Norm(matrixX) + (Norm(matrixY)));
        for(int i=0; i<InitializeData.rows; i++)
        {
            for(int j=0; j<InitializeData.columns; j++)
            {
                RealMatrix s = (matrixY.getRowMatrix(j)).multiply(matrixX.getRowMatrix(i).transpose());
                double xy = s.getEntry(0,0);
                double pui = InitializeData.binaryP.getEntry(i,j);
                double cui = InitializeData.matrixC.getEntry(i,j);
                sumCost = sumCost + cui*(Math.pow((pui - xy),2));
            }
        }
        System.out.println("Normalization is: "+Normalization);
        System.out.println("Cost is: "+ (sumCost+Normalization));
        return sumCost+Normalization;
    }
    private static double Norm(RealMatrix matrix)
    {
        double sum=0.0;
        for(int i=0; i<matrix.getRowDimension(); i++)
        {
            sum = sum+ Math.pow(matrix.getRowMatrix(i).getFrobeniusNorm(),2);
        }
        return sum;
    }
    static boolean checkCost(double sum1,double sum2)
    {
        double  sub = sum2 - sum1;
        return !(InitializeData.ThresHold > (Math.abs(sub)));
    }
    //sets subMatrix X
    void setX(RealMatrix X, int start,int end)
    {
        for(int i =start; i<end; i++)
        {
            matrixX.setRowVector(i,X.getRowVector(i));
        }
    }
    //sets subMatrix Y
    void setY(RealMatrix Y, int start,int end)
    {
        for(int i =start; i<end; i++)
        {
            matrixY.setRowVector(i,Y.getRowVector(i));
        }
    }
    RealMatrix getMatrixX()
    {

        return matrixX;
    }
    RealMatrix getMatrixY()
    {

        return matrixY;
    }
    int getSumForce()
    {

        return sumRam;}
    ArrayList<Integer> getWorkersForce()
    {

        return WorkersForce;}
    //fills recomendationMatrix with new values.
    private void recomendationMatrix()
    {
        RealMatrix pui;
        for(int i=0; i<InitializeData.rows; i++)
        {
            for(int j=0; j<InitializeData.columns; j++)
            {
                pui = matrixY.getRowMatrix(j).multiply(matrixX.getRowMatrix(i).transpose());
                double Pui = pui.getEntry(0,0);
                if(InitializeData.dataMatrix.getEntry(i,j)==0)
                    InitializeData.recomendationMatrix.setEntry(i,j,Pui);
                else
                    InitializeData.recomendationMatrix.setEntry(i,j,0.0);
            }
        }
    }
    private void closeConnection()
    {
        try
        {
            in.close();
            providerSocket.close();
            connection.close();
            System.out.println("Master Closed Connection");
        }catch (IOException ioException) {ioException.printStackTrace();}}

    public static void main(String args[])
    {

        Master m =  new Master();
        new ClientHandler(m);
        m.recomendationMatrix();
        Thread t = new AndroidClient();
        t.start();
        try
        {
            t.join();
        }catch (InterruptedException e){e.printStackTrace();}

    }
}

