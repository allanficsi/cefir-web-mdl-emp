package br.com.aptare.cefit.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.geral.entity.Feriado;
import br.com.aptare.cefit.geral.service.FeriadoService;

@RestController
@RequestMapping("/api/feriado")
@CrossOrigin(origins = "*")
public class FeriadoController extends AptareCrudController<Feriado, FeriadoService>
{
   public FeriadoController()
   {
      super(FeriadoService.getInstancia());
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "descricao" };
   }
}