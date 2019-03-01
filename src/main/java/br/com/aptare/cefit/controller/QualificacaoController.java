package br.com.aptare.cefit.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.profissional.entity.Qualificacao;
import br.com.aptare.cefit.profissional.service.QualificacaoService;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;

@RestController
@RequestMapping("/api/qualificacao")
@CrossOrigin(origins = "*")
public class QualificacaoController extends AptareCrudController<Qualificacao, QualificacaoService>
{
   public QualificacaoController()
   {
      super(QualificacaoService.getInstancia());
   }
   
   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, Qualificacao qualificacao, String status) throws AptareException
   {
      qualificacao.setFlagAtivo(status);
      qualificacao.setAuditoria(new Auditoria());
      qualificacao.getAuditoria().setDataAlteracao(new Date());
      qualificacao.getAuditoria().setCodigoUsuarioAlteracao(this.getUsuarioFromRequest(request).getCodigo());
   }
   
   @Override
   protected void ativarInativar(Qualificacao entity) throws AptareException
   {
      getService().ativarInativar(entity);
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "descricao" };
   }
}
