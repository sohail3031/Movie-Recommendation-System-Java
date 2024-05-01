package ec.project;

import java.util.Comparator;

class DistanceComparator implements Comparator<MovieDistance> {
    @Override
    public int compare(MovieDistance movie1, MovieDistance movie2) {
        return Double.compare(movie2.getDistance(), movie1.getDistance());
    }
}
