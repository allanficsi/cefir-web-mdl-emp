package br.com.aptare.cefit.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.espaco.entity.ItemEspaco;
import br.com.aptare.cefit.espaco.service.ItemEspacoService;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;

@RestController
@RequestMapping("/api/itemEspaco")
@CrossOrigin(origins = "*")
public class ItemEspacoController extends AptareCrudController<ItemEspaco, ItemEspacoService>
{
   public ItemEspacoController()
   {
      super(ItemEspacoService.getInstancia());
   }
   
   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, ItemEspaco itemEspaco, String status) throws AptareException
   {
      itemEspaco.setFlagAtivo(status);
      itemEspaco.setAuditoria(new Auditoria());
      itemEspaco.getAuditoria().setDataAlteracao(new Date());
      itemEspaco.getAuditoria().setCodigoUsuarioAlteracao(this.getUsuarioFromRequest(request).getCodigo());
   }
   
   @Override
   protected void ativarInativar(ItemEspaco entity) throws AptareException
   {
      getService().ativarInativar(entity);
   }
   
   @Override
   protected String[] getOrdenacaoPesquisar()
   {
      return new String[] { "descricao" };
   }
}
