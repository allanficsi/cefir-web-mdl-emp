package br.com.aptare.cefit.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import br.com.aptare.cefit.response.Response;
import br.com.aptare.cefit.trabalhador.dto.TrabalhadorAgendaDTO;
import br.com.aptare.cefit.trabalhador.entity.TrabalhadorAgenda;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import br.com.aptare.cefit.empregador.service.EmpregadorService;
import br.com.aptare.cefit.trabalhador.dto.TrabalhadorCboDTO;
import br.com.aptare.cefit.trabalhador.dto.TrabalhadorDTO;
import br.com.aptare.cefit.trabalhador.dto.TrabalhadorDeficienciaDTO;
import br.com.aptare.cefit.trabalhador.entity.Trabalhador;
import br.com.aptare.cefit.trabalhador.entity.TrabalhadorCbo;
import br.com.aptare.cefit.trabalhador.entity.TrabalhadorDeficiencia;
import br.com.aptare.cefit.trabalhador.service.TrabalhadorService;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;

@RestController
@RequestMapping("/api/trabalhador")
@CrossOrigin(origins = "*")
public class TrabalhadorController extends AptareCrudController<Trabalhador, TrabalhadorService>
{

   public TrabalhadorController()
   {
      super(TrabalhadorService.getInstancia());
   }

   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, Trabalhador entity, String status) throws AptareException
   {
      entity.setSituacao(status.equals("S") ? EmpregadorService.SITUACAO_ATIVA : EmpregadorService.SITUACAO_INATIVA);
      entity.setAuditoria(new Auditoria());
      entity.getAuditoria().setDataAlteracao(new Date());
      entity.getAuditoria().setCodigoUsuarioAlteracao(super.getUsuarioFromRequest(request).getCodigo());
   }

   @Override
   protected void ativarInativar(Trabalhador entity) throws AptareException
   {
      this.getService().ativarInativar(entity);
   }

   @Override
   protected String[] juncaoPesquisar()
   {
      return new String[] { "cadastroUnico.pessoaFisica.listaTelefone*",
                            "cadastroUnico.listaEndereco.correio*", "cadastroUnico.listaEndereco.extensaoEndereco*", "auditoria.usuarioInclusao" };
   }

   @Override
   protected String[] juncaoGet()
   {
      return new String[] { "listaTrabalhadorCbo*.cbo*", "listaTrabalhadorDeficiencia*","listaTrabalhadorAgenda*", "cadastroUnico.pessoaFisica.listaTelefone*",
         "cadastroUnico.listaEndereco.correio*", "cadastroUnico.listaEndereco.extensaoEndereco*", "auditoria.usuarioInclusao" };
   }

   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "cadastroUnico.nome" };
   }

   @Override
   protected void completarAlterar(Trabalhador entity, HttpServletRequest request)
   {
      entity.setSituacao(TrabalhadorService.SITUACAO_ATIVA);
   }
   
   @Override
   protected Object atualizarEntidadeResponse(Trabalhador trabalhador)
   {
      TrabalhadorDTO dto = new TrabalhadorDTO();
      dto = this.convertToDto(trabalhador);
     
      // ATUALIZANDO CADASTRO UNICO
      dto.setCodigoCadastroUnico(dto.getCadastroUnico().getPessoaFisica().getCodigoCadastroUnico());
      dto.getCadastroUnico().setCodigo(dto.getCodigoCadastroUnico());

      // ATUALIZANDO CODIGO CBO e TRABALHADOR (listaTrabalhadorCbo)
      if (trabalhador.getListaTrabalhadorCbo() != null && trabalhador.getListaTrabalhadorCbo().size() > 0)
      {
         List<TrabalhadorCbo> lista = new ArrayList<TrabalhadorCbo>(trabalhador.getListaTrabalhadorCbo());
         List<TrabalhadorCboDTO> listaDTO = new ArrayList<TrabalhadorCboDTO>(dto.getListaTrabalhadorCbo());

         for (int i = 0; i < lista.size(); i++)
         {
            listaDTO.get(i).setCodigoTrabalhador(lista.get(i).getCodigoTrabalhador());
            listaDTO.get(i).setCodigoCbo(lista.get(i).getCodigoCbo());
         }
      }
      
      // ATUALIZANDO CODIGO TRABALHADOR (listaTrabalhadorDeficiencia
      if (trabalhador.getListaTrabalhadorDeficiencia() != null && trabalhador.getListaTrabalhadorDeficiencia().size() > 0)
      {
         List<TrabalhadorDeficiencia> lista = new ArrayList<TrabalhadorDeficiencia>(trabalhador.getListaTrabalhadorDeficiencia());
         List<TrabalhadorDeficienciaDTO> listaDTO = new ArrayList<TrabalhadorDeficienciaDTO>(dto.getListaTrabalhadorDeficiencia());

         for (int i = 0; i < lista.size(); i++)
         {
            listaDTO.get(i).setCodigoTrabalhador(lista.get(i).getCodigoTrabalhador());
         }
      }

      //ATUALIZANDO CODIGO TRABALHADOR (listaTrabalhadorAgenda
      if (trabalhador.getListaTrabalhadorAgenda() != null && trabalhador.getListaTrabalhadorAgenda().size() > 0)
      {
         List<TrabalhadorAgenda> lista = new ArrayList<TrabalhadorAgenda>(trabalhador.getListaTrabalhadorAgenda());
         List<TrabalhadorAgendaDTO> listaDTO = new ArrayList<TrabalhadorAgendaDTO>(dto.getListaTrabalhadorAgenda());

         for (int i = 0; i < lista.size(); i++)
         {
            listaDTO.get(i).setCodigoTrabalhador(lista.get(i).getCodigoTrabalhador());
         }
      }
      return dto;
   }

   @Override
   protected List<Object> atualizarListaResponse(List<Trabalhador> lista)
   {
      return lista.stream().map(trabalhador -> convertToDto(trabalhador)).collect(Collectors.toList());
   }

   private TrabalhadorDTO convertToDto(Trabalhador trabalhador)
   {      
      TrabalhadorDTO dto = new TrabalhadorDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(trabalhador, dto);

      return dto;

   }
   @PostMapping(path = "/salvarManutencao")
   public ResponseEntity<Response<Object>> salvarManutencao(HttpServletRequest request, @RequestBody Trabalhador entity, BindingResult result)
   {
      Response<Object> response = new Response<Object>();
      try
      {
         validarInserir(entity, result);
         if (result.hasErrors())
         {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
         }

         getService().salvarManutencao(entity);
      }
      catch (AptareException e)
      {
         response.getErrors().add(e.getMensagem());
         return ResponseEntity.badRequest().body(response);
      }
      return ResponseEntity.ok(response);
   }

}
