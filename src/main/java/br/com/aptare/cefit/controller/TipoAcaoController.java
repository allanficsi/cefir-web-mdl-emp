package br.com.aptare.cefit.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.acao.entity.TipoAcao;
import br.com.aptare.cefit.acao.service.TipoAcaoService;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;

@RestController
@RequestMapping("/api/tipoAcao")
@CrossOrigin(origins = "*")
public class TipoAcaoController extends AptareCrudController<TipoAcao, TipoAcaoService>
{
   public TipoAcaoController()
   {
      super(TipoAcaoService.getInstancia());
   }
   
   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, TipoAcao tipoAcao, String status) throws AptareException
   {
      tipoAcao.setFlagAtivo(status);
      tipoAcao.setAuditoria(new Auditoria());
      tipoAcao.getAuditoria().setDataAlteracao(new Date());
      tipoAcao.getAuditoria().setCodigoUsuarioAlteracao(this.getUsuarioFromRequest(request).getCodigo());
   }
   
   @Override
   protected void ativarInativar(TipoAcao entity) throws AptareException
   {
      getService().ativarInativar(entity);
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "descricao" };
   }
}
