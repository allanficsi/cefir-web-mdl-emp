package br.com.aptare.cefit.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.vagas.dto.EncaminhamentoDTO;
import br.com.aptare.cefit.vagas.entity.Encaminhamento;
import br.com.aptare.cefit.vagas.service.EncaminhamentoService;
import br.com.aptare.fda.exception.AptareException;

@RestController
@RequestMapping("/api/encaminhamento")
@CrossOrigin(origins = "*")
public class EncaminhamentoController extends AptareCrudController<Encaminhamento, EncaminhamentoService>
{
   public EncaminhamentoController()
   {
      super(EncaminhamentoService.getInstancia());
   }

   @Override
   protected String[] juncaoPesquisar()
   {
      return new String[] { "vaga.empregadorEntity.cadastroUnico.pessoaFisica*", "auditoria.usuarioInclusao",
                            "trabalhador.cadastroUnico.pessoaFisica", "vaga.empregadorEntity.cadastroUnico.pessoaJuridica*"};
   }
   
   @Override
   protected String[] ordenacaoGet()
   {
      return new String[] { "listaVagaAgendamento.numeroDia" };
   }
   
   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, Encaminhamento encaminhamento, String status) throws AptareException
   {
      encaminhamento.setFlagAtivo(status);
      encaminhamento.setDataCancelamento(new Date());
      encaminhamento.setCodigoUsuarioCancelamento(this.getUsuarioFromRequest(request).getCodigo());
   }
   
   @Override
   protected void ativarInativar(Encaminhamento encaminhamento) throws AptareException
   {
      getService().inativar(encaminhamento);
   }

   @Override
   protected Object atualizarEntidadeResponse(Encaminhamento vaga)
   {
      EncaminhamentoDTO dto = this.convertToDto(vaga);
      return dto;
   }

   @Override
   protected List<Object> atualizarListaResponse(List<Encaminhamento> lista)
   {
      return lista.stream().map(vaga -> convertToDto(vaga)).collect(Collectors.toList());
   }

   private EncaminhamentoDTO convertToDto(Encaminhamento encaminhamento)
   {
      EncaminhamentoDTO dto = new EncaminhamentoDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(encaminhamento, dto);
      
      return dto;
   }

}
