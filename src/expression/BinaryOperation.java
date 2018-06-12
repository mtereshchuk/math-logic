package expression;

import java.util.Objects;

public abstract class BinaryOperation implements Expression {

    private Expression first, second;
    private boolean value;
    String symbol;

    BinaryOperation(Expression first, Expression second) {
        this.first = first;
        this.second = second;
        this.value = true;
    }

    public Expression getFirst() {
        return first;
    }

    public Expression getSecond() {
        return second;
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
        return "(" + first.toString() + symbol + second.toString() + ")";
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof BinaryOperation) {
            BinaryOperation binaryOperation = (BinaryOperation) expression;
            if (symbol.equals(binaryOperation.symbol)
                    && first.equals(binaryOperation.first)
                    && second.equals(binaryOperation.second)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, first, second);
    }
}
