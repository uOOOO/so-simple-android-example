package com.uoooo.simple.example.ui.paging;

import com.uoooo.simple.example.domain.interactor.MovieUseCase;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class RecommendMovieRepository_Factory implements Factory<RecommendMovieRepository> {
  private final Provider<MovieUseCase> movieUseCaseProvider;

  public RecommendMovieRepository_Factory(Provider<MovieUseCase> movieUseCaseProvider) {
    this.movieUseCaseProvider = movieUseCaseProvider;
  }

  @Override
  public RecommendMovieRepository get() {
    return newInstance(movieUseCaseProvider.get());
  }

  public static RecommendMovieRepository_Factory create(
      Provider<MovieUseCase> movieUseCaseProvider) {
    return new RecommendMovieRepository_Factory(movieUseCaseProvider);
  }

  public static RecommendMovieRepository newInstance(MovieUseCase movieUseCase) {
    return new RecommendMovieRepository(movieUseCase);
  }
}
