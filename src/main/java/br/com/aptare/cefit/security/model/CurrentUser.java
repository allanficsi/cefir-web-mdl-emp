package br.com.aptare.cefit.security.model;

import br.com.aptare.cefit.seguranca.dto.UsuarioDTO;

public class CurrentUser
{
   private String token;

   private UsuarioDTO usuario;

   public CurrentUser(String token, UsuarioDTO usuario)
   {
      this.token = token;
      this.usuario = usuario;
   }

   public String getToken()
   {
      return token;
   }

   public void setToken(String token)
   {
      this.token = token;
   }

   public UsuarioDTO getUsuario()
   {
      return usuario;
   }

   public void setUsuario(UsuarioDTO usuario)
   {
      this.usuario = usuario;
   }

}
