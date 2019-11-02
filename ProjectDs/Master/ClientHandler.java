package Master;
import org.apache.commons.math3.linear.RealMatrix;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
class ClientHandler
{
    private ObjectOutputStream out1 = null,out2 = null, out3 = null,out4 = null;
    private ObjectInputStream in1 = null,in2 = null,in3 = null,in4 = null;
    private Socket requestWorker1 = null,requestWorker2 = null,requestWorker3 = null, requestWorker4 = null;
    private Master m;
    private int limitX1,limitX2,limitX3,limitX4,limitY1,limitY2,limitY3,limitY4;

    ClientHandler(Master m)
    {
        this.m =m;
        initializeForceTools();
        openConnection();
        sendDimensions();
        sendPandC();
        calculateX();//X calculated
        calculateY();//Y calculated
        double cost1 = m.calculateError();
        double cost2 ;

        boolean flag = true;
        sendSignal(true);
        int numOfIterations = 1;
        while(flag)
        {
            calculateX();//X calculated
            calculateY();//Y calculated
            cost2 = m.calculateError();numOfIterations++;

            if(!Master.checkCost(cost1,cost2)|| numOfIterations==5) {flag = false;}
            sendSignal(flag);
            cost1=cost2;
        }
        System.out.println("Finished Training!");
        closeConnection();
    }
    private void calculateX()
    {
        Thread t1 =  new CalculateMatrixX(0,limitX1,m.getMatrixX(),m.getMatrixY(),out1,in1,m);
        Thread t2 = new CalculateMatrixX(limitX1,limitX2,m.getMatrixX(),m.getMatrixY(),out2,in2,m);
        Thread t3 = new CalculateMatrixX(limitX2,limitX3,m.getMatrixX(),m.getMatrixY(),out3,in3,m);
        Thread t4 = new CalculateMatrixX(limitX3,limitX4,m.getMatrixX(),m.getMatrixY(),out4,in4,m);
        t2.start();
        t1.start();
        t3.start();
        t4.start();
        try
        {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        }catch (InterruptedException e){e.printStackTrace();}
    }
    private void calculateY()
    {
       Thread t1 =  new CalculateMatrixY(0,limitY1,m.getMatrixY(),m.getMatrixX(),out1,in1,m);
       Thread t3 =  new CalculateMatrixY(limitY2,limitY3,m.getMatrixY(),m.getMatrixX(),out3,in3,m);
       Thread t2 =  new CalculateMatrixY(limitY1,limitY2,m.getMatrixY(),m.getMatrixX(),out2,in2,m);
        Thread t4 =  new CalculateMatrixY(limitY3,limitY4,m.getMatrixY(),m.getMatrixX(),out4,in4,m);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        try
        {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        }catch (InterruptedException e){e.printStackTrace();}
    }
    private void closeConnection()
    {
        try{
            out1.close();
            out2.close();
            out3.close();
            out4.close();
            in1.close();
            in2.close();
            in3.close();
            in4.close();
            requestWorker1.close();
            requestWorker2.close();
            requestWorker3.close();
            requestWorker4.close();
        }catch (IOException e){e.printStackTrace();}
    }
    private void openConnection()
    {
        try
        {
            requestWorker1 = new Socket(IP.ipWorker1,IP.portWorker1);
            requestWorker2 = new Socket(IP.ipWorker2,IP.portWorker2);
            requestWorker3 = new Socket(IP.ipWorker3,IP.portWorker3);
            requestWorker4 = new Socket(IP.ipWorker4,IP.portWorker4);
            System.out.println("connected with Workers. calculating...");
            out1 = new ObjectOutputStream(requestWorker1.getOutputStream());
            out2 = new ObjectOutputStream(requestWorker2.getOutputStream());
            out3 = new ObjectOutputStream(requestWorker3.getOutputStream());
            out4 = new ObjectOutputStream(requestWorker4.getOutputStream());
            in1 = new ObjectInputStream(requestWorker1.getInputStream());
            in2 = new ObjectInputStream(requestWorker2.getInputStream());
            in3 = new ObjectInputStream(requestWorker3.getInputStream());
            in4 = new ObjectInputStream(requestWorker4.getInputStream());
        }catch (IOException e){e.printStackTrace();}

    }
    private void initializeForceTools()
    {
        int sumForce = m.getSumForce();
        ArrayList<Integer> workForce = m.getWorkersForce();
        //fix limit X1
        double X1 = (InitializeData.rows* workForce.get(0))/sumForce;
        if(X1%2!=0.0)
            limitX1 = ((int) X1) +1;
        else
            limitX1 =(int)X1;

        //fix limit X2
        double X2 = (InitializeData.rows* workForce.get(1))/sumForce;
        if(X2%2!=0.0)
            limitX2 = ((int) X2) +1;
        else
            limitX2 =(int)X2;
        //fix limit X3
        double X3 = (InitializeData.rows* workForce.get(2))/sumForce;
        if(X3%2!=0.0)
            limitX3 = ((int) X3) +1;
        else
            limitX3 =(int)X3;
        //fix limit X4
        limitX4 = InitializeData.rows;

        //fix limit Y1
        double Y1 = (InitializeData.columns * workForce.get(0))/sumForce;
        if(Y1%2!=0.0)
            limitY1 = (int)Y1 +1;
        else
            limitY1 = (int)Y1;
        //fix limit Y2
        double Y2 = (InitializeData.columns*workForce.get(1))/sumForce;
        if(Y2%2!=0.0)
            limitY2 = (int)Y2 +1;
        else
            limitY2 = (int)Y2;
        //fix limit Y3
        double Y3 = (InitializeData.columns*workForce.get(2))/sumForce;
        if(Y3%2!=0.0)
            limitY3 = (int)Y3 +1;
        else
            limitY3 = (int)Y3;
        //fix limit Y4
        limitY4 = InitializeData.columns;
    }
    private void sendSignal(boolean flag)
    {
        try
        {
            out1.writeBoolean(flag);out1.flush();
            out2.writeBoolean(flag);out2.flush();
            out3.writeBoolean(flag);out3.flush();
            out4.writeBoolean(flag);out4.flush();
            out1.reset();out2.reset();out3.reset();out4.reset();
        }catch (IOException e){e.printStackTrace();}

    }
    private void sendDimensions()
    {
        try
        {
            out1.reset();
            out1.writeInt(InitializeData.K);out1.flush();
            out1.writeInt(0);out1.flush();
            out1.writeInt(limitX1);out1.flush();
            out1.writeInt(0);out1.flush();
            out1.writeInt(limitY1);out1.flush();
            out2.reset();
            out2.writeInt(InitializeData.K);out2.flush();
            out2.writeInt(limitX1);out2.flush();
            out2.writeInt(limitX2);out2.flush();
            out2.writeInt(limitY1);out2.flush();
            out2.writeInt(limitY2);out2.flush();
            out3.reset();
            out3.writeInt(InitializeData.K);out3.flush();
            out3.writeInt(limitX2);out3.flush();
            out3.writeInt(limitX3);out3.flush();
            out3.writeInt(limitY2);out3.flush();
            out3.writeInt(limitY3);out3.flush();
            out4.reset();
            out4.writeInt(InitializeData.K);out4.flush();
            out4.writeInt(limitX3);out4.flush();
            out4.writeInt(limitX4);out4.flush();
            out4.writeInt(limitY3);out4.flush();
            out4.writeInt(limitY4);out4.flush();

        }catch (IOException e){e.printStackTrace();}
    }
    private void sendPandC()
    {

        sendMatrix(InitializeData.matrixC);
        sendMatrix(InitializeData.binaryP);
    }
    private void sendMatrix(RealMatrix matrix)
    {
        try
        {
            out2.reset();out1.reset();out3.reset();out4.reset();
            out1.writeObject(matrix);out1.flush();
            out2.writeObject(matrix);out2.flush();
            out3.writeObject(matrix);out3.flush();
            out4.writeObject(matrix);out4.flush();

        }catch(IOException e){e.printStackTrace();}
    }
}
