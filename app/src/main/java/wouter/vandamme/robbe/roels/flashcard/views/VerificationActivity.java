package wouter.vandamme.robbe.roels.flashcard.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import wouter.vandamme.robbe.roels.flashcard.R;


public class VerificationActivity extends HeaderActivity {

    public VerificationActivity(){
        super("Here you find some extra information on the question at hand, if you want you can give feedback about the question, submit an edit of the quesiton or just continiue to the next question");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        ((TextView) findViewById(R.id.responseTextView)).setText(getIntent().getExtras().getString("answercheck"));
        ((TextView) findViewById(R.id.extraInfoTextView)).setText(getIntent().getExtras().getString("extraInfo"));
        ((TextView) findViewById(R.id.title_id)).setText(getIntent().getExtras().getString("GroupName"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_verification, menu);
        return true;
    }

}
