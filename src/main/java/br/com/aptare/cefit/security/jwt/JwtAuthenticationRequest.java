package br.com.aptare.cefit.security.jwt;

import java.io.Serializable;

public class JwtAuthenticationRequest implements Serializable
{

   private static final long serialVersionUID = 1L;

   private String login;

   private String senha;

   public JwtAuthenticationRequest()
   {
      super();
   }

   public JwtAuthenticationRequest(String dsLogin, String dsPassword)
   {
      this.setLogin(login);
      this.setSenha(senha);
   }

   public String getLogin()
   {
      return login;
   }

   public void setLogin(String login)
   {
      this.login = login;
   }

   public String getSenha()
   {
      return senha;
   }

   public void setSenha(String senha)
   {
      this.senha = senha;
   }

}
