package dmorand.playit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public final class PlayItActivity extends Activity implements OnClickListener, OnInitListener {
    private static final int SPEECH_RECOGNITION_REQUEST_CODE = 0;

    private ListView mRecognitionResults;
    private TextToSpeech mTextToSpeech;
    private Set<String> mArtists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadArtists();

        Button playItButton = (Button) findViewById(R.id.play_it_button);
        playItButton.setOnClickListener(this);

        mRecognitionResults = (ListView) findViewById(R.id.recognition_results);
        mRecognitionResults.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(mArtists)));

        mTextToSpeech = new TextToSpeech(this, this);
    }

    public void onInit(int arg0) {
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
            List<String> results = filterResults(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS));
            mRecognitionResults.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results));

            if (!results.isEmpty()) {
                mTextToSpeech.speak(results.get(0), TextToSpeech.QUEUE_FLUSH, null);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private List<String> filterResults(List<String> results) {
        List<String> filteredResults = new ArrayList<String>();
        for (String result : results) {
            if (mArtists.contains(result)) {
                filteredResults.add(result);
            }
        }

        return filteredResults;
    }

    private void loadArtists() {
        mArtists = new TreeSet<String>();
        File file = new File("/sdcard/music");
        for (String artist : file.list()) {
            mArtists.add(artist.toLowerCase());
        }
    }
}