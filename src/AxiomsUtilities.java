import expression.*;

public class AxiomsUtilities {

    public static boolean axiom1(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (b instanceof Implication) {
            Expression c = ((Implication) b).getFirst();
            Expression d = ((Implication) b).getSecond();
            if (a.equals(d)) {
                return true;
            }
        }
        return false;
    }

    public static boolean axiom2(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof Implication && b instanceof Implication) {
            Expression c = ((Implication) a).getFirst();
            Expression d = ((Implication) a).getSecond();
            Expression e = ((Implication) b).getFirst();
            Expression f = ((Implication) b).getSecond();
            if (e instanceof Implication && f instanceof Implication) {
                Expression g = ((Implication) e).getFirst();
                Expression h = ((Implication) e).getSecond();
                Expression i = ((Implication) f).getFirst();
                Expression j = ((Implication) f).getSecond();
                if (h instanceof Implication) {
                    Expression k = ((Implication) h).getFirst();
                    Expression l = ((Implication) h).getSecond();
                    if (c.equals(g) && c.equals(i) && d.equals(k) && l.equals(j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean axiom3(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (b instanceof Implication) {
            Expression c = ((Implication) b).getFirst();
            Expression d = ((Implication) b).getSecond();
            if (d instanceof Conjunction) {
                Expression e = ((Conjunction) d).getFirst();
                Expression f = ((Conjunction) d).getSecond();
                if (a.equals(e) && c.equals(f)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean axiom4(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof Conjunction) {
            Expression c = ((Conjunction) a).getFirst();
            Expression d = ((Conjunction) a).getSecond();
            if (b.equals(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean axiom5(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof Conjunction) {
            Expression c = ((Conjunction) a).getFirst();
            Expression d = ((Conjunction) a).getSecond();
            if (b.equals(d)) {
                return true;
            }
        }
        return false;
    }

    public static boolean axiom6(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (b instanceof Disjunction) {
            Expression c = ((Disjunction) b).getFirst();
            Expression d = ((Disjunction) b).getSecond();
            if (a.equals(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean axiom7(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (b instanceof Disjunction) {
            Expression c = ((Disjunction) b).getFirst();
            Expression d = ((Disjunction) b).getSecond();
            if (a.equals(d)) {
                return true;
            }
        }
        return false;
    }

    public static boolean axiom8(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof  Implication && b instanceof Implication) {
            Expression c = ((Implication) a).getFirst();
            Expression d = ((Implication) a).getSecond();
            Expression e = ((Implication) b).getFirst();
            Expression f = ((Implication) b).getSecond();
            if (e instanceof Implication && f instanceof Implication) {
                Expression g = ((Implication) e).getFirst();
                Expression h = ((Implication) e).getSecond();
                Expression i = ((Implication) f).getFirst();
                Expression j = ((Implication) f).getSecond();
                if (i instanceof Disjunction) {
                    Expression k = ((Disjunction) i).getFirst();
                    Expression l = ((Disjunction) i).getSecond();
                    if (c.equals(k) && d.equals(h) && d.equals(j) && g.equals(l)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean axiom9(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof Implication && b instanceof Implication) {
            Expression c = ((Implication) a).getFirst();
            Expression d = ((Implication) a).getSecond();
            Expression e = ((Implication) b).getFirst();
            Expression f = ((Implication) b).getSecond();
            if (e instanceof Implication && f instanceof Negation) {
                Expression g = ((Implication) e).getFirst();
                Expression h = ((Implication) e).getSecond();
                Expression i = ((Negation) f).getFirst();
                if (h instanceof Negation) {
                    Expression j = ((Negation) h).getFirst();
                    if (c.equals(g) && c.equals(i) && d.equals(j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean axiom10(Implication implication) {
        Expression a = implication.getFirst();
        Expression b = implication.getSecond();
        if (a instanceof Negation) {
            Expression c = ((Negation) a).getFirst();
            if (c instanceof Negation) {
                Expression d = ((Negation) c).getFirst();
                if (b.equals(d)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String checkAxioms(Implication implication) {
        if (axiom1(implication)) return "1";
        if (axiom2(implication)) return "2";
        if (axiom3(implication)) return "3";
        if (axiom4(implication)) return "4";
        if (axiom5(implication)) return "5";
        if (axiom6(implication)) return "6";
        if (axiom7(implication)) return "7";
        if (axiom8(implication)) return "8";
        if (axiom9(implication)) return "9";
        if (axiom10(implication)) return "10";
        return null;
    }

    public static Expression makeAxiom1(Expression a, Expression b) {
        return new Implication(a, new Implication(b, a));
    }

    public static Expression makeAxiom2(Expression a, Expression b, Expression c) {
        return new Implication(
                new Implication(a, b),
                new Implication(
                        new Implication(a, new Implication(b, c)),
                        new Implication(a, c)
                )
        );
    }

    public static Expression makeAxiom6(Expression a, Expression b) {
        return new Implication(a, new Disjunction(a, b));
    }

    public static Expression makeAxiom7(Expression a, Expression b) {
        return new Implication(b, new Disjunction(a, b));
    }
}
