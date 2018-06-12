package expression;

public class Disjunction extends BinaryOperation {

    public Disjunction(Expression first, Expression second) {
        super(first, second);
        this.symbol = "|";
    }
}
