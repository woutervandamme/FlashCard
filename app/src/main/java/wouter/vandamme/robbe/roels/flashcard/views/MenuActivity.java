package wouter.vandamme.robbe.roels.flashcard.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import wouter.vandamme.robbe.roels.flashcard.R;

public class MenuActivity extends HeaderActivity {


    public MenuActivity() {
        super("Create a new group or choose one of the groups you're already part of.");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void toCreateGroup(View view){
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }

    public void toQuestion(View view){
        Intent intent = new Intent(this, QuestionActivity.class);
        startActivity(intent);
    }


}
