package br.com.aptare.cefit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.response.Response;
import br.com.aptare.cefit.security.jwt.JwtAuthenticationRequest;
import br.com.aptare.cefit.security.jwt.JwtTokenUtil;
import br.com.aptare.cefit.security.model.CurrentUser;
import br.com.aptare.cefit.seguranca.dto.UsuarioDTO;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Usuario;
import br.com.aptare.seguranca.servico.UsuarioService;

@RestController
@CrossOrigin(origins = "*")
public class LoginController
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;


    @PostMapping(value = "/api/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
    {
       try 
       {
          final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getLogin(), authenticationRequest.getSenha()));
    
          SecurityContextHolder.getContext().setAuthentication(authentication);
          final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getLogin());
          final String token = jwtTokenUtil.generateToken(userDetails);      
    
          Usuario objUsuario = new Usuario();
          objUsuario.setLogin(authenticationRequest.getLogin());
          final Usuario usuario = UsuarioService.getInstancia().get(objUsuario, null, null);
          
          if(usuario != null)
          {
             UsuarioDTO usuarioDTO = new UsuarioDTO();
             usuarioDTO.setCodigo(usuario.getCodigo());
             usuarioDTO.setLogin(usuario.getLogin());  
             usuarioDTO.setCodigoCadastroUnico(usuario.getCodigoCadastroUnico());
       
             return ResponseEntity.ok(new CurrentUser(token, usuarioDTO));
          }
          else
          {
             Response<List<Usuario>> response = new Response<List<Usuario>>();
             response.getErrors().add("Por favor, verifique seu login e senha.");
             return ResponseEntity.badRequest().body(response);
          }
       }
       catch (AuthenticationException e)
       {
          Response<List<Usuario>> response = new Response<List<Usuario>>();
          response.getErrors().add("Por favor, verifique seu login e senha.");
          return ResponseEntity.badRequest().body(response);
       }
       catch (AptareException e)
       {
          Response<List<Usuario>> response = new Response<List<Usuario>>();
          response.getErrors().add("Ocorreu um erro inesperado, favor tentar novamente.");
          return ResponseEntity.badRequest().body(response);
       }
    }

    @PostMapping(value = "/api/refresh")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request)
    {       
       try
       {
          String token = request.getHeader("Authorization");
          String login = jwtTokenUtil.getLoginFromToken(token);
   
          Usuario objUsuario = new Usuario();
          objUsuario.setLogin(login);
          final Usuario usuario = UsuarioService.getInstancia().get(objUsuario, null, null);
          
          UsuarioDTO usuarioDTO = new UsuarioDTO();
          usuarioDTO.setCodigo(usuario.getCodigo());
          usuarioDTO.setLogin(usuario.getLogin());
   
          if (jwtTokenUtil.canTokenBeRefreshed(token))
          {
             String refreshedToken = jwtTokenUtil.refreshToken(token);
             return ResponseEntity.ok(new CurrentUser(refreshedToken, usuarioDTO));
          }
          else
          {
             return ResponseEntity.badRequest().body(null);
          }
       }
       catch (AptareException e)
       {
          Response<List<Usuario>> response = new Response<List<Usuario>>();
          response.getErrors().add("Ocorreu um erro inesperado, favor tentar novamente.");
          return ResponseEntity.badRequest().body(response);
       }
    }

    @PostMapping(value = "/api/validate")
    public ResponseEntity<?> validateAuthenticationToken(HttpServletRequest request, @RequestBody String dsLogin)
    {

        String token = request.getHeader("Authorization");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(dsLogin);

        if (jwtTokenUtil.validateToken(token, userDetails))
        {
            return ResponseEntity.ok("Token Valido!");
        }
        else
        {
            return ResponseEntity.badRequest().body(null);
        }

    }
}