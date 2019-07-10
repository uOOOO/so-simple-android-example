package com.uoooo.mvvm.example.data.mapper

import com.uoooo.mvvm.example.data.entity.INVALID_ID
import com.uoooo.mvvm.example.data.entity.MovieEntity
import com.uoooo.mvvm.example.data.entity.VideoEntity
import com.uoooo.mvvm.example.domain.model.MovieModel
import com.uoooo.mvvm.example.domain.model.Video
import com.uoooo.mvvm.example.domain.model.VideoModel

fun MovieEntity.mapToModel(): MovieModel? {
    if (id == INVALID_ID) {
        return null
    }
    return try {
        MovieModel(
            voteCount,
            id,
            voteAverage,
            title!!,
            popularity,
            posterPath,
            originalLanguage,
            originalTitle,
            genreIds,
            backdropPath,
            adult,
            overview,
            releaseDate
        )
    } catch (ignored: Exception) {
        null
    }
}

fun VideoEntity.mapToModel(): VideoModel? {
    return try {
        VideoModel(
            id!!,
            key!!,
            name!!,
            Video.Site.values()[site.ordinal],
            size,
            Video.Type.values()[type.ordinal]
        )
    } catch (ignored: Exception) {
        null
    }
}

//class MovieMapper : Mapper<MovieEntity, MovieModel?> {
//    override fun mapToModel(entity: MovieEntity): MovieModel? {
//        if (entity.id == INVALID_ID || entity.title.isNullOrEmpty()) {
//            return null
//        }
//        return MovieModel(
//            entity.voteCount,
//            entity.id,
//            entity.voteAverage,
//            entity.title,
//            entity.popularity,
//            entity.posterPath,
//            entity.originalLanguage,
//            entity.originalTitle,
//            entity.genreIds,
//            entity.backdropPath,
//            entity.adult,
//            entity.overview,
//            entity.releaseDate
//        )
//    }
//}
