package engine.users;

import DTO.TransactionDTO;
import DTO.UserDTO;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    static private int id = 0;

    private final Map<String, User> usersMap;

    public UserManager() {
        usersMap = new HashMap<>();
    }

    public synchronized void addUser(String username, String type) {
        id++;
        if(type.equals("customer")){
            usersMap.put(username, new Customer(id, username));
        }
        else{   //type.equals("vendor")
            usersMap.put(username, new Vendor(id, username));
        }
    }

    public synchronized void removeUser(String username) {
        usersMap.remove(username);
    }

    public synchronized Map<String, User> getUsers() {
        return Collections.unmodifiableMap(usersMap);
    }

    public boolean isUserExists(String username) {
        return usersMap.containsKey(username);
    }

    public UserDTO getUserInfo(String userName){
        if(usersMap.containsKey(userName)){
            User user = usersMap.get(userName);
            return new UserDTO(user.getId(), user.getName(), user.getType());
        }
        else{
            return null;
        }
    }

    public List<TransactionDTO> getUserTransactions(String userName){
        if(usersMap.containsKey(userName)){
            return usersMap.get(userName).getAccount().getTransactionDTOList();
        }
        else{
            return null;
        }
    }
}
