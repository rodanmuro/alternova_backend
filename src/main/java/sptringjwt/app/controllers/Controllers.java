package sptringjwt.app.controllers;

import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import sptringjwt.app.models.AuthenticationRequest;
import sptringjwt.app.models.AuthenticationResponse;
import sptringjwt.app.models.Joke;
import sptringjwt.app.models.Usuario;
import sptringjwt.app.repositorios.JokeRepository;
import sptringjwt.app.repositorios.UsuarioRepository;
import sptringjwt.app.services.MyUserDetailsService;
import sptringjwt.app.utils.JwtUtil;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class Controllers {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    JokeRepository jokeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {
        return "Hello ";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));

            final UserDetails userDetails = myUserDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername());

            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }

    @PostMapping("/registrarse")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) throws Exception {
        try {
            Usuario _usuario = new Usuario(usuario.getUsername(), passwordEncoder.encode(usuario.getPassword()));
            usuarioRepository.save(_usuario);

            final UserDetails userDetails = myUserDetailsService.loadUserByUsername(usuario.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthenticationResponse(jwt));

        } catch (Exception e) {
            throw new Exception("El usuario no pudo ser registrado", e);
        }
    }

    @PostMapping("/joke")
    public ResponseEntity<?> saveJoke(@RequestBody Joke joke, Principal principal) throws Exception {

        try {
            // Joke _joke = new Joke(joke.getUsuario(), joke.getTexto());
            Joke _joke = new Joke(principal.getName(), joke.getTexto());
            jokeRepository.save(_joke);
            return ResponseEntity.ok(_joke);
        } catch (Exception e) {
            throw new Exception("El usuario no pudo ser registrado", e);
        }
    }

    @GetMapping("/joke")
    public ResponseEntity<?> getJokes(Principal principal) {

        try {
            String usuario = principal.getName();

            List<Joke> listaJokes = jokeRepository.findByUsuario(usuario);

            if (listaJokes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(listaJokes, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/joke/{id}")
    public ResponseEntity<?> deleteJoke(@PathVariable("id") long id){
        try {
            jokeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
