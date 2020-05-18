package SimplexMethods;

abstract class Simplex {
    protected double[] result; //массив решений X*
    protected double[][] data; //общая таблица
    protected int m; //кол-во уравнений ограничений
    protected int n; //кол-во переменных
    protected double[] Z;
    protected double[][] Xi;
    protected double[] X;

    public Simplex() {}

    public Simplex(int m, int n, double[] z, double[][] xi, double[] x) {
        this.m = m;
        this.n = n;
        Z = z.clone();
        Xi = xi.clone();
        X = x.clone();
        int l = 4 + n + m;

        data = new double[m + 1][l];
        result = new double[m + n];
        FillTable();
    }

    protected double[][] getData() {
        return data;
    }

    protected abstract Simplex Method();

    protected void FillTable() {
        double[][] Base = new double[m][m];
        double[] Cbase = {0, 0};
        double[] IndBase = new double[m];
        for (int i = 0; i < m; i++) //создаем базис
        {
            Base[i][i] = 1;
            IndBase[i] = n + i;
        }

        for (int i = 0, t = 0; i < m; i++) {
            data[i][t++] = IndBase[i];
            data[i][t++] = Cbase[i];
            data[i][t++] = X[i];
            for (int j = 0; j < n; j++) {
                data[i][t++] = Xi[i][j];
            }
            for (int j = 0; j < m; j++) {
                data[i][t++] = Base[i][j];
            }
            t = 0;
        }
        QSol();
        ThetaSol();
    }

    public static void PrintZ(double[] Z, int n) {
        String str = "";
        if (Z[0] > 1 || Z[0] < 0) {
            str += Z[0] + "x1";
        } else {
            str += "x1";
        }

        for (int i = 1; i < n; i++) {
            if (Z[i] > 1) {
                str += " + " + Z[i] + "x" + (i + 1);
            } else if (Z[i] == 1) {
                str += " + x" + (i + 1);
            } else {
                str += Z[i] + " x" + (i + 1);
            }
        }
        str += " -> max";
        System.out.println(str);

    }

    public static void PrintXi(double[][] Xi, int n, int m, double[] X, String[] sign) {
        String str;
        for (int i = 0; i < m; i++) {
            str = "";
            str += Xi[i][0] + "x" + "1";
            for (int j = 1; j < n; j++) {
                if (Xi[i][j] > 0) {
                    str += " + " + Xi[i][j] + "x" + (j + 1);
                } else if (Xi[i][j] < 0) {
                    str += " " + Xi[i][j] + "x" + (j + 1);
                }
            }

            str += " " + sign[i] + " " + X[i];
            System.out.println(str);
        }
    }

    protected abstract void PrintTable();

    protected abstract void PrintResult();

    protected abstract boolean IsDone();

    protected abstract void QSol(); //считаем строку di

    protected abstract void ThetaSol(); //считаем самый правый столбик

    protected abstract void NewT();
}