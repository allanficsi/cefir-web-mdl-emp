package br.com.aptare.cefit.controller.externo;

import br.com.aptare.cadastroUnico.entidade.Correio;
import br.com.aptare.cadastroUnico.servico.CorreioService;
import br.com.aptare.cefit.controller.AptareCrudController;
import br.com.aptare.cefit.controller.CorreioController;
import br.com.aptare.cefit.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/correio/externo")
@CrossOrigin(origins = "*")
public class CorreioExternoController extends CorreioController
{

   public CorreioExternoController()
   {
      super();
   }

   @PostMapping(path = "/externo/get")
   @Override
   public ResponseEntity<Response<Object>> get(HttpServletRequest request, @RequestBody Correio correio) {
      return super.get(request, correio);
   }
}

