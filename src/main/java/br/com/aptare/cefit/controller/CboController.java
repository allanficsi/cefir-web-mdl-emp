package br.com.aptare.cefit.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.trabalhador.entity.Cbo;
import br.com.aptare.cefit.trabalhador.service.CboService;

@RestController
@RequestMapping("/api/cbo")
@CrossOrigin(origins = "*")
public class CboController extends AptareCrudController<Cbo, CboService>
{
   
   public CboController()
   {
      super(CboService.getInstancia());
   }

}
