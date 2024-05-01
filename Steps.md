# Project Execution Steps

1. Before running the project, open the "XAMPP" application and run the "MySQL" server.
2. Open the "MovieRecommenderSystem" in eclipse as an existing project.
3. Before running the applilcation, make sure that ports 8080 and 3306 are free.
4. Run "App.java" as a "java application".
5. Open the below url
   [MovieRecommenderSystem](http://localhost:8080/)
6. If application failed to run, check if "MySQL" server is running and ports 8080 and 3306 are free.
7. If the application run successfully, it will open a browser window.
8. It will take 5-10 seconds to open the page for the first time or when the landing page is reloaded because in the backend it will fetch the data from files to make the predictions.
9. If the reload fails for some reason, then please reload the page or re-run the application.
10. Select any movie of your choise and hit the "Recommend" button.
11. You will be redirecetd to a page on which you can see the related movies based on the selected movie.
12. If you want to search for other movies, then please use the default back button of the browser.

# Project Outcome

Based on the recommendations, we can see that "Cosine Similarity" and "KNN with Cosine Similarity" are giving almost identicial results. The "Tf-Idf Similarity" is also recommending the similar movies but its not that accurate when compared to "Cosine Similarity" and "KNN with Cosine Similarity".

# Note

There are some dummy files and commented code in the project which we used for testing purpose. Please ignore it.
