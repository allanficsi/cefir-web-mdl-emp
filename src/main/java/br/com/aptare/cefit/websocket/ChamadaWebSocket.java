package br.com.aptare.cefit.websocket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ChamadaWebSocket
{

   private final SimpMessagingTemplate template;

   @Autowired
   public ChamadaWebSocket(final SimpMessagingTemplate template)
   {
      this.template = template;
   }

   @MessageMapping("/chamar")
   public void onReceiveMessage(@Nullable final String message)
   {
      this.template.convertAndSend("/chamada",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ": " + message);
   }
}
