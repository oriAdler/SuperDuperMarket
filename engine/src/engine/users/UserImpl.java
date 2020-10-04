package engine.users;

public class UserImpl implements User {
    final private int id;
    final private String name;
    final private String type;

    public UserImpl(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
