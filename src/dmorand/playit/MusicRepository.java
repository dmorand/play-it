package dmorand.playit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class MusicRepository {
	private final Map<String, Artist> _artists = new HashMap<String, Artist>();

	public void addSong(String artistName, String title, long id) {
		Artist artist = _artists.get(artistName);
		
		if (artist == null) {
			artist = new Artist(artistName);
			_artists.put(artistName, artist);
		}
		
		artist.addSong(new Song(title, id));
	}

	public boolean containsArtist(String artistName) {
		return _artists.containsKey(artistName);
	}

	public List<SongMatch> matchSongs(Set<String> words,
			double averageWordCount, double threshold) {
		List<SongMatch> matchedSongs = new ArrayList<SongMatch>();
		for (Artist artist : _artists.values()) {
			matchedSongs.addAll(artist.matchSong(words, averageWordCount,
					threshold));
		}

		return matchedSongs;
	}

	public Song getRandomSong() {
		return _artists.get(new Random().nextInt(_artists.size()))
				.getRandomSong();
	}
}