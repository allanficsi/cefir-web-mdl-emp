package br.com.aptare.cefit.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.painelEletronico.entity.TipoSenha;
import br.com.aptare.cefit.painelEletronico.service.TipoSenhaService;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;

@RestController
@RequestMapping("/api/tipoSenha")
@CrossOrigin(origins = "*")
public class TipoSenhaController extends AptareCrudController<TipoSenha, TipoSenhaService>
{
   public TipoSenhaController()
   {
      super(TipoSenhaService.getInstancia());
   }
   
   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, TipoSenha tipoSenha, String status) throws AptareException
   {
      tipoSenha.setFlagAtivo(status);
      tipoSenha.setAuditoria(new Auditoria());
      tipoSenha.getAuditoria().setDataAlteracao(new Date());
      tipoSenha.getAuditoria().setCodigoUsuarioAlteracao(this.getUsuarioFromRequest(request).getCodigo());
   }
   
   @Override
   protected void ativarInativar(TipoSenha entity) throws AptareException
   {
      getService().ativarInativar(entity);
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "descricao" };
   }
}
