package br.com.aptare.cefit.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.comum.entidade.Parametro;
import br.com.aptare.comum.servico.ParametroService;

@RestController
@RequestMapping("/api/parametro")
@CrossOrigin(origins = "*")
public class ParametroController extends AptareCrudController<Parametro, ParametroService>
{   
   public ParametroController()
   {
      super(ParametroService.getInstancia());
   }
}
