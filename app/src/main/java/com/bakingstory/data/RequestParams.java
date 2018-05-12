package com.bakingstory.data;

public interface RequestParams {
    //       &append_to_response=videos,reviews
    String API_KEY = "api_key";
    String MOVIE_CATEGORY = "category";
    String MOVIE_ID = "movie_id";
    String APPEND_TO_RESPONSE = "append_to_response";

    //        Configurations
    String IMAGES = "images";
    String CHANGE_KEYS = "change_keys";
    String IMAGE_BASE_URL = "base_url";
    String SECURE_BASE_URL = "secure_base_url";
    String BACKDROP_SIZES = "backdrop_sizes";
    String LOGO_SIZES = "logo_sizes";
    String POSTER_SIZES = "poster_sizes";
    String PROFILE_SIZES = "profile_sizes";
    String STILL_SIZES = "still_sizes";


    //        Movie Params
    String LANGUAGE = "language";
    String PAGE = "page";
    String REGION = "region";
    String RESULTS = "results";

    String POSTER_PATH = "poster_path";
    String ADULT = "adult";
    String OVERVIEW = "overview";
    String RELEASE_DATE = "release_date";
    String GENRE_IDS = "genre_ids";
    String ID = "id";
    String ORIGINAL_TITLE = "original_title";
    String ORIGINAL_LANGUAGE = "original_language";
    String TITLE = "title";
    String BACKGROP_PATH = "backdrop_path";
    String POPULARITY = "popularity";
    String VOTE_COUNT = "vote_count";
    String VIDEO = "video";
    String VOTE_AVERAGE = "vote_average";
    String TOTAL_RESULTS = "total_results";
    String TOTAL_PAGES = "total_pages";


    // Movie Videos
    String VIDEO_ID = "id";
    String VIDEO_ISO_639_1 = "iso_639_1";
    String VIDEO_ISO_3166_1 = "iso_3166_1";
    String VIDEO_KEY = "key";
    String VIDEO_SITE = "site";
    String VIDEO_SIZE = "size";
    String VIDEO_TYPE = "type";

    // Movie Review params
    String REVIEW_ID = "id";
    String REVIEW_AUTHOR = "author";
    String REVIEW_CONTENT = "content";
    String REVIEW_URL = "url";


    String VIDEO_NAME = "name";
    String REVIEWS = "reviews";
    String VIDEOS = "videos";
}
