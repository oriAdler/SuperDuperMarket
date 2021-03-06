package engine.users;

import DTO.UserDTO;
import engine.accounts.Account;

import java.util.List;

public interface User {
    int getId();
    String getName();
    String getType();
    Account getAccount();

    List<Account.Transaction> getTransactionList();

    UserDTO userToUserDTO();
}
