package ec.project;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) throws IOException {
        this.movieService = movieService;
    }
    
//    url to handle the landing page
    @RequestMapping("/")
    public String movieTitle(Model model) throws IOException {
        movieService.initial();
        
    	List<String> titles = movieService.readMovieTitles();
    	
    	model.addAttribute("titles", titles);
        
    	return "index";
    }
    
//    url that shows the recommended movies
    @RequestMapping("/recommend-movies")
    public String recommendMovies(@RequestParam(name="movies-titles", required=true, defaultValue="EC Spring") String name, Model model) throws IOException {
        try {
//	    	PrintStream logFile = new PrintStream(new FileOutputStream("MovieRecommenderSystem/src/main/java/ec/project/dataset/logs.txt"));
//	        
//	    	System.setOut(logFile);
	    	movieService.setRecommendedMoviesTitlesIndex(new ArrayList<Integer>());
	    	
	    	List<String> titles = movieService.getMovieTitles();
	    	List<Movie> movies = movieService.getMoviesList();
	    	
	    	if (titles.size() == 0) {
	    		movieService.trainModels(movies);
	    	} 
	
	//    	get the cosine similarity
	    	double[][] similarities = movieService.getSimilarityArray();
	//    	get the recommended movies based on cosine similarity
	    	String[] relatedMoviesNames = movieService.recommendedMoviesCosine(movies, name, similarities);
	    	int[] relatedMoviesIndexes = movieService.getRecommendedMoviesIndexCosine(relatedMoviesNames, movies);
	    	List<Integer> temp = movieService.getRecommendedMoviesTitlesIndex();
	    	List<Movie> recommendedMoviesCosine = new ArrayList<Movie>();
	    	List<Movie> recommendedMoviesCosineKNN = new ArrayList<Movie>();
	    	double[][] tfidf = movieService.getTfidfArray();
	    	int selectedMovieIndex = movieService.getSelectedMovieIndex(movies, name);
	    	
	//    	KNN algorithm
	    	KNN knn = new KNN();
	    	
	    	List<Integer> knnRecommendedIndexes = knn.findNearestNeighbors(similarities, 4803, selectedMovieIndex);
	    	List<String> knnIndexes = knn.makeWeightedPredictions(knnRecommendedIndexes, similarities, selectedMovieIndex, movies);
	    	List<String> knnLastFiveValues = knnIndexes.subList(knnIndexes.size() - 5, knnIndexes.size());
	    	
	//    	Tf-Idf algorithm
	    	TFIDFCalculator calculator = new TFIDFCalculator();
	        
	        List<Movie> recommendedMoviesCosineTfIdf = calculator.recommendMovies(movies, similarities, name).subList(5,  10);
	    	
	        
	    	for (Integer i : temp) {
	    		recommendedMoviesCosine.add(movies.get(i));
	    	}
	    	
	    	for (int i = 0; i < recommendedMoviesCosine.size(); i++) {
	    		System.out.println("Cosine Similarity: " + recommendedMoviesCosine.get(i).title);
	    	}
	    	
	    	for (String i : knnLastFiveValues) {
	    		recommendedMoviesCosineKNN.add(movies.get(movieService.getMovieNameForKNNRecommendation(i, movies)));
	    	}
	    	
	    	for (int i = 0; i < recommendedMoviesCosineKNN.size(); i++) {
	    		System.out.println("Cosine Similarity with KNN: " + recommendedMoviesCosineKNN.get(i).title);
	    	}
	    	
	    	for (int i = 0; i < recommendedMoviesCosineTfIdf.size(); i++) {
	    		System.out.println("If-Idf Similarity: " + recommendedMoviesCosineTfIdf.get(i).title);
	    	}
	    	
//	    	System.setOut(System.out);
	    	
	//    	selected Movie Name
	    	model.addAttribute("selectedMovie", name);
	    	
	//    	recommended movies using cosine similarity
	    	model.addAttribute("cs_title_1", recommendedMoviesCosine.get(0).title);
	    	model.addAttribute("cs_title_2", recommendedMoviesCosine.get(1).title);
	    	model.addAttribute("cs_title_3", recommendedMoviesCosine.get(2).title);
	    	model.addAttribute("cs_title_4", recommendedMoviesCosine.get(3).title);
	    	model.addAttribute("cs_title_5", recommendedMoviesCosine.get(4).title);
	    	model.addAttribute("cs_image_1", movieService.fetchPoster(recommendedMoviesCosine.get(0).movieId));
	    	model.addAttribute("cs_image_2", movieService.fetchPoster(recommendedMoviesCosine.get(1).movieId));
	    	model.addAttribute("cs_image_3", movieService.fetchPoster(recommendedMoviesCosine.get(2).movieId));
	    	model.addAttribute("cs_image_4", movieService.fetchPoster(recommendedMoviesCosine.get(3).movieId));
	    	model.addAttribute("cs_image_5", movieService.fetchPoster(recommendedMoviesCosine.get(4).movieId));
	    	model.addAttribute("cs_popularity_1", recommendedMoviesCosine.get(0).popularity);
	    	model.addAttribute("cs_popularity_2", recommendedMoviesCosine.get(1).popularity);
	    	model.addAttribute("cs_popularity_3", recommendedMoviesCosine.get(2).popularity);
	    	model.addAttribute("cs_popularity_4", recommendedMoviesCosine.get(3).popularity);
	    	model.addAttribute("cs_popularity_5", recommendedMoviesCosine.get(4).popularity);
	    	model.addAttribute("cs_runtime_1", recommendedMoviesCosine.get(0).runtime);
	    	model.addAttribute("cs_runtime_2", recommendedMoviesCosine.get(1).runtime);
	    	model.addAttribute("cs_runtime_3", recommendedMoviesCosine.get(2).runtime);
	    	model.addAttribute("cs_runtime_4", recommendedMoviesCosine.get(3).runtime);
	    	model.addAttribute("cs_runtime_5", recommendedMoviesCosine.get(4).runtime);
	    	model.addAttribute("cs_vote_average_1", recommendedMoviesCosine.get(0).voteAverage);
	    	model.addAttribute("cs_vote_average_2", recommendedMoviesCosine.get(1).voteAverage);
	    	model.addAttribute("cs_vote_average_3", recommendedMoviesCosine.get(2).voteAverage);
	    	model.addAttribute("cs_vote_average_4", recommendedMoviesCosine.get(3).voteAverage);
	    	model.addAttribute("cs_vote_average_5", recommendedMoviesCosine.get(4).voteAverage);
	    	model.addAttribute("cs_vote_count_1", recommendedMoviesCosine.get(0).voteCount);
	    	model.addAttribute("cs_vote_count_2", recommendedMoviesCosine.get(1).voteCount);
	    	model.addAttribute("cs_vote_count_3", recommendedMoviesCosine.get(2).voteCount);
	    	model.addAttribute("cs_vote_count_4", recommendedMoviesCosine.get(3).voteCount);
	    	model.addAttribute("cs_vote_count_5", recommendedMoviesCosine.get(4).voteCount);
	
	//    	recommended movies using cosine similarity & knn
	    	model.addAttribute("cs_knn_title_1", recommendedMoviesCosineKNN.get(0).title);
	    	model.addAttribute("cs_knn_title_2", recommendedMoviesCosineKNN.get(1).title);
	    	model.addAttribute("cs_knn_title_3", recommendedMoviesCosineKNN.get(2).title);
	    	model.addAttribute("cs_knn_title_4", recommendedMoviesCosineKNN.get(3).title);
	    	model.addAttribute("cs_knn_title_5", recommendedMoviesCosineKNN.get(4).title);
	    	model.addAttribute("cs_knn_image_1", movieService.fetchPoster(recommendedMoviesCosineKNN.get(0).movieId));
	    	model.addAttribute("cs_knn_image_2", movieService.fetchPoster(recommendedMoviesCosineKNN.get(1).movieId));
	    	model.addAttribute("cs_knn_image_3", movieService.fetchPoster(recommendedMoviesCosineKNN.get(2).movieId));
	    	model.addAttribute("cs_knn_image_4", movieService.fetchPoster(recommendedMoviesCosineKNN.get(3).movieId));
	    	model.addAttribute("cs_knn_image_5", movieService.fetchPoster(recommendedMoviesCosineKNN.get(4).movieId));
	    	model.addAttribute("cs_knn_popularity_1", recommendedMoviesCosineKNN.get(0).popularity);
	    	model.addAttribute("cs_knn_popularity_2", recommendedMoviesCosineKNN.get(1).popularity);
	    	model.addAttribute("cs_knn_popularity_3", recommendedMoviesCosineKNN.get(2).popularity);
	    	model.addAttribute("cs_knn_popularity_4", recommendedMoviesCosineKNN.get(3).popularity);
	    	model.addAttribute("cs_knn_popularity_5", recommendedMoviesCosineKNN.get(4).popularity);
	    	model.addAttribute("cs_knn_runtime_1", recommendedMoviesCosineKNN.get(0).runtime);
	    	model.addAttribute("cs_knn_runtime_2", recommendedMoviesCosineKNN.get(1).runtime);
	    	model.addAttribute("cs_knn_runtime_3", recommendedMoviesCosineKNN.get(2).runtime);
	    	model.addAttribute("cs_knn_runtime_4", recommendedMoviesCosineKNN.get(3).runtime);
	    	model.addAttribute("cs_knn_runtime_5", recommendedMoviesCosineKNN.get(4).runtime);
	    	model.addAttribute("cs_knn_vote_average_1", recommendedMoviesCosineKNN.get(0).voteAverage);
	    	model.addAttribute("cs_knn_vote_average_2", recommendedMoviesCosineKNN.get(1).voteAverage);
	    	model.addAttribute("cs_knn_vote_average_3", recommendedMoviesCosineKNN.get(2).voteAverage);
	    	model.addAttribute("cs_knn_vote_average_4", recommendedMoviesCosineKNN.get(3).voteAverage);
	    	model.addAttribute("cs_knn_vote_average_5", recommendedMoviesCosineKNN.get(4).voteAverage);
	    	model.addAttribute("cs_knn_vote_count_1", recommendedMoviesCosineKNN.get(0).voteCount);
	    	model.addAttribute("cs_knn_vote_count_2", recommendedMoviesCosineKNN.get(1).voteCount);
	    	model.addAttribute("cs_knn_vote_count_3", recommendedMoviesCosineKNN.get(2).voteCount);
	    	model.addAttribute("cs_knn_vote_count_4", recommendedMoviesCosineKNN.get(3).voteCount);
	    	model.addAttribute("cs_knn_vote_count_5", recommendedMoviesCosineKNN.get(4).voteCount);
	
	//    	recommended movies using tf-idf similarity
	    	model.addAttribute("tf_title_1", recommendedMoviesCosineTfIdf.get(0).title);
	    	model.addAttribute("tf_title_2", recommendedMoviesCosineTfIdf.get(1).title);
	    	model.addAttribute("tf_title_3", recommendedMoviesCosineTfIdf.get(2).title);
	    	model.addAttribute("tf_title_4", recommendedMoviesCosineTfIdf.get(3).title);
	    	model.addAttribute("tf_title_5", recommendedMoviesCosineTfIdf.get(4).title);
	    	model.addAttribute("tf_image_1", movieService.fetchPoster(recommendedMoviesCosineTfIdf.get(0).movieId));
	    	model.addAttribute("tf_image_2", movieService.fetchPoster(recommendedMoviesCosineTfIdf.get(1).movieId));
	    	model.addAttribute("tf_image_3", movieService.fetchPoster(recommendedMoviesCosineTfIdf.get(2).movieId));
	    	model.addAttribute("tf_image_4", movieService.fetchPoster(recommendedMoviesCosineTfIdf.get(3).movieId));
	    	model.addAttribute("tf_image_5", movieService.fetchPoster(recommendedMoviesCosineTfIdf.get(4).movieId));
	    	model.addAttribute("tf_popularity_1", recommendedMoviesCosineTfIdf.get(0).popularity);
	    	model.addAttribute("tf_popularity_2", recommendedMoviesCosineTfIdf.get(1).popularity);
	    	model.addAttribute("tf_popularity_3", recommendedMoviesCosineTfIdf.get(2).popularity);
	    	model.addAttribute("tf_popularity_4", recommendedMoviesCosineTfIdf.get(3).popularity);
	    	model.addAttribute("tf_popularity_5", recommendedMoviesCosineTfIdf.get(4).popularity);
	    	model.addAttribute("tf_runtime_1", recommendedMoviesCosineTfIdf.get(0).runtime);
	    	model.addAttribute("tf_runtime_2", recommendedMoviesCosineTfIdf.get(1).runtime);
	    	model.addAttribute("tf_runtime_3", recommendedMoviesCosineTfIdf.get(2).runtime);
	    	model.addAttribute("tf_runtime_4", recommendedMoviesCosineTfIdf.get(3).runtime);
	    	model.addAttribute("tf_runtime_5", recommendedMoviesCosineTfIdf.get(4).runtime);
	    	model.addAttribute("tf_vote_average_1", recommendedMoviesCosineTfIdf.get(0).voteAverage);
	    	model.addAttribute("tf_vote_average_2", recommendedMoviesCosineTfIdf.get(1).voteAverage);
	    	model.addAttribute("tf_vote_average_3", recommendedMoviesCosineTfIdf.get(2).voteAverage);
	    	model.addAttribute("tf_vote_average_4", recommendedMoviesCosineTfIdf.get(3).voteAverage);
	    	model.addAttribute("tf_vote_average_5", recommendedMoviesCosineTfIdf.get(4).voteAverage);
	    	model.addAttribute("tf_vote_count_1", recommendedMoviesCosineTfIdf.get(0).voteCount);
	    	model.addAttribute("tf_vote_count_2", recommendedMoviesCosineTfIdf.get(1).voteCount);
	    	model.addAttribute("tf_vote_count_3", recommendedMoviesCosineTfIdf.get(2).voteCount);
	    	model.addAttribute("tf_vote_count_4", recommendedMoviesCosineTfIdf.get(3).voteCount);
	    	model.addAttribute("tf_vote_count_5", recommendedMoviesCosineTfIdf.get(4).voteCount);
        } catch (ResourceNotFoundException e) {
			// TODO: handle exception
        	throw new ResourceNotFoundException("Something went wrong!. Please try again!");
		}
    	
    	return "recommended-movies";
    }
}

