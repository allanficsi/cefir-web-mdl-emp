package br.com.aptare.cefit.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CriptoMD5 {
	
	public static String stringHexa(String nome)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(nome.getBytes());
			
			byte[] bytes = md.digest();
			
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
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
}
