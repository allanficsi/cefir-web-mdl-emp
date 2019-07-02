package br.com.aptare.cefit.controller.externo;

import br.com.aptare.cadastroUnico.entidade.Cargo;
import br.com.aptare.cadastroUnico.servico.CargoService;
import br.com.aptare.cefit.controller.AptareCrudController;
import br.com.aptare.cefit.controller.CargoController;
import br.com.aptare.cefit.response.Response;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/cargo/externo")
@CrossOrigin(origins = "*")
public class CargoExternoController extends CargoController {
   public CargoExternoController() {
      super();
   }

   @RequestMapping(path = "/externo/pesquisar")
   @Override
   public ResponseEntity<Response<List<Object>>> pesquisar(HttpServletRequest request, @RequestBody Cargo cargo) {
      return super.pesquisar(request, cargo);
   }

}
