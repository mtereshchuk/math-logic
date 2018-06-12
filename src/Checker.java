import expression.*;
import parser.ExpressionParser;
import parser.Parser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Checker {

    private boolean axiom1(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (b instanceof Implication) {
            Expression c = ((Implication) b).getFirst();
            Expression d = ((Implication) b).getSecond();
            if (a.equals(d)) {
                return true;
            }
        }
        return false;
    }

    private boolean axiom2(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof Implication && b instanceof Implication) {
            Expression c = ((Implication) a).getFirst();
            Expression d = ((Implication) a).getSecond();
            Expression e = ((Implication) b).getFirst();
            Expression f = ((Implication) b).getSecond();
            if (e instanceof Implication && f instanceof Implication) {
                Expression g = ((Implication) e).getFirst();
                Expression h = ((Implication) e).getSecond();
                Expression i = ((Implication) f).getFirst();
                Expression j = ((Implication) f).getSecond();
                if (h instanceof Implication) {
                    Expression k = ((Implication) h).getFirst();
                    Expression l = ((Implication) h).getSecond();
                    if (c.equals(g) && c.equals(i) && d.equals(k) && l.equals(j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean axiom3(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (b instanceof Implication) {
            Expression c = ((Implication) b).getFirst();
            Expression d = ((Implication) b).getSecond();
            if (d instanceof Conjunction) {
                Expression e = ((Conjunction) d).getFirst();
                Expression f = ((Conjunction) d).getSecond();
                if (a.equals(e) && c.equals(f)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean axiom4(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof Conjunction) {
            Expression c = ((Conjunction) a).getFirst();
            Expression d = ((Conjunction) a).getSecond();
            if (b.equals(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean axiom5(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof Conjunction) {
            Expression c = ((Conjunction) a).getFirst();
            Expression d = ((Conjunction) a).getSecond();
            if (b.equals(d)) {
                return true;
            }
        }
        return false;
    }

    private boolean axiom6(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (b instanceof Disjunction) {
            Expression c = ((Disjunction) b).getFirst();
            Expression d = ((Disjunction) b).getSecond();
            if (a.equals(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean axiom7(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (b instanceof Disjunction) {
            Expression c = ((Disjunction) b).getFirst();
            Expression d = ((Disjunction) b).getSecond();
            if (a.equals(d)) {
                return true;
            }
        }
        return false;
    }

    private boolean axiom8(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof  Implication && b instanceof Implication) {
            Expression c = ((Implication) a).getFirst();
            Expression d = ((Implication) a).getSecond();
            Expression e = ((Implication) b).getFirst();
            Expression f = ((Implication) b).getSecond();
            if (e instanceof Implication && f instanceof Implication) {
                Expression g = ((Implication) e).getFirst();
                Expression h = ((Implication) e).getSecond();
                Expression i = ((Implication) f).getFirst();
                Expression j = ((Implication) f).getSecond();
                if (i instanceof Disjunction) {
                    Expression k = ((Disjunction) i).getFirst();
                    Expression l = ((Disjunction) i).getSecond();
                    if (c.equals(k) && d.equals(h) && d.equals(j) && g.equals(l)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean axiom9(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof Implication && b instanceof Implication) {
            Expression c = ((Implication) a).getFirst();
            Expression d = ((Implication) a).getSecond();
            Expression e = ((Implication) b).getFirst();
            Expression f = ((Implication) b).getSecond();
            if (e instanceof Implication && f instanceof Negation) {
                Expression g = ((Implication) e).getFirst();
                Expression h = ((Implication) e).getSecond();
                Expression i = ((Negation) f).getFirst();
                if (h instanceof Negation) {
                    Expression j = ((Negation) h).getFirst();
                    if (c.equals(g) && c.equals(i) && d.equals(j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean axiom10(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof Negation) {
            Expression c = ((Negation) a).getFirst();
            if (c instanceof Negation) {
                Expression d = ((Negation) c).getFirst();
                if (b.equals(d)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String checkAxioms(Implication implication) {
        if (axiom1(implication)) return "1";
        if (axiom2(implication)) return "2";
        if (axiom3(implication)) return "3";
        if (axiom4(implication)) return "4";
        if (axiom5(implication)) return "5";
        if (axiom6(implication)) return "6";
        if (axiom7(implication)) return "7";
        if (axiom8(implication)) return "8";
        if (axiom9(implication)) return "9";
        if (axiom10(implication)) return "10";
        return null;
    }

    private void input() {
        lines = new ArrayList<>();
        try {
            lines = Files.lines(Paths.get("output.txt")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    private void clean() {
        cleanLines = new ArrayList<>();
        for (String line : lines) {
            cleanLines.add(line.replaceAll("\\s+", ""));
        }
    }

    private Parser parser;
    private HashMap<Expression, Integer> assumptions;
    private List<String> lines, cleanLines;

    private void getAssumptions(String firstString) {
        assumptions = new HashMap<>();
        List<String> parts = Arrays.asList(firstString.split("\\|-"));
        if (!parts.get(0).equals("")) {
            List<String> assumptionsString = Arrays.asList(parts.get(0).split(","));
            for (int i = 0; i < assumptionsString.size(); i++) {
                assumptions.put(parser.parse(assumptionsString.get(i)), i + 1);
            }
        }
    }

    private List<Expression> expressions;
    private HashMap<Expression, Integer> expressionNumber;

    private void fill() {
        expressions = new ArrayList<>();
        expressionNumber = new HashMap<>();
        for (int i = 1; i < cleanLines.size(); i++) {
            Expression expression = parser.parse(cleanLines.get(i));
            expressions.add(expression);
            if (expressionNumber.containsKey(expression)) {
                expressionNumber.put(expression, Math.min(i, expressionNumber.get(expression)));
            } else {
                expressionNumber.put(expression, i);
            }
        }
    }

    private void doProof() {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output2.txt"))) {
            String proof;
            HashMap<Expression, String> modusPonens = new HashMap<>();
            for (int i = 0; i < expressions.size(); i++) {
                Expression expression = expressions.get(i);
                proof = null;
                if (assumptions.containsKey(expression)) {
                    proof = "Предп. " + Integer.toString(assumptions.get(expression));
                }
                if (expression instanceof Implication) {
                    Implication implication = (Implication) expression;
                    if (proof == null) {
                        String number = checkAxioms(implication);
                        if (number != null) {
                            proof = "Сх. акс. " + number;
                        }
                    }
                    if (expressionNumber.containsKey(implication.getFirst())
                            && expressionNumber.containsKey(implication.getSecond())) {
                        int a = expressionNumber.get(implication.getFirst());
                        int b = expressionNumber.get(implication.getSecond());
                        if (b > a && b > (i + 1)) {
                            modusPonens.put(implication.getSecond(), Integer.toString(i + 1) + ", " + Integer.toString(a));
                        }
                    }
                }
                if (proof == null && modusPonens.containsKey(expression)) {
                    proof = "M.P. " + modusPonens.get(expression);
                }
                if (proof == null) {
                    proof = "Не доказано";
                }
                bw.write("("
                        + Integer.toString(i + 1)
                        + ") "
                        + cleanLines.get(i + 1)
                        + " "
                        + "(" + proof
                        + ")\n");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void check() {
        parser = new ExpressionParser();
        input();
        clean();
        getAssumptions(cleanLines.get(0));
        fill();
        doProof();
    }

    public static void main(String[] args) {
        new Checker().check();
    }
}
