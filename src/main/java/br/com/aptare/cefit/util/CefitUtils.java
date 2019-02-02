package br.com.aptare.cefit.util;

import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtils;

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
}
