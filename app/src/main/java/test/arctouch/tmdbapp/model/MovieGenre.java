package test.arctouch.tmdbapp.model;

public enum MovieGenre {

    BLANK(-1, ""),
    ACTION(28, "Action"),
    ADVENTURE(12, "Adventure"),
    ANIMATION(16, "Animation"),
    COMEDY(35, "Comedy"),
    CRIME(80, "Crime"),
    DOCUMENTARY(99, "Documentary"),
    DRAMA(18, "Drama"),
    FAMILY(10751, "Family"),
    FANTASY(14, "Fantasy"),
    HISTORY(36, "History"),
    HORROR(27, "Horror"),
    MUSIC(10402, "Music"),
    MYSTERY(9648, "Mystery"),
    ROMANCE(10749, "Romance"),
    SCIENCE_FICTION(878, "Science Fiction"),
    TV_MOVIE(10770, "TV Movie"),
    THRILLER(53, "Thriller"),
    WAR(10752, "War"),
    WESTERN(37, "Western"),
    FOREIGN(10769, "Foreign");

    int id;
    String title;

    MovieGenre(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static MovieGenre getById(int id) {
        for (MovieGenre movieGenre : values()) {
            if (movieGenre.id == id) {
                return movieGenre;
            }
        }
        return MovieGenre.BLANK;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}