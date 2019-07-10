package com.uoooo.mvvm.example.data.entity

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.uoooo.mvvm.example.data.source.remote.response.MoviePopularResponse
import com.uoooo.mvvm.example.data.source.remote.response.VideosResponse
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

    @Test
    fun `videos response deserialize`() {
        val json = "{\n" +
                "   \"id\":301528,\n" +
                "   \"results\":[\n" +
                "      {\n" +
                "         \"id\":\"5c6d1de192514120d5033790\",\n" +
                "         \"iso_639_1\":\"en\",\n" +
                "         \"iso_3166_1\":\"US\",\n" +
                "         \"key\":\"LDXYRzerjzU\",\n" +
                "         \"name\":\"Toy Story 4 | Official Teaser Trailer\",\n" +
                "         \"site\":\"YouTube1\",\n" +
                "         \"size\":1080,\n" +
                "         \"type\":\"Teaser\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"id\":\"5d0b262c92514171b0b85c16\",\n" +
                "         \"iso_639_1\":\"en\",\n" +
                "         \"iso_3166_1\":\"US\",\n" +
                "         \"key\":\"Pl9JS8-gnWQ\",\n" +
                "         \"name\":\"Toy Story 4 | Official Trailer 2\",\n" +
                "         \"site\":\"YouTube\",\n" +
                "         \"size\":1080,\n" +
                "         \"type\":\"Trailer2\"\n" +
                "      }\n" +
                "   ]\n" +
                "}"
        val adapter = moshi.newBuilder()
            .add(
                Video.Type::class.java,
                EnumJsonAdapter.create(Video.Type::class.java).withUnknownFallback(Video.Type.UNKNOWN)
            )
            .add(
                Video.Site::class.java,
                EnumJsonAdapter.create(Video.Site::class.java).withUnknownFallback(Video.Site.UNKNOWN)
            )
            .build().adapter(VideosResponse::class.java).nullSafe()
        val response = adapter.fromJson(json)
        assertEquals(response?.results?.size, 2)
    }
}
