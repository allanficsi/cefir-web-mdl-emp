package br.com.aptare.cefit.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.espaco.entity.Local;
import br.com.aptare.cefit.espaco.service.LocalService;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;

@RestController
@RequestMapping("/api/local")
@CrossOrigin(origins = "*")
public class LocalController extends AptareCrudController<Local, LocalService>
{
   public LocalController()
   {
      super(LocalService.getInstancia());
   }
   
   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, Local localidade, String status) throws AptareException
   {
      localidade.setFlagAtivo(status);
      localidade.setAuditoria(new Auditoria());
      localidade.getAuditoria().setDataAlteracao(new Date());
      localidade.getAuditoria().setCodigoUsuarioAlteracao(this.getUsuarioFromRequest(request).getCodigo());
   }
   
   @Override
   protected void ativarInativar(Local entity) throws AptareException
   {
      getService().ativarInativar(entity);
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "nome" };
   }
}
