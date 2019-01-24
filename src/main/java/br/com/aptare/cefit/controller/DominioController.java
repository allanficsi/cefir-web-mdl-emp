package br.com.aptare.cefit.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.comum.entidade.Dominio;
import br.com.aptare.comum.servico.DominioService;

@RestController
@RequestMapping("/api/dominio")
@CrossOrigin(origins = "*")
public class DominioController extends AptareCrudController<Dominio, DominioService>
{
   
   public DominioController()
   {
      super(DominioService.getInstancia());
   }

}
