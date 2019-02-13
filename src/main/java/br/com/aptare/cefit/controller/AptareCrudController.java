package br.com.aptare.cefit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.aptare.cefit.response.Response;
import br.com.aptare.cefit.security.jwt.JwtTokenUtil;
import br.com.aptare.cefit.util.RetirarLazy;
import br.com.aptare.fda.crud.service.AptareService;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Usuario;
import br.com.aptare.seguranca.servico.UsuarioService;

public class AptareCrudController<Entity, Service extends AptareService<Entity>>
{
   @Autowired
   protected JwtTokenUtil jwtTokenUtil;

   private Service service;
   
   @Autowired
   public ModelMapper modelMapper;

   public AptareCrudController()
   {
      super();
   }

   public AptareCrudController(Service service)
   {
      this();
      this.service = service;
   }

   /**
    * 
    */
   @PostMapping(path = "/pesquisar")
   public ResponseEntity<Response<List<Object>>> pesquisar(HttpServletRequest request, @RequestBody Entity entity)
   {
      Response<List<Object>> response = new Response<List<Object>>();
      try
      {
         List<Entity> lista = null;
         lista = getService().pesquisar(entity, juncaoPesquisar(), getOrdenacaoPesquisar());
         
         if (lista != null)
         {
            lista = (List<Entity>) new RetirarLazy<List<Entity>>(lista).execute();
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

   /**
    * 
    */
   @PostMapping(path = "/get")
   public ResponseEntity<Response<Object>> get(HttpServletRequest request, @RequestBody Entity entity)
   {
      Response<Object> response = new Response<Object>();
      try
      {
         Entity retorno = null;
         retorno = getService().get(entity, juncaoGet(), getOrdenacaoPesquisar());
         
         retorno = new RetirarLazy<Entity>(retorno).execute();
         response.setData(this.atualizarEntidadeResponse(retorno));
         this.executarPosGet(retorno);
         return ResponseEntity.ok(response);
      }
      catch (Exception e)
      {
         response.getErrors().add(e.getMessage());
         return ResponseEntity.badRequest().body(response);
      }
   }

   @PostMapping()
   public ResponseEntity<Response<Object>> inserir(HttpServletRequest request, @RequestBody Entity entity, BindingResult result)
   {
      Response<Object> response = new Response<Object>();
      try
      {
         validarInserir(entity, result);
         if (result.hasErrors())
         {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
         }

         completarInserir(entity, request);
         Object objInserir = this.atualizarEntidadeResponse(getService().inserir(entity));
         response.setData(objInserir);
      }
      catch (AptareException e)
      {
         response.getErrors().add(e.getMensagem());
         return ResponseEntity.badRequest().body(response);
      }
      return ResponseEntity.ok(response);
   }

   @PutMapping()
   public ResponseEntity<Response<Object>> alterar(HttpServletRequest request, @RequestBody Entity entity, BindingResult result)
   {
      Response<Object> response = new Response<Object>();
      try
      {
         validarAlterar(entity, result);
         if (result.hasErrors())
         {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
         }

         completarAlterar(entity, request);
         Object objAlterar = this.atualizarEntidadeResponse(getService().alterar(entity));
         response.setData(objAlterar);
      }
      catch (AptareException e)
      {
         response.getErrors().add(e.getMensagem());
         return ResponseEntity.badRequest().body(response);
      }
      return ResponseEntity.ok(response);
   }

   @PostMapping(path = "/inativar")
   public ResponseEntity<Response<String>> inativar(HttpServletRequest request, @RequestBody Entity entity)
   {
      Response<String> response = new Response<String>();
      try
      {
         this.atualizarStatusEntidade(request, entity, "N");
         this.ativarInativar(entity);
      }
      catch (Exception e)
      {
         response.getErrors().add(e.getMessage());
         return ResponseEntity.badRequest().body(response);
      }

      return ResponseEntity.ok(new Response<String>());
   }
   
   @PostMapping(path = "/ativar")
   public ResponseEntity<Response<String>> ativar(HttpServletRequest request, @RequestBody Entity entity)
   {
      Response<String> response = new Response<String>();
      try
      {
         atualizarStatusEntidade(request, entity, "S");
         ativarInativar(entity);
      }
      catch (Exception e)
      {
         response.getErrors().add(e.getMessage());
         return ResponseEntity.badRequest().body(response);
      }

      return ResponseEntity.ok(new Response<String>());
   }

   public Usuario getUsuarioFromRequest(HttpServletRequest request) throws AptareException
   {
      String token = request.getHeader("Authorization");
      String login = jwtTokenUtil.getLoginFromToken(token);

      Usuario usuario = new Usuario();
      usuario.setLogin(login);

      return UsuarioService.getInstancia().get(usuario, null, null);
   }
   
   protected void ativarInativar(Entity entity) throws AptareException
   {
   }

   @SuppressWarnings("unchecked")
   protected List<Object> atualizarListaResponse(List<Entity> lista)
   {
      return (List<Object>) lista;
   }

   protected Object atualizarEntidadeResponse(Entity entity)
   {
      return (Object) entity;
   }

   protected void validarInserir(Entity entity, BindingResult result)
   {
   }

   protected void validarAlterar(Entity entity, BindingResult result)
   {
   }

   protected void completarInserir(Entity entity, HttpServletRequest request)
   {
   }

   protected void completarAlterar(Entity entity, HttpServletRequest request)
   {
   }

   protected void atualizarStatusEntidade(HttpServletRequest request, Entity entity, String status) throws AptareException
   {
   }

   protected void executarPosGet(Entity entity)
   {
   }

   protected String[] juncaoPesquisar()
   {
      return null;
   }
   
   protected String[] juncaoGet()
   {
      return null;
   }

   protected String[] getOrdenacaoPesquisar()
   {
      return null;
   }

   public Service getService()
   {
      return service;
   }

   public void setService(Service service)
   {
      this.service = service;
   }
}
