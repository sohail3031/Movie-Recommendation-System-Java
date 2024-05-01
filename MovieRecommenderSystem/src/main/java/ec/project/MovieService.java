package ec.project;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

//import org.tartarus.snowball.ext.PorterStemmer;
//import org.tartarus.snowball.ext.porterStemmer;
//import org.tartarus.snowball.ext.porterStemmer;
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.net.URI;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MovieService {
    private List<String> movieTitles = new ArrayList<String>();
    private List<Integer> recommendedMoviesTitlesIndex = new ArrayList<Integer>();
    private List<Movie> moviesList = new ArrayList<Movie>();
    private double[][] similarityArray;
    private double[][] tfidfArray;

	public double[][] getTfidfArray() {
		return tfidfArray;
	}

	public void setTfidfArray(double[][] tfidfArray) {
		this.tfidfArray = tfidfArray;
	}

	public List<String> readMovieTitles() {
        
        if (movieTitles.size() == 0) {
	        BufferedReader reader = null;
	        String filePath = "src/main/java/ec/project/dataset/titles.txt";
	
	        try {
	            reader = new BufferedReader(new FileReader(filePath));
	            String line;
	            
	            while ((line = reader.readLine()) != null) {
	                movieTitles.add(line);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
        
        return movieTitles;
    }
	
//	public double[][] getTfIdfFromFile(List<Movie> movies) throws FileNotFoundException {
//		double[][] tfidf = new double[movies.size()][movies.size()];
//		
////		if (tfidf[0][0] == 0.0) {
//	    	Scanner sc = new Scanner(new BufferedReader(new FileReader("src/main/java/ec/project/dataset/tfidf_vectors.txt")));
//	    	
//	    	try {
//	    		while (sc.hasNextLine()) {
//	                for (int i = 0; i < tfidf.length; i++) {
//	                    String[] line = sc.nextLine().trim().split(" ");
//	                    
//	                    for (int j = 0; j < line.length; j++) {
//	                    	tfidf[i][j] = Double.parseDouble(line[j]);
//	                    	
//	                    	System.out.println("Similarity: " + tfidf[i][j]);
//	                    }
//	                }
//	            }
//	    	} catch (Exception e) {
//	    		e.printStackTrace();
//	    	}
////    	}
//		
//		return tfidf;
//	}
	
	public double[][] getTfIdfFromFile(List<Movie> movies) throws FileNotFoundException {
	    double[][] tfidf = new double[movies.size()][movies.size()];
	    Scanner sc = new Scanner(new BufferedReader(new FileReader("src/main/java/ec/project/dataset/tfidf_vectors.txt")));
	    sc.useDelimiter(",|\\s"); // Set the scanner delimiter to comma and whitespace

	    try {
	        for (int i = 0; sc.hasNextLine() && i < tfidf.length; i++) {
	            String[] line = sc.nextLine().trim().split(",\\s*"); // Split by comma and optional whitespace
	            
	            for (int j = 0; j < line.length; j++) {
	                tfidf[i][j] = Double.parseDouble(line[j]);
	            }
	        }
	    } catch (Exception e) {
//	        e.printStackTrace();
	    } finally {
	        sc.close(); // Close the scanner to prevent resource leaks
	    }

	    return tfidf;
	}

	
	public double[][] getSimilarityFromFile(List<Movie> movies) throws FileNotFoundException {
    	double[][] similarities = new double[movies.size()][movies.size()];
    	
    	if (similarities[0][0] == 0.0) {
	    	Scanner sc = new Scanner(new BufferedReader(new FileReader("src/main/java/ec/project/dataset/cosine_similarity.txt")));
	    	
	    	try {
	    		while (sc.hasNextLine()) {
	                for (int i = 0; i < similarities.length; i++) {
	                    String[] line = sc.nextLine().trim().split(" ");
	                    
	                    for (int j = 0; j < line.length; j++) {
	                    	similarities[i][j] = Double.parseDouble(line[j]);
	                    	
//	                    	System.out.println("Similarity: " + similarities[i][j]);
	                    }
	                }
	            }
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
    	}
    	
    	return similarities;
	}
	
//	public double[][] getSimilarityFromFile(List<Movie> movies) throws FileNotFoundException {
//    	double[][] similarities = new double[movies.size()][movies.size()];
//    	Scanner sc = new Scanner(new BufferedReader(new FileReader("C:/Masters/Enterprise Computing/workspace/Project/src/main/java/ec/project/dataset/cosine_similarity.txt")));
//    	
//    	try {
//    		while (sc.hasNextLine()) {
//                for (int i = 0; i < similarities.length; i++) {
//                    String[] line = sc.nextLine().trim().split(" ");
//                    
//                    for (int j = 0; j < line.length; j++) {
//                    	similarities[i][j] = Double.parseDouble(line[j]);
//                    }
//                }
//            }
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
//    	
//    	return similarities;
//    }
	
	public List<Movie> loadMovies() throws IOException {
        List<String> titles = new ArrayList<String>();
        List<Movie> movies = new ArrayList<Movie>();
        
        if (movies.size() == 0) {
	        FileReader creditsReader = new FileReader("src/main/java/ec/project/dataset/tmdb_5000_credits.csv");
	        Iterable<CSVRecord> creditsRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(creditsReader);
	        
	        for (CSVRecord record : creditsRecords) {
	    		Movie movie = new Movie();
	    		
	    		if(!record.get("movie_id").isEmpty() && !record.get("title").isEmpty() && !record.get("cast").isEmpty() && !record.get("crew").isEmpty()) {
	    			movie.movieId = Integer.parseInt(record.get("movie_id"));
	                movie.title = record.get("title");
	                titles.add(movie.title);
	                movie.cast = convertCastDataFormat(Arrays.asList(record.get("cast").split("},")));
	                movie.crew = convertCrewDataFormat(Arrays.asList(record.get("crew").split("},")));
	            
	                movies.add(movie);
	    		}
	        }
	        
	        storeTitlesInFile(titles);
	
	        FileReader moviesReader = new FileReader("src/main/java/ec/project/dataset/tmdb_5000_movies.csv");
	        Iterable<CSVRecord> moviesRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(moviesReader);
	
	        for (CSVRecord record : moviesRecords) {
	            int movieId = Integer.parseInt(record.get("id"));
	            Movie movie = null;
	            
	            for (Movie m : movies) {
	                if (m.movieId == movieId) {
	                    movie = m;
	               
	                    break;
	                }
	            }
	            
	            if (movie != null && isValidRecord(record)) {
	                movie.overview = Arrays.asList(record.get("overview").split(" "));
	                movie.genres = convertOtherDataFormat(Arrays.asList(record.get("genres").split("},")));
	                movie.keywords = convertOtherDataFormat(Arrays.asList(record.get("keywords").split("},")));
	                movie.voteAverage = Double.parseDouble(record.get("vote_average"));
	                movie.voteCount = Integer.parseInt(record.get("vote_count"));
	                movie.budget = Double.parseDouble(record.get("budget"));
	                movie.originalLanguage = record.get("original_language");
	                movie.releaseDate = record.get("release_date");
	                movie.revenue = Double.parseDouble(record.get("revenue"));
	            	movie.runtime = (int) Double.parseDouble(record.get("runtime"));
	                movie.status = record.get("status");
	                movie.productionCompanies = convertOtherDataFormat(Arrays.asList(record.get("production_companies").split("},")));
	                movie.productionCountries = convertOtherDataFormat(Arrays.asList(record.get("production_countries").split("},")));
	                movie.popularity = Double.parseDouble(record.get("popularity"));
	            }
	        } 
        }

        return movies;
    }
	
	public void trainModels(List<Movie> movies) throws IOException {
		preprocessMovies(movies);
//        applyPorterStemmer(movies);
        
//        CountVectorizer countVectorizer = new CountVectorizer(movies);
//        countVectorizer.fit(movies);
//      double[][] transformedData = countVectorizer.transform();
//      double[][] transformedData1 = countVectorizer.fit_transform();
        
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
//      double[][] transformData2 = cosineSimilarity.calculateCosineSimilarity(transformedData);
      double[][] transformData1 = cosineSimilarity.calculateCosineSimilarity(movies);
      
      storeCosineSimilarityInFile(transformData1);
      
      double[][] similarities = getSimilarityFromFile(movies);
      int selectedMovieIndex = getSelectedMovieIndex(movies, "Avatar");
    
      KNN knn = new KNN();
    
      List<Integer> temp1 = knn.findNearestNeighbors(similarities, 5, selectedMovieIndex);

      knn.makePredictions(temp1, movies); 
	}
	
	public int getSelectedMovieIndex(List<Movie> movies, String title) {
    	int movieIndex = -1;
    	
    	for (int i = 0; i < movies.size(); i++) {
			if (movies.get(i).title.equalsIgnoreCase(title)) {
				movieIndex = i;
				
				break;
			}
		}
    	
    	return movieIndex;
    }
	
//	public int[] getRecommendedMoviesIndex(String[] recommendedMovies, List<Movie> movies) {
//		int[] recommendedMoviesIndex = new int[5];
//		int count = 0;
//		
//		for (int i = 0; i < movies.size(); i++) {
//			if (movies.get(i).title.equalsIgnoreCase(title)) {
//				
//			}
//		}
//		
//		return recommendedMoviesIndex;
//	}
	
	public List<String> convertCastDataFormat(List<String> dataList) {
        List<String> result = new ArrayList<String>();

        try {
            for (String jsonData : dataList) {
                String[] temp = jsonData.split("\"name\": \"");
                StringBuilder stringBuilder = new StringBuilder();
                
                inner: for (int i = 0; i < temp[1].length(); i++) {
                    char currentChar = temp[1].charAt(i);
                    
                    if (currentChar == ',') {
                    	break inner;
                    }
                    
                    if (currentChar != '}' && currentChar != ']' && currentChar != '"' && currentChar != '.') {
                        stringBuilder.append(currentChar);
                    }
                }

                String trimmedString = stringBuilder.toString().trim().toLowerCase();
                
                if (!trimmedString.isEmpty()) {
                    result.add(trimmedString);
                }
            }
        } catch (Exception e) {
            // Handle exceptions if needed
        }

        return result;
    }
	
	public List<String> convertCrewDataFormat(List<String> crewData) {
    	List<String> result = new ArrayList<String>();

        try {
            for (String jsonData : crewData) {
                String[] temp = jsonData.split("\"name\": \"");
                StringBuilder stringBuilder = new StringBuilder();
                
                inner: for (int i = 0; i < temp[1].length(); i++) {
                    char currentChar = temp[1].charAt(i);
                    
                    if (currentChar == '"') {
                    	break inner;
                    }
                    
                    if (currentChar != '}' && currentChar != ']' && currentChar != '"' && currentChar != '.') {
                        stringBuilder.append(currentChar);
                    }
                }

                String trimmedString = stringBuilder.toString().trim().toLowerCase();
                
                if (!trimmedString.isEmpty()) {
                    result.add(trimmedString);
                }
            }
        } catch (Exception e) {
            // Handle exceptions if needed
        }

        return result;
    }
	
	public void storeTitlesInFile(List<String> titles) {
    	try {
    		Files.write(Paths.get("src/main/java/ec/project/dataset/titles.txt"), titles);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
	
	public boolean isValidRecord(CSVRecord record) {
        return !record.get("overview").isEmpty() && !record.get("genres").isEmpty() && !record.get("keywords").isEmpty() && !record.get("vote_average").isEmpty() && !record.get("vote_count").isEmpty() && !record.get("budget").isEmpty() && !record.get("original_language").isEmpty() && !record.get("release_date").isEmpty() && !record.get("revenue").isEmpty() && !record.get("runtime").isEmpty() && !record.get("status").isEmpty() && !record.get("production_companies").isEmpty() && !record.get("production_countries").isEmpty() && !record.get("popularity").isEmpty();
    }
	
	public List<String> convertOtherDataFormat(List<String> crewData) {
    	List<String> result = new ArrayList<String>();

        try {
            for (String jsonData : crewData) {
                String[] temp = jsonData.split("\"name\": \"");
                StringBuilder stringBuilder = new StringBuilder();
                
                inner: for (int i = 0; i < temp[1].length(); i++) {
                    char currentChar = temp[1].charAt(i);
                    
                    if (currentChar == '"') {
                    	break inner;
                    }
                    
                    if (currentChar != '}' && currentChar != ']' && currentChar != '"' && currentChar != '.') {
                        stringBuilder.append(currentChar);
                    }
                }

                String trimmedString = stringBuilder.toString().trim().toLowerCase();
                
                if (!trimmedString.isEmpty()) {
                    result.add(trimmedString);
                }
            }
        } catch (Exception e) {
            // Handle exceptions if needed
        }

        return result;
    }
	
	public void preprocessMovies(List<Movie> movies) {
        for (Movie movie : movies) {
        	try {
                movie.tags = new ArrayList<>();
                
                movie.tags.addAll(movie.overview);
                movie.tags.addAll(movie.genres);
                movie.tags.addAll(movie.keywords);
                movie.tags.addAll(movie.cast);
                movie.tags.addAll(movie.crew);
			} catch (Exception e) {
//				System.out.println(movie.title);
			}
        }
    }
	
//	public void applyPorterStemmer(List<Movie> movies) throws IOException {
//		PorterStemmer porterStemmer = new PorterStemmer();
//    	
//    	for (int j = 0; j < movies.size(); j++) {
//        	StringBuilder stringBuilder = new StringBuilder();
//        	StringBuilder temp = new StringBuilder();
//        	
//    		for (int i = 0; i < movies.get(j).tags.size(); i++) {
//    			stringBuilder.append(movies.get(j).tags.get(i) + " ");
//    		}
//    		
//    		Analyzer analyzer = new StandardAnalyzer();
//    		
//    		try (StringReader reader = new StringReader(stringBuilder.toString())) {
//                org.apache.lucene.analysis.TokenStream stream = analyzer.tokenStream(null, reader);
//                CharTermAttribute termAttribute = stream.addAttribute(CharTermAttribute.class);
//
//                stream.reset();
//                
//                while (stream.incrementToken()) {
//                    String term = termAttribute.toString();
//                    temp.append(term + " ");
//                }
//                
//                stream.end();
//            }
//
//            analyzer.close();
//    		
//    		porterStemmer.setCurrent(temp.toString());
//    		porterStemmer.stem();
//    		
//    		movies.get(j).tags = Arrays.asList(porterStemmer.getCurrent().split(" "));
//    	}
//    }
	
	public String fetchPoster(int movieId) throws IOException {
		String api_key = "8265bd1679663a7ea12ac168da84d2e8";
		String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + api_key + "&language=en-US";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
		}
		in.close();

		ObjectMapper mapper = new ObjectMapper();
		String posterPath = mapper.readTree(response.toString()).get("poster_path").asText();
		
		return posterPath;
	}
	
	public void storeCosineSimilarityInFile(double[][] transformedData1) {
	   	 String fileName = "src/main/java/ec/project/dataset/cosine_similarity.txt";
	
	     try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
	         for (double[] row : transformedData1) {
	             String line = Arrays.toString(row).replaceAll("[\\[\\],]", "");
	             
	             writer.write(line);
	             writer.newLine();
	         }
	     } catch (IOException e) {
	         System.err.println("Error writing to file: " + e.getMessage());
	     }
	
	     try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
	         String line;
	         int row = 0;
	         
	         while ((line = reader.readLine()) != null) {
	             String[] values = line.split("\\s+"); // Split by spaces
	             
	             for (int col = 0; col < values.length; col++) {
	            	 transformedData1[row][col] = Integer.parseInt(values[col]);
	             }
	             
	             row++;
	         }
	     } catch (IOException e) {
	         System.err.println("Error reading from file: " + e.getMessage());
	     }
	}
	
	public String[] recommendedMoviesCosine(List<Movie> movies, String title, double[][] similarities) {
    	String[] recommendedMovies = new String[5];
    	int movieIndex = -1;
    	double[] distances = null;
    	List<MovieDistance> movieDistances = new ArrayList<>();
    	
    	try {
    		for (int i = 0; i < movies.size(); i++) {
    			if (movies.get(i).title.equalsIgnoreCase(title)) {
    				movieIndex = i;
    				distances = similarities[i];
    				
    				break;
    			}
    		}
    		
    		for (int i = 0; i < distances.length; i++) {
                movieDistances.add(new MovieDistance(i, distances[i]));
            }
    		
    		Collections.sort(movieDistances, new DistanceComparator());

            // Get the top 5 movies (excluding the first element)
            List<MovieDistance> topMovies = movieDistances.subList(1, 6);
            
            for (int i = 0; i < topMovies.size(); i++) {
            	recommendedMovies[i] = movies.get(topMovies.get(i).getIndex()).title;
            }

            // Print the top 5 movie indices and distances (for testing)
            for (MovieDistance movie : topMovies) {
                System.out.println("Movie Index: " + movie.getIndex() + ", Distance: " + movie.getDistance() + ", Title: " + movies.get(movie.getIndex()).title);
                recommendedMoviesTitlesIndex.add(movie.getIndex());
            }
    	} catch (Exception e) {
			// TODO: handle exception
    		return recommendedMovies;
		}
    	
    	return recommendedMovies;
    }
	
	public int getMovieNameForKNNRecommendation(String name, List<Movie> movies) {
		int index = -1;
		
		for (int i = 0; i < movies.size(); i++) {
			if (movies.get(i).title.equalsIgnoreCase(name)) {
				index = i;
				
				break;
			}
		}
		
		return index;
	}
	
	public int[] getRecommendedMoviesIndexCosine(String[] relatedMoviesName, List<Movie> movies) {
		int[] indexes = new int[5];
		int count = 0;
		
		try {
			for (int i = 0; i < movies.size(); i++) {
				if (count == 4) {
					break;
				}
				
				for (int j = 0; j < relatedMoviesName.length; j++) {
					if (count == 4) {
						break;
					}
					
					if (relatedMoviesName[j].equalsIgnoreCase(movies.get(i).title)) {
						indexes[j] = i;
						
						count++;
					}
				}
			}
		} catch (ResourceNotFoundException e) {
			// TODO: handle exception
			throw new ResourceNotFoundException("Something went wrong! Try again or search for other movie.");
		}
		
		return indexes;
	}
	
	public List<Integer> temp() {
		return recommendedMoviesTitlesIndex;
	}
	
	public void initial() throws IOException {
		readMovieTitles();

		moviesList = loadMovies();
		similarityArray = getSimilarityFromFile(moviesList);
		tfidfArray = getTfIdfFromFile(moviesList);
//		System.out.println("Called");
	}

	public double[][] getSimilarityArray() {
		return similarityArray;
	}

	public void setSimilarityArray(double[][] similarityArray) {
		this.similarityArray = similarityArray;
	}

	public List<String> getMovieTitles() {
		return movieTitles;
	}

	public void setMovieTitles(List<String> movieTitles) {
		this.movieTitles = movieTitles;
	}

	public List<Movie> getMoviesList() {
		return moviesList;
	}

	public void setMoviesList(List<Movie> moviesList) {
		this.moviesList = moviesList;
	}

	public List<Integer> getRecommendedMoviesTitlesIndex() {
		return recommendedMoviesTitlesIndex;
	}

	public void setRecommendedMoviesTitlesIndex(List<Integer> recommendedMoviesTitles) {
		this.recommendedMoviesTitlesIndex = recommendedMoviesTitles;
	}
}

