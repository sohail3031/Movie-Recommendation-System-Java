package ec.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TFIDFCalculator {

    // Calculate the term frequency (TF) for a term in a document
    public double tf(List<String> doc, String term) {
        double result = 0;
        
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        
        return result / doc.size();
    }

    // Calculate the inverse document frequency (IDF) for a term in a corpus
    public double idf(List<Movie> movies, String term) {
        double n = 0;
        
        for (Movie movie : movies) {
            for (String tag : movie.tags) {
                if (term.equalsIgnoreCase(tag)) {
                    n++;
        
                    break;
                }
            }
        }
        
        return Math.log(movies.size() / n);
    }

    // Calculate the TF-IDF for a term in a document
    public double tfIdf(List<String> doc, List<Movie> movies, String term) {
        return tf(doc, term) * idf(movies, term);
    }
    
    // Calculate the cosine similarity between two documents
    public double cosineSimilarity(Map<String, Double> doc1, Map<String, Double> doc2) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        
        for (String key : doc1.keySet()) {
            dotProduct += doc1.get(key) * doc2.getOrDefault(key, 0.0);
            normA += Math.pow(doc1.get(key), 2);
            normB += Math.pow(doc2.getOrDefault(key, 0.0), 2);
        }
        
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
    
    // Method to create a 2D array from the TF-IDF vectors of all movies
    public double[][] createTfIdfMatrix(List<Movie> movies) {
        // Assuming all movies have the same set of tags, find the total number of unique tags
        Set<String> uniqueTags = new HashSet();
        
        for (Movie movie : movies) {
            uniqueTags.addAll(movie.getTags());
        }
        
        // Create a list of all tags for indexing
        List<String> tagList = new ArrayList<>(uniqueTags);
        
        // Initialize the 2D array
        double[][] tfIdfMatrix = new double[movies.size()][tagList.size()];
        
        // Fill the 2D array with TF-IDF values
        for (int i = 0; i < movies.size(); i++) {
            Map<String, Double> tfIdfVector = createTfIdfVector(movies.get(i).getTags(), movies);
        
            for (int j = 0; j < tagList.size(); j++) {
                String tag = tagList.get(j);
                tfIdfMatrix[i][j] = tfIdfVector.getOrDefault(tag, 0.0);
            }
        }
        
        return tfIdfMatrix;
    }
    
    // Create a TF-IDF vector for each movie
    public Map<String, Double> createTfIdfVector(List<String> tags, List<Movie> movies) {
        Map<String, Double> tfIdfVector = new HashMap<>();
        
        for (String tag : tags) {
            double tfIdfValue = tfIdf(tags, movies, tag);
        
            tfIdfVector.put(tag, tfIdfValue);
        }
        
        return tfIdfVector;
    }
    
    public void writeMatrixToFile(double[][] matrix, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (double[] row : matrix) {
                for (int i = 0; i < row.length; i++) {
                    writer.write(Double.toString(row[i]));
                    
                    if (i < row.length - 1) {
                        writer.write(",");
                    }
                }
                
                writer.newLine();
            }
        }
    }

    // Method to read the 2D array from a file
    public double[][] readMatrixFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // Initialize line with the first read
            
            if (line == null) {
                throw new IOException("File is empty");
            }
            
            String[] firstLine = line.split(",");
            double[][] matrix = new double[firstLine.length][];
            int row = 0;
            
            do {
                String[] values = line.split(",");
                matrix[row] = new double[values.length];
            
                for (int col = 0; col < values.length; col++) {
                    matrix[row][col] = Double.parseDouble(values[col]);
                }
                
                row++;
            } while ((line = reader.readLine()) != null);
            
            return matrix;
        }
    }
    
    public double[][] createSimilarityMatrix(List<Movie> movies) {
        double[][] similarityMatrix = new double[movies.size()][movies.size()];

        for (int i = 0; i < movies.size(); i++) {
            for (int j = 0; j < movies.size(); j++) {
                if (i == j) {
                    similarityMatrix[i][j] = 1; // The similarity of a movie with itself is 1
                } else {
                    Map<String, Double> tfIdfVectorI = createTfIdfVector(movies.get(i).getTags(), movies);
                    Map<String, Double> tfIdfVectorJ = createTfIdfVector(movies.get(j).getTags(), movies);
                    similarityMatrix[i][j] = cosineSimilarity(tfIdfVectorI, tfIdfVectorJ);
                    
//                    System.out.println("Similarity: " + i + " -> " + j + " -> " + similarityMatrix[i][j]);
                }
            }
        }

        return similarityMatrix;
    }
    
//    public List<Movie> recommendMovies(List<Movie> allMovies, double[][] similarityScores) {
//        // Create a list to store the movies and their associated similarity scores
//        List<MovieScorePair> movieScorePairs = new ArrayList<MovieScorePair>();
//
//        // Assume each movie has a corresponding row in the similarityScores array
//        for (int i = 0; i < allMovies.size(); i++) {
//            // Assume the similarity score for the movie is at index [i][i]
//            double score = similarityScores[i][i];
//            movieScorePairs.add(new MovieScorePair(allMovies.get(i), score));
//        }
//
//        // Sort the movieScorePairs by their similarity scores in descending order
//        Collections.sort(movieScorePairs, new Comparator<MovieScorePair>() {
//            public int compare(MovieScorePair o1, MovieScorePair o2) {
//                return Double.compare(o2.getScore(), o1.getScore());
//            }
//        });
//
//        // Extract the sorted movies from the movieScorePairs
//        List<Movie> sortedMovies = new ArrayList<Movie>();
//        for (MovieScorePair pair : movieScorePairs) {
//            sortedMovies.add(pair.getMovie());
//        }
//
//        return sortedMovies;
//    }
    
    public List<Movie> recommendMovies(List<Movie> allMovies, double[][] similarityScores, String selectedMovieTitle) {
        // Find the index of the selected movie
        int selectedIndex = -1;
        for (int i = 0; i < allMovies.size(); i++) {
            if (allMovies.get(i).title.equalsIgnoreCase(selectedMovieTitle)) {
                selectedIndex = i;
                break;
            }
        }

        // Check if the selected movie was found
        if (selectedIndex == -1) {
            throw new IllegalArgumentException("Selected movie not found in the list.");
        }

        // Create a list to store the movies and their associated similarity scores
        List<MovieScorePair> movieScorePairs = new ArrayList<MovieScorePair>();

        // Use the selected index to access the similarity scores for the selected movie
        for (int i = 0; i < allMovies.size(); i++) {
            if (i != selectedIndex) { // Skip the selected movie itself
                double score = similarityScores[selectedIndex][i];
                movieScorePairs.add(new MovieScorePair(allMovies.get(i), score));
            }
        }

        // Sort the movieScorePairs by their similarity scores in descending order
        Collections.sort(movieScorePairs, new Comparator<MovieScorePair>() {
            public int compare(MovieScorePair o1, MovieScorePair o2) {
                return Double.compare(o2.getScore(), o1.getScore());
            }
        });

        // Extract the sorted movies from the movieScorePairs
        List<Movie> sortedMovies = new ArrayList<Movie>();
        for (MovieScorePair pair : movieScorePairs) {
            sortedMovies.add(pair.getMovie());
        }

        return sortedMovies;
    }




//    public static void main(String[] args) throws IOException {
//        // Create a list of movies with tags
//        List<Movie> movies = new ArrayList<Movie>();
//        Movie movie1 = new Movie();
//        Movie movie2 = new Movie();
//        Movie movie3 = new Movie();
//        
//        movie1.title = "Movie 1";
//        movie1.tags = Arrays.asList("t1", "t2", "t3");
//        movie2.title = "Movie 2";
//        movie2.tags = Arrays.asList("t2", "t3", "t4");
//        movie3.title = "Movie 3";
//        movie3.tags = Arrays.asList("t3", "t4", "t5");
//
//        // Initialize the TFIDFCalculator
//        TFIDFCalculator calculator = new TFIDFCalculator();
//
//        // Calculate the TF-IDF matrix for the movies
//        double[][] tfIdfMatrix = calculator.createTfIdfMatrix(movies);
//
//        // Print the TF-IDF matrix
//        for (double[] row : tfIdfMatrix) {
//            for (double value : row) {
//                System.out.printf("%.3f ", value);
//            }
//            System.out.println();
//        }
//
//        // Optionally, write the matrix to a file
//        calculator.writeMatrixToFile(tfIdfMatrix, "tfidf_matrix.csv");
//    }
    
//    // Example usage
//    public static void main(String[] args) {
//        // Assuming you have a List<Movie> movies with a getTags() method
//        List<Movie> movies = /* ... */;
//        
//        TFIDFCalculator calculator = new TFIDFCalculator();
//        
//        // Calculate TF-IDF vectors for each movie
//        Map<Integer, Map<String, Double>> movieVectors = new HashMap<>();
//        for (Movie movie : movies) {
//            Map<String, Double> tfIdfVector = calculator.createTfIdfVector(movie.tags, movies);
//            movieVectors.put(movie.tags, tfIdfVector);
//        }
//        
//        // Now you can use movieVectors to calculate cosine similarities between movies
//    }
}

