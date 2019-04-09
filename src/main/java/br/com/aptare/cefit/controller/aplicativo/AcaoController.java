package br.com.aptare.cefit.controller.aplicativo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.acao.dto.AcaoDTO;
import br.com.aptare.cefit.acao.dto.TipoAcaoDTO;
import br.com.aptare.cefit.acao.entity.Acao;
import br.com.aptare.cefit.acao.service.AcaoService;
import br.com.aptare.cefit.response.Response;
import br.com.aptare.fda.exception.AptareException;

@RestController("acaoAplicativoController")
@RequestMapping("/aplicativo/acao")
@CrossOrigin(origins = "*")
public class AcaoController
{
   
   @PostMapping(path = "/consultar")
   public ResponseEntity<Response<List<AcaoDTO>>> consultar(HttpServletRequest request, @RequestBody AcaoDTO to)
   {
      Response<List<AcaoDTO>> response = new Response<List<AcaoDTO>>();
      try
      {
         
         Acao filter = new Acao();
         List<Acao> lista = AcaoService.getInstancia().pesquisar(filter, new String[] {"tipoAcao"}, new String[] {"tipoAcao.descricao", "nome"});
         
         List<AcaoDTO> retorno = null;
         
         if (lista != null
               && lista.size() > 0)
         {
            AcaoDTO add = null;
            retorno = new ArrayList<AcaoDTO>();
            
            for (Acao elemento : lista)
            {
               add = new AcaoDTO();
               add.setNome(elemento.getNome());
               add.setTipoAcao(new TipoAcaoDTO());
               add.getTipoAcao().setDescricao(elemento.getTipoAcao().getDescricao());
               retorno.add(add);
            }
         }
         
         response.setData(retorno);
      }
      catch (Exception e)
      {
         if (e instanceof AptareException)
         {
            response.getErrors().add(((AptareException)e).getMensagem()); 
         }
         else
         {
            response.getErrors().add(e.getMessage());
         }
         return ResponseEntity.badRequest().body(response);
      }
      
      return ResponseEntity.ok(response);
   }

}
