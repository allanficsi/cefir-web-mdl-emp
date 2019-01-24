package br.com.aptare.cefit.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.empregador.dto.EmpregadorDTO;
import br.com.aptare.cefit.empregador.entity.Empregador;
import br.com.aptare.cefit.empregador.service.EmpregadorService;
import br.com.aptare.fda.exception.AptareException;

@RestController
@RequestMapping("/api/empregador")
@CrossOrigin(origins = "*")
public class EmpregadorController extends AptareCrudController<Empregador, EmpregadorService>
{

   public EmpregadorController()
   {
      super(EmpregadorService.getInstancia());
   }

   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, Empregador entity, String status) throws AptareException
   {
      entity.setSituacao( status.equals("S") ? EmpregadorService.SITUACAO_ATIVA : EmpregadorService.SITUACAO_INATIVA );
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
                            "cadastroUnico.pessoaJuridica*.listaContato*.listaTelefone*", 
                            "cadastroUnico.pessoaFisica*.listaTelefone*",
                            "cadastroUnico.listaEndereco.correio*", 
                            "cadastroUnico.listaEndereco.extensaoEndereco*", 
                            "auditoria.usuarioInclusao" };
   }
   
   @Override
   protected String[] getOrdenacaoPesquisar()
   {
      return new String[] { "cadastroUnico.pessoaJuridica.nomeFantasia" };
   }

   @Override
   protected Object atualizarEntidadeResponse(Empregador empregador)
   {
      EmpregadorDTO dto = new EmpregadorDTO();
      dto = this.convertToDto(empregador);
      if(dto.getCadastroUnico().getTipoPessoa().equals("J"))
      {
         dto.setCodigoCadastroUnico(dto.getCadastroUnico().getPessoaJuridica().getCodigoCadastroUnico());
         dto.getCadastroUnico().setCodigo(dto.getCodigoCadastroUnico());
      }
      else
      {
         dto.setCodigoCadastroUnico(dto.getCadastroUnico().getPessoaFisica().getCodigoCadastroUnico());
         dto.getCadastroUnico().setCodigo(dto.getCodigoCadastroUnico());
      }
      
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

      return dto;
   }

}
