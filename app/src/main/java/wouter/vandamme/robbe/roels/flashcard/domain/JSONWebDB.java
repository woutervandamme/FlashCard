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
import domain.QuestionFactory;
import domain.User;

/**
 * Created by Robbe on 24/12/2014.
 */
public class JSONWebDB implements Database {
    private String url = "http://twittervoetbal.be/dbFlashcards.php";
    private String dbPrefix = "`c9flashcard`.";
    private String userTable = "`User`";
    private String groupTable = "`Group`";
    private String questionsInGroupTable = "`QuestionsInGroup`";
    private String messageTable = "`Message`";
    private String usersInGroupTable = "`UsersInGroup`";
    private String questionTable = "`Question`";




    @Override
    public User getUser(String email) throws DBException {
        User user = null;
        String sql = "SELECT * FROM "+dbPrefix + userTable +" WHERE email = ?";
        String params = "line="+sql+"&vals="+email+"&types="+"s";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            JSONArray json;
            try {
               json = new JSONArray(dbTask.getJsonString());
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);
                    String mail = "" + row.getString("email");
                    String name = "" + row.getString("name");
                    String password = "" + row.getString("password");
                    user = new User(mail,name,password);
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
        String sql = "SELECT * FROM +"+dbPrefix + questionsInGroupTable +"WHERE groupID = ?";
        String params = "line="+sql+"&vals="+id+"&types="+"i";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        Question q = null;
        while (!dbTask.done()) {
        }
        Log.v("DATA", "SQL is: '" + dbTask.getJsonString() + "'");
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
                q.setId(questionID);
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
        String sql = "SELECT * FROM " + dbPrefix + messageTable + " WHERE ID = ?";
        String params = "line="+sql+"&vals="+id+"&types="+"i";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        Message m = null;
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            JSONArray json;
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
        String sql = "SELECT * FROM "+ dbPrefix + usersInGroupTable + " WHERE groupID = ?";
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
        String sql = "SELECT * FROM "+ dbPrefix + messageTable + " WHERE UserID = ? ";
        String params = "line="+sql+"&vals="+email+"&types="+"s";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        ArrayList<Message> messages = new ArrayList<Message>();
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            JSONArray json;
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
    public ArrayList<Group> getGroupsForUser(String email) throws DBException {
        String sql = "SELECT * FROM " + dbPrefix + usersInGroupTable + " WHERE userID = ?";
        String params = "line="+sql+"&vals="+email+"&types="+"s";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        ArrayList<Group> groups = new ArrayList<Group>();
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            JSONArray json;
            try {
                json = new JSONArray(dbTask.getJsonString());
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);
                    int groupID = row.getInt("GroupID");
                    Group g = getGroup(groupID);
                    groups.add(g);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Log.v("DATA", "NOT FETCHED" );
        }
        return groups;
    }

    @Override
    public User getGroupAdmin(int id) throws DBException {
        Group g = getGroup(id);
        return g.getAdmin();
    }

    @Override
    public Group getGroup(int id) throws DBException {
        String sql = "SELECT * FROM "+ dbPrefix + groupTable + " WHERE id = ?";
        String params = "line="+sql+"&vals="+id+"&types="+"i";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        Group group = null;
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            JSONArray json;
            try {
                json = new JSONArray(dbTask.getJsonString());
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);
                    User u = getUser(row.getString("admin"));
                    group = new Group(u,row.getString("name"),row.getInt("userCanInvite")==1,row.getInt("userCanAddQuestions")==1);
                    group.setId(id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.v("DATA", "NOT FETCHED" );
        }
        return group;


    }

    @Override
    public Question getQuestion(int id) throws DBException {
        String sql = "SELECT * FROM "+ dbPrefix + questionTable +" WHERE id = ?";
        String params = "line="+sql+"&vals="+id+"&types="+"i";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        Question question = null;
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            JSONArray json;
            try {
                json = new JSONArray(dbTask.getJsonString());
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);
                    question = QuestionFactory.getQuestion(row.getString("answer"),row.getString("question"),row.getString("type"));
                    question.setExtraInfo(row.getString("extrainfo"));
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
        return question;
    }

    @Override
    public void updateQuestion(Question question) throws DBException {
        String values = null;
        String sql = "UPDATE "+ dbPrefix + questionTable +" SET answer = ?, extraInfo = ?, question = ?, type = ? WHERE id = ?";
        try {
            values = question.getAnswer() + "===" + question.getExtraInfo() + "===" + question.getQuestion() + "===" + QuestionFactory.toDatabaseString(question)+ "==="+question.getId();
        } catch (DomainException e) {
            throw new DBException(e);
        }

        String params = "line="+sql+"&vals="+values+"&types="+"ssssi";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
        }
        else{
            Log.v("DATA", "NOT INSERTED" );
        }
    }

    @Override
    public void updateUser(User user) throws DBException {
        String sql = "UPDATE "+ dbPrefix + userTable + " SET name = ?,pw = ? WHERE e-mail = ?";
        String values = user.getName()+"==="+user.getPw()+"==="+user.getEmail();
        String params = "line="+sql+"&vals="+values+"&types="+"sss";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
        }
        else{
            Log.v("DATA", "NOT INSERTED" );
        }
    }

    @Override
    public void updateGroup(int id, String name,boolean canInvite, boolean canAdd) throws DBException {
        String sql = "UPDATE "+ dbPrefix + groupTable +" SET name = ?,userCanInvite = ? , userCanAddQuestions = ?  WHERE ID = ?";
        int invite = (canInvite) ? 1:0;
        int add = (canAdd) ? 1:0;

        String values = name+"==="+invite+"==="+add+"==="+id;
        String params = "line="+sql+"&vals="+values+"&types="+"siii";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
        }
        else{
            Log.v("DATA", "NOT INSERTED" );
        }
    }

    @Override
    public void removeUserFromGroup(int id, String email) throws DBException {
        String sql = "DELETE FROM " + dbPrefix + usersInGroupTable + " WHERE GroupID = ? AND UserID = ?";
        String values = id+"==="+email;
        String params = "line="+sql+"&vals="+values+"&types="+"is";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
        }
        else{
            Log.v("DATA", "NOT INSERTED" );
        }
    }

    @Override
    public void addUserToGroup(int id, String email) throws DBException {
        String sql = "INSERT INTO "+ dbPrefix + usersInGroupTable + " (GroupID,UserID) VALUES(?,?)";
        String values = id+"==="+email;
        String params = "line="+sql+"&vals="+values+"&types="+"is";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
        }
        else{
            Log.v("DATA", "NOT INSERTED" );
        }


    }

    @Override
    public void addUser(User user) throws DBException {
        String sql = "INSERT INTO " + dbPrefix + userTable + "(email,name,pw) VALUES(?,?,?)";
        String values = user.getEmail()+"==="+user.getName()+"==="+user.getPw();

        String params = "line="+sql+"&vals="+values+"&types="+"sss";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
        }
        else{
            Log.v("DATA", "NOT INSERTED" );
        }
    }

    @Override
    public void addQuestion(Question question, int groupID) throws DBException {
        String values = null;
        String sql = "INSERT INTO "+ dbPrefix + questionTable +"(answer,extraInfo,question,type) VALUES(?,?,?,?)";
        try {
            values = question.getAnswer() + "===" + question.getExtraInfo() + "===" + question.getQuestion() + "===" + QuestionFactory.toDatabaseString(question);
        } catch (DomainException e) {
            e.printStackTrace();
        }
        String params = "line="+sql+"&vals="+values+"&types="+"ssss";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            addQuestionToGroup(Integer.parseInt(dbTask.getJsonString()),groupID);
        }
        else{
            Log.v("DATA", "NOT INSERTED" );
        }
    }

    @Override
    public void addGroup(Group group) throws DBException {
        String sql = "INSERT INTO "+ dbPrefix + groupTable + "(name,admin,userCanInvite,userCanAddQuestions) VALUES(?,?,?,?)";

        int canIvite = (group.canUserInviteFriends()) ? 1:0;
        int canAdd = (group.canUserAddQuestion()) ? 1:0;


        String values = group.getName() + "===" + group.getAdmin().getEmail() + "===" +  canIvite + "===" + canAdd;
        String params = "line="+sql+"&vals="+values+"&types="+"ssii";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
            addUserToGroup(Integer.parseInt(dbTask.getJsonString()),group.getAdmin().getEmail());
        }
        else{
            Log.v("DATA", "NOT INSERTED" );
        }

    }

    @Override
    public void sendMessage(Message message) throws DBException {
        String sql = "INSERT INTO "+ dbPrefix + messageTable + "(title,body,type,UserID) VALUES(?,?,?,?)";
        String values = message.getTitle() + "===" + message.getBody()+ "===" + message.getType().toString() + "===" + message.getReceiver().getEmail();
        String params = "line="+sql+"&vals="+values+"&types="+"ssss";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
        }
        else{
            Log.v("DATA", "NOT INSERTED" );
        }
    }

    @Override
    public void addQuestionToGroup(int QuestionID, int groupID) throws DBException {
        String sql = "INSERT INTO "+ dbPrefix + questionsInGroupTable + " (GroupID,QuestionID) VALUES(?,?)";
        String values = groupID+"==="+QuestionID;
        String params = "line="+sql+"&vals="+values+"&types="+"ii";
        DatabaseTask dbTask = new DatabaseTask();
        dbTask.execute(url,params);
        while (!dbTask.done()) {
        }
        if (dbTask.fetched()) {
        }
        else{
            Log.v("DATA", "NOT INSERTED" );
        }



    }

}
