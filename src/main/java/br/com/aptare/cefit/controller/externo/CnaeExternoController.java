package br.com.aptare.cefit.controller.externo;

import br.com.aptare.cefit.controller.AptareCrudController;
import br.com.aptare.cefit.empregador.entity.Cnae;
import br.com.aptare.cefit.empregador.service.CnaeService;
import br.com.aptare.cefit.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/cnae/externo")
@CrossOrigin(origins = "*")
public class CnaeExternoController extends AptareCrudController<Cnae, CnaeService>
{

   public CnaeExternoController()
   {
      super(CnaeService.getInstancia());
   }

   @PostMapping(path = "/externo/pesquisar")
   @Override
   public ResponseEntity<Response<List<Object>>> pesquisar(HttpServletRequest request, @RequestBody  Cnae cnae) {
      return super.pesquisar(request, cnae);
   }
}
