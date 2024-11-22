import java.io.*;
import java.util.ArrayList;


class Retele extends Task{
    // linia = nodul
    // coloana = pozitia din acoperire
    int n;
    int m;
    int[][] links;
    int k;
    int[][] y;
    String result;
    ArrayList<Integer> solution;
    ArrayList<Integer> finalResult;
    public static void main(String[] args) throws IOException, InterruptedException {
        Retele r = new Retele();
        r.solve();
    }

    private void showMat(int[][] mat, int n, int m) {
        for(int i = 1; i <= n; i++) {
            for(int j = 1; j <= m; j++) {
                //System.out.print("i = " + i +" si " + "j= " + j + ": ");
                System.out.print(mat[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void initMat(int[][] mat) {
        for(int i = 1; i < mat.length; i++ ){
            for(int j = 1; j < mat.length; j++) {
                if(i == j) {
                    mat[i][j] = 1;
                }
            }
        }
    }

    private int withoutLink(int[][]mat, int n) {
        int count = 0;
        for(int i = 1; i <= n; i++) {
            for(int j = 1; j <= n; j++) {
                if(mat[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        int count = 1;
        for(int i = 1; i <= k; i++) {
            for(int j = 1; j <= n; j++) {
                y[i][j] = count++;
            }
        }
        // showMat(y, k, n);
        FileWriter myWriter = new FileWriter("sat.cnf");
        myWriter.write("p cnf ");
        myWriter.write(n * k + " ");
        // System.out.println("without" + withoutLink(links, n));
       // int noConditions = k + m + (k * (k - 1)) * withoutLink(links, n) / 2 + k * (k - 1) / 2 * n + (n * (n - 1)) / 2 * k;
        int noConditions = k + (n * (n - 1) / 2 - m) * k * (k - 1) + k * (k - 1) * n / 2;
        myWriter.write(noConditions + "\n");

        // conditia a
        for(int idx = 1; idx <= k; idx++) {
            for(int v = 1; v <= n; v++) {
                myWriter.write(y[idx][v] + " ");
            }
            myWriter.write("0\n");
        }


        // conditia b
        for(int v = 1; v <= n; v++) {
            for(int w = v + 1; w <= n; w++) {
                if(links[v][w] == 0) { // non-edge
                    for(int first_idx = 1; first_idx <= k; first_idx++) {
                        for(int sec_idx = 1; sec_idx <= k; sec_idx++) {
                            if(first_idx != sec_idx) {
                                myWriter.write(-y[first_idx][v] + " " + -y[sec_idx][w] + " 0\n");
                            }
                        }
                    }
                }
            }
        }

        // conditia c  (ith != jth)
        for(int first_idx = 1; first_idx <= k; first_idx++) {
            for(int second_idx = first_idx + 1; second_idx <=k; second_idx++) {
                for(int v = 1; v <= n; v++) {
                    myWriter.write(-y[first_idx][v] + " " + -y[second_idx][v] + " 0\n");
                }
            }
        }

//        for(int j = 1; j <= k; j++) { // conditie pentru pozitia k din acoperire
//            for(int i = 1; i <= n; i++) { // verific pentru fiecare nod
//                myWriter.write(y[i][j] + " ");
//            }
//            myWriter.write("0\n");
//        }
//
//        for(int i = 1; i <= n; i++) { // conditiile de a aparea cel mult o data in acoperire
//            for(int j = 1; j <= k; j++) {
//                myWriter.write(y[i][j] * (-1) + " ");
//            }
//            myWriter.write("0\n");
//        }
//
//        for(int p = 1; p <= n; p++) {// pentru muchii
//            for(int q = 1; q <= n; q++) {
//                if(links[p][q] == 1) { // daca exista muchie
//                    for(int r = 1; r <= k; r++) {
//                        myWriter.write(y[p][r] + " ");
//                    }
//                    for(int r = 1; r <= k; r++) {
//                        myWriter.write(y[q][r] + " ");
//                    }
//                    myWriter.write("0\n");
//                }
//            }
//        }
        // myWriter.write("0");
        myWriter.close();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        File file = new File("sat.sol");
        BufferedReader br;
        br = new BufferedReader(new FileReader(file));
        result = String.valueOf(br.readLine());
        br.readLine();

        if(result.equals("False")) {
            return;
        }
        String[] arr_result = br.readLine().split(" ");
        solution = new ArrayList<>();
        int dim = 0;
        for (String s : arr_result) {
            if (Integer.parseInt(s) > 0) {
                solution.add(Integer.parseInt(s));
            }
        }

        finalResult = new ArrayList<>();
        for(int i = 0; i <= k; i++) {
            for(int j = 0; j <= n; j++) {
                if(solution.contains(y[i][j])) {
                    finalResult.add(y[i][j]);
                }
            }
        }

    }

    @Override
    public void formulateOracleQuestion() throws IOException {

    }


    @Override
    public void readProblemData() throws IOException {
        InputStreamReader in= new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);
        String line = br.readLine();

        String[] args = line.split(" ");

        n = Integer.parseInt(args[0]);
        m = Integer.parseInt(args[1]);
        k = Integer.parseInt(args[2]);

        links = new int[n + 1][n + 1];
        initMat(links);
        // showMat(links, n, n);
        for(int i = 0; i < m; i++) {
            line = br.readLine();
            args = line.split(" ");
            int u = Integer.parseInt(args[0]);
            int v = Integer.parseInt(args[1]);
            links[u][v] = links[v][u] = 1;
            // System.out.println(u + " " + v);
        }

        // System.out.println(n + " " + m + " " + k);
        // showMat(links, n, n);
        y = new int[k + 1][m + 1];
    }

    @Override
    public void writeAnswer() throws IOException {
        System.out.println(result);
//        for(Integer i : finalResult) {
//            System.out.print(i + " ");
//        }
        System.out.println();
    }


}