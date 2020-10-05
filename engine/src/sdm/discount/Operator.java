package sdm.discount;

public enum Operator {
    IRRELEVANT("IRRELEVANT"), ONE_OF("ONE-OF"), ALL_OR_NOTHING("ALL-OR-NOTHING");

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
