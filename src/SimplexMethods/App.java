package SimplexMethods;

public class App {
    public static void main(String[] args) {
        double[] Z = {8, 6};
        int n = 2;
        int m = 2;
        double[] X = {11, 10};
        double[][] Clim = {{2, 5}, {4, 1}};
        String[] sign = {"<=", "<="};
        new Gomori(m,n,Z,Clim,X).Method();
    }
}
