package Master;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.random.*;
import java.io.*;
class InitializeData
{
     static RealMatrix dataMatrix,recomendationMatrix;
    static  RealMatrix binaryP, matrixC;
    private RealMatrix matrixX,matrixY;
    private static final double A = 40.0;
    static final int rows =835;
    static final int columns =1692;
    static final int K = 20;
    static final double LCost = 0.1;
    static final double ThresHold = 0.1;
    private static RandomGenerator r = new JDKRandomGenerator() ;

    InitializeData(String name)
    {
        r.setSeed(1);
        dataMatrix = MatrixUtils.createRealMatrix(rows,columns);
        recomendationMatrix = MatrixUtils.createRealMatrix(rows,columns);
        fillData(name);
        binaryP = MatrixUtils.createRealMatrix(rows,columns);
        matrixC = MatrixUtils.createRealMatrix(rows,columns);
        matrixX = MatrixUtils.createRealMatrix(rows,K);
        matrixY = MatrixUtils.createRealMatrix(columns,K);
        calculatePMatrix(binaryP);
        calculateCMatrix(matrixC);
        createX(matrixX,r);
        createY(matrixY,r);
    }
    private void fillData(String Filename)
    {

        try
        {
            BufferedReader in = new BufferedReader(new FileReader(new java.io.File(Filename)));
            int row,column;
            double data;

            for (String x = in.readLine(); x != null; x = in.readLine())
            {
                String[] tokens = x.split(", ");
                row = Integer.parseInt(tokens[0]);
                column = Integer.parseInt(tokens[1]);
                data = Double.parseDouble(tokens[2]);
                dataMatrix.setEntry(row,column,data);
            }
        }catch (IOException e){System.out.println("File I/O error!");}
    }
    //fills P Matrix
    private void calculatePMatrix(RealMatrix P)
    {
        for(int i=0; i<dataMatrix.getRowDimension(); i++)
        {
            for (int j = 0; j < dataMatrix.getColumnDimension(); j++)
            {
                if (dataMatrix.getEntry(i, j) != 0.0)
                {
                    P.setEntry(i, j, 1.0);
                }
            }
        }
    }
    //fills C Matrix
    private void calculateCMatrix(RealMatrix C)
    {
        for(int i=0; i<dataMatrix.getRowDimension(); i++)
        {
            for (int j = 0; j < dataMatrix.getColumnDimension(); j++)
            {
                double mul = dataMatrix.getEntry(i,j);
                mul = mul*A;
                C.setEntry(i,j,mul);
                C.addToEntry(i,j,1.0);
            }
        }
    }
    //creates Random X
    private void createX(RealMatrix x,RandomGenerator r)
    {
        for(int i=0; i<x.getRowDimension(); i++)
        {
            for(int j=0; j<x.getColumnDimension(); j++)
            {
                x.setEntry(i,j,Math.abs(r.nextDouble()));
            }
        }
        x.scalarAdd(0.01);
    }
    //creates Random Y
    private void createY(RealMatrix y,RandomGenerator r)
    {
        for(int i=0; i<y.getRowDimension(); i++)
        {
            for(int j=0; j<y.getColumnDimension(); j++)
            {
                y.setEntry(i,j,Math.abs(r.nextDouble()));
            }
        }
        y.scalarAdd(0.01);
    }
    RealMatrix getMatrixX()
    {

        return matrixX;}
    RealMatrix getMatrixY()
    {

        return matrixY;
    }
}