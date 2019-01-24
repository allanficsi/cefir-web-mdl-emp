package br.com.aptare.cefit.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.empregador.entity.Cnae;
import br.com.aptare.cefit.empregador.service.CnaeService;

@RestController
@RequestMapping("/api/cnae")
@CrossOrigin(origins = "*")
public class CnaeController extends AptareCrudController<Cnae, CnaeService>
{
   
   public CnaeController()
   {
      super(CnaeService.getInstancia());
   }

}
