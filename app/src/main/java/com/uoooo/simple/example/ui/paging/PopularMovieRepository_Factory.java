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
public final class PopularMovieRepository_Factory implements Factory<PopularMovieRepository> {
  private final Provider<MovieUseCase> movieUseCaseProvider;

  public PopularMovieRepository_Factory(Provider<MovieUseCase> movieUseCaseProvider) {
    this.movieUseCaseProvider = movieUseCaseProvider;
  }

  @Override
  public PopularMovieRepository get() {
    return newInstance(movieUseCaseProvider.get());
  }

  public static PopularMovieRepository_Factory create(Provider<MovieUseCase> movieUseCaseProvider) {
    return new PopularMovieRepository_Factory(movieUseCaseProvider);
  }

  public static PopularMovieRepository newInstance(MovieUseCase movieUseCase) {
    return new PopularMovieRepository(movieUseCase);
  }
}
