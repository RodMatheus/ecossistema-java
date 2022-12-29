package br.com.exemplo.comum.api.v1.filler;

import br.com.exemplo.comum.api.v1.model.dto.PlanoDeContasDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class PlanoDeContasDTOFiller {

    public void fill(final List<PlanoDeContasDTO> planosDeContasDTOS) {
        log.info("Adicionando numeração de itens.");
        this.planosDeContasDTONumbered(planosDeContasDTOS, Strings.EMPTY,  new AtomicLong(1));
    }

    private void planosDeContasDTONumbered(List<PlanoDeContasDTO> planosDeContasDTO, String numeracao, AtomicLong contador) {
        for(PlanoDeContasDTO plano : planosDeContasDTO) {
            planosDeContasDTO.set(planosDeContasDTO.indexOf(plano), plano.withNumeracao(numeracao + contador.toString()));

            if(!CollectionUtils.isEmpty(plano.filhos())) {
                this.planosDeContasDTONumbered(plano.filhos(),numeracao + contador + ".", new AtomicLong(contador.getAndIncrement()));
            }
        }
    }
}