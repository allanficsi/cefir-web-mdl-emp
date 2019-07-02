package br.com.aptare.cefit.controller.externo;

import br.com.aptare.cefit.controller.AptareCrudController;
import br.com.aptare.cefit.controller.DominioController;
import br.com.aptare.cefit.response.Response;
import br.com.aptare.comum.entidade.Dominio;
import br.com.aptare.comum.servico.DominioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/dominio/externo")
@CrossOrigin(origins = "*")
public class DominioExternoController extends DominioController
{
   public DominioExternoController()
   {
      super();
   }

   @RequestMapping("/externo/pesquisar")
   @Override
   public ResponseEntity<Response<List<Object>>> pesquisar(HttpServletRequest request, @RequestBody Dominio dominio) {
      return super.pesquisar(request, dominio);
   }


}
