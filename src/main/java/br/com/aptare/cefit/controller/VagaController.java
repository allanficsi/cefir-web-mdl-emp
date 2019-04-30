package br.com.aptare.cefit.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aptare.cefit.vagas.dto.VagaDTO;
import br.com.aptare.cefit.vagas.entity.Vaga;
import br.com.aptare.cefit.vagas.service.VagaService;

@RestController
@RequestMapping("/api/vaga")
@CrossOrigin(origins = "*")
public class VagaController extends AptareCrudController<Vaga, VagaService>
{
   public VagaController()
   {
      super(VagaService.getInstancia());
   }

   @Override
   protected Object atualizarEntidadeResponse(Vaga vaga)
   {
      VagaDTO dto = new VagaDTO();
      dto = this.convertToDto(vaga);
      return dto;
   }

   @Override
   protected List<Object> atualizarListaResponse(List<Vaga> lista)
   {
      return lista.stream().map(vaga -> convertToDto(vaga)).collect(Collectors.toList());
   }

   private VagaDTO convertToDto(Vaga vaga)
   {
      VagaDTO dto = new VagaDTO();
      modelMapper.getConfiguration().setAmbiguityIgnored(true);
      modelMapper.map(vaga, dto);
      
      return dto;
   }

}
