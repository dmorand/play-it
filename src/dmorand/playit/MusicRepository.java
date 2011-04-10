package dmorand.playit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class MusicRepository {
    private final List<Artist> _artists = new ArrayList<Artist>();

    public void addArtist(Artist artist) {
        _artists.add(artist);
    }

    public boolean containsArtist(String name) {
        for (Artist artist : _artists) {
            if (artist.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    public List<SongMatch> matchSongs(Set<String> words, double threshold) {
        List<SongMatch> matchedSongs = new ArrayList<SongMatch>();
        for (Artist artist : _artists) {
            matchedSongs.addAll(artist.matchSong(words, threshold));
        }

        return matchedSongs;
    }
}