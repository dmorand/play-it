package dmorand.playit;

import java.util.ArrayList;
import java.util.List;

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

    public boolean containsAlbum(String title) {
        for (Artist artist : _artists) {
            if (artist.containsAlbum(title)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsSong(String title) {
        for (Artist artist : _artists) {
            if (artist.containsSong(title)) {
                return true;
            }
        }

        return false;
    }
}