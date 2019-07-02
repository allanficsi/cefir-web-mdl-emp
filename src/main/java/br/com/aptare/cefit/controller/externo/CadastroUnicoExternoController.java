package br.com.aptare.cefit.controller.externo;

import br.com.aptare.cadastroUnico.entidade.CadastroUnico;
import br.com.aptare.cadastroUnico.servico.CadastroUnicoService;
import br.com.aptare.cefit.cadastroUnico.dto.CadastroUnicoDTO;
import br.com.aptare.cefit.controller.AptareCrudController;
import br.com.aptare.cefit.controller.CadastroUnicoController;
import br.com.aptare.cefit.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cadastroUnico/externo")
@CrossOrigin(origins = "*")
public class CadastroUnicoExternoController extends CadastroUnicoController
{
   public CadastroUnicoExternoController()
   {
      super();
   }

   @RequestMapping(path = "/externo/get")
   @Override
   public ResponseEntity<Response<Object>> get(HttpServletRequest request, @RequestBody CadastroUnico cadastroUnico) {
      return super.get(request, cadastroUnico);
   }
}
