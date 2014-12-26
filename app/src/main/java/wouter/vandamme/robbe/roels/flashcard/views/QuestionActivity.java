package wouter.vandamme.robbe.roels.flashcard.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import wouter.vandamme.robbe.roels.flashcard.R;

public class QuestionActivity extends HeaderActivity {


    public QuestionActivity() {
        super("Answer the question in the text field and press 'validate' to check your answer");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    public void verify(View view){
        Intent intent = new Intent(this, VerificationActivity.class);
        startActivity(intent);
    }

}
