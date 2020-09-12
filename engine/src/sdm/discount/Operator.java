package sdm.discount;

//TODO: enum name is ONE_OF but value in XML is ONE-OF, can't use Operator.valueOf()..
// Is there another solution ? or should i use string in discount?
// What is the advantage of enum? there is going to be anyway a cast to string for UI.
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
