package br.com.aptare.cefit.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.aptare.cefit.seguranca.dto.UsuarioDTO;

public class JwtUserFactory
{

   private JwtUserFactory()
   {
   }

   public static JwtUser create(UsuarioDTO usuario)
   {
      return new JwtUser(usuario.getCodigo(), usuario.getLogin(), usuario.getSenha(), mapToGranteAuthorities("USER"));
   }

   private static List<GrantedAuthority> mapToGranteAuthorities(String grupo)
   {

      List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
      authorities.add(new SimpleGrantedAuthority(grupo));
      return authorities;

   }

}