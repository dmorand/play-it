package dmorand.playit;

import java.util.ArrayList;
import java.util.List;

public final class Album {
    private final String _title;
    private final List<Song> _songs = new ArrayList<Song>();

    public Album(String _title) {
        this._title = _title;
    }

    public String getTitle() {
        return _title;
    }

    public void addSong(Song song) {
        _songs.add(song);
    }

    public boolean containsSong(String title) {
        for (Song song : _songs) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }

        return false;
    }
}
