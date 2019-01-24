package br.com.aptare.cefit.security.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.aptare.cefit.security.jwt.JwtUserFactory;
import br.com.aptare.cefit.seguranca.dto.UsuarioDTO;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Usuario;
import br.com.aptare.seguranca.servico.UsuarioService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService
{

   @Override
   public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException
   {

      try
      {
         Usuario usuario = new Usuario();
         usuario.setLogin(login);

         usuario = UsuarioService.getInstancia().get(usuario, null, null);
         
         if (usuario == null)
         {
            throw new UsernameNotFoundException(String.format("Nenhum usuário encontrado com login: ", login));
         }
         else
         {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setLogin(usuario.getLogin());
            usuarioDTO.setSenha(usuario.getSenha());
            return JwtUserFactory.create(usuarioDTO);
         }

      }
      catch (AptareException e)
      {
         throw new UsernameNotFoundException(e.getMensagem());
      }

   }

}
