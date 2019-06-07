package br.com.aptare.cefit.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.vagas.dto.EncaminhamentoDTO;
import br.com.aptare.cefit.vagas.entity.Encaminhamento;
import br.com.aptare.cefit.vagas.service.EncaminhamentoService;

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
   protected String[] juncaoGet()
   {
      return new String[] { "cboEntity", "empregadorEntity.cadastroUnico.pessoaFisica*", 
                            "trabalhadorEntity*.cadastroUnico*.pessoaFisica*",
                            "empregadorEntity.cadastroUnico.pessoaJuridica*", "listaVagaDia*", "listaVagaAgendamento*"};
   }
   
   @Override
   protected String[] ordenacaoGet()
   {
      return new String[] { "listaVagaAgendamento.numeroDia" };
   }

   @Override
   protected Object atualizarEntidadeResponse(Encaminhamento vaga)
   {
      EncaminhamentoDTO dto = new EncaminhamentoDTO();
      dto = this.convertToDto(vaga);
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
