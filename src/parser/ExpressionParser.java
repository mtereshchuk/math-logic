package parser;

import expression.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpressionParser implements Parser {

    private String currentExpression;
    private int currentIndex;

    @Override
    public Expression parse(String s) {
        currentExpression = s;
        currentIndex = 0;
        return implication();
    }

    private Expression implication() {
        Expression current = disjunction();
        Expression result = current;
        while (currentIndex < currentExpression.length()) {
            char operation = currentExpression.charAt(currentIndex);
            if (operation != '-') {
                break;
            }
            currentIndex += 2;
            current = implication();
            result = new Implication(result, current);
        }
        return result;
    }

    private Expression disjunction() {
        Expression current = conjunction();
        Expression result = current;
        while (currentIndex < currentExpression.length()) {
            char operation = currentExpression.charAt(currentIndex);
            if (operation != '|') {
                break;
            }
            currentIndex++;
            current = conjunction();
            result = new Disjunction(result, current);
        }
        return result;
    }

    private Expression conjunction() {
        Expression current = negation();
        Expression result = current;
        while (currentIndex < currentExpression.length()) {
            char operation = currentExpression.charAt(currentIndex);
            if (operation != '&') {
                break;
            }
            currentIndex++;
            current = negation();
            result = new Conjunction(result, current);
        }
        return result;
    }

    private Expression negation() {
        if (currentExpression.charAt(currentIndex) == '!') {
            currentIndex++;
            return new Negation(negation());
        }
        return brackets();
    }

    private Expression brackets() {
        if (currentExpression.charAt(currentIndex) == '(') {
            currentIndex++;
            Expression result = implication();
            currentIndex++;
            return result;
        }
        return letter();
    }

    private Expression letter() {
        StringBuilder stringBuilder = new StringBuilder();
        while (currentIndex < currentExpression.length()
                && (Character.isLetter(currentExpression.charAt(currentIndex))
                || Character.isDigit(currentExpression.charAt(currentIndex)))) {
            stringBuilder.append(currentExpression.charAt(currentIndex));
            currentIndex++;
        }
        return new Variable(stringBuilder.toString());
    }

    public static void main(String[] args) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            List<String> lines = Files.lines(Paths.get("input.txt")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            if (!lines.isEmpty()) {
                bw.write(new ExpressionParser().parse(lines.get(0).replaceAll("\\s+", "")).toString());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
