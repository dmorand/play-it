package dmorand.playit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private static final String MUSIC_LOCATION = "/sdcard/music";
    private static final int SPEECH_RECOGNITION_REQUEST_CODE = 0;

    private MusicRepository _musicRepository;
    private ListView _recognitionResults;
    private TextToSpeech _textToSpeech;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadMusicRepository();

        Button playItButton = (Button) findViewById(R.id.play_it_button);
        playItButton.setOnClickListener(this);

        _recognitionResults = (ListView) findViewById(R.id.recognition_results);
        _textToSpeech = new TextToSpeech(this, this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SPEECH_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> originalResults = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            List<String> filteredResults = filterResults(originalResults);

            if (filteredResults.isEmpty()) {
                _recognitionResults.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, originalResults));
            } else {
                _recognitionResults.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filteredResults));
                _textToSpeech.speak(filteredResults.get(0), TextToSpeech.QUEUE_FLUSH, null);
            }
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    private List<String> filterResults(List<String> results) {
        List<String> filteredResults = new ArrayList<String>();
        for (String result : results) {
            if (_musicRepository.containsSong(result)) {
                filteredResults.add(result);
                continue;
            }

            if (_musicRepository.containsAlbum(result)) {
                filteredResults.add(result);
                continue;
            }

            if (_musicRepository.containsArtist(result)) {
                filteredResults.add(result);
            }
        }

        return filteredResults;
    }

    private void loadMusicRepository() {
        _musicRepository = new MusicRepository();

        File musicDirectory = new File(MUSIC_LOCATION);
        for (File artistDirectory : musicDirectory.listFiles()) {
            Artist artist = new Artist(artistDirectory.getName());

            for (File albumDirectory : artistDirectory.listFiles()) {
                Album album = new Album(albumDirectory.getName());

                for (File songFile : albumDirectory.listFiles()) {
                    album.addSong(new Song(songFile));
                }

                artist.addAlbum(album);
            }

            _musicRepository.addArtist(artist);
        }
    }
}