package br.com.aptare.cefit.controller;

import br.com.aptare.cefit.trabalhador.entity.TrabalhadorAgenda;
import br.com.aptare.cefit.trabalhador.service.AgendaService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/agenda")
@CrossOrigin("*")
public class AgendaTrabalhadorController extends AptareCrudController<TrabalhadorAgenda, AgendaService>
{
    public AgendaTrabalhadorController()
    {
        super(AgendaService.getInstancia());
    }

}
