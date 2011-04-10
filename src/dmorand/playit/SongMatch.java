package dmorand.playit;

public final class SongMatch implements Comparable<SongMatch> {
    private final Song _song;
    private final double _score;

    public SongMatch(Song song, double score) {
        _song = song;
        _score = score;
    }

    public int compareTo(SongMatch songMatch) {
        return -Double.compare(_score, songMatch._score);
    }

    public Song getSong() {
        return _song;
    }

    public double getScore() {
        return _score;
    }
}