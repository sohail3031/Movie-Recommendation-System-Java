package ec.project;

class MovieScorePair {
    private Movie movie;
    private double score;

    public MovieScorePair(Movie movie, double score) {
        this.movie = movie;
        this.score = score;
    }

    public Movie getMovie() {
        return movie;
    }

    public double getScore() {
        return score;
    }
}