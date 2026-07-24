package LeilaoOnlineJUnit.infra;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageRestError {

    private HttpStatus status;
    private String mensagem;
    private LocalDateTime dataHora;

    MessageRestError(HttpStatus status, String mensagem)
    {
        this.status = status;
        this.mensagem = mensagem;
        this.dataHora = LocalDateTime.now();
    }

}
