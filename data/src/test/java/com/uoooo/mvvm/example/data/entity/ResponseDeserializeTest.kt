package com.uoooo.mvvm.example.data.entity

import com.squareup.moshi.Moshi
import com.uoooo.mvvm.example.data.source.remote.response.MoviePopularResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class ResponseDeserializeTest {
    val moshi by lazy {
        return@lazy Moshi.Builder().build()
    }

    @Test
    fun `movie popular response deserialize`() {
        val json = "{\n" +
                "  \"page\":1,\n" +
                "  \"total_results\":421574,\n" +
                "  \"total_pages\":21079,\n" +
                "  \"results\":[\n" +
                "    {\n" +
                "      \"vote_count\":765,\n" +
                "      \"id\":320288,\n" +
                "      \"video\":false,\n" +
                "      \"vote_average\":6.2,\n" +
                "      \"title\":\"Dark Phoenix\",\n" +
                "      \"popularity\":260.597,\n" +
                "      \"poster_path\":\"\\/kZv92eTc0Gg3mKxqjjDAM73z9cy.jpg\",\n" +
                "      \"original_language\":\"en\",\n" +
                "      \"original_title\":\"Dark Phoenix\",\n" +
                "      \"genre_ids\":[\n" +
                "        878,\n" +
                "        28\n" +
                "      ],\n" +
                "      \"backdrop_path\":\"\\/phxiKFDvPeQj4AbkvJLmuZEieDU.jpg\",\n" +
                "      \"adult\":false,\n" +
                "      \"overview\":\"The X-Men face their most formidable and powerful foe when one of their own, Jean Grey, starts to spiral out of control. During a rescue mission in outer space, Jean is nearly killed when she's hit by a mysterious cosmic force. Once she returns home, this force not only makes her infinitely more powerful, but far more unstable. The X-Men must now band together to save her soul and battle aliens that want to use Grey's new abilities to rule the galaxy.\",\n" +
                "      \"release_date\":\"2019-06-05\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"vote_count\":186,\n" +
                "      \"id\":479455,\n" +
                "      \"video\":false,\n" +
                "      \"vote_average\":6.1,\n" +
                "      \"title\":\"Men in Black: International\",\n" +
                "      \"popularity\":219.211,\n" +
                "      \"poster_path\":\"\\/dPrUPFcgLfNbmDL8V69vcrTyEfb.jpg\",\n" +
                "      \"original_language\":\"en\",\n" +
                "      \"original_title\":\"Men in Black: International\",\n" +
                "      \"genre_ids\":[\n" +
                "        28,\n" +
                "        35,\n" +
                "        878,\n" +
                "        12\n" +
                "      ],\n" +
                "      \"backdrop_path\":\"\\/2FYzxgLNuNVwncilzUeCGbOQzP7.jpg\",\n" +
                "      \"adult\":false,\n" +
                "      \"overview\":\"The Men in Black have always protected the Earth from the scum of the universe. In this new adventure, they tackle their biggest, most global threat to date: a mole in the Men in Black organization.\",\n" +
                "      \"release_date\":\"2019-06-12\"\n" +
                "    },\n" +
                "    {\n" +
                "    }\n" +
                "  ]\n" +
                "}"
        val adapter = moshi.adapter(MoviePopularResponse::class.java).nullSafe()
        val response = adapter.fromJson(json)
        assertEquals(response?.page, 1)
        assertEquals(response?.results?.size, 3)
    }
}
