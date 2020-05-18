package SimplexMethods;

import SimplexMethods.BasicSimplex;
import SimplexMethods.DualSimplex;

class Gomori {
    private final int m;
    private final int n;
    private final double[] Z;
    private final double[][] Xi;
    private final double[] X;
    private DualSimplex d;

    public Gomori(int m, int n, double[] z, double[][] xi, double[] x) {
        this.m = m;
        this.n = n;
        Z = z.clone();
        Xi = xi.clone();
        X = x.clone();
        BasicSimplex b = new BasicSimplex(m, n, Z, Xi, X);
        d = new DualSimplex((BasicSimplex) b.Method());

    }

    private double DecimalPlaces(double num, double place) {
        double scale = Math.pow(10, place);
        return Math.round(num * scale) / scale;
    }

    public Simplex Method() {
        while (!IsDone()) {
            d.CreateNewTable();
            d.Method();
        }

        d.PrintResult();
        return d;
    }

    private boolean IsDone() {

        for (int i = 0; i < d.data.length - 2; i++)
        {
            if (DecimalPlaces(d.data[i][2],2)  % 1 != 0) {
                return false;
            }
        }
        return true;

    }

}