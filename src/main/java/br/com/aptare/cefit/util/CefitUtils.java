package br.com.aptare.cefit.util;

import br.com.aptare.cadastroUnico.entidade.CadastroUnico;
import br.com.aptare.cadastroUnico.entidade.Endereco;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.Map;

public class CefitUtils
{
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public static void copiarPropriedades(Object origem, Object destino) throws Exception
   {
      try
      {
         if (destino instanceof Map)
         {
            Map voMap = (Map) destino;
            BeanMap bm = new BeanMap(origem);
            voMap.putAll(bm);
            voMap.remove("class");
            voMap.remove("servletWrapper");
            voMap.remove("multipartRequestHandler");
         }
         else
         {
            PropertyUtils.copyProperties(destino, origem);
         }
      }
      catch (Exception e)
      {
         throw new Exception(e);
      }
   }

   public static void atualizarDadosEndereco(CadastroUnico cadastroUnico)
   {
      if (cadastroUnico != null)
      {
         if (cadastroUnico.getListaEndereco() != null && cadastroUnico.getListaEndereco().size() > 0)
         {
            for (Endereco element : cadastroUnico.getListaEndereco())
            {
               try
               {
                  if ((element.getCorreio().getCep() == null))
                  {
                     element.setCorreio(null);
                  }
               }
               catch (Exception e)
               {
                  element.setCorreio(null);
               }
            }
         }
      }
   }

   public static void atualizarDadosEndereco(Endereco endereco)
   {
      if (endereco != null)
      {
         try
         {
            if ((endereco.getCorreio().getCep() == null))
            {
               endereco.setCorreio(null);
            }
         }
         catch (Exception e)
         {
            endereco.setCorreio(null);
         }
      }
   }
}
