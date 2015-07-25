package huybdse02612.fpt.edu.Entity;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

/**
 * Created by huy on 7/24/2015.
 */
public class ListUsers {
    private HashMap<String, User>dicPeople;

    public ListUsers() {
        dicPeople = new HashMap<String, User>();
    }

    public int getCount() {
        return dicPeople.size();
    }

    public void addUser(User user) {
        if (!dicPeople.containsKey(user.getmIpAddress())){
            dicPeople.put(user.getmIpAddress(),user);
        }
    }

    public ArrayList<String> getListUserName() {
        ArrayList<String> alUsers=new ArrayList<>();
        for (User user : dicPeople.values()) {
            alUsers.add(user.getmName());
        }
        return alUsers;
    }

    public User getUserByIndex(int index){
        return dicPeople.values().size() > index ? (User) dicPeople.values().toArray()[index] : null;
    }

    public User getUserByIP(String ip) {
        try {
            return dicPeople.get(ip);
        } catch (Exception e) {
            return null;
        }
    }

}
