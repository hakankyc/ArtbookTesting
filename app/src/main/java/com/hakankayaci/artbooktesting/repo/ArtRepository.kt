package com.hakankayaci.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.hakankayaci.artbooktesting.api.RetrofitAPI
import com.hakankayaci.artbooktesting.model.ImageResponse
import com.hakankayaci.artbooktesting.roomdb.Art
import com.hakankayaci.artbooktesting.roomdb.ArtDao
import com.hakankayaci.artbooktesting.roomdb.ArtDatabase
import com.hakankayaci.artbooktesting.util.Resource
import javax.inject.Inject

class ArtRepository @Inject constructor(private val artDato: ArtDao
,private val retrofitAPI: RetrofitAPI
): ArtRepositoryInterface {
    override suspend fun insertArt(art: Art) {
        artDato.insertArt(art)
    }

    override suspend fun deleteArt(art: Art) {
        artDato.deleteArt(art)
    }

    override fun getArt(): LiveData<List<Art>> {
        return artDato.observeArts()
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {

            val response = retrofitAPI.imageSearch(imageString)
            if(response.isSuccessful){
                response.body().let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error",null)
            }else{
                Resource.error("Error",null)
            }

        }catch (e : Exception){
            Resource.error("No data!", null)
        }
    }

}