

import mysql.connector
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

db = mysql.connector.connect(
    host="localhost", user="dbUser", passwd="dbPassword", database="social_network"
)
cursor = db.cursor()

cursor.execute("SELECT user_id,post_id FROM likes")
likes = cursor.fetchall()

users = list(set([like[0] for like in likes]))
posts = list(set([like[1] for like in likes]))

if not users or not posts:
    print("No users or posts found. Returning empty recommendations.")
    def recommend_posts(user_id):
        return []

matrix = np.zeros((len(users), len(posts)))

for user_id, post_id in likes:
    matrix[users.index(user_id)][posts.index(post_id)] = 1

similarity = cosine_similarity(matrix.T)


def recommend_posts(user_id):
    if user_id not in users:
        return []

    user_index=users.index(user_id)
    user_likes = matrix[user_index]
    scores=similarity.dot(user_likes)
    recommended_posts=np.argsort(scores)[::-1][:5]
    return [posts[i] for i in recommended_posts]
