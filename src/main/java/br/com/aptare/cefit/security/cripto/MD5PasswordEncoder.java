package br.com.aptare.cefit.security.cripto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;

public class MD5PasswordEncoder implements PasswordEncoder
{

   @Override
   public String encode(CharSequence charSequence)
   {
      try
      {

         MessageDigest md = MessageDigest.getInstance("MD5");
         md.update(charSequence.toString().getBytes());

         byte[] bytes = md.digest();

         StringBuilder s = new StringBuilder();
         for (int i = 0; i < bytes.length; i++)
         {
            int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
            int parteBaixa = bytes[i] & 0xf;
            if (parteAlta == 0) s.append('0');
            s.append(Integer.toHexString(parteAlta | parteBaixa));
         }

         return s.toString();
      }

      catch (NoSuchAlgorithmException e)
      {
         return null;
      }
   }

   @Override
   public boolean matches(CharSequence charSequence, String s)
   {
      return encode(charSequence).equals(s);
   }
}