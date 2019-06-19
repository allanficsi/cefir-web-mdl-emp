package br.com.aptare.cefit.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.cadastroUnico.dto.TelefoneDTO;
import br.com.aptare.cefit.vagas.dto.EncaminhamentoNaoAtendidoDTO;
import br.com.aptare.cefit.vagas.entity.EncaminhamentoNaoAtendido;
import br.com.aptare.cefit.vagas.service.EncaminhamentoNaoAtendidoService;

@RestController
@RequestMapping("/api/encaminhamentoNaoAtendido")
@CrossOrigin(origins = "*")
public class EncaminhamentoNaoAtendidoController extends AptareCrudController<EncaminhamentoNaoAtendido, EncaminhamentoNaoAtendidoService>
{
   public EncaminhamentoNaoAtendidoController()
   {
      super(EncaminhamentoNaoAtendidoService.getInstancia());
   }

   @Override
   protected String[] juncaoPesquisar()
   {
      return new String[] {"trabalhador.cadastroUnico.pessoaFisica.listaTelefone", "auditoria.usuarioInclusao"};
   }
   
   @Override
   protected String[] juncaoGet()
   {
      return new String[] {"trabalhador.cadastroUnico.pessoaFisica"};
   }
   
   @Override
   protected String[] ordenacaoGet()
   {
      return new String[] { "listaVagaAgendamento.numeroDia" };
   }


   @Override
   protected List<Object> atualizarListaResponse(List<EncaminhamentoNaoAtendido> lista)
   {
      return lista.stream().map(vaga -> convertToDto(vaga)).collect(Collectors.toList());
   }

   private EncaminhamentoNaoAtendidoDTO convertToDto(EncaminhamentoNaoAtendido encaminhamentoNaoAtendido)
   {
      EncaminhamentoNaoAtendidoDTO dto = new EncaminhamentoNaoAtendidoDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(encaminhamentoNaoAtendido, dto);
      
      if(dto.getTrabalhador() != null
            && dto.getTrabalhador().getCadastroUnico() != null
            && dto.getTrabalhador().getCadastroUnico().getPessoaFisica() != null
            && dto.getTrabalhador().getCadastroUnico().getPessoaFisica().getListaTelefone() != null
            && dto.getTrabalhador().getCadastroUnico().getPessoaFisica().getListaTelefone().size() > 0)
      {
         String telefoneExtenso = "";
         int cont = 1;
         
         for (TelefoneDTO telefone : dto.getTrabalhador().getCadastroUnico().getPessoaFisica().getListaTelefone())
         {
            telefoneExtenso += "(" + telefone.getDdd() + ") " + telefone.getNumero();
            
            if(cont != dto.getTrabalhador().getCadastroUnico().getPessoaFisica().getListaTelefone().size())
            {
               telefoneExtenso += ", ";
            }
            
            cont++;
         }
         
         dto.getTrabalhador().setTelefoneExtenso(telefoneExtenso);
      }
      
      return dto;
   }

}
