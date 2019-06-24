package br.com.aptare.cefit.controller;

import br.com.aptare.cadastroUnico.entidade.Contato;
import br.com.aptare.cefit.cadastroUnico.dto.ContatoDTO;
import br.com.aptare.cefit.empregador.dto.EmpregadorDTO;
import br.com.aptare.cefit.empregador.entity.Empregador;
import br.com.aptare.cefit.empregador.service.EmpregadorService;
import br.com.aptare.cefit.response.Response;
import br.com.aptare.cefit.util.RetirarLazy;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empregador")
@CrossOrigin(origins = "*")
public class EmpregadorController extends AptareCrudController<Empregador, EmpregadorService>
{

   public EmpregadorController()
   {
      super(EmpregadorService.getInstancia());
   }

   @PostMapping(path = "/resetarSenha")
   public ResponseEntity<Response<Object>> resetarSenha(HttpServletRequest request, @RequestBody Empregador entity)
   {
      Response<Object> response = new Response<Object>();
      try
      {
         Empregador objAtualizar = new RetirarLazy<Empregador>(getService().resetarSenha(entity)).execute();
         response.setData(objAtualizar);
      }
      catch (AptareException e)
      {
         response.getErrors().add(e.getMensagem());
         return ResponseEntity.badRequest().body(response);
      }
      return ResponseEntity.ok(response);
   }




   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, Empregador entity, String status) throws AptareException
   {
      entity.setSituacao( status.equals("S") ? EmpregadorService.SITUACAO_ATIVA : EmpregadorService.SITUACAO_INATIVA );
      entity.setAuditoria(new Auditoria());
      entity.getAuditoria().setDataAlteracao(new Date());
      entity.getAuditoria().setCodigoUsuarioAlteracao(super.getUsuarioFromRequest(request).getCodigo());
   }
   
   @Override
   protected void ativarInativar(Empregador entity) throws AptareException
   {
      this.getService().ativarInativar(entity);
   }

   @Override
   protected String[] juncaoPesquisar()
   {
      return new String[] { "cadastroUnico.pessoaJuridica*", "cadastroUnico.pessoaFisica*" };
   }
   
   @Override
   protected String[] juncaoGet()
   {
      return new String[] { "cadastroUnico.pessoaJuridica*.listaContato*.cargo*", 
                            "cadastroUnico.pessoaJuridica*.listaContato*.listaTelefone*.auditoria*", 
                            "cadastroUnico.pessoaFisica*.listaTelefone*.auditoria*",
                            "cadastroUnico.listaEndereco.correio*", 
                            "cadastroUnico.listaEndereco.extensaoEndereco*", 
                            "auditoria.usuarioInclusao" };
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "cadastroUnico.pessoaJuridica.nomeFantasia" };
   }

   @Override
   protected Object atualizarEntidadeResponse(Empregador empregador)
   {
      EmpregadorDTO dto = new EmpregadorDTO();
      dto = this.convertToDto(empregador);
      return dto;
   }

   @Override
   protected List<Object> atualizarListaResponse(List<Empregador> lista)
   {
      return lista.stream().map(empregador -> convertToDto(empregador)).collect(Collectors.toList());
   }

   private EmpregadorDTO convertToDto(Empregador empregador)
   {
      EmpregadorDTO dto = new EmpregadorDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(empregador, dto);
      
      // Campos do cadastro unico do empregador
      if(dto.getCadastroUnico() != null) 
      {
         dto.getCadastroUnico().getAuditoria().setCodigoUsuarioInclusao(empregador.getCadastroUnico().getAuditoria().getCodigoUsuarioInclusao());
         dto.getCadastroUnico().getAuditoria().setCodigoUsuarioAlteracao(empregador.getCadastroUnico().getAuditoria().getCodigoUsuarioAlteracao());
      }
      
      if(dto.getCadastroUnico().getTipoPessoa().equals("J"))
      {
         // Campos do empregador
         dto.setCodigoCadastroUnico(dto.getCadastroUnico().getPessoaJuridica().getCodigoCadastroUnico());
         dto.getCadastroUnico().setCodigo(dto.getCodigoCadastroUnico());
         dto.getAuditoria().setCodigoUsuarioInclusao(empregador.getAuditoria().getCodigoUsuarioInclusao());
         dto.getAuditoria().setCodigoUsuarioAlteracao(empregador.getAuditoria().getCodigoUsuarioAlteracao());

         
         if(dto.getCadastroUnico() != null
               && dto.getCadastroUnico().getPessoaJuridica() != null
               && dto.getCadastroUnico().getPessoaJuridica().getListaContato() != null)
         {
            List<ContatoDTO> listaContatoDTO = new ArrayList<ContatoDTO>(dto.getCadastroUnico().getPessoaJuridica().getListaContato());
            List<Contato> listaContato = new ArrayList<Contato>(empregador.getCadastroUnico().getPessoaJuridica().getListaContato());
            
            // Ordenacao dto 
            Collections.sort(listaContatoDTO, new Comparator<ContatoDTO>()
            {
               @Override
               public int compare(ContatoDTO c1, ContatoDTO c2)
               {

                  return c1.getCodigo().compareTo(c2.getCodigo());
               }
            });           
            
            // Ordenacao entity
            Collections.sort(listaContato, new Comparator<Contato>()
            {
               @Override
               public int compare(Contato c1, Contato c2)
               {

                  return c1.getCodigo().compareTo(c2.getCodigo());
               }
            });
            
            
            for (int i = 0; i < listaContatoDTO.size(); i++)
            {
               listaContatoDTO.get(i).setCodigoCargo((Long)(listaContato.get(i).getCodigoCargo()));
               listaContatoDTO.get(i).getCargo().setCodigo((Long)(listaContato.get(i).getCodigoCargo()));
               listaContatoDTO.get(i).getAuditoria().setCodigoUsuarioInclusao(listaContato.get(i).getAuditoria().getCodigoUsuarioInclusao());
               listaContatoDTO.get(i).getAuditoria().setCodigoUsuarioAlteracao(listaContato.get(i).getAuditoria().getCodigoUsuarioAlteracao());
            }
         }
      }
      else
      {
         dto.setCodigoCadastroUnico(dto.getCadastroUnico().getPessoaFisica().getCodigoCadastroUnico());
         dto.getCadastroUnico().setCodigo(dto.getCodigoCadastroUnico());
      }
      
      
      return dto;
   }

}
