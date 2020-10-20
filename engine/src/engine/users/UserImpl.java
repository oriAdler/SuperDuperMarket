package engine.users;

import DTO.UserDTO;
import engine.accounts.Account;

import java.util.List;

public class UserImpl implements User {
    final private int id;
    final private String name;
    final private String type;
    final private Account account;

    public UserImpl(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        account = new Account();
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

    @Override
    public List<Account.Transaction> getTransactionList() {
        return this.account.getTransactionList();
    }

    @Override
    public UserDTO userToUserDTO() {
        return new UserDTO(this.id, this.name, this.type);
    }

    public Account getAccount() {
        return account;
    }
}
