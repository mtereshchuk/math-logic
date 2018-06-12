package expression;

import java.util.Objects;

public class Negation implements Expression {

    private Expression first;
    private boolean value;
    private String symbol;

    public Negation(Expression first) {
        this.first = first;
        this.symbol = "!";
        this.value = false;
    }

    public Expression getFirst() {
        return first;
    }

    @Override
    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return symbol + first.toString();
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof Negation) {
            Negation negation = (Negation) expression;
            if (first.equals(negation.first)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, first);
    }
}
