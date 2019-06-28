package br.com.aptare.cefit;

import br.com.aptare.cefit.vagas.entity.Vaga;
import br.com.aptare.cefit.vagas.service.VagaService;
import br.com.aptare.fda.exception.AptareException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CefitWebApplicationTests {




	@Test
	public void givenPassword_whenHashing_thenVerifying()
			throws NoSuchAlgorithmException, AptareException {

		Vaga v = new Vaga();
		v.setCodigoEmpregador(90l);

		VagaService.getInstancia().pesquisar(v,null,null);

		int n = VagaService.getInstancia().pesquisar(v,null,null).size();

		System.out.println("ARRAY SIZEEEE" + n);
	}


	public String MD5(String md5)
	{
		try
		{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i)
			{
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		}
		catch (java.security.NoSuchAlgorithmException e)
		{
		}
		return null;
	}

}

