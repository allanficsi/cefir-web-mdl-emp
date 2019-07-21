package br.com.aptare.cefit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import br.com.aptare.cefit.trabalhador.dto.*;
import br.com.aptare.cefit.trabalhador.entity.*;
import br.com.aptare.cefit.trabalhador.service.TrabalhadorPresencaService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.cadastroUnico.dto.TelefoneDTO;
import br.com.aptare.cefit.empregador.service.EmpregadorService;
import br.com.aptare.cefit.response.Response;
import br.com.aptare.cefit.trabalhador.dto.TrabalhadorAgendaDTO;
import br.com.aptare.cefit.trabalhador.dto.TrabalhadorCboDTO;
import br.com.aptare.cefit.trabalhador.dto.TrabalhadorDTO;
import br.com.aptare.cefit.trabalhador.dto.TrabalhadorDeficienciaDTO;
import br.com.aptare.cefit.trabalhador.dto.TrabalhadorLogDTO;
import br.com.aptare.cefit.trabalhador.entity.Trabalhador;
import br.com.aptare.cefit.trabalhador.entity.TrabalhadorAgenda;
import br.com.aptare.cefit.trabalhador.entity.TrabalhadorCbo;
import br.com.aptare.cefit.trabalhador.entity.TrabalhadorDeficiencia;
import br.com.aptare.cefit.trabalhador.service.TrabalhadorService;
import br.com.aptare.cefit.util.CefitUtils;
import br.com.aptare.cefit.util.RetirarLazy;
import br.com.aptare.cefit.vagas.entity.Vaga;
import br.com.aptare.cefit.vagas.service.EncaminhamentoService;
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

    @PostMapping(path = "/listarTrabalhadoresDisponiveis")
    public ResponseEntity<Response<List<Object>>> listarTrabalhadoresDisponiveis(HttpServletRequest request, @RequestBody Vaga vaga)
    {
        Response<List<Object>> response = new Response<List<Object>>();
        try
        {
            List<Trabalhador> lista = null;
            lista = EncaminhamentoService.getInstancia().listarTrabalhadoresDisponiveis(vaga);

            if (lista != null)
            {
                lista = (List<Trabalhador>) new RetirarLazy<List<Trabalhador>>(lista).execute();
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

    @PostMapping(path = "/salvarManutencao")
    public ResponseEntity<Response<Object>> salvarManutencao(HttpServletRequest request, @RequestBody Trabalhador entity,
                                                             BindingResult result)
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

    @PostMapping(path = "/alterarSituacaoDeIngresso")
    public ResponseEntity<Response<Object>> alterarSituacaoIngresso(HttpServletRequest request, @RequestBody Trabalhador entity)
    {
        Response<Object> response = new Response<Object>();
        try
        {
            Trabalhador objAtualizar = new RetirarLazy<Trabalhador>(getService().alterarSituacaoDeIngresso(entity)).execute();
            response.setData(this.atualizarEntidadeResponse(objAtualizar));
        }
        catch (AptareException e)
        {
            response.getErrors().add(e.getMensagem());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/adicionarRemoverRejeicao")
    public ResponseEntity<Response<Object>> adicionarRemoverRejeicao(HttpServletRequest request, @RequestBody Trabalhador entity)
    {
        Response<Object> response = new Response<Object>();
        try
        {
            Trabalhador objAtualizar = new RetirarLazy<Trabalhador>(getService().adicionarRemoverRejeicao(entity)).execute();
            response.setData(this.atualizarEntidadeResponse(objAtualizar));
        }
        catch (AptareException e)
        {
            response.getErrors().add(e.getMensagem());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/adicionarRemoverPresenca")
    public ResponseEntity<Response<Object>> adicionarRemoverPresenca(HttpServletRequest request, @RequestBody Trabalhador entity)
    {
        Response<Object> response = new Response<Object>();
        try
        {
            Trabalhador objAtualizar = new RetirarLazy<Trabalhador>(getService().adicionarRemoverPresenca(entity)).execute();
            response.setData(this.atualizarEntidadeResponse(objAtualizar));
        }
        catch (AptareException e)
        {
            response.getErrors().add(e.getMensagem());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/listarPresencas")
    public ResponseEntity<Response<Object>> listarPresencas(HttpServletRequest request, @RequestBody TrabalhadorPresenca entity)
    {
        Response<Object> response = new Response<Object>();
        try
        {
            Trabalhador objAtualizar = new RetirarLazy<Trabalhador>(TrabalhadorPresencaService.getInstancia().listarPresencas(entity)).execute();
            response.setData(this.atualizarEntidadeResponse(objAtualizar));
        }
        catch (AptareException e)
        {
            response.getErrors().add(e.getMensagem());
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
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
        return new String[] { "cadastroUnico.pessoaFisica.listaTelefone*.auditoria*.usuarioInclusao*", "cadastroUnico.listaEndereco.correio*",
                "cadastroUnico.listaEndereco.extensaoEndereco*", "auditoria.usuarioInclusao", "auditoria.usuarioAlteracao*" };
    }

    @Override
    protected String[] juncaoGet()
    {
        return new String[] { "listaTrabalhadorCbo*.cbo*", "listaTrabalhadorDeficiencia*", "listaTrabalhadorLog*",
                "cadastroUnico.pessoaFisica.listaTelefone*", "cadastroUnico.listaEndereco.correio*",
                "cadastroUnico.listaEndereco.extensaoEndereco*","cadastroUnico.listaEndereco.auditoria*.usuarioInclusao", "auditoria.usuarioInclusao", "auditoria.usuarioAlteracao*",
                "listaTrabalhadorRejeicao*.empregador*.cadastroUnico*", "listaTrabalhadorAgenda*"};
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
        TrabalhadorDTO dto = this.convertToDto(trabalhador);

        // ATUALIZANDO CADASTRO UNICO
        if (dto.getCadastroUnico() != null)
        {
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

            // ATUALIZANDO CODIGO TRABALHADOR (listaTrabalhadorDeficiencia)
            if (trabalhador.getListaTrabalhadorDeficiencia() != null && trabalhador.getListaTrabalhadorDeficiencia().size() > 0)
            {
                List<TrabalhadorDeficiencia> lista = new ArrayList<TrabalhadorDeficiencia>(trabalhador.getListaTrabalhadorDeficiencia());
                List<TrabalhadorDeficienciaDTO> listaDTO = new ArrayList<TrabalhadorDeficienciaDTO>(dto.getListaTrabalhadorDeficiencia());

                for (int i = 0; i < lista.size(); i++)
                {
                    listaDTO.get(i).setCodigoTrabalhador(lista.get(i).getCodigoTrabalhador());
                }
            }

            // ATUALIZANDO CODIGO TRABALHADOR (listaTrabalhadorAgenda
            if (trabalhador.getListaTrabalhadorAgenda() != null && trabalhador.getListaTrabalhadorAgenda().size() > 0)
            {
                List<TrabalhadorAgenda> lista = new ArrayList<TrabalhadorAgenda>(trabalhador.getListaTrabalhadorAgenda());
                List<TrabalhadorAgendaDTO> listaDTO = new ArrayList<TrabalhadorAgendaDTO>(dto.getListaTrabalhadorAgenda());

                for (int i = 0; i < lista.size(); i++)
                {
                    listaDTO.get(i).setCodigoTrabalhador(lista.get(i).getCodigoTrabalhador());
                }
            }
            // ATUALIZANDO CODIGO EMPREGADOR e TRABALHADOR (listaTrabalhadorRejeicao)
            if (trabalhador.getListaTrabalhadorRejeicao() != null && trabalhador.getListaTrabalhadorRejeicao().size() > 0) {
                List<TrabalhadorRejeicao> lista = new ArrayList<TrabalhadorRejeicao>(trabalhador.getListaTrabalhadorRejeicao());
                List<TrabalhadorRejeicaoDTO> listaDTO = new ArrayList<TrabalhadorRejeicaoDTO>(dto.getListaTrabalhadorRejeicao());

                for (int i = 0; i < lista.size(); i++) {
                    listaDTO.get(i).setCodigoTrabalhador(lista.get(i).getCodigoTrabalhador());
                    listaDTO.get(i).setCodigoEmpregador(lista.get(i).getCodigoEmpregador());
                }
            }
            return dto;
        }

        //SETANDO CODIGO DO TRABALHADOR
        if (dto.getListaTrabalhadorPresenca() != null) {
            for (TrabalhadorPresencaDTO element : dto.getListaTrabalhadorPresenca()) {
                element.setCodigoTrabalhador(trabalhador.getCodigo());
            }
            return dto;
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
        CefitUtils.atualizarDadosEndereco(trabalhador.getCadastroUnico());

        TrabalhadorDTO dto = new TrabalhadorDTO();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.map(trabalhador, dto);

        if (dto.getCadastroUnico() != null
                && dto.getCadastroUnico().getPessoaFisica() != null
                && dto.getCadastroUnico().getPessoaFisica().getListaTelefone() != null
                && dto.getCadastroUnico().getPessoaFisica().getListaTelefone().size() > 0)
        {
            String telefoneExtenso = "";
            int cont = 1;

            for (TelefoneDTO telefone : dto.getCadastroUnico().getPessoaFisica().getListaTelefone())
            {
                telefoneExtenso += "(" + telefone.getDdd() + ") " + telefone.getNumero();

                if (cont != dto.getCadastroUnico().getPessoaFisica().getListaTelefone().size())
                {
                    telefoneExtenso += ", ";
                }

                cont++;
            }

            dto.setTelefoneExtenso(telefoneExtenso);
        }

        // ORDENA LISTA DE LOG'S POR DATA (DESC)
        if (dto != null && dto.getListaTrabalhadorLog() != null && dto.getListaTrabalhadorLog().size() > 0)
        {
            List<TrabalhadorLogDTO> listaAgenda = new ArrayList<TrabalhadorLogDTO>(dto.getListaTrabalhadorLog());

            Collections.sort(listaAgenda, (a1, a2) -> a2.getDataOperacao().compareTo(a1.getDataOperacao()));

            dto.setListaTrabalhadorLogOrdenada(listaAgenda);
        }

        return dto;
    }

}
