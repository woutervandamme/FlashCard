package wouter.vandamme.robbe.roels.flashcard.domain;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import db.DBException;
import db.Database;
import domain.DomainException;
import domain.Group;
import domain.Message;
import domain.MessageType;
import domain.Question;
import domain.QuestionImage;
import domain.QuestionText;
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
    public Question getRandomQuestion(int id) throws DBException {
        String sql = "select * from QuestionsInGroup where gropuID = ?";
        String params = "line="+sql+"&vals="+id+"&types="+"i";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        Question q = null;
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            JSONArray json;
            Log.v("DATA", "String is: '" + dbTask.getJsonString() + "'");
            try {
                json = new JSONArray(dbTask.getJsonString());
                Random r = new Random();
                int rowNumber = (int) (json.length() * r.nextFloat());

                JSONObject row = json.getJSONObject(rowNumber);
                int questionID = row.getInt("QuestionID");
                q = this.getQuestion(questionID);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.v("DATA", "NOT FETCHED" );
        }
        return q;
    }

    @Override
    public Message getMessage(int id) throws DBException {
        String sql = "select * from Message where ID = ?";
        String params = "line="+sql+"&vals="+id+"&types="+"i";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        Message m = null;
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            JSONArray json;
            Log.v("DATA", "String is: '" + dbTask.getJsonString() + "'");
            try {
                json = new JSONArray(dbTask.getJsonString());
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);
                    User u = getUser(row.getString("UserID"));
                    m = new Message(id,row.getString("title"),row.getString("body"), MessageType.toMessageType(row.getString("type")),u);
                 }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (DomainException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.v("DATA", "NOT FETCHED" );
        }
        return m;

    }

    @Override
    public ArrayList<User> getUsersFromGroup(int id) throws DBException {
        String sql = "select * from UsersInGroup where groupID = ?";
        String params = "line="+sql+"&vals="+id+"&types="+"i";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        ArrayList<User> users = new ArrayList<User>();
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            JSONArray json;
            try {
                json = new JSONArray(dbTask.getJsonString());
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);
                    User u = getUser(row.getString("UserID"));
                    users.add(u);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Log.v("DATA", "NOT FETCHED" );
        }
        return users;
    }

    @Override
    public ArrayList<Message> getMessages(String email) throws DBException {
        String sql = "select * from Message where UserID = ? ";
        String params = "line="+sql+"&vals="+email+"&types="+"s";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        ArrayList<Message> messages = new ArrayList<Message>();
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            JSONArray json;
            Log.v("DATA", "String is: '" + dbTask.getJsonString() + "'");
            try {
                json = new JSONArray(dbTask.getJsonString());
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);
                    User u = getUser(row.getString("UserID"));
                    Message m = new Message(row.getInt("ID"),row.getString("title"),row.getString("body"), MessageType.toMessageType(row.getString("type")),u);
                    messages.add(m);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (DomainException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.v("DATA", "NOT FETCHED" );
        }
        return messages;
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
