package ec.project;

import java.util.*;
import java.util.concurrent.Callable;
import java.nio.charset.StandardCharsets;

public class CountVectorizer {
    private String input = "content";
    private String encoding = "utf-8";
    private String decode_error = "strict";
    private String strip_accents = null;
    private boolean lowercase = true;
    private Callable<String> preprocessor = null;
    private List<Movie> movies = null;
    private Callable<String> tokenizer = null;
    private List<String> stop_words = null;
    private String token_pattern = "(?u)\\b\\w\\w+\\b";
    private int[] ngram_range = {1, 1};
    private String analyzer = "word";
    private double max_df = 1.0;
    private int min_df = 1;
    private Integer max_features = null;
    private Map<String, Integer> vocabulary = null;
    private boolean binary = false;
    private String dtype = "np.int64";

    public CountVectorizer() {}
    
    public CountVectorizer(List<Movie> movies) {
    	this.movies = movies;
    }

    public CountVectorizer(String input, String encoding, String decode_error, String strip_accents,
                            boolean lowercase, Callable<String> preprocessor, Callable<String> tokenizer,
                            List<String> stop_words, String token_pattern, int[] ngram_range, String analyzer,
                            double max_df, int min_df, Integer max_features, Map<String, Integer> vocabulary,
                            boolean binary, String dtype) {
        this.input = input;
        this.encoding = encoding;
        this.decode_error = decode_error;
        this.strip_accents = strip_accents;
        this.lowercase = lowercase;
        this.preprocessor = preprocessor;
        this.tokenizer = tokenizer;
        this.stop_words = stop_words;
        this.token_pattern = token_pattern;
        this.ngram_range = ngram_range;
        this.analyzer = analyzer;
        this.max_df = max_df;
        this.min_df = min_df;
        this.max_features = max_features;
        this.vocabulary = vocabulary;
        this.binary = binary;
        this.dtype = dtype;
    }
    
    public Map<String, Integer> fit(List<String> raw_documents) {
        Map<String, Integer> vocabulary = new HashMap<>();
        
        for (String doc : raw_documents) {
            String[] tokens = analyze(doc);
            
            for (String token : tokens) {
                vocabulary.putIfAbsent(token, vocabulary.size());
            }
        }
        
        return vocabulary;
    }
    
    public double[][] transform() {
    	double[][] transformedData = new double[movies.size()][movies.size()];
        
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            List<String> tags = movie.tags;
            double[] vector = new double[tags.size()];
            
            for (int j = 0; j < tags.size(); j++) {
                String tag = tags.get(j);
                double count = 0.0;
                
                for (String movieTag : tags) {
                    if (movieTag.equals(tag)) {
                        count++;
                    }
                }
                
                transformedData[i][j] = count;
            }
        }
        
        return transformedData;
    }
    
    private String[] analyze(String document) {
        document = document.replaceAll("[^\\w\\s']", "");
        
        return document.split("\\s+");
    }
    
    public double[][] fit_transform() {
        buildVocabulary();
     
        return transform();
    }

    private void buildVocabulary() {
        vocabulary = new HashMap<>();
        int index = 0;
        
        for (Movie movie : movies) {
            for (String tag : movie.tags) {
                vocabulary.putIfAbsent(tag, index++);
            }
        }
    }
}
