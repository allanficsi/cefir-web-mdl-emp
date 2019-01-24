package br.com.aptare.cefit.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cadastroUnico.entidade.Correio;
import br.com.aptare.cadastroUnico.servico.CorreioService;

@RestController
@RequestMapping("/api/correio")
@CrossOrigin(origins = "*")
public class CorreioController extends AptareCrudController<Correio, CorreioService>
{
   
   public CorreioController()
   {
      super(CorreioService.getInstancia());
   }

}
