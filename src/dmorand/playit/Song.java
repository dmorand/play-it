package dmorand.playit;

import java.io.File;

public final class Song {
    private final File _file;
    private final String _title;

    public Song(File file) {
        _file = file;
        _title = _file.getName().replaceAll("^\\d+ ", "").replaceAll("\\.[^.]+$", "");
    }

    public File getFile() {
        return _file;
    }

    public String getTitle() {
        return _title;
    }
}