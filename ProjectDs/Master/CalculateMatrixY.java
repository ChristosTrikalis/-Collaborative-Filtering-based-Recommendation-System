package Master;
import org.apache.commons.math3.linear.RealMatrix;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class CalculateMatrixY extends Thread
{
    private int startMatrix,limitMatrix;
    private RealMatrix matrixY,matrixX;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Master m;

    CalculateMatrixY(int startMatrix, int limitMatrix, RealMatrix matrixY, RealMatrix matrixX, ObjectOutputStream out, ObjectInputStream in,Master m)
    {
        this.startMatrix = startMatrix;
        this.limitMatrix = limitMatrix;
        this.matrixY = matrixY;
        this.matrixX = matrixX;
        this.out = out;
        this.in = in;
        this.m = m;
    }
    public void run()
    {

        sendMatrix(matrixX,out);
        matrixY = receiveMatrix(in);
        m.setY(matrixY,startMatrix,limitMatrix);

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
