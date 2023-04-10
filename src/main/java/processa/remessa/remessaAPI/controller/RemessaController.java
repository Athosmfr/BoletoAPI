package processa.remessa.remessaAPI.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import processa.remessa.remessaAPI.model.Remessa;
import processa.remessa.remessaAPI.service.RemessaService;

/**
 *
 * @author Gabriel Ferraro
 */
@Tag(name = "Anotação", description = "exemplo")
@RestController
@RequestMapping("/remessa")
public class RemessaController {

    @Autowired
    private RemessaService remessaService;

    /***
     * Retorna todas remessas na uri "/remessa"
     * 
     * @return todas as remessas registradas
     */
    @GetMapping
    @Operation(summary = "Retorna todas as remessas",
            description = "Realiza o retorno de todas as remessas na URI.")
    @ApiResponse(responseCode = "200", description = "Remessas retornadas.")
    @ApiResponse(responseCode = "404", description = "Remessas não encontradas.")
    public ResponseEntity<List<RemessaDTO>> findAll() {
        //adquire remessas
        List<Remessa> remessas = remessaService.getAllRemessas();
        //Constroi as remessas com o dto para exportar objetos na uri
        List<RemessaDTO> remessaDTOList = remessas.stream()
            .map(remessa -> {
                return new RemessaDTO(
                    remessa.getId(),
                    remessa.getPagador(),
                    remessa.getNomeBeneficiario(),
                    remessa.getVencimentoRemessa(),
                    remessa.getValorRemessa()
                );
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(remessaDTOList);
    }
    
    /***
     * Compensa valor a ser pago na remessa. recebe artibutos via body, se o valor enviado for igual ao valor a ser compensado, uma mensagem de sucesso é retornada.
     * 
     * @param clientDTO
     * @return 
     */
    @PostMapping("/compensa_boleto")
    @Operation(summary = "Realiza a compensação do valor a ser pago na remessa.",
            description = "Compensa valor a ser pago na remessa. recebe artibutos via body, se o valor enviado" +
                    " for igual ao valor a ser compensado, uma mensagem de sucesso é retornada")
    @ApiResponse(responseCode = "200", description = "Valor da remessa compensado com sucesso.")
    @ApiResponse(responseCode = "201", description = "Requisição bem sucedida e compensação criada com sucesso.")
    @ApiResponse(responseCode = "400", description = "Erro por valor invalido inserido.")
    public String compensaBoleto(@Parameter(description = "Map de Strings que passa por todos os dados do body para depois atribuir eles a variaveis.", required = true) @RequestBody Map<String, String> body) {
//        @Parameter(description = "Id necessario para buscar a remessa a ser compensada", required = true)
        long id = Long.parseLong(body.get("id"));
        //adquirindo remessa persistida no BD pelo id
        Remessa remessa = remessaService.getRemessaById(id);
        Double valor = Double.parseDouble(body.get("valor"));
        
        String compensacao;
        if(valor > remessa.getValorRemessa()){
            compensacao = "Valor enviado é maior que o valor da remessa!";
        } else if (valor < remessa.getValorRemessa()){
            compensacao = "Valor enviado é insuficiente para compensar remessa!";
        } else {
            compensacao = "Remessa compensada com sucesso!!!";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("id da remssa: ")
        .append(id).append('\n')
        .append("Nome do pagador: ").append(remessa.getPagador()).append('\n')
        .append("Nome da empresa: ").append(remessa.getNomeBeneficiario()).append('\n')
        .append("Data de vencimento da remessa: ").append(remessa.getVencimentoRemessa()).append('\n')
        .append("Valor recebido: ").append(valor).append('\n')
        .append("Valor a ser compensado: ").append(remessa.getValorRemessa()).append('\n')
        .append(compensacao);
        String result = sb.toString();
        return result;
    }
}
