package dmorand.playit;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentUris;
import android.net.Uri;

public final class Song {
	private final long _id;
	private final String _title;
	private final Set<String> _words;

	public Song(String title, long id) {
		_id = id;
		_title = title;
		_words = StringUtils.getWords(_title);
	}

	public String getTitle() {
		return _title;
	}

	public Uri getUri() {
		return ContentUris.withAppendedId(
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				_id);
	}

	public double match(Set<String> words, double averageWordCount) {
		Set<String> intersection = new HashSet<String>(_words);
		intersection.retainAll(words);

		int wordCount = _words.size();
		double score = intersection.size();
		score /= wordCount;
		score += wordCount * 0.02;
		score -= Math.abs(wordCount - averageWordCount) * 0.02;
		return score;
	}
}