import expression.*;
import parser.ExpressionParser;
import parser.Parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ProofMachine {

    private Parser parser;
    private String line;
    private Expression mainProvable;
    private List<Expression> mainAssumptionsList;
    private Set<Variable> variablesSet;
    private List<Variable> variablesList;
    private Map<String, Boolean> variablesNameMap;
    private List<Expression> proof;
    private Map<Set<Expression>, List<Expression>> proofs;
    private boolean allIsBad;
    private Set<Expression> badSet;

    private ProofMachine() {
        parser = new ExpressionParser();
        mainAssumptionsList = new ArrayList<>();
        variablesSet = new HashSet<>();
        variablesList = new ArrayList<>();
        variablesNameMap = new HashMap<>();
        proof = new ArrayList<>();
        proofs = new HashMap<>();
        allIsBad = false;
    }

    private void input() {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.lines(Paths.get("input.txt")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        }catch (IOException ignored){}
        line = lines.get(0).replaceAll("\\s+", "");
    }

    private void split() {
        List<String> parts = Arrays.asList(line.split("\\|="));
        mainProvable = parser.parse(parts.get(1));
        if (!parts.get(0).equals("")) {
            List<String> assumptionsString = Arrays.asList(parts.get(0).split(","));
            for (String anAssumptionsString : assumptionsString) {
                Expression expression = parser.parse(anAssumptionsString);
                mainProvable = new Implication(expression, mainProvable);
                mainAssumptionsList.add(expression);
            }
        }
    }

    private void getVariables(Expression expr) {
        if (expr instanceof BinaryOperation) {
            BinaryOperation binOp = (BinaryOperation) expr;
            getVariables(binOp.getFirst());
            getVariables(binOp.getSecond());
        }
        if (expr instanceof Negation) {
            Negation neg = (Negation) expr;
            getVariables(neg.getFirst());
        }
        if (expr instanceof Variable) {
            Variable var = (Variable) expr;
            variablesSet.add(var);
        }
    }

    private List<Expression> getProofFromFile(String fileName, String first, String second) {
        if (fileName.equals("")) {
            return new ArrayList<>();
        }
        List<String> inputLines = new ArrayList<>();
        try {
            inputLines = Files.lines(Paths.get(fileName)).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        }catch (IOException ignored){}
        for (int i = 0; i < inputLines.size(); i++) {
            inputLines.set(i, inputLines.get(i).replaceAll("A", "QWERTYPO"));
        }
        if (!second.equals("")) {
            for (int i = 0; i < inputLines.size(); i++) {
                inputLines.set(i, inputLines.get(i).replaceAll("B", second));
            }
        }
        for (int i = 0; i < inputLines.size(); i++) {
            inputLines.set(i, inputLines.get(i).replaceAll("QWERTYPO", first));
        }
        return inputLines.stream().map(s -> parser.parse(s)).collect(Collectors.toList());
    }

    private List<Expression> proofRec(Expression expr) {
        List<Expression> res;
        if (expr instanceof BinaryOperation) {
            BinaryOperation binOp = (BinaryOperation) expr;
            List<Expression> firstProof = proofRec(binOp.getFirst());
            List<Expression> secondProof = proofRec(binOp.getSecond());
            res = firstProof;
            res.addAll(secondProof);
            String fileName = "";
            boolean value = false;
            if (!binOp.getFirst().getValue() && !binOp.getFirst().getValue()) {
                if (binOp instanceof Conjunction) {
                    fileName = "src/proofs/FFand.txt";
                    value = false;
                }
                if (binOp instanceof Disjunction) {
                    fileName = "src/proofs/FFor.txt";
                    value = false;
                }
                if (binOp instanceof Implication) {
                    fileName = "src/proofs/FFfoll.txt";
                    value = true;
                }
            }
            if (!binOp.getFirst().getValue() && binOp.getSecond().getValue()) {
                if (binOp instanceof Conjunction) {
                    fileName = "src/proofs/FTand.txt";
                    value = false;
                }
                if (binOp instanceof Disjunction) {
                    fileName = "src/proofs/FTor.txt";
                    value = true;
                }
                if (binOp instanceof Implication) {
                    fileName = "src/proofs/FTfoll.txt";
                    value = true;
                }
            }
            if (binOp.getFirst().getValue() && !binOp.getSecond().getValue()) {
                if (binOp instanceof Conjunction) {
                    fileName = "src/proofs/TFand.txt";
                    value = false;
                }
                if (binOp instanceof Disjunction) {
                    fileName = "src/proofs/TFor.txt";
                    value = true;
                }
                if (binOp instanceof Implication) {
                    fileName = "src/proofs/TFfoll.txt";
                    value = false;
                }
            }
            if (binOp.getFirst().getValue() && binOp.getSecond().getValue()) {
                if (binOp instanceof Conjunction) {
                    fileName = "src/proofs/TTand.txt";
                    value = true;
                }
                if (binOp instanceof Disjunction) {
                    fileName = "src/proofs/TTor.txt";
                    value = true;
                }
                if (binOp instanceof Implication) {
                    fileName = "src/proofs/TTfoll.txt";
                    value = true;
                }
            }
            res.addAll(getProofFromFile(fileName, binOp.getFirst().toString(), binOp.getSecond().toString()));
            binOp.setValue(value);
        } else if (expr instanceof Negation) {
            Negation neg = (Negation) expr;
            res = proofRec(neg.getFirst());
            String fileName = "";
            boolean value;
            if (!neg.getFirst().getValue()) {
                fileName = "";
                value = true;
            } else {
                fileName = "src/proofs/Tnot.txt";
                value = false;
            }
            res.addAll(getProofFromFile(fileName, neg.getFirst().toString(), ""));
            neg.setValue(value);
        } else {
            Variable var = (Variable) expr;
            res = new ArrayList<>();
            boolean value;
            Expression e;
            if (variablesNameMap.get(var.getName())) {
                e = var;
                value = true;
            } else {
                e = new Negation(var);
                value = false;
            }
            res.add(e);
            var.setValue(value);
        }
        return res;
    }

    private void proofForAllValues() {
        variablesList.addAll(variablesSet);
        for (int i = 0; i < 1 << variablesSet.size(); i++) {
            int j = 0;
            Set<Expression> set = new HashSet<>();
            variablesNameMap.clear();
            for (Variable variable : variablesSet) {
                Expression expr = new Variable(variable.getName());
                if ((i & (1 << j)) == (1 << j)) {
                    variablesNameMap.put(variable.getName(), true);
                } else {
                    variablesNameMap.put(variable.getName(), false);
                    expr = new Negation(expr);
                }
                j++;
                set.add(expr);
            }
            List<Expression> curProof = proofRec(mainProvable);
            if (!mainProvable.getValue()) {
                allIsBad = true;
                badSet = set;
                return;
            }
            proofs.put(set, curProof);
        }
    }

    private List<Expression> doDeduction(List<Expression> expressions, Expression r, Set<Expression> assumptionsSet) {
        Map<Expression, Integer> expressionNumber = new HashMap<>();
        for (int i = 0; i < expressions.size(); i++) {
            Expression expression = expressions.get(i);
            if (expressionNumber.containsKey(expression)) {
                expressionNumber.put(expression, Math.min(i + 1, expressionNumber.get(expression)));
            } else {
                expressionNumber.put(expression, i + 1);
            }
        }
        HashMap<Expression, Expression> modusPonens = new HashMap<>();
        List<Expression> res = new ArrayList<>();
        for (int i = 0; i < expressions.size(); i++) {
            Expression expr = expressions.get(i);
            boolean f = false;
            if (expr instanceof Implication) {
                Implication implication = (Implication) expr;
                if (AxiomsUtilities.checkAxioms(implication) != null) {
                    f = true;
                }
                if (expressionNumber.containsKey(implication.getFirst())
                        && expressionNumber.containsKey(implication.getSecond())) {
                    int a = expressionNumber.get(implication.getFirst());
                    int b = expressionNumber.get(implication.getSecond());
                    if (b > a && b > (i + 1)) {
                        modusPonens.put(implication.getSecond(), implication.getFirst());
                    }
                }
            }
            if (assumptionsSet.contains(expr) || f) {
                res.add(AxiomsUtilities.makeAxiom1(expr, r));
                res.add(expr);
                res.add(new Implication(r, expr));
            } else if (expr.equals(r)) {
                Implication a = (Implication) AxiomsUtilities.makeAxiom2(r, new Implication(r, r), r);
                res.add(AxiomsUtilities.makeAxiom1(r, r));
                res.add(AxiomsUtilities.makeAxiom1(r, new Implication(r, r)));
                res.add(a);
                res.add(a.getSecond());
                res.add(new Implication(r, r));
            } else {
                Expression j = modusPonens.get(expr);
                Implication a = (Implication) AxiomsUtilities.makeAxiom2(r, j, expr);
                res.add(a);
                res.add(a.getSecond());
                res.add(new Implication(r, expr));
            }
        }
        return res;
    }

    private class Node{
        Node first, second;
        String name;
        boolean value;
        int depth;

        Node(String name, boolean value, int depth){
            this.name = name;
            this.value = value;
            this.depth = depth;
        }
    }

    private Node root;

    private void buildTree(Node cur, int depth) {
        if (depth + 1 > variablesList.size()) {
            return;
        }
        cur.first = new Node(variablesList.get(depth).toString(), false, depth);
        cur.second = new Node(variablesList.get(depth).toString(), true, depth);
        buildTree(cur.first, depth + 1);
        buildTree(cur.second, depth + 1);
    }

    private List<Expression> merge(Expression r, List<Expression> first, List<Expression> second, Set<Expression> assumptionsSet) {
        List<Expression> res;
        Expression notR = new Negation(r);
        res = doDeduction(first, notR, assumptionsSet);
        //res.add(new Variable("\nFirst Deduction End\n"));
        res.addAll(doDeduction(second, r, assumptionsSet));
        //res.add(new Variable("\nSecond Deduction End\n"));
        res.add(AxiomsUtilities.makeAxiom6(r, notR));
        Disjunction disjunction = new Disjunction(r, notR);
        res.addAll(getProofFromFile("src/proofs/Contrpos.txt", r.toString(), disjunction.toString()));
        res.add(new Implication(new Negation(disjunction), notR));
        //res.add(new Variable("\nPunkt 1 End\n"));
        res.add(AxiomsUtilities.makeAxiom7(r, notR));
        res.addAll(getProofFromFile("src/proofs/Contrpos.txt", notR.toString(), disjunction.toString()));
        res.add(new Implication(new Negation(disjunction), new Negation(notR)));
        //res.add(new Variable("\nPunkt 2 End\n"));
        res.addAll(getProofFromFile("src/proofs/Ror!R.txt", r.toString(), ""));
        //res.add(new Variable("\nR and not R End\n"));
        res.addAll(getProofFromFile("src/proofs/EndExclus.txt", r.toString(), mainProvable.toString()));
        //res.add(new Variable("\nExclusion End\n"));
        return res;
    }

    private List<Expression> mergeRec(Node cur, Set<Expression> set) {
        List<Expression> first, second;
        Set<Expression> set1 = new HashSet<>(set);
        Set<Expression> set2 = new HashSet<>(set);
        Set<Expression> set3 = new HashSet<>(set);
        if (cur.depth != -1) {
            Expression v;
            if (cur.value) {
                v = new Variable(cur.name);
            } else {
                v = new Negation(new Variable(cur.name));
            }
            set1.add(v);
            set2.add(v);
            set3.add(v);
        }
        if (cur.depth == variablesList.size() - 1) {
            return proofs.get(set3);
        }
        first = mergeRec(cur.first, set1);
        second = mergeRec(cur.second, set2);
        return merge(new Variable(cur.first.name), first, second, set3);
    }

    private void output() {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            /*Set<Expression> set = new HashSet<>();
            set.add(new Negation(new Variable("A")));
            for (Expression e : proofs.get(set)) {
                bw.write(e.toString() + "\n");
            }
            bw.write("\n");
            set.clear();
            set.add(new Variable("A"));
            for (Expression e : proofs.get(set)) {
                bw.write(e.toString() + "\n");
            }*/
            if (!allIsBad) {
                bw.write(line.replace("=", "-") + "\n");
                int i = 0;
                for (Expression e : proof) {
                    try {
                        bw.write(e.toString() + "\n");
                        i++;
                    } catch (NullPointerException n) {
                        System.err.println(i);
                    }
                }
            } else {
                bw.write("Высказывание ложно при ");
                int i = 0;
                //boolean flag;
                for (Expression expr : badSet) {
                    //flag = true;
                    if (expr instanceof Variable) {
                        //if (!mainAssumptionsSet.contains(expr)) {
                            bw.write(((Variable) expr).getName() + "=И");
                        //} else {
                          //  flag = false;
                        //}
                    } else if (expr instanceof Negation){
                        if (((Negation) expr).getFirst() instanceof Variable) {
                            //if (!mainAssumptionsSet.contains(((Negation) expr).getFirst())) {
                                bw.write(((Variable) ((Negation) expr).getFirst()).getName() + "=Л");
                            //} else {
                              //  flag = false;
                            //}
                        }
                    }
                    if (i < badSet.size() - 1) {// && flag) {
                        bw.write(", ");
                    }
                    i++;
                }
            }
        } catch (IOException ignored) {}
    }

    private void doDeductionLeftward() {
        Expression cur = mainProvable;
        for (int i = 0; i < mainAssumptionsList.size(); i++) {
            proof.add(mainAssumptionsList.get(mainAssumptionsList.size() - 1 - i));
            if (cur instanceof Implication) {
                proof.add(((Implication) cur).getSecond());
                cur = ((Implication) cur).getSecond();
            }
        }
    }

    private void run() {
        input();
        split();
        getVariables(mainProvable);
        proofForAllValues();
        if (!allIsBad) {
            root = new Node("", false, -1);
            buildTree(root, 0);
            proof = mergeRec(root, new HashSet<>());
            if (!mainAssumptionsList.isEmpty()) {
                doDeductionLeftward();
            }
        }
        output();
        new Checker().check();
    }

    public static void main(String[] args) {
        new ProofMachine().run();
    }
}
