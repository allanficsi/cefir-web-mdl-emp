package br.com.aptare.cefit.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cadastroUnico.entidade.Cargo;
import br.com.aptare.cadastroUnico.servico.CargoService;
import br.com.aptare.fda.exception.AptareException;

@RestController
@RequestMapping("/api/cargo")
@CrossOrigin(origins = "*")
public class CargoController extends AptareCrudController<Cargo, CargoService>
{
   public CargoController()
   {
      super(CargoService.getInstancia());
   }
   
   @Override
   protected void atualizarStatusEntidade(HttpServletRequest request, Cargo cargo, String status) throws AptareException
   {
      cargo.setFlagAtivo(status);
      cargo.getAuditoria().setDataAlteracao(new Date());
      cargo.getAuditoria().setCodigoUsuarioAlteracao(super.getUsuarioFromRequest(request).getCodigo());
   }
   
   @Override
   protected String[] getOrdenacaoPesquisar()
   {
      return new String[] { "descricao" };
   }
}
