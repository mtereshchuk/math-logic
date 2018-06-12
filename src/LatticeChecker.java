import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LatticeChecker {

    private Scanner in;
    private PrintWriter out;
    private int n;
    private ArrayList<Integer>[] dirG, revG, dirReachV, revReachV;
    private Set<Integer> visited;
    private int[][] sup, inf, psCompl;

    private void dfs(int u, ArrayList<Integer>[] g) {
        visited.add(u);
        for (int i = 0; i < g[u].size(); i++) {
            int v = g[u].get(i);
            if (!visited.contains(v)) {
                dfs(v, g);
            }
        }
    }

    private void printSupOrInfFail(char c, int u, int v) {
        out.print("Операция '" + c + "' не определена: " + (u + 1) + c + (v + 1));
    }

    private boolean checkSupOrInf(ArrayList<Integer>[] reachV, int[][] supOrInf) {
        char c = reachV == dirReachV ? '+' : '*';
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                List<Integer> list = new ArrayList<>(reachV[u]);
                list.retainAll(reachV[v]);
                int minOrMax = -1;
                for (Integer i : list) {
                    if (reachV[i].containsAll(list) && minOrMax == -1) {
                        minOrMax = i;
                    } else if (reachV[i].containsAll(list) && minOrMax != -1) {
                        printSupOrInfFail(c, u, v);
                        return false;
                    }
                }
                if (minOrMax == -1) {
                    printSupOrInfFail(c, u, v);
                    return false;
                } else {
                    supOrInf[u][v] = minOrMax;
                }
            }
        }
        return true;
    }

    private boolean checkDistributivity() {
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                for (int w = 0; w < n; w++) {
                    if (inf[u][sup[v][w]] != sup[inf[u][v]][inf[u][w]]) {
                        out.print("Нарушается дистрибутивность: " + (u + 1) + '*' + '(' + (v + 1) + '+' + (w + 1) + ')');
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void printPseudoCompletionFail(int u, int v) {
        out.print("Операция '->' не определена: " + (u + 1) + "->" + (v + 1));
    }

    private boolean checkPseudoCompletion() {
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                List<Integer> list = new ArrayList<>();
                for (int w = 0; w < n; w++) {
                    if (dirReachV[inf[u][w]].contains(v)) {
                        list.add(w);
                    }
                }
                if (list.isEmpty()) {
                    printPseudoCompletionFail(u, v);
                    return false;
                } else {
                    int max = -1;
                    for (Integer i : list) {
                        if (revReachV[i].containsAll(list) && max == -1) {
                            max = i;
                        }/* else if (revReachV[i].containsAll(list) && max != -1) {
                            printPseudoCompletionFail(u, v);
                            return false;
                        }*/
                    }
                    if (max == -1) {
                        printPseudoCompletionFail(u, v);
                        return false;
                    } else {
                        psCompl[u][v] = max;
                    }
                }
            }
        }
        return true;
    }

    private int getMinOrMax(ArrayList<Integer>[] reachV) {
        List<Integer> list = new ArrayList<>();
        for (int v = 0; v < n; v++) {
            list.add(v);
        }
        int minOrMax = -1;
        for (int v = 0; v < n; v++) {
            if (reachV[v].containsAll(list)) {
                minOrMax = v;
                break;
            }
        }
        return minOrMax;
    }

    private boolean checkBooleanProperty() {
        int min = getMinOrMax(dirReachV);
        int max = getMinOrMax(revReachV);
        for (int v = 0; v < n; v++) {
            if (sup[v][psCompl[v][min]] != max) {
                out.print("Не булева алгебра: " + (v + 1) + "+~" + (v + 1));
                return false;
            }
        }
        return true;
    }

    private void solve() {
        n = in.nextInt();
        dirG = new ArrayList[n];
        revG = new ArrayList[n];
        dirReachV = new ArrayList[n];
        revReachV = new ArrayList[n];
        in.nextLine();
        for (int i = 0; i < n; i++) {
            dirG[i] = new ArrayList<>();
            revG[i] = new ArrayList<>();
            dirReachV[i] = new ArrayList<>();
            revReachV[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            dirG[i].addAll(Arrays.stream(in.nextLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList()));
            for (int j = 0; j < dirG[i].size(); j++) {
                dirG[i].set(j, dirG[i].get(j) - 1);
            }
            for (int j = 0; j < dirG[i].size(); j++) {
                revG[dirG[i].get(j)].add(i);
            }
        }

        visited = new HashSet<>();
        for (int v = 0; v < n; v++) {
            visited.clear();
            dfs(v, dirG);
            dirReachV[v].addAll(visited);

            visited.clear();
            dfs(v, revG);
            revReachV[v].addAll(visited);
        }

        sup = new int[n][n];
        inf = new int[n][n];
        psCompl = new int[n][n];
        if (checkSupOrInf(dirReachV, sup)
                && checkSupOrInf(revReachV, inf)
                && checkDistributivity()
                && checkPseudoCompletion()
                && checkBooleanProperty()) {
            out.print("Булева алгебра");
        }
    }

    private void run() {
        try {
            in = new Scanner(new InputStreamReader(new FileInputStream("input.txt")));
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream("output.txt")));
            solve();
            out.close();
        } catch (FileNotFoundException ignored) {}
    }

    public static void main(String[] args) {
        new LatticeChecker().run();
    }
}
