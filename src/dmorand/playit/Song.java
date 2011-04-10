package dmorand.playit;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public final class Song {
    private final File _file;
    private final String _title;
    private final Set<String> _words;

    public Song(File file) {
        _file = file;
        _title = _file.getName().replaceAll("^\\d+ ", "").replaceAll("\\.[^.]+$", "");
        _words = StringUtils.getWords(_title);
    }

    public File getFile() {
        return _file;
    }

    public String getTitle() {
        return _title;
    }

    public double match(Set<String> words) {
        Set<String> intersection = new HashSet<String>(_words);
        intersection.retainAll(words);

        int wordCount = _words.size();
        return (((double) intersection.size()) / wordCount) + (wordCount * 0.02);
    }
}