package br.com.aptare.cefit.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.response.Response;
import br.com.aptare.cefit.util.CefitUtils;
import br.com.aptare.cefit.util.RetirarLazy;
import br.com.aptare.cefit.vagas.dto.VagaDTO;
import br.com.aptare.cefit.vagas.dto.VagaDiaDTO;
import br.com.aptare.cefit.vagas.entity.Vaga;
import br.com.aptare.cefit.vagas.service.VagaService;
import br.com.aptare.fda.exception.AptareException;

@RestController
@RequestMapping("/api/vaga")
@CrossOrigin(origins = "*")
public class VagaController extends AptareCrudController<Vaga, VagaService>
{
   public VagaController()
   {
      super(VagaService.getInstancia());
   }

   @PostMapping(path = "/alterarSituacaoVaga")
   public ResponseEntity<Response<Object>> alterarSituacaoVaga(HttpServletRequest request, @RequestBody Vaga vaga)
   {
      Response<Object> response = new Response<Object>();
      try
      {
         Vaga objAtualizar = new RetirarLazy<Vaga>(getService().alterarSituacaoVaga(vaga)).execute();
         response.setData(this.atualizarEntidadeResponse(objAtualizar));
      }
      catch (AptareException e)
      {
         response.getErrors().add(e.getMensagem());
         return ResponseEntity.badRequest().body(response);
      }
      return ResponseEntity.ok(response);
   }

   @PostMapping(path = "/listarVagasEncaminhamento")
   public ResponseEntity<Response<List<Object>>> listarVagasEncaminhamento(HttpServletRequest request, @RequestBody Vaga vaga)
   {
      Response<List<Object>> response = new Response<List<Object>>();
      try
      {
         List<Vaga> lista = null;
         lista = getService().pesquisar(vaga,
                 new String[] { "listaEncaminhamento", "trabalhadorEntity*.cadastroUnico*.pessoaFisica*", "cboEntity",
                         "empregador.cadastroUnico.pessoaFisica*", "auditoria.usuarioInclusao", "auditoria.usuarioAlteracao*",
                         "endereco.extensaoEndereco*", "endereco.correio*", "listaVagaDia", "empregador.cadastroUnico.pessoaJuridica*" },
                 new String[] { "descricao" });

         if (lista != null)
         {
            lista = (List<Vaga>) new RetirarLazy<List<Vaga>>(lista).execute();
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
   protected String[] juncaoGet()
   {
      return new String[] { "cboEntity", "empregador.cadastroUnico.pessoaFisica*", "trabalhadorEntity*.cadastroUnico*.pessoaFisica*",
              "empregador.cadastroUnico.pessoaJuridica*", "listaVagaDia*" };
   }

   @Override
   protected String[] juncaoPesquisar()
   {
      return new String[] { "trabalhadorEntity*.cadastroUnico*.pessoaFisica*", "cboEntity", "trabalhadorEntity.cadastroUnico",
              "empregador.cadastroUnico" };
   }

   @Override
   protected String[] ordenacaoGet()
   {
      return new String[] { "listaVagaDia.data" };
   }

   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "descricao" };
   }

   @Override
   protected Object atualizarEntidadeResponse(Vaga vaga)
   {
      VagaDTO dto = new VagaDTO();
      dto = this.convertToDto(vaga);

      return dto;
   }

   @Override
   protected List<Object> atualizarListaResponse(List<Vaga> lista)
   {
      return lista.stream().map(vaga -> convertToDto(vaga)).collect(Collectors.toList());
   }

   private VagaDTO convertToDto(Vaga vaga)
   {
      CefitUtils.atualizarDadosEndereco(vaga.getEndereco());

      VagaDTO dto = new VagaDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(vaga, dto);

      if (vaga.getCodigoEmpregador() != null && dto.getCodigoEmpregador() == null)
      {
         dto.setCodigoEmpregador(vaga.getCodigoEmpregador());
      }

      if (vaga.getEmpregador() != null)
      {
         dto.getEmpregador().setCodigoCadastroUnico(vaga.getEmpregador().getCodigoCadastroUnico());
         dto.getEmpregador().setCodigo(vaga.getCodigoEmpregador());
      }

      dto.setCodigoEndereco(vaga.getCodigoEndereco());

      String descricaoDias = "";
      String descricaoEndereco = "";

      String logradouro = "";
      String complemento = "";
      String pontoReferencia = "";

      // Endereco do servico
      if (dto.getEndereco() != null)
      {
         if (dto.getEndereco().getCorreio() != null)
         {
            logradouro = dto.getEndereco().getCorreio().getLogradouro();
         }
         else
         {
            logradouro = dto.getEndereco().getExtensaoEndereco().getLogradouro();
         }

         if (dto.getEndereco().getComplemento() != null)
         {
            complemento = dto.getEndereco().getComplemento();
         }

         if (dto.getEndereco().getPontoReferencia() != null)
         {
            pontoReferencia = " - " + dto.getEndereco().getPontoReferencia();
         }

         descricaoEndereco += "CEP: " + dto.getEndereco().getCepFormatado() + " ";
         descricaoEndereco += "Rua: " + logradouro + " N. " + dto.getEndereco().getNumero() + " Complemento: ";
         descricaoEndereco += complemento + ", Referência: " + pontoReferencia;

         dto.setDescricaoEndereco(descricaoEndereco);
      }

      if (dto.getListaVagaDia() != null)
      {
         for (VagaDiaDTO vagaDia : dto.getListaVagaDia())
         {
            // DATA HORA SERVICO

            descricaoDias += new SimpleDateFormat("dd/MM/yyyy").format(vagaDia.getData());
            descricaoDias += " - ";
            descricaoDias += vagaDia.getHorarioEntrada().substring(0, 2) + ":" + vagaDia.getHorarioEntrada().substring(2, 4);
            descricaoDias += " às ";
            descricaoDias += vagaDia.getHorarioSaida().substring(0, 2) + ":" + vagaDia.getHorarioSaida().substring(2, 4);

            descricaoDias += "; ";
         }

         dto.setDescricaoDia(descricaoDias);
      }

      return dto;
   }

}
