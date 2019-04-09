package br.com.aptare.cefit.controller.aplicativo;

import java.util.Date;
import java.util.LinkedHashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cadastroUnico.entidade.CadastroUnico;
import br.com.aptare.cadastroUnico.entidade.PessoaFisica;
import br.com.aptare.cadastroUnico.entidade.TelefonePF;
import br.com.aptare.cefit.response.Response;
import br.com.aptare.cefit.seguranca.dto.UsuarioDTO;
import br.com.aptare.cefit.trabalhador.service.TrabalhadorService;
import br.com.aptare.cefit.util.RetirarLazy;
import br.com.aptare.fda.exception.AptareException;
import br.com.aptare.seguranca.entidade.Auditoria;
import br.com.aptare.seguranca.entidade.Usuario;
import br.com.aptare.seguranca.servico.UsuarioService;

@RestController
@RequestMapping("/aplicativo/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController
{
   
   @PostMapping(path = "/logar")
   public ResponseEntity<Response<UsuarioDTO>> logar(HttpServletRequest request, @RequestBody UsuarioDTO to)
   {
      Response<UsuarioDTO> response = new Response<UsuarioDTO>();
      try
      {
         Usuario filter = new Usuario();
         
         filter.setLogin(to.getLogin());
         filter = UsuarioService.getInstancia().get(filter, null, null);
         
         if (!to.getSenha().equals(filter.getSenha()))
         {
            throw new AptareException("msg.geral", new String[] {"O Usuário ou senha inválido."});
         }
         
         filter = new RetirarLazy<Usuario>(filter).execute();
         
         if(filter != null) 
         {
            UsuarioDTO retorno = new UsuarioDTO();
            retorno.setCodigo(filter.getCodigo());
            retorno.setCodigoCadastroUnico(filter.getCodigoCadastroUnico());
            retorno.setNome(filter.getNome());
            response.setData(retorno);
         }
      }
      catch (Exception e)
      {
         if (e instanceof AptareException)
         {
            response.getErrors().add(((AptareException)e).getMensagem()); 
         }
         else
         {
            response.getErrors().add(e.getMessage());
         }
         return ResponseEntity.badRequest().body(response);
      }
      
      return ResponseEntity.ok(response);
   }
   
   @PostMapping(path = "/cadastrar")
   public ResponseEntity<Response<UsuarioDTO>> cadastrar(HttpServletRequest request, @RequestBody UsuarioDTO to)
   {
      Response<UsuarioDTO> response = new Response<UsuarioDTO>();
      try
      {
         Usuario entity = new Usuario();
         
         // dados usuario
         entity.setLogin(to.getLogin());
         entity.setNome(to.getNome());
         entity.setSenha(to.getSenha());
         entity.setSituacao(UsuarioService.SITUACAO_ATIVO);
         entity.setFlagAdministrador("N");
         entity.setAuditoria(new Auditoria());
         entity.getAuditoria().setCodigoUsuarioInclusao(1L);
         entity.getAuditoria().setDataInclusao(new Date());
         
         // dados cadastro unico
         entity.setCadastroUnico(new CadastroUnico());
         entity.getCadastroUnico().setCpfCnpj(to.getCpf());
         entity.getCadastroUnico().setNome(to.getNome());
         entity.getCadastroUnico().setPessoaFisica(new PessoaFisica());
         entity.getCadastroUnico().getPessoaFisica().setDataNascimento(to.getDataNascimento());
         entity.getCadastroUnico().setAuditoria(new Auditoria());
         entity.getCadastroUnico().getAuditoria().setCodigoUsuarioInclusao(1L);
         entity.getCadastroUnico().getAuditoria().setDataInclusao(new Date());
         
         // dados celular
         TelefonePF celular = new TelefonePF();
         celular.setDdd(85);
         celular.setNumero(to.getCelular().intValue());
         celular.setFlagAtivo("S");
         celular.setTipo(1);
         celular.setFlagWhats(false);
         celular.setFlagPrincipal("S");
         celular.setFlagSms("N");
         celular.setAuditoria(new Auditoria());
         celular.getAuditoria().setCodigoUsuarioInclusao(1L);
         celular.getAuditoria().setDataInclusao(new Date());
         entity.getCadastroUnico().getPessoaFisica().setListaTelefone(new LinkedHashSet<TelefonePF>());
         entity.getCadastroUnico().getPessoaFisica().getListaTelefone().add(celular);
         
         TrabalhadorService.getInstancia().cadastrarUsuario(entity);
         
         if(entity != null) 
         {
            UsuarioDTO retorno = new UsuarioDTO();
            retorno.setCodigo(retorno.getCodigo());
            retorno.setCodigoCadastroUnico(retorno.getCodigoCadastroUnico());
            response.setData(retorno);
         }
      }
      catch (Exception e)
      {
         if (e instanceof AptareException)
         {
            response.getErrors().add(((AptareException)e).getMensagem()); 
         }
         else
         {
            response.getErrors().add(e.getMessage());
         }
         return ResponseEntity.badRequest().body(response);
      }
      
      return ResponseEntity.ok(response);
   }
   
   public String MD5(String md5)
   {
      try
      {
         java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
         byte[] array = md.digest(md5.getBytes());
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < array.length; ++i)
         {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
         }
         return sb.toString();
      }
      catch (java.security.NoSuchAlgorithmException e)
      {
      }
      return null;
   }

}
