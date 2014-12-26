package wouter.vandamme.robbe.roels.flashcard.domain;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import db.DBException;
import db.Database;
import domain.Group;
import domain.Message;
import domain.Question;
import domain.User;

/**
 * Created by Robbe on 24/12/2014.
 */
public class JSONWebDB implements Database {
    private String url = "http://twittervoetbal.be/dbFlashcards.php";

    @Override
    public User getUser(String email) throws DBException {
        User user = null;
        String sql = "select * from User where email = ?";
        String params = "line="+sql+"&vals="+email+"&types="+"s";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            JSONArray json;
            Log.v("DATA", "String is: '" + dbTask.getJsonString() + "'");
            try {
               json = new JSONArray(dbTask.getJsonString());
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);
                    String mail = "" + row.getString("email");
                    String name = "" + row.getString("name");
                    String password = "" + row.getString("password");
                    user = new User(email,name,password);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.v("DATA", "NOT FETCHED" );
        }
        return user;
    }

    @Override
    public Question getRandomQuestion(int i) throws DBException {
        return null;
    }

    @Override
    public Message getMessage(int i) throws DBException {
        return null;
    }

    @Override
    public ArrayList<User> getUsersFromGroup(int i) throws DBException {
        return null;
    }

    @Override
    public ArrayList<Message> getMessages(String s) throws DBException {
        return null;
    }

    @Override
    public ArrayList<Group> getGroupsForUser(String s) throws DBException {
        return null;
    }

    @Override
    public User getGroupAdmin(int i) throws DBException {
        return null;
    }

    @Override
    public Group getGroup(int i) throws DBException {
        return null;
    }

    @Override
    public Question getQuestion(int i) throws DBException {
        return null;
    }

    @Override
    public void updateQuestion(Question question) throws DBException {

    }

    @Override
    public void updateUser(User user) throws DBException {

    }

    @Override
    public void updateGroupName(int i, String s) throws DBException {

    }

    @Override
    public void removeUserFromGroup(int i, String s) throws DBException {

    }

    @Override
    public void addUserToGroup(int i, String s) throws DBException {

    }

    @Override
    public void addUser(User user) throws DBException {

    }

    @Override
    public void addQuestion(Question question) throws DBException {

    }

    @Override
    public void addGroup(Group group) throws DBException {

    }

    @Override
    public void sendMessage(Message message) throws DBException {

    }

}
