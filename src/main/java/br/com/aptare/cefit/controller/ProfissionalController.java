package br.com.aptare.cefit.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.profissional.dto.ProfissionalDTO;
import br.com.aptare.cefit.profissional.entity.Profissional;
import br.com.aptare.cefit.profissional.service.ProfissionalService;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;

@RestController
@RequestMapping("/api/profissional")
@CrossOrigin(origins = "*")
public class ProfissionalController extends AptareCrudController<Profissional, ProfissionalService>
{

   public ProfissionalController()
   {
      super(ProfissionalService.getInstancia());
   }

   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, Profissional entity, String status) throws AptareException
   {
      entity.setFlagAtivo(status);
      entity.setAuditoria(new Auditoria());
      entity.getAuditoria().setDataAlteracao(new Date());
      entity.getAuditoria().setCodigoUsuarioAlteracao(super.getUsuarioFromRequest(request).getCodigo());
   }

   @Override
   protected void ativarInativar(Profissional entity) throws AptareException
   {
      this.getService().ativarInativar(entity);
   }

   @Override
   protected String[] juncaoPesquisar()
   {
      return new String[] { "cadastroUnico.pessoaFisica" };
   }

   @Override
   protected String[] juncaoGet()
   {
      return new String[] { "listaProfissionalQualificacao*.qualificacao*",
                            "cadastroUnico.pessoaFisica.listaTelefone*",
                            "cadastroUnico.listaEndereco.correio*",
                            "cadastroUnico.listaEndereco.extensaoEndereco*", 
                            "auditoria.usuarioInclusao" };
   }

   @Override
   protected String[] getOrdenacaoPesquisar()
   {
      return new String[] { "cadastroUnico.nome" };
   }

   @Override
   protected Object atualizarEntidadeResponse(Profissional profissional)
   {
      ProfissionalDTO dto = new ProfissionalDTO();
      dto = this.convertToDto(profissional);
     
      // ATUALIZANDO CADASTRO UNICO
      dto.setCodigoCadastroUnico(dto.getCadastroUnico().getPessoaFisica().getCodigoCadastroUnico());
      dto.getCadastroUnico().setCodigo(dto.getCodigoCadastroUnico());

      return dto;
   }

   @Override
   protected List<Object> atualizarListaResponse(List<Profissional> lista)
   {
      return lista.stream().map(profissional -> convertToDto(profissional)).collect(Collectors.toList());
   }

   private ProfissionalDTO convertToDto(Profissional profissional)
   {      
      ProfissionalDTO dto = new ProfissionalDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(profissional, dto);
      
      
      
      

      return dto;

   }

}
