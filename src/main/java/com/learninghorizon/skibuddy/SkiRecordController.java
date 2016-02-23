package com.learninghorizon.skibuddy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learninghorizon.skibuddy.model.SkiEvent;
import com.learninghorizon.skibuddy.model.SkiSession;
import com.learninghorizon.skibuddy.model.TravelPoint;
import com.learninghorizon.skibuddy.model.User;
import com.learninghorizon.skibuddy.mongoconfig.MongoDBAdapter;
import com.learninghorizon.skibuddy.util.DataUtil;

@RestController
@RequestMapping(value="/learninghorizon/v1")
public class SkiRecordController {
	MongoDBAdapter mongoDBAdapter = new MongoDBAdapter(); 
	
	/*@RequestMapping(value="/createuser", method=RequestMethod.POST)
	public User createUser(@RequestParam("facebookId") String facebookId,
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("tagLine") String tagLine,
			Model model){
		
			User user = new User();
			user.setFacebookId(facebookId);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setTagLine(tagLine);
			User responseUser = DataUtil.addNewUser(user);
			return responseUser;
	}*/
	
	@RequestMapping(value="/createuser", method=RequestMethod.POST)
	public User createUser(@RequestBody User user,
			Model model) throws IOException{
			System.out.println("facebook id :"+user.getFacebookId());
			/*User user = new User();
			user.setFacebookId(facebookId);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setTagLine(tagLine);*/
			User responseUser = DataUtil.addNewUser(user);
			mongoDBAdapter.addUser(user.getFacebookId(), responseUser);
			return responseUser;
	}
	@RequestMapping(value="/getuser/{facebookId}")
	public User getUser(@PathVariable("facebookId") String facebookId){
		return mongoDBAdapter.getUser(facebookId);
	}
	
	@RequestMapping(value="/savetagline/{userid}")
	public User saveTagLine(@PathVariable("userid") String userId,
			@RequestParam String tagLine) throws IOException{
		mongoDBAdapter.updateTagLine(userId, tagLine);
		User user = DataUtil.getUser(userId);
		return user;
	}
	
	@RequestMapping(value="/createevntt/{userid}", method=RequestMethod.POST)
	public SkiEvent createSkiEvent(@PathVariable("userid") String userId, 
			@RequestParam String eventName,
			@RequestParam String eventDescription,
			@RequestParam String locationName,
			@RequestParam String latitude,
			@RequestParam String longitude,
			@RequestParam String eventInvites
			){
		
		SkiEvent skiEvent = new SkiEvent();
		skiEvent.setEventName(eventName);
		skiEvent.setEventDescription(eventDescription);
		skiEvent.setLocationName(locationName);
		skiEvent.setLatitude(latitude);
		skiEvent.setLongitude(longitude);
		String[] allEventInvites = eventInvites.split(",");
		for(String id:allEventInvites){
			User user = DataUtil.getUser(id);
			if(null!=user){
				//skiEvent.setEventInvite(user);
			}
		}
		User user = DataUtil.getUser(userId);
		user.setCreatedEvent(skiEvent);
		return skiEvent;
	}
	
	@RequestMapping(value="/createsession/{userid}", method=RequestMethod.POST)
	public SkiSession createSession(@PathVariable("userid") String userId,
			@RequestParam String sessionName,
			@RequestParam String maxSpeed,
			@RequestParam String averageSpeed,
			@RequestParam String altitude,
			@RequestParam String distance,
			@RequestParam String slope,
			@RequestParam String vertical,
			@RequestParam long date,
			@RequestParam String duration,
			@RequestParam String travelPoints){
		SkiSession skiSession = new SkiSession();
		skiSession.setSessionName(sessionName);
		skiSession.setMaxSpeed(maxSpeed);
		skiSession.setAverageSpeed(averageSpeed);
		skiSession.setAltitude(altitude);
		skiSession.setDistance(distance);
		skiSession.setSlope(slope);
		skiSession.setVertical(vertical);
		skiSession.setDate(date);
		skiSession.setDuration(duration);
		String[] travelPointsArray = travelPoints.split(",");
		for(String tPoint : travelPointsArray){
			String latitude = tPoint.split("|")[0];
			String longitude = tPoint.split("|")[1];
			TravelPoint travelPoint = new TravelPoint();
			if(null != travelPoint){
				travelPoint.setLatitude(latitude);
				travelPoint.setLongitude(longitude);
				skiSession.setTravelPoint(travelPoint);
			}
		}
		User user = DataUtil.getUser(userId);
		user.setSkiSession(skiSession);
		return skiSession;
	}
	
	@RequestMapping(value="/createsession/{userid}", method=RequestMethod.PUT)
	public SkiSession saveSession(@PathVariable("userid") String userId,
			@RequestBody SkiSession skiSession) throws IOException{
		User user = mongoDBAdapter.getUser(userId);
		user.setSkiSession(skiSession);
		mongoDBAdapter.saveSession(userId, skiSession);
		System.out.println(userId);
		return skiSession;
	}
	
	@RequestMapping(value="/createevent/{userid}", method=RequestMethod.POST)
	public SkiEvent createEvent(@PathVariable("userid") String userId,
			@RequestBody SkiEvent skiEvent, Model model) throws IOException {
		try {
		//User user = mongoDBAdapter.getUser(userId);
		//user.setCreatedEvent(skiEvent);
		mongoDBAdapter.createdEvent(skiEvent, userId);
		
		mongoDBAdapter.createEvent(skiEvent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String eventUser : skiEvent.getEventInvites()){
			//User userInvite = mongoDBAdapter.getUser(eventUser);
			//userInvite.setEventInvite(skiEvent);
			if(!eventUser.equalsIgnoreCase(userId)){
				mongoDBAdapter.createEventInvite(eventUser, skiEvent);
			}
		}
		return skiEvent;
	}
	

	@RequestMapping(value="/listusers/{userid}", method=RequestMethod.GET)
	public List<User> getAllUsers(@PathVariable("userid") String userId) throws IOException{
		return mongoDBAdapter.getAllUsers(userId);
	}
	
	@RequestMapping(value="loadusers/{userid}", method=RequestMethod.POST)
	public List<User> loadUsers(@PathVariable("userid") String userId,
			@RequestBody List<String> userIds){
		List<User> users = new ArrayList<User>();
		//users.add(mongoDBAdapter.getUser(userId));
		for(String individualId : userIds){
			if(userId != individualId){
			users.add(mongoDBAdapter.getUser(individualId));
			}
		}
		return users;
	}
	
	@RequestMapping(value = "/sharelocation/{userid}", method=RequestMethod.GET)
	public void saveLocationPreferences(@PathVariable("userid") String userId,
			Model model) throws IOException{
		User user = mongoDBAdapter.getUser(userId);
		if(user.isShareLocation()){
			user.setShareLocation(false);
			mongoDBAdapter.updateShareLocation(userId, false);
		}else{
			user.setShareLocation(true);
			mongoDBAdapter.updateShareLocation(userId, true);
		}
		
	}
	@RequestMapping(value = "/savelocation/{userid}/{state}/{city}/{latitude}/{longitude:.+}", method=RequestMethod.GET)
	public void saveLocation(@PathVariable("userid") String userId,
			@PathVariable("state") String state,
			@PathVariable("city") String city,
			@PathVariable("latitude") String latitude,
			@PathVariable("longitude") String longitude,
			Model model) throws IOException{
		User user = mongoDBAdapter.getUser(userId);
		user.setLastKnownCity(city);
		user.setLastKnownState(state);
		user.setLastKnownLatitude(latitude);
		user.setLastKnownLongitude(longitude);
		mongoDBAdapter.updateUser(user);
	}
	
	@RequestMapping(value="/getskievents/{userid}", method=RequestMethod.GET)
	public List<SkiEvent> getSkiEvents(@PathVariable("userid") String userId){
		return DataUtil.getAllSkiEvents();
	}
	
	
	@RequestMapping(value="/getskisessions/{userid}", method=RequestMethod.GET)
	public List<SkiSession> getSkiSessions(@PathVariable("userid") String userId ){
		return DataUtil.getUser(userId).getSkiSessions();
	}
	
}
