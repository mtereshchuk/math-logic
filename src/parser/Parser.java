package parser;

import expression.Expression;

public interface Parser {
    Expression parse(String s);
}
