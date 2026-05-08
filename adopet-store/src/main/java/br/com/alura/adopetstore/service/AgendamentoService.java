package br.com.alura.adopetstore.service;

import br.com.alura.adopetstore.email.EmailRelatorioGerado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class AgendamentoService {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private EmailRelatorioGerado enviador;

    @Scheduled(cron = "0 0 2 * * *")
    public void envioEmailsAgendado(){
        var estoqueZerado = relatorioService.infoEstoque();
        var faturamentoObtido = relatorioService.faturamentoObtido();

        // Aguarda a conclusão de múltiplos CompletableFuture ao mesmo tempo.
        // O método allOf recebe vários futures (estoqueZerado, faturamentoObtido) e cria
        // um novo CompletableFuture que só é concluído quando TODOS os futures passados
        // como parâmetro terminarem.
        // O .join() força a espera (bloqueio) até que esse CompletableFuture combinado
        // esteja concluído, garantindo que tanto estoqueZerado quanto faturamentoObtido
        // tenham terminado antes de seguir a execução.
        CompletableFuture.allOf(estoqueZerado, faturamentoObtido).join();

        // Recupera os dados do CompletableFuture, já que o Scheduled não permite retorno sem ser vazio e não consegue recuperar dados antigos
        try {
            enviador.enviar(estoqueZerado.get(), faturamentoObtido.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Thread do agendamento: " + Thread.currentThread().getName());
    }

}