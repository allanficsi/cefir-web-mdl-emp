package br.com.aptare.cefit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import br.com.aptare.cefit.util.RetirarLazy;
import br.com.aptare.cefit.vagas.dto.VagaAgendamentoDTO;
import br.com.aptare.cefit.vagas.dto.VagaDTO;
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
         lista = getService().pesquisar(vaga, new String[] { "listaEncaminhamento", "trabalhadorEntity*.cadastroUnico*.pessoaFisica*", "cboEntity",
                                                             "empregadorEntity.cadastroUnico.pessoaFisica*",
                                                             "empregadorEntity.cadastroUnico.pessoaJuridica*"}, new String[] { "descricao" });
         
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
      return new String[] { "cboEntity*", "empregadorEntity.cadastroUnico.pessoaFisica*",
                            "trabalhadorEntity*.cadastroUnico*.pessoaFisica*",
                            "empregadorEntity.cadastroUnico.pessoaJuridica*", "listaVagaDia*", "listaVagaAgendamento*"};
   }
   
   @Override
   protected String[] juncaoPesquisar()
   {
      return new String[] { "trabalhadorEntity*.cadastroUnico*.pessoaFisica*", "cboEntity" };
   }
   
   @Override
   protected String[] ordenacaoGet()
   {
      return new String[] { "listaVagaAgendamento.numeroDia" };
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
      VagaDTO dto = new VagaDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(vaga, dto);
      
      //Ordenando listaAgenda
      if(dto != null 
            && dto.getListaVagaAgendamento() != null
            && dto.getListaVagaAgendamento().size() > 0) 
      {
         List<VagaAgendamentoDTO> listaAgenda = new ArrayList<VagaAgendamentoDTO>(dto.getListaVagaAgendamento());
         
         Collections.sort(listaAgenda, new Comparator<VagaAgendamentoDTO>()
         {
            @Override
            
            public int compare(VagaAgendamentoDTO a1, VagaAgendamentoDTO a2)
            {
               return a1.getNumeroDia().compareTo(a2.getNumeroDia());
            }
         });
         
         dto.setListaVagaAgendamentoOrdenada(listaAgenda);
      }
      
      return dto;
   }

}
