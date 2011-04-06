package dmorand.playit;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public final class PlayItActivity extends Activity implements OnClickListener {
    private static final int SPEECH_RECOGNITION_REQUEST_CODE = 0;

    private ListView mRecognitionResults;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button playItButton = (Button) findViewById(R.id.play_it_button);
        playItButton.setOnClickListener(this);

        mRecognitionResults = (ListView) findViewById(R.id.recognition_results);
    }

    public void onClick(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.play_it);
        startActivityForResult(intent, SPEECH_RECOGNITION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mRecognitionResults.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}