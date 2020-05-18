package SimplexMethods;

import java.util.ArrayList;
import java.util.List;

class DualSimplex extends Simplex {
    private int line = 0; //направляющая строка
    private int[] columns; //индексы колонок

    public DualSimplex(BasicSimplex bs) {
        super();
        Z = bs.Z.clone();
        m = bs.m;
        n = bs.n;

        this.data = new double[m + 2][bs.data[0].length - 1];
        columns = new int[data[0].length - 2];

        for (int i = 0; i < columns.length; i++) {
            columns[i] = -1;
        }
        for (int i = 0; i < bs.data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = bs.data[i][j];
            }
        }
        result = new double[n + m];
    }

    protected void CreateNewTable() {
        int mx = MaxFrac();
        double[] NewRow = new double[data[0].length];
        double[][] arr = new double[data.length + 1][data[0].length + 1];
        columns = new int[data[0].length - 2];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = -1;
        }
        for (int i = 0; i < data.length - 2; i++) {
            for (int j = 0; j < data[0].length; j++) {
                arr[i][j] = data[i][j];
            }
        }

        NewRow[0] = data[0].length - 3;
        NewRow[1] = 0;
        NewRow[2] = -Frac(data[mx][2]);
        for (int i = 3; i < data[0].length; i++) {
            NewRow[i] = -Frac(data[mx][i]);
        }

        for (int i = 0; i < NewRow.length; i++) {
            arr[data.length - 2][i] = NewRow[i];
        }
        arr[data.length - 2][data[0].length] = 1;
        this.data = arr;
        QSol();
       /* PrintTable();
        NewT();*/
    }

    @Override
    protected void QSol() {
        int d = data.length - 2;
        for (int i = 0; i < data[0].length - 2; i++)
            data[d][i] = 0;

        for (int j = 0, k = 2; j < data[0].length - 2; j++, k++) {
            for (int i = 0; i < data.length - 2; i++) {
                data[data.length - 2][j] += data[i][1] * data[i][k];
            }
        }
        for (int z = 1, i = 0; i < n; z++, i++) {
            data[d][z] -= Z[i];
        }
    }

    @Override
    protected void ThetaSol() {
        //ищем напр строку
        line = 0;
        for (int i = 0; i < data.length - 2; i++) {
            if (data[i][2] < 0 && (data[i][2] < data[line][2]))
                line = i;
        }
        //ищем напр столбец
        for (int i = 1, j = 0, c = 3; i < data[0].length - 2; i++, c++) {
            if (data[line][c] < -0) {
                columns[j++] = c;
            }

        }
        for (int i = 0, j = 1; i < columns.length; i++, j++) {
            if (columns[i] >= 0) {
                data[data.length - 1][i] = Math.abs(data[data.length - 2][columns[i] - 2] / data[line][columns[i]]);
            }
        }
    }

    @Override
    protected boolean IsDone() {

        for (int i = 0; i < data.length - 2; i++) {
            if (data[data.length - 2][i] < 0 || data[i][2] < 0) {
                return false;
            }
        }

        for (int i = 0; i < data[0].length - 3; i++)
            data[data.length - 1][i] = 0;
        return true;
    }

    @Override
    protected void NewT() {
        int column = columns[0];
        int min = 0;
        for (int i = 1, k = 1; i < columns.length; i++) {
            if (columns[i] >= 0) {
                if (data[data.length - 1][i] < data[data.length - 1][min]) {
                    min = k;
                    column = columns[i];
                }
                k++;
            }

        }
        for (int i = 0; i < columns.length; i++) {
            columns[i] = -1;
            data[data.length - 1][i] = 0;
        }
        int start = 2;
        int end = data[0].length;
        //меняем индексы базисов

        data[line][0] = column - 3;
        data[line][1] = column >= n ? 0 : Z[column - 3];

        double el = data[line][column];

        for (int i = start; i < end; i++)
            data[line][i] /= el;


        for (int i = 0; i < data.length - 2; i++) {
            if (i == line)
                continue;
            for (int j = start; j < end; j++) {
                if (j == column)
                    continue;
                data[i][j] -= data[i][column] * data[line][j];
            }
        }
        for (int i = 0; i < data.length - 2; i++)
            if (i != line)
                data[i][column] = 0;
        QSol();
    }

    @Override
    protected Simplex Method() {
        while (!IsDone()) {
            ThetaSol();
            PrintTable();
            NewT();
            PrintTable();
        }
        return this;
    }

    @Override
    protected void PrintTable() {
        System.out.println();
        List<String> columnNames = new ArrayList<String>(data[0].length + 1);
        CreateColumns(columnNames);
        int row = data.length + 2; //кол-во строк
        int col = data[0].length + 1;
        String[][] print = new String[row][col];
        int r = 0; //print row
        int l = 0; //print column
        int d = 0; //data column

        //1-я строка
        for (int i = 0; i < 4; i++) {
            print[r][l++] = " ";
        }
        for (int i = 0; i < n; i++) {
            print[r][l++] = "" + Z[i];
        }

        for (int i = 0; i < data[0].length - n - 3; i++) {
            print[r][l++] = "" + 0;
        }
        l = 0;

        //шапка
        r++;
        for (int i = 0; i < columnNames.size(); i++) {
            print[1][i] = columnNames.get(i);
        }

        //таблица
        r++;
        for (int i = 0, t = 0, p = 0; i < data.length - 2; i++, r++, d++) {
            print[r][p++] = "" + (i + 1); //i
            print[r][p++] = "" + String.format("%.0f", data[d][t++] + 1); //B
            print[r][p++] = "" + (data[d][t++]); //C
            print[r][p++] = "" + String.format("%.1f", data[d][t++]); //X
            for (int j = 0; j < data[0].length - 3; j++, t++)//Xi
            {
                if (data[d][t] == -0) data[d][t] = Math.abs(data[d][t]);
                print[r][p++] = String.format("%.1f", data[d][t]);
            }
            p = 0;
            t = 0;
        }
        //оценки
        int test = 1;
        String[] acc = {"Qi", "Qj"};
        for (int i = 0, j = r; i < 2; i++, j++) {
            print[j][0] = "" + acc[i];
        }
        for (int i = 1; i < 3; i++) {
            print[r][i] = "-";
        }

        for (int i = 1, j = r + 1; i < 4; i++) {
            print[j][i] = "-";
        }
        //data.length - 1
        for (int i = 0, cp = 3; i < data[0].length - 2; i++) {
            print[r][cp++] = "" + String.format("%.1f", data[d][i]);
        }
        d++;
        r++;

        //Qj
        for (int i = 0, cp = 4, k = 0; i < columns.length; i++) {
            if (columns[k] >= 0 && columns[k] - 3 == i) {
                print[r][cp++] = "" + String.format("%.1f", data[data.length - 1][k]);
                k++;
            } else
                print[r][cp++] = "-";
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

        for (int i = 0; i < data.length - 2; i++) {
            if (data[i][0] < n + m)
                result[(int) data[i][0]] = data[i][2];
        }
        System.out.println("Result for Z = 0" + ": ");
        String str;
        for (int i = 0; i < result.length; i++) {
            System.out.print(String.format("%10s", "x" + (i + 1) + ": " + String.format("%.2f", result[i])));
        }

    }

    private void CreateColumns(List<String> a) {
        a.add("i");
        a.add("Base");
        a.add("C");
        a.add("X");
        for (int i = 1; i < data[0].length - 2; i++) {
            a.add("x" + i);
        }
    }

    private double DecimalPlaces(double num, double place) {
        double scale = Math.pow(10, place);
        return Math.round(num * scale) / scale;
    }

    private double Frac(double d) {
        if (d == 0)
            return d;
        double d2 = d - Math.floor(d);
        return d2;
    }

    private int MaxFrac() {
        double FrX = 0;
        int l = 0;
        for (int i = 0; i < data.length - 2; i++) {
            if (data[i][0] < n) {
                FrX = Frac(DecimalPlaces(data[0][2], 2));
            }
        }

        for (int i = 1; i < data.length - 2; i++) {
            if (data[i][0] < n) {
                double k = Frac(DecimalPlaces(data[i][2], 2));
                if (FrX < k) {
                    l = i;
                    FrX = k;
                }
            }

        }

        return l;
    }

}