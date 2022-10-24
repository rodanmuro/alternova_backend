package sptringjwt.app.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sptringjwt.app.models.Usuario;


public interface UsuarioRepository extends  JpaRepository<Usuario, Long>{
    List<Usuario> findByUsername(String username);
}
