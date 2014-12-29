package wouter.vandamme.robbe.roels.flashcard.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import db.DBException;
import domain.Group;
import facade.Facade;
import wouter.vandamme.robbe.roels.flashcard.R;

public class MenuActivity extends HeaderActivity {
    Facade facade;

    ListView groupList;
    Intent toQuestion;
    public MenuActivity() {
        super("Create a new group or choose one of the groups you're already part of.");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        facade = Facade.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        groupList = (ListView) findViewById(R.id.groupsListView);

        groupList.setOnItemClickListener(getOnItemClickListener());
        ArrayAdapter adapter = getArrayAdapter();

        groupList.setAdapter(adapter);

        TextView header = (TextView) findViewById(R.id.title_id);
        header.setText("Menu");
        toQuestion  =  new Intent(this,QuestionActivity.class);

    }

    public void toCreateGroup(View view) {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }


    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Group group = (Group) adapter.getItemAtPosition(position);
                Log.v("ID on click: ", group.getId()+"");
                toQuestion.putExtra("GroupID",group.getId());
                toQuestion.putExtra("GroupName",group.getName());
                toQuestion.putExtra("canAddQuestions",group.canUserAddQuestion());
                toQuestion.putExtra("canInvite",group.canUserInviteFriends());
                startActivity(toQuestion);
            }
        };
    }

    public ArrayAdapter getArrayAdapter() {
        ArrayAdapter adapter = null;
        try {
            //Slightly "hacky" way to set the text color without having to rewrite the entire ListView
            adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, facade.getGroups()) {
                @Override
                public View getView(int position, View convertView,
                                    ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView textView = (TextView) view.findViewById(android.R.id.text1);

            /*YOUR CHOICE OF COLOR*/
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(24);

                    return view;
                }
            };
        } catch (DBException e) {
            e.printStackTrace();
        }
        return adapter;
    }


    @Override
    public void onResume(){
        super.onResume();
        ArrayAdapter adapter = getArrayAdapter();
        groupList.setAdapter(adapter);
    }
}
