package ec.project;

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

public class CosineSimilarity {
	public static double[][] calculateCosineSimilarity(List<Movie> movies) {
        int numMovies = movies.size();
        double[][] similarityMatrix = new double[numMovies][numMovies];

        for (int i = 0; i < numMovies; i++) {
            for (int j = 0; j < numMovies; j++) {
                if (i == j) {
                    similarityMatrix[i][j] = 1.0;  // Similarity of a movie with itself is 1
                } else {
                    double similarity = cosineSimilarity(movies.get(i).tags, movies.get(j).tags);
                    similarityMatrix[i][j] = similarity;
                }
            }
        }

        return similarityMatrix;
    }

    private static double cosineSimilarity(List<String> tags1, List<String> tags2) {
        double intersectionSize = countCommonTags(tags1, tags2);
        double unionSize = tags1.size() + tags2.size() - intersectionSize;

        return intersectionSize / unionSize;
    }

    private static double countCommonTags(List<String> tags1, List<String> tags2) {
        int count = 0;

        for (String tag : tags1) {
            if (tags2.contains(tag)) {
                count++;
            }
        }
        
        return count;
    }
}
