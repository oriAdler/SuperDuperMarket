package engine.users;

import java.util.ArrayList;
import java.util.List;

public class Vendor extends UserImpl {
    private List<String> regionsList;

    public Vendor(int id, String name) {
        super(id, name, "Vendor");
        regionsList = new ArrayList<>();
    }
}
