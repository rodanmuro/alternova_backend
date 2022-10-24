package sptringjwt.app.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sptringjwt.app.models.Joke;


public interface JokeRepository extends JpaRepository<Joke,Long> {
    List<Joke> findByUsuario(String usuario);    
    void deleteById(Long id);
}
