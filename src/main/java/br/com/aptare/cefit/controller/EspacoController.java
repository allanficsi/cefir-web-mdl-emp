package br.com.aptare.cefit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.espaco.dto.EspacoDTO;
import br.com.aptare.cefit.espaco.dto.EspacoItemEspacoDTO;
import br.com.aptare.cefit.espaco.entity.Espaco;
import br.com.aptare.cefit.espaco.entity.EspacoItemEspaco;
import br.com.aptare.cefit.espaco.service.EspacoService;
import br.com.aptare.cefit.response.Response;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;

@RestController
@RequestMapping("/api/espaco")
@CrossOrigin(origins = "*")
public class EspacoController extends AptareCrudController<Espaco, EspacoService>
{
   public EspacoController()
   {
      super(EspacoService.getInstancia());
   }
   
   @Override
   protected String[] juncaoPesquisar()
   {
      return new String[] { "listaEspacoItemEspaco", "local" };
   }
   
   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, Espaco espaco, String status) throws AptareException
   {
      espaco.setFlagAtivo(status);
      espaco.setAuditoria(new Auditoria());
      espaco.getAuditoria().setDataAlteracao(new Date());
      espaco.getAuditoria().setCodigoUsuarioAlteracao(this.getUsuarioFromRequest(request).getCodigo());
   }
   
   @Override
   protected Object atualizarEntidadeResponse(Espaco espaco)
   {
      EspacoDTO dto = new EspacoDTO();
      dto = this.convertToDto(espaco);
      return dto;
   }
   
   @Override
   protected List<Object> atualizarListaResponse(List<Espaco> lista)
   {
      return lista.stream().map(espaco -> convertToDto(espaco)).collect(Collectors.toList());
   }
   
   private EspacoDTO convertToDto(Espaco espaco)
   {
      EspacoDTO dto = new EspacoDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(espaco, dto);
      
      if(espaco.getEndereco() != null)
      {
         dto.setCodigoEndereco(espaco.getCodigoEndereco());
         dto.getEndereco().setCodigo(dto.getCodigoEndereco());
      }

      dto.setCodigoLocal(espaco.getCodigoLocal());
      
      // Auditoria Espaco
      if(espaco.getAuditoria() != null)
      {
         dto.getAuditoria().setCodigoUsuarioInclusao(espaco.getAuditoria().getCodigoUsuarioInclusao());
         dto.getAuditoria().setCodigoUsuarioAlteracao(espaco.getAuditoria().getCodigoUsuarioAlteracao());
      }
      
      // Auditoria Endereco
      if(espaco.getEndereco() != null
            && espaco.getEndereco().getAuditoria() != null)
      {
         dto.getEndereco().getAuditoria().setCodigoUsuarioInclusao(espaco.getEndereco().getAuditoria().getCodigoUsuarioInclusao());
         dto.getEndereco().getAuditoria().setCodigoUsuarioAlteracao(espaco.getEndereco().getAuditoria().getCodigoUsuarioAlteracao());
      }
      
      // Lista Itens
      if(espaco.getListaEspacoItemEspaco() != null
            && espaco.getListaEspacoItemEspaco().size() > 0) 
      {
         
         List<EspacoItemEspacoDTO> listaEspacoItemEspacoDTO = new ArrayList<EspacoItemEspacoDTO>(dto.getListaEspacoItemEspaco());
         List<EspacoItemEspaco> listaEspacoItemEspaco = new ArrayList<EspacoItemEspaco>(espaco.getListaEspacoItemEspaco());
         
         // Ordenacao dto 
         Collections.sort(listaEspacoItemEspacoDTO, new Comparator<EspacoItemEspacoDTO>()
         {
            @Override
            public int compare(EspacoItemEspacoDTO c1, EspacoItemEspacoDTO c2)
            {

               return c1.getCodigo().compareTo(c2.getCodigo());
            }
         });           
         
         // Ordenacao entity
         Collections.sort(listaEspacoItemEspaco, new Comparator<EspacoItemEspaco>()
         {
            @Override
            public int compare(EspacoItemEspaco c1, EspacoItemEspaco c2)
            {

               return c1.getCodigo().compareTo(c2.getCodigo());
            }
         });
         
         
         for (int i = 0; i < listaEspacoItemEspacoDTO.size(); i++)
         {
            listaEspacoItemEspacoDTO.get(i).setCodigoEspaco((Long)(listaEspacoItemEspaco.get(i).getCodigoEspaco()));
         }
      }
         
      
      return dto;
   }
   
   @PostMapping(path = "/salvarManutencao")
   public ResponseEntity<Response<Object>> salvarManutencao(HttpServletRequest request, @RequestBody Espaco entity, BindingResult result)
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

         getService().salvarManutencao(entity);
      }
      catch (AptareException e)
      {
         response.getErrors().add(e.getMensagem());
         return ResponseEntity.badRequest().body(response);
      }
      return ResponseEntity.ok(response);
   }
   
   @Override
   protected void ativarInativar(Espaco espaco) throws AptareException
   {
      getService().ativarInativar(espaco);
   }
   
   @Override
   protected String[] juncaoGet()
   {
      return new String[] { "endereco.extensaoEndereco*" , 
                            "endereco.correio*", 
                            "listaEspacoItemEspaco*.itemEspaco*", 
                            "auditoria*", 
                            "endereco.auditoria.usuarioInclusao" };
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "nome" };
   }
   
   @Override
   protected String[] ordenacaoGet()
   {
      return new String[] { "nome", "listaEspacoItemEspaco.itemEspaco.descricao" };
   }
}
