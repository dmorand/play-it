package dmorand.playit;

import java.util.ArrayList;
import java.util.List;

public final class Artist {
    private final String _name;
    private final List<Album> _albums = new ArrayList<Album>();

    public Artist(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void addAlbum(Album album) {
        _albums.add(album);
    }

    public boolean containsAlbum(String title) {
        for (Album album : _albums) {
            if (album.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsSong(String title) {
        for (Album album : _albums) {
            if (album.containsSong(title)) {
                return true;
            }
        }

        return false;
    }
}