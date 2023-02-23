package br.com.exemplo.comum.infrastructure.job;

import br.com.exemplo.comum.domain.service.ClassificacaoOrcamentariaService;
import br.com.exemplo.comum.domain.service.PlanoDeContasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class CacheRedisSchedule {

    private final PlanoDeContasService planoDeContasService;
    private final ClassificacaoOrcamentariaService classificacaoOrcamentariaService;

    public CacheRedisSchedule(PlanoDeContasService planoDeContasService,
                              ClassificacaoOrcamentariaService classificacaoOrcamentariaService) {
        this.planoDeContasService = planoDeContasService;
        this.classificacaoOrcamentariaService = classificacaoOrcamentariaService;
    }
    @Scheduled(fixedDelay = 630000)
    public void iniciaCache(){
        log.info("Iniciando a preparaçao de caches no Redis.");

        log.info("Iniciando cache para Planos de contas.");
        planoDeContasService.contaPaisPlanos();
        planoDeContasService.pesquisaPaisPlano();

        log.info("Iniciando cache para Classificações orçamentárias.");
        classificacaoOrcamentariaService.contaPaisPlanos();
        classificacaoOrcamentariaService.pesquisaPaisPlanos();
    }
}
