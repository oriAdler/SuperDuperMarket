package sdm.discount;

public enum Operator {
    IRRELEVANT("Irrelevant"), ONE_OF("One-of"), ALL_OR_NOTHING("All-or-nothing");

    final private String name;

    Operator(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
