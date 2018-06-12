package expression;

public class Conjunction extends BinaryOperation {

    public Conjunction(Expression first, Expression second) {
        super(first, second);
        this.symbol = "&";
    }
}
