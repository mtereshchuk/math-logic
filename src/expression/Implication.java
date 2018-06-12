package expression;

public class Implication extends BinaryOperation {

    public Implication(Expression first, Expression second) {
        super(first, second);
        this.symbol = "->";
    }
}
