package Master;
import org.apache.commons.math3.linear.*;
import java.io.*;
public class CalculateMatrixX extends Thread
{
    private int startMatrix,limitMatrix;
    private RealMatrix matrixX,matrixY;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Master m;

    CalculateMatrixX(int startMatrix, int limitMatrix, RealMatrix matrixX, RealMatrix matrixY, ObjectOutputStream out, ObjectInputStream in,Master m)
    {
        this.startMatrix = startMatrix;
        this.limitMatrix = limitMatrix;
        this.matrixX = matrixX;
        this.matrixY = matrixY;
        this.out = out;
        this.in = in;
        this.m =m;
    }
    public void run()
    {
        sendMatrix(matrixX,out);
        sendMatrix(matrixY,out);
        matrixX = receiveMatrix(in);
        m.setX(matrixX,startMatrix,limitMatrix);
    }
    private void sendMatrix(RealMatrix matrix,ObjectOutputStream out)
    {
        try {
            out.reset();
            out.writeObject(matrix);out.flush();
        }catch(IOException e){e.printStackTrace();}
    }
    private RealMatrix receiveMatrix(ObjectInputStream in)
    {
        RealMatrix matrix = null;
        try{
            matrix =(RealMatrix) in.readObject();
        }catch (ClassNotFoundException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}

        return matrix;
    }

}
