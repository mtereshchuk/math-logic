/*
package expression;

import java.security.spec.ECField;
import java.util.List;
import java.util.Objects;

public class WithAssumptions implements Expression {

    public Expression first;
    public List<Expression> assumptions;
    private String symbol;

    public WithAssumptions(Expression first, List<Expression> assumptions) {
        this.first = first;
        this.assumptions = assumptions;
        this.symbol = "|-";
    }

    @Override
    public String toString() {
        return symbol + first.toString();
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof WithAssumptions) {
            WithAssumptions withAssumptions = (WithAssumptions) expression;
            if (first.equals(withAssumptions.first)) {
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
*/
