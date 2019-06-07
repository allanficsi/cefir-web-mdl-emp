package br.com.aptare.cefit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.painelEletronico.entity.Senha;
import br.com.aptare.cefit.painelEletronico.service.SenhaService;
import br.com.aptare.cefit.response.Response;
import br.com.aptare.cefit.util.RetirarLazy;

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
