package br.com.aptare.cefit.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.acao.dto.AcaoDTO;
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
      if(entity.getCodigoEspaco() == null 
            || entity.getCodigoEspaco() <= 0
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
      return new String[] { "espaco", "tipoAcao", "listaAcaoProfissional*.profissional*.cadastroUnico" };
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "nome" };
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
      
      return dto;
   }

}
