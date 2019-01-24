package br.com.aptare.cefit.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataCustom
{
   public static final int MILISEGUNDOS = 1;
   public static final int SEGUNDOS = 1000*MILISEGUNDOS;
   public static final int MINUTOS = 60*SEGUNDOS;
   public static final int HORAS = 60*MINUTOS;
   public static final int DIAS = 24*HORAS;
   public static final int SEMANAS = 7*DIAS;
   public static final int MESES = 30*DIAS;
   public static final int ANOS = 365*DIAS;
   
   public static final int SQL_DATE = 1;
   public static final int SQL_TIMESTAMP = 2;
   
   
   /**
    * Retorna diferenca entre a data1 e a data2
    * @param data1 - subtraendo
    * @param data2 - minuendo
    * @param unidade - ver constantes
    * @return diferenca
    */
   public static int diferenca(Date data1, Date data2, int unidade)
   {
      long m1 = data1.getTime();
      long m2 = data2.getTime();
      return (int) ((m1 - m2) / unidade);
   }
   
   public static String getDataSql(Date data, int tipo)
   {
      
      if (data == null)
      {         
         return null;  
      }

      SimpleDateFormat sdf;
      String retorno = "";
      
      if (tipo == SQL_DATE)
      {
         sdf = new SimpleDateFormat("dd/MM/yyyy");
         retorno = "TO_DATE('";
         retorno += sdf.format(data) + "','dd/mm/yyyy')";
      }
      else
      {
         sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
         retorno = "TO_TIMESTAMP('";
         retorno += sdf.format(data) + "','dd/mm/yyyy hh24:mi:ss')::timestamp";
      }
      
      return retorno;
      
   }
}
