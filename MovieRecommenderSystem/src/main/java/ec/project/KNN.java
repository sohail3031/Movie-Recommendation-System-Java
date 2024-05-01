package ec.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KNN {
    public static double euclideanDistance(double[] point1, double[] point2) {
        double sum = 0.0;
        
        for (int i = 0; i < point1.length; i++) {
            sum += Math.pow(point1[i] - point2[i], 2);
        }
        
        return Math.sqrt(sum);
    }

    public static List<Integer> findNearestNeighbors(double[][] similarities, int k, int dataPointIndex) {
    	List<double[]> distances = new ArrayList<double[]>();

    	for (int i = 0; i < similarities[dataPointIndex].length; i++) {
    	    if (i != dataPointIndex) {
    	        double distance = similarities[dataPointIndex][i];
    	
    	        distances.add(new double[]{i, distance});
    	    }
    	}

        Collections.sort(distances, new Comparator<double[]>() {
            public int compare(double[] o1, double[] o2) {
                return Double.compare(o1[1], o2[1]);
            }
        });

        List<Integer> nearestNeighbors = new ArrayList<Integer>();
        int numberOfNeighbors = Math.min(k, distances.size()); // Ensure we don't exceed the list size
        
        for (int i = 0; i < numberOfNeighbors; i++) {
            nearestNeighbors.add((int) distances.get(i)[0]);
        }

        return nearestNeighbors;
    }

    public static void makePredictions(List<Integer> nearestNeighbors, List<Movie> movies) {
        for (int neighborIndex : nearestNeighbors) {
            Movie neighborMovie = movies.get(neighborIndex);

            System.out.println("Neighbor Movie: " + neighborMovie.title);
        }
    }
    
    public static List<String> makeWeightedPredictions(List<Integer> nearestNeighbors, double[][] similarities, int dataPointIndex, List<Movie> movies) {
        // Create a map to store movies and their weighted scores
        Map<Movie, Double> movieScores = new HashMap<Movie, Double>();
        List<String> indexes = new ArrayList<String>();
        // Calculate the total distance to normalize weights
        double totalDistance = 0.0;
        
        for (int neighborIndex : nearestNeighbors) {
            totalDistance += similarities[dataPointIndex][neighborIndex];
        }

        // Assign weighted scores to each neighbor movie
        for (int neighborIndex : nearestNeighbors) {
            Movie neighborMovie = movies.get(neighborIndex);
            double weight = 1 - (similarities[dataPointIndex][neighborIndex] / totalDistance);
        
            movieScores.put(neighborMovie, weight);
        }

        // Sort movies by their weighted scores
        List<Map.Entry<Movie, Double>> sortedMovies = new ArrayList<>(movieScores.entrySet());

        Collections.sort(sortedMovies, new Comparator<Map.Entry<Movie, Double>>() {
            public int compare(Map.Entry<Movie, Double> o1, Map.Entry<Movie, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Print out the recommended movies based on weighted scores
        for (Map.Entry<Movie, Double> entry : sortedMovies) {
        	indexes.add(entry.getKey().title);
//            System.out.println("Recommended Movie: " + entry.getKey().title + " - Score: " + entry.getValue());
        }
        
        return indexes;
    }
}