package com.uoooo.simple.example.data.movie

import com.uoooo.simple.example.domain.movie.MovieModel
import com.uoooo.simple.example.domain.movie.Video
import com.uoooo.simple.example.domain.movie.VideoModel

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
