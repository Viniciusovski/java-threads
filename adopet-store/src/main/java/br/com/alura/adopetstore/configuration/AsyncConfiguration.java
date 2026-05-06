package br.com.alura.adopetstore.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 Classe de configuração que registra um Executor assíncrono.
 O ThreadPoolTaskExecutor cria um pool de threads com:
 - 3 threads fixas (core e máximo)
 - Fila de até 100 tarefas
 - Prefixo "AsynchThread-" nos nomes das threads
 Esse Executor será usado pelo Spring para rodar métodos assíncronos (@Async).
**/
@Configuration
public class AsyncConfiguration {

    @Bean
    public Executor asyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsynchThread-");
        executor.initialize();
        return executor;
    }
}
