package com.usfzy.photogallery.retrofit

import com.usfzy.photogallery.model.FlickrResponse
import retrofit2.http.GET

private const val API_KEY = "1f8c393ae5e8dd1bf45972022d729c18"

interface FlickrApi {

    @GET(
        "services/rest/?method=flickr.interestingness.getList&api_key=$API_KEY&format=json&nojsoncallback=1&extras=url_s"
    )
    suspend fun fetchPhotos(): FlickrResponse
}