package Worker1;
import org.apache.commons.math3.linear.*;
class Worker1
{
    private static RealMatrix Pmatrix,Icu,I,Ici;
    private static final double LAMDA = 0.1;
    void initialize(RealMatrix P, int k)
    {
        Pmatrix = P;
        Icu = MatrixUtils.createRealIdentityMatrix(Pmatrix.getColumnDimension());
        Ici = MatrixUtils.createRealIdentityMatrix(Pmatrix.getRowDimension());
        I = MatrixUtils.createRealIdentityMatrix(k);
    }
    //calculates yTy or xTx
    private RealMatrix preCalculate(RealMatrix matrix)
    {
        return (matrix.transpose()).multiply(matrix);

    }
    //calculates Diagonal Cu or Ci
    RealMatrix Diagonial(double[] cu)
    {
        return MatrixUtils.createRealDiagonalMatrix(cu);

    }
    //calculates Xu
    void calculateXu(RealMatrix matrix,RealMatrix X, RealMatrix D,int i)
    {
        RealMatrix Xu;
        Xu = (preCalculate(matrix));
        RealMatrix par = (D.subtract(Icu));
        par = (matrix.transpose()).multiply(par);
        par = par.multiply(matrix);
        Xu = Xu.add(par);
        Xu = Xu.add(I.scalarMultiply(LAMDA));
        Xu = new LUDecomposition(Xu).getSolver().getInverse();
        Xu = Xu.multiply(matrix.transpose());
        Xu = Xu.multiply(D);
        Xu = Xu.multiply((Pmatrix.getRowMatrix(i)).transpose());
        X.setRowMatrix(i,Xu.transpose());
    }
    //calculates Yi
    void calculateYi(RealMatrix matrix,RealMatrix Y, RealMatrix D, int i)
    {

        RealMatrix Yi;
        Yi = preCalculate(matrix);
        RealMatrix par2 = D.subtract(Ici);
        par2 = (matrix.transpose()).multiply(par2);
        par2 = par2.multiply(matrix);
        Yi = Yi.add(par2);
        Yi = Yi.add(I.scalarMultiply(LAMDA));
        Yi = new LUDecomposition(Yi).getSolver().getInverse();
        Yi = Yi.multiply(matrix.transpose());
        Yi = Yi.multiply(D);
        Yi = Yi.multiply(Pmatrix.getColumnMatrix(i));

        Y.setRowMatrix(i,Yi.transpose());

    }
}

