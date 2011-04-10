package dmorand.playit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Artist {
    private final String _name;
    private final Set<String> _words;
    private final List<Song> _songs = new ArrayList<Song>();

    public Artist(String name) {
        _name = name;
        _words = StringUtils.getWords(_name);
    }

    public String getName() {
        return _name;
    }

    public void addSong(Song song) {
        _songs.add(song);
    }

    public List<SongMatch> matchSong(Set<String> words, double threshold) {
        Set<String> intersection = new HashSet<String>(_words);
        intersection.retainAll(words);
        double artistBonus = ((double) intersection.size()) / words.size();

        List<SongMatch> songMatches = new ArrayList<SongMatch>();
        for (Song song : _songs) {
            double score = song.match(words);
            if (score >= threshold) {
                songMatches.add(new SongMatch(song, score + artistBonus));
            }
        }

        return songMatches;
    }
}