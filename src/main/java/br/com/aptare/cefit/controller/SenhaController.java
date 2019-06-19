package br.com.aptare.cefit.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.painelEletronico.entity.Senha;
import br.com.aptare.cefit.painelEletronico.service.SenhaService;

@RestController
@RequestMapping("/api/senha")
@CrossOrigin(origins = "*")
public class SenhaController extends AptareCrudController<Senha, SenhaService>
{
   public SenhaController()
   {
      super(SenhaService.getInstancia());
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "descricao" };
   }
}
