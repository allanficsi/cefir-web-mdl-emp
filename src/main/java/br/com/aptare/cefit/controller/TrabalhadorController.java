package br.com.aptare.cefit.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
      return new String[] { "cadastroUnico.pessoaFisica" };
   }

   @Override
   protected String[] juncaoGet()
   {
      return new String[] { "listaTrabalhadorCbo*.cbo*", "listaTrabalhadorDeficiencia*", "cadastroUnico.pessoaFisica.listaTelefone*",
         "cadastroUnico.listaEndereco.correio*", "cadastroUnico.listaEndereco.extensaoEndereco*", "auditoria.usuarioInclusao" };
   }

   @Override
   protected String[] getOrdenacaoPesquisar()
   {
      return new String[] { "cadastroUnico.nome" };
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

}
