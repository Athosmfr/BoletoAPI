package processa.remessa.remessaAPI.service;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import processa.remessa.remessaAPI.controller.RemessaDTO;
import processa.remessa.remessaAPI.model.Remessa;
import processa.remessa.remessaAPI.repository.RemessaRepository;

/**
 *
 * @author Gabriel Ferraro
 */
@Service
public class RemessaService {

    @Autowired
    private RemessaRepository remessaRepository;
    
    public List<Remessa> getAllRemessas() {
        return remessaRepository.findAll();
    }

    public Remessa getRemessaById(Long id) {
        return remessaRepository.findById(id)
                .orElseThrow(() -> new Error("Can't get client; id: " + id));
    }

    public Remessa saveRemessa(Remessa remessa) {
        return remessaRepository.save(remessa);
    }

    public Remessa fromDTO(RemessaDTO remessa) {
        return new Remessa(
            remessa.getId(),
            remessa.getPagador(),
            remessa.getNomeBeneficiario(),
            remessa.getVencimentoRemessa(),
            remessa.getValorRemessa()
        );
    }
}
