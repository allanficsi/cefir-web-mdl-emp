package br.com.aptare.cefit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.acao.dto.AcaoDTO;
import br.com.aptare.cefit.acao.dto.AgendaDTO;
import br.com.aptare.cefit.acao.entity.Acao;
import br.com.aptare.cefit.acao.service.AcaoService;
import br.com.aptare.fda.exception.AptareException;

@RestController
@RequestMapping("/api/acao")
@CrossOrigin(origins = "*")
public class AcaoController extends AptareCrudController<Acao, AcaoService>
{
   public AcaoController()
   {
      super(AcaoService.getInstancia());
   }
   
   @Override
   protected void ativarInativar(Acao entity) throws AptareException
   {
      this.getService().ativarInativar(entity);
   }
   
   @Override
   protected void completarInserir(Acao entity, HttpServletRequest request)
   {
      // Se espaco, agenda ou profissional nao estiver preenchidos, situacao = PENDENTE else ATIVA
      if(entity.getCodigoEsp() == null 
            || entity.getCodigoEsp() <= 0
            || entity.getListaAgenda() == null 
            || entity.getListaAgenda().size() <= 0
            || entity.getListaAcaoProfissional() == null
            || entity.getListaAcaoProfissional().size() <= 0)
      {
         entity.setSituacao(AcaoService.ACAO_PENDENTE);
      }
      else 
      {
         entity.setSituacao(AcaoService.ACAO_ATIVA);
      }
   }

   @Override
   protected String[] juncaoPesquisar()
   {
      return new String[] { "espaco", "tipoAcao" };
   }
   
   @Override
   protected String[] juncaoGet()
   {
      return new String[] { "espaco", "tipoAcao", "listaAcaoProfissional*.profissional*.cadastroUnico", "listaAgenda*", "auditoria.usuarioInclusao" };
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "nome" };
   }
   
   @Override
   protected String[] ordenacaoGet()
   {
      return new String[] { "listaAgenda.dataAgenda" };
   }

   @Override
   protected Object atualizarEntidadeResponse(Acao acao)
   {
      AcaoDTO dto = new AcaoDTO();
      dto = this.convertToDto(acao);
      return dto;
   }

   @Override
   protected List<Object> atualizarListaResponse(List<Acao> lista)
   {
      return lista.stream().map(acao -> convertToDto(acao)).collect(Collectors.toList());
   }

   private AcaoDTO convertToDto(Acao acao)
   {
      AcaoDTO dto = new AcaoDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(acao, dto);
      
      //Ordenando listaAgenda
      if(dto != null 
            && dto.getListaAgenda() != null
            && dto.getListaAgenda().size() > 0) 
      {
         List<AgendaDTO> listaAgenda = new ArrayList<AgendaDTO>(dto.getListaAgenda());
         
         Collections.sort(listaAgenda, new Comparator<AgendaDTO>()
         {
            @Override
            
            public int compare(AgendaDTO a1, AgendaDTO a2)
            {

               return a1.getDataAgenda().compareTo(a2.getDataAgenda());
            }
         });
         
         dto.setListaAgendaOrdenada(listaAgenda);
      }
      
      return dto;
   }

}
