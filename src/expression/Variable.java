package expression;

import java.util.Objects;

public class Variable implements Expression {

    private boolean value;
    private String name;

    public Variable(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public Variable(String name) {
        this.name = name;
        this.value = true;
    }

    public String getName() {
        return name;
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
        return name;
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof Variable) {
            Variable variable = (Variable) expression;
            if (name.equals(variable.name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
