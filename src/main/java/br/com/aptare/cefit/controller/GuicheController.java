package br.com.aptare.cefit.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.painelEletronico.entity.Guiche;
import br.com.aptare.cefit.painelEletronico.service.GuicheService;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;

@RestController
@RequestMapping("/api/guiche")
@CrossOrigin(origins = "*")
public class GuicheController extends AptareCrudController<Guiche, GuicheService>
{
   public GuicheController()
   {
      super(GuicheService.getInstancia());
   }
   
   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, Guiche guiche, String status) throws AptareException
   {
      guiche.setFlagAtivo(status);
      guiche.setAuditoria(new Auditoria());
      guiche.getAuditoria().setDataAlteracao(new Date());
      guiche.getAuditoria().setCodigoUsuarioAlteracao(this.getUsuarioFromRequest(request).getCodigo());
   }
   
   @Override
   protected void ativarInativar(Guiche entity) throws AptareException
   {
      getService().ativarInativar(entity);
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "descricao" };
   }
}
