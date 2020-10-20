package engine.users;

import DTO.TransactionDTO;
import DTO.UserDTO;
import engine.accounts.Account;

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

    public synchronized Map<String, User> getUsers() {
        return Collections.unmodifiableMap(usersMap);
    }

    public boolean isUserExists(String username) {
        return usersMap.containsKey(username);
    }

    //TODO: can it be a problem to get the user to the outside ? just server side
    public synchronized User getUser(String userName){
        return usersMap.get(userName);
    }
//
//    public synchronized UserDTO getUserInfo(String userName){
//        if(usersMap.containsKey(userName)){
//            User user = usersMap.get(userName);
//            return new UserDTO(user.getId(), user.getName(), user.getType());
//        }
//        else{
//            return null;
//        }
//    }
//
//    public synchronized List<TransactionDTO> getUserTransactions(String userName){
//        if(usersMap.containsKey(userName)){
//            return usersMap.get(userName).getAccount().getTransactionDTOList();
//        }
//        else{
//            return null;
//        }
//    }
//
//    public synchronized Account getUserAccount(String userName){
//        if(usersMap.containsKey(userName)){
//            return usersMap.get(userName).getAccount();
//        }
//        else{
//            return null;
//        }
//    }
}
