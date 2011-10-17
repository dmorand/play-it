package dmorand.playit;

import static android.provider.MediaStore.Audio.AudioColumns.ARTIST;
import static android.provider.MediaStore.MediaColumns.TITLE;
import static android.provider.MediaStore.MediaColumns._ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public final class PlayItActivity extends Activity implements OnClickListener,
		OnInitListener {
	private static final int SPEECH_RECOGNITION_REQUEST_CODE = 0;

	private MusicRepository _musicRepository;
	private ListView _recognitionResults;
	private TextToSpeech _textToSpeech;
	private MediaPlayer _mediaPlayer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		loadMusicRepository();

		Button playItButton = (Button) findViewById(R.id.play_it_button);
		playItButton.setOnClickListener(this);

		_recognitionResults = (ListView) findViewById(R.id.recognition_results);
		List<String> songs = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			songs.add(_musicRepository.getRandomSong().getTitle());
		}

		_recognitionResults.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, songs));

		_textToSpeech = new TextToSpeech(this, this);
	}

	public void onInit(int arg0) {
	}

	public void onClick(View v) {
		if (_mediaPlayer != null) {
			_mediaPlayer.stop();
		}

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.play_it);
		startActivityForResult(intent, SPEECH_RECOGNITION_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == SPEECH_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			ArrayList<String> results = intent
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			List<SongMatch> songMatches = matchSongs(results, 8, 0.6);
			Collections.sort(songMatches);

			if (songMatches.isEmpty()) {
				_recognitionResults.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, results));
			} else {
				List<String> songMatchTexts = new ArrayList<String>();
				for (SongMatch songMatch : songMatches) {
					songMatchTexts.add("(" + songMatch.getScore() + ") "
							+ songMatch.getSong().getTitle());
				}

				_recognitionResults.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, songMatchTexts));

				Song song = songMatches.get(0).getSong();
				_textToSpeech.speak(song.getTitle(), TextToSpeech.QUEUE_FLUSH,
						null);

				try {
					_mediaPlayer = new MediaPlayer();
					_mediaPlayer.setDataSource(song.getUri().toString());
					_mediaPlayer.prepare();
					_mediaPlayer.start();
				} catch (Exception exception) {
					_textToSpeech.speak("Impossible de jouer la chanson",
							TextToSpeech.QUEUE_FLUSH, null);
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, intent);
	}

	private List<SongMatch> matchSongs(List<String> results, int maxHypothesis,
			double threshold) {
		results = results.subList(0, Math.min(results.size(), maxHypothesis));

		double averageWordCount = 0;
		Set<String> words = new HashSet<String>();

		for (String result : results) {
			Set<String> resultWords = StringUtils.getWords(result);
			averageWordCount += resultWords.size();
			words.addAll(resultWords);
		}

		averageWordCount /= results.size();
		return _musicRepository.matchSongs(words, averageWordCount, threshold);
	}

	private void loadMusicRepository() {
		_musicRepository = new MusicRepository();

		ContentResolver contentResolver = getContentResolver();
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor cursor = contentResolver.query(uri, null, null, null, null);

		if (cursor == null || !cursor.moveToFirst()) {
			Toast.makeText(getApplicationContext(), "Cannot retrieve music",
					Toast.LENGTH_SHORT);
			return;
		}

		while (!cursor.isAfterLast()) {
			String artist = cursor.getString(cursor.getColumnIndex(ARTIST));
			String title = cursor.getString(cursor.getColumnIndex(TITLE));
			long id = cursor.getLong(cursor.getColumnIndex(_ID));
			_musicRepository.addSong(artist, title, id);
		}
	}
}