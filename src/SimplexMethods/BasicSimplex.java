package SimplexMethods;

class BasicSimplex extends Simplex {
    private int l; //направляющая строка

    public BasicSimplex(int m, int n, double[] z, double[][] xi, double[] x) {

        super(m, n, z, xi, x);
    }

    @Override
    protected Simplex Method() {
        PrintTable();
        while (!IsDone()) {
            NewT();
            if (!IsDone()) ThetaSol();
            PrintTable();
        }
        PrintResult();
        return this;
    }

    @Override
    protected void PrintTable() {
        System.out.println();
        String[] columnNames = {"i", "Base", "C", "X", "x1", "x2", "x3", "x4", "O"};
        int row = m + 3;
        int col = 5 + n + m;
        String[][] print = new String[row][col];
        int r = 0;
        int l = 0;
        //1-я строка
        for (int i = 0; i < 4; i++) {
            print[r][l++] = " ";
        }
        for (int i = 0; i < n; i++) {
            print[r][l++] = "" + Z[i];
        }
        for (int i = 0; i < m; i++) {
            print[r][l++] = "" + 0;
        }
        print[r][l++] = "";
        l = 0;

        //шапка
        r++;
        for (int i = 0; i < columnNames.length; i++) {
            print[1][i] = columnNames[i];
        }

        //таблица
        r++;
        for (int i = 0, t = 0, p = 0; i < m; i++, r++) {
            print[r][p++] = "" + (i + 1); //i
            print[r][p++] = "" + String.format("%.0f", data[i][t++] + 1); //B
            print[r][p++] = "" + (data[i][t++]); //C
            print[r][p++] = "" + String.format("%.2f", data[i][t++]); //X
            for (int j = 0; j < n + m; j++, t++)//Xi
            {
                print[r][p++] = String.format("%.2f", data[i][t]);
            }

            if (data[i][t] == 0)
                print[r][p++] = " - ";
            else
                print[r][p++] = "" + String.format("%.2f", data[i][t++]);
            p = 0;
            t = 0;
        }

        //оценки
        for (int i = 0, p = 0; i < 1; i++, r++) {
            print[r][p++] = "Q";
            for (int j = 0; j < 2; j++) {
                print[r][p++] = " - ";
            }
            for (int j = 0; j < n + m + 1; j++) {
                print[r][p++] = "" + String.format("%.2f", data[m][j]);
            }
            print[r][p++] = " - ";
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(String.format("%8s", print[i][j]));
            }
            System.out.println();
        }
    }

    @Override
    protected void PrintResult() {
        for (int i = 0; i < m; i++)
            result[(int) data[i][0]] = data[i][2];

        System.out.println("Result for Z = 0" + ": ");
        String str;
        for (int i = 0; i < n + m; i++) {
            if (result[i] % 10 != 0)
                System.out.print(String.format("%10s", "x" + (i + 1) + ": " + String.format("%.2f", result[i])));
            else
                System.out.print(String.format("%8s", "x" + (i + 1) + ": " + String.format("%.0f", result[i])));
        }
    }

    @Override
    protected boolean IsDone() {
        for (int i = 1; i < n + m + 1; i++)
            if (data[m][i] < 0)
                return false;

        return true;
    }

    @Override
    protected void QSol() {
        for (int i = 0; i < n + 1 + m; i++)
            data[m][i] = 0;
        for (int i = 0; i < n + m + 1; i++)
            data[m][i] = 0;

        for (int j = 0, x = 2, c = 1; j < n + 1 + m; j++, x++) {
            for (int i = 0; i < m; i++) {
                data[m][j] += data[i][c] * data[i][x];
            }
        }
        for (int z = 1, i = 0; i < n; z++, i++) {
            data[m][z] -= Z[i];
        }

    }

    @Override
    protected void ThetaSol() {
        l = 1;
        for (int i = 2; i < n + m + 1; i++) {
            if (data[m][i] < data[m][l])
                l = i;
        }
        l += 2;
        for (int i = 0; i < m; i++) {
            if (data[i][l] > 0)
                data[i][3 + n + m] = data[i][2] / data[i][l];
        }
    }

    @Override
    protected void NewT() {
        int k = 0;
        for (int i = 0; i < m; i++) {
            if (data[i][3 + m + n] < data[k][3 + m + n])
                k = i;
        }
        int start = 2;
        int end = n + m + 3;
        //меняем индексы базисов
        data[k][0] = l - 3;
        data[k][1] = Z[l - 3];
        double el = data[k][l];

        for (int i = start; i < end; i++)
            data[k][i] /= el;


        for (int i = 0; i < m; i++) {
            if (i == k)
                continue;
            for (int j = start; j < end; j++) {
                if (j == l)
                    continue;
                data[i][j] -= data[i][l] * data[k][j];
            }
        }
        for (int i = 0; i < m; i++)
            if (i != k)
                data[i][l] = 0;
        QSol();
    }
}