<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UFT-8">
		<title>Recommended Movies</title>
		<style>
			body {
			    font-family: 'Arial', sans-serif;
            	background-color: #f4f4f4;
			    margin: 0;
			    padding: 20px;
			}
			.container {
			    max-width: 800px;
			    margin: auto;
			    background: #fff;
			    padding: 20px;
			    border-radius: 8px;
			    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
			}
			h1, h2 {
			    color: #333;
			}
			table {
			    width: 100%;
			    border-collapse: collapse;
			    margin-top: 20px;
			}
			th, td {
			    border: 1px solid #ddd;
			    text-align: left;
			    padding: 8px;
			}
			th {
			    background-color: #f2f2f2;
			}
			tr:nth-child(even) {
			    background-color: #f9f9f9;
			}
			img {
			    width: 150px;
			    height: 150px;
			    object-fit: cover;
			}
		</style>
	</head>
<body>
    <div class="container">
		<center>
			<h1>Recommended Movies Based on <b>"${selectedMovie}"</b></h1>
			<hr>
			<h2>Recommended Movies Based on <b>"Cosine Similarity"</b></h1>
			<br/>
			<table border="10">
				<thead>
					<tr>
						<td>Id</td>
						<td>Title</td>
						<td>Image</td>
						<td>Popularity</td>
						<td>Run Time</td>
						<td>Vote Average</td>
						<td>Vote Count</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>1</td>
						<td>${cs_title_1}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${cs_image_1}" alt="No Poster Available"/></td>
						<td>${cs_popularity_1}</td>
						<td>${cs_runtime_1}</td>
						<td>${cs_vote_average_1}</td>
						<td>${cs_vote_count_1}</td>
					</tr>
					<tr>
						<td>2</td>
						<td>${cs_title_2}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${cs_image_2}" alt="No Poster Available"/></td>
						<td>${cs_popularity_2}</td>
						<td>${cs_runtime_2}</td>
						<td>${cs_vote_average_2}</td>
						<td>${cs_vote_count_2}</td>
					</tr>
					<tr>
						<td>3</td>
						<td>${cs_title_3}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${cs_image_3}" alt="No Poster Available"/></td>
						<td>${cs_popularity_3}</td>
						<td>${cs_runtime_3}</td>
						<td>${cs_vote_average_3}</td>
						<td>${cs_vote_count_3}</td>
					</tr>
					<tr>
						<td>4</td>
						<td>${cs_title_4}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${cs_image_4}" alt="No Poster Available"/></td>
						<td>${cs_popularity_4}</td>
						<td>${cs_runtime_4}</td>
						<td>${cs_vote_average_4}</td>
						<td>${cs_vote_count_4}</td>
					</tr>
					<tr>
						<td>5</td>
						<td>${cs_title_5}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${cs_image_5}" alt="No Poster Available"/></td>
						<td>${cs_popularity_5}</td>
						<td>${cs_runtime_5}</td>
						<td>${cs_vote_average_5}</td>
						<td>${cs_vote_count_5}</td>
					</tr>
				</tbody>
			</table>
			<br />
			<hr>
			<h2>Recommended Movies Based on <b>"Cosine Similarity & KNN"</b></h1>
			<br/>
			<table border="10">
				<thead>
					<tr>
						<td>Id</td>
						<td>Title</td>
						<td>Image</td>
						<td>Popularity</td>
						<td>Run Time</td>
						<td>Vote Average</td>
						<td>Vote Count</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>1</td>
						<td>${cs_knn_title_1}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${cs_knn_image_1}" alt="No Poster Available"/></td>
						<td>${cs_knn_popularity_1}</td>
						<td>${cs_knn_runtime_1}</td>
						<td>${cs_knn_vote_average_1}</td>
						<td>${cs_knn_vote_count_1}</td>
					</tr>
					<tr>
						<td>2</td>
						<td>${cs_knn_title_2}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${cs_knn_image_2}" alt="No Poster Available"/></td>
						<td>${cs_knn_popularity_2}</td>
						<td>${cs_knn_runtime_2}</td>
						<td>${cs_knn_vote_average_2}</td>
						<td>${cs_knn_vote_count_2}</td>
					</tr>
					<tr>
						<td>3</td>
						<td>${cs_knn_title_3}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${cs_knn_image_3}" alt="No Poster Available"/></td>
						<td>${cs_knn_popularity_3}</td>
						<td>${cs_knn_runtime_3}</td>
						<td>${cs_knn_vote_average_3}</td>
						<td>${cs_knn_vote_count_3}</td>
					</tr>
					<tr>
						<td>4</td>
						<td>${cs_knn_title_4}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${cs_knn_image_4}" alt="No Poster Available"/></td>
						<td>${cs_knn_popularity_4}</td>
						<td>${cs_knn_runtime_4}</td>
						<td>${cs_knn_vote_average_4}</td>
						<td>${cs_knn_vote_count_4}</td>
					</tr>
					<tr>
						<td>5</td>
						<td>${cs_knn_title_5}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${cs_knn_image_5}" alt="No Poster Available"/></td>
						<td>${cs_knn_popularity_5}</td>
						<td>${cs_knn_runtime_5}</td>
						<td>${cs_knn_vote_average_5}</td>
						<td>${cs_knn_vote_count_5}</td>
					</tr>
				</tbody>
			</table>
			<br />
			<hr/>
			<h2>Recommended Movies Based on <b>"Tf-Idf Similarity"</b></h1>
			<br/>
			<table border="10">
				<thead>
					<tr>
						<td>Id</td>
						<td>Title</td>
						<td>Image</td>
						<td>Popularity</td>
						<td>Run Time</td>
						<td>Vote Average</td>
						<td>Vote Count</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>1</td>
						<td>${tf_title_1}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${tf_image_1}" alt="No Poster Available"/></td>
						<td>${tf_popularity_1}</td>
						<td>${tf_runtime_1}</td>
						<td>${tf_vote_average_1}</td>
						<td>${tf_vote_count_1}</td>
					</tr>
					<tr>
						<td>2</td>
						<td>${tf_title_2}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${tf_image_2}" alt="No Poster Available"/></td>
						<td>${tf_popularity_2}</td>
						<td>${tf_runtime_2}</td>
						<td>${tf_vote_average_2}</td>
						<td>${tf_vote_count_2}</td>
					</tr>
					<tr>
						<td>3</td>
						<td>${tf_title_3}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${tf_image_3}" alt="No Poster Available"/></td>
						<td>${tf_popularity_3}</td>
						<td>${tf_runtime_3}</td>
						<td>${tf_vote_average_3}</td>
						<td>${tf_vote_count_3}</td>
					</tr>
					<tr>
						<td>4</td>
						<td>${tf_title_4}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${tf_image_4}" alt="No Poster Available"/></td>
						<td>${tf_popularity_4}</td>
						<td>${tf_runtime_4}</td>
						<td>${tf_vote_average_4}</td>
						<td>${tf_vote_count_4}</td>
					</tr>
					<tr>
						<td>5</td>
						<td>${tf_title_5}</td>
						<td><img src="https://image.tmdb.org/t/p/w500${tf_image_5}" alt="No Poster Available"/></td>
						<td>${tf_popularity_5}</td>
						<td>${tf_runtime_5}</td>
						<td>${tf_vote_average_5}</td>
						<td>${tf_vote_count_5}</td>
					</tr>
				</tbody>
			</table>
		</center>
	</div>
</body>
</html>