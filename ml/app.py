from flask import Flask, request, jsonify
from recommend import recommend_posts

app = Flask(__name__)


@app.route("/recommend/<long:user_id>", methods=['GET'])
def recommend(user_id):
    recommendations = recommend_posts(user_id)
    return jsonify({"recommended_posts": recommendations})


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001)
