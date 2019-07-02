package br.com.aptare.cefit.controller;

import br.com.aptare.cefit.empregador.dto.EmpregadorDTO;
import br.com.aptare.cefit.empregador.entity.Empregador;
import br.com.aptare.cefit.response.Response;
import br.com.aptare.cefit.seguranca.dto.UsuarioDTO;
import br.com.aptare.cefit.usuarioExtends.service.UsuarioServiceCefit;
import br.com.aptare.cefit.util.RetirarLazy;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;
import br.com.aptare.seguranca.entidade.Usuario;
import br.com.aptare.seguranca.servico.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioControllerCefit extends AptareCrudController<Usuario,UsuarioService>
{

   @PostMapping(path = "/externo/externo/resetarSenha")
   public ResponseEntity<Response<Object>> resetarSenha(HttpServletRequest request, @RequestBody Usuario entity)
   {
      Response<Object> response = new Response<Object>();
      try
      {
         Usuario objAtualizar = new RetirarLazy<Usuario>(UsuarioServiceCefit.getInstancia().resetarSenha(entity)).execute();
         response.setData(objAtualizar);
      }
      catch (AptareException e)
      {
         response.getErrors().add(e.getMensagem());
         return ResponseEntity.badRequest().body(response);
      }
      return ResponseEntity.ok(response);
   }

   @PostMapping(path = "/redefinirSenha")
   public ResponseEntity<Response<Object>> redefinirSenha(HttpServletRequest request, @RequestBody Usuario usuario) throws AptareException {
      Response<Object> response = new Response<Object>();
      try
      {
          //AUDITORIA
         Auditoria auditoria = new Auditoria();
         auditoria.setCodigoUsuarioAlteracao(usuario.getCodigo());
         auditoria.setDataAlteracao(new Date());
         usuario.setAuditoria(auditoria);

         //ALTERANDO A SENHA
         UsuarioService.getInstancia().alterarSenha(usuario);

         //ENVIANDO RESPOSTA
         response.setData(this.atualizarEntidadeResponse(usuario));
      }
      catch (AptareException e)
      {
         response.getErrors().add(e.getMensagem());
         return ResponseEntity.badRequest().body(response);
      }
      return ResponseEntity.ok(response);
   }

    protected Object atualizarEntidadeResponse(Usuario usuario)
    {
        UsuarioDTO dto = null;

        if(usuario != null)
        {
            dto = this.convertToDto(usuario);
        }

        return dto;
    }
    private UsuarioDTO convertToDto(Usuario usuario)
   {
      UsuarioDTO dto = new UsuarioDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(usuario, dto);

      return dto;
   }

}
