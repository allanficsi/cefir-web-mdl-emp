package br.com.aptare.cefit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.painelEletronico.entity.Chamada;
import br.com.aptare.cefit.painelEletronico.service.ChamadaService;
import br.com.aptare.cefit.painelEletronico.service.SenhaService;
import br.com.aptare.cefit.response.Response;
import br.com.aptare.cefit.util.RetirarLazy;

@RestController
@RequestMapping("/api/chamada")
@CrossOrigin(origins = "*")
public class ChamadaController extends AptareCrudController<Chamada, ChamadaService>
{
   public ChamadaController()
   {
      super(ChamadaService.getInstancia());
   }
   
   @GetMapping(path = "/resetarSenha")
   public ResponseEntity<Response<Object>> resetarSenha(HttpServletRequest request)
   {
      Response<Object> response = new Response<Object>();
      try
      {
         Long codigoUsuario = this.getUsuarioFromRequest(request).getCodigo();
         SenhaService.getInstancia().resetarSenha(codigoUsuario);
         return ResponseEntity.ok(response);
      }
      catch (Exception e)
      {
         response.getErrors().add(e.getMessage());
         return ResponseEntity.badRequest().body(response);
      }
   }
   
   @GetMapping(path = "/retornarUltima")
   public ResponseEntity<Response<Object>> retornarUltima()
   {
      Response<Object> response = new Response<Object>();
      try
      {
         Chamada retorno = null;
         retorno = getService().retornarUltima();
         
         retorno = new RetirarLazy<Chamada>(retorno).execute();
         
         if(retorno != null) 
         {
            response.setData(this.atualizarEntidadeResponse(retorno));
         }
         
         this.executarPosGet(retorno);
         return ResponseEntity.ok(response);
      }
      catch (Exception e)
      {
         response.getErrors().add(e.getMessage());
         return ResponseEntity.badRequest().body(response);
      }
   }
   
   @PostMapping(path = "/retornarProxima")
   public ResponseEntity<Response<Object>> retornarProxima(HttpServletRequest request, @RequestBody Chamada chamada)
   {
      Response<Object> response = new Response<Object>();
      try
      {
         Chamada retorno = null;
         
         if(chamada != null
               && chamada.getSenha() != null
               && chamada.getSenha().getCodigoTipoSenha() != null)
         {
            retorno = getService().retornarProxima(chamada.getSenha().getCodigoTipoSenha());
            retorno = new RetirarLazy<Chamada>(retorno).execute();
         }
         
         if(retorno != null) 
         {
            response.setData(this.atualizarEntidadeResponse(retorno));
         }
         
         this.executarPosGet(retorno);
         return ResponseEntity.ok(response);
      }
      catch (Exception e)
      {
         response.getErrors().add(e.getMessage());
         return ResponseEntity.badRequest().body(response);
      }
   }
   
   @GetMapping(path = "/listarUltimasChamadas")
   public ResponseEntity<Response<List<Object>>> listarUltimasChamadas()
   {
      Response<List<Object>> response = new Response<List<Object>>();
      try
      {
         List<Chamada> lista = null;
         lista = getService().listarUltimasChamadas();
         
         if (lista != null)
         {
            lista = (List<Chamada>) new RetirarLazy<List<Chamada>>(lista).execute();
            List<Object> listaRetorno = this.atualizarListaResponse(lista);
            response.setData(listaRetorno);
         }
         
         return ResponseEntity.ok(response);
      }
      catch (Exception e)
      {
         response.getErrors().add(e.getMessage());
         return ResponseEntity.badRequest().body(response);
      }
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "descricao" };
   }
}
