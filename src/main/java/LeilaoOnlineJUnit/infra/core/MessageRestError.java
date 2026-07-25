package LeilaoOnlineJUnit.infra.core;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MessageRestError {

    private HttpStatus status;
    private String mensagem;
    private LocalDateTime dataHora;

    private Map<String,String> camposErros = new HashMap<>();

    MessageRestError(HttpStatus status, String mensagem)
    {
        this.status = status;
        this.mensagem = mensagem;
        this.dataHora = LocalDateTime.now();
    }

    MessageRestError(HttpStatus status, String mensagem, Map<String,String> camposErros)
    {
        this.status = status;
        this.mensagem = mensagem;
        this.dataHora = LocalDateTime.now();
        this.camposErros = camposErros;
    }

}
