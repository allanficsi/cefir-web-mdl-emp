package br.com.aptare.cefit.controller.externo;

import br.com.aptare.cadastroUnico.entidade.Contato;
import br.com.aptare.cefit.cadastroUnico.dto.ContatoDTO;
import br.com.aptare.cefit.controller.AptareCrudController;
import br.com.aptare.cefit.controller.EmpregadorController;
import br.com.aptare.cefit.empregador.dto.EmpregadorDTO;
import br.com.aptare.cefit.empregador.entity.Empregador;
import br.com.aptare.cefit.empregador.service.EmpregadorService;
import br.com.aptare.cefit.response.Response;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empregador/externo")
@CrossOrigin(origins = "*")
public class EmpregadorExternoController extends EmpregadorController
{

   public EmpregadorExternoController()
   {
      super();
   }

   @PostMapping(path = "/externo/inserir")
   @Override
   public ResponseEntity<Response<Object>> inserir(HttpServletRequest request, @RequestBody Empregador empregador, BindingResult result) {
      return super.inserir(request, empregador, result);
   }
   @PostMapping(path = "/externo/get")
   @Override
   public ResponseEntity<Response<Object>> get(HttpServletRequest request, @RequestBody Empregador empregador) {
      return super.get(request, empregador);
   }
}
