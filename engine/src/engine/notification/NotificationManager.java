package engine.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationManager {

    private final Map<String, List<String>> userNameToNotificationsList;

    public NotificationManager() {
        this.userNameToNotificationsList = new HashMap<>();
    }

    public synchronized void addNotification(String userName, String notification){
        if(userNameToNotificationsList.containsKey(userName)){
            userNameToNotificationsList.get(userName).add(notification);
        }
        else{   //user doesn't have notifications list, make a new one and add to Map
            List<String> notificationList = new ArrayList<>();
            notificationList.add(notification);
            userNameToNotificationsList.put(userName, notificationList);
        }
    }

    public synchronized void addNotificationList(String userName, List<String> notificationList){
        if(userNameToNotificationsList.containsKey(userName)){
            userNameToNotificationsList.get(userName).addAll(notificationList);
        }
        else{   //user doesn't have notifications list, use the list from parameter and add to map
            userNameToNotificationsList.put(userName, notificationList);
        }
    }

    public synchronized List<String> getUserNotifications(String userName, int fromIndex){
        if(userNameToNotificationsList.containsKey(userName)){
            List<String> userNotificationsList = userNameToNotificationsList.get(userName);
            if(fromIndex < 0 || fromIndex > userNotificationsList.size()){
                fromIndex = 0;
            }
            return userNotificationsList.subList(fromIndex, userNotificationsList.size());
        }
        else{   //if user has no notifications returns an empty list
            return new ArrayList<>();
        }
    }

    public int getVersion(String userName){
        if(userNameToNotificationsList.containsKey(userName)){
            return userNameToNotificationsList.get(userName).size();
        }
        else{   //if user has no notifications returns 0
            return 0;
        }
    }
}
