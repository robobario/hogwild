package nz.hogwild.service;

import com.google.common.collect.Maps;

import java.util.Map;

public class SessionStore {
    private static final SessionStore SESSION_STORE = new SessionStore();
    Map<String, String> idToUser = Maps.newHashMap();

    public void addUser(String id, String email){
        idToUser.put(id, email);
    }

    public String get(String id){
        return idToUser.get(id);
    }

    public static SessionStore sessionStore(){
        return SESSION_STORE;
    }
}
