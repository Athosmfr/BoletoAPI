package processa.remessa.remessaAPI.controller;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author Gabriel Ferraro
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RemessaDTO {
     
    private Long id;
    private String pagador;
    private String nomeBeneficiario;
    private Date vencimentoRemessa;
    private Double valorRemessa;
}