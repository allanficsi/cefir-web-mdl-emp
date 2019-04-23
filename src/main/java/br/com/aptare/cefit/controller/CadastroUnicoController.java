package br.com.aptare.cefit.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cadastroUnico.entidade.CadastroUnico;
import br.com.aptare.cadastroUnico.servico.CadastroUnicoService;
import br.com.aptare.cefit.cadastroUnico.dto.CadastroUnicoDTO;

@RestController
@RequestMapping("/api/cadastroUnico")
@CrossOrigin(origins = "*")
public class CadastroUnicoController extends AptareCrudController<CadastroUnico, CadastroUnicoService>
{
   public CadastroUnicoController()
   {
      super(CadastroUnicoService.getInstancia());
   }

   @Override
   protected String[] juncaoPesquisar()
   {
      return new String[] { "pessoaFisica" };
   }
   
   @Override
   protected String[] juncaoGet()
   {
      //return new String[] { "pessoaFisica.listaTelefone*", "listaEndereco.extensaoEndereco*", "listaEndereco.correio*", "auditoria.usuarioInclusao" };
      return new String[] { "pessoaFisica*.listaTelefone*", "pessoaJuridica*.listaContato*.cargo*", 
                            "pessoaJuridica*.listaContato*.listaTelefone*.auditoria*", 
                            "listaEndereco.extensaoEndereco*", "listaEndereco.correio*", "auditoria.usuarioInclusao" };
   }
   
   @Override
   protected String[] ordenacaoPesquisar()
   {
      return new String[] { "nome" };
   }

   @Override
   protected Object atualizarEntidadeResponse(CadastroUnico cadastroUnico)
   {
      CadastroUnicoDTO dto = null;

      if(cadastroUnico != null)
      {
         dto = this.convertToDto(cadastroUnico);
      }
      
      return dto;
   }

   @Override
   protected List<Object> atualizarListaResponse(List<CadastroUnico> lista)
   {
      return lista.stream().map(cadastroUnico -> convertToDto(cadastroUnico)).collect(Collectors.toList());
   }

   private CadastroUnicoDTO convertToDto(CadastroUnico cadastroUnico)
   {
      CadastroUnicoDTO dto = new CadastroUnicoDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(cadastroUnico, dto);
      
      return dto;
   }

}
