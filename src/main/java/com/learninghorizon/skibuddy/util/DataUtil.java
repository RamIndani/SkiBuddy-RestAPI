package com.learninghorizon.skibuddy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.learninghorizon.skibuddy.model.SkiEvent;
import com.learninghorizon.skibuddy.model.User;

public class DataUtil {

	
	public final static HashMap<String, User> users = new HashMap<String, User>();
	public final static HashMap<String, SkiEvent> skiEvents = new HashMap<String, SkiEvent>();
	
	public static User addNewUser(User user){
		if(null == users.get(user.getFacebookId())){
			users.put(user.getFacebookId(), user); 
		}
		return users.get(user.getFacebookId());
	}
	
	public static User getUser(String id){
		return users.get(id);
	}
	
	public static void addSkiEvent(SkiEvent skiEvent){
		if(skiEvents.containsKey(skiEvent.getId())){
			return;
		}
		skiEvents.put(skiEvent.getId(), skiEvent);
	}
	
	public static SkiEvent getSkiEvent(String eventId){
		return skiEvents.get(eventId);
	}
	
	public static List<SkiEvent> getAllSkiEvents(){
		List<SkiEvent> allEvents = new ArrayList<SkiEvent>();
		for(SkiEvent skiEvent : skiEvents.values()){
			allEvents.add(skiEvent);
		}
		return allEvents;
	}

	public static List<User> getAllUsers() {
		List<User> allUsers = new ArrayList<User>();
		for(User skiUser : users.values()){
			allUsers.add(skiUser);
		}
		return allUsers;
	}
}
