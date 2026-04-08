package br.com.alura;

import java.math.BigDecimal;

public class AppBanco {
    public static void main(String[] args) {
        var cliente = new Cliente("João");
        var conta = new Conta(cliente, new BigDecimal("150"));

        var operacao = new OperacaoSaque(conta, new BigDecimal("150"));

        Thread saqueJoao = new Thread(operacao);
        Thread saqueMaria = new Thread(operacao);

        saqueJoao.start();
        saqueMaria.start();

        System.out.println("Thread: --" + Thread.currentThread().getName());

        try {
            // O metodo join() é usado para fazer a thread atual (main, por exemplo)
            // "esperar" até que outra thread termine sua execução.
            //
            // Nesse caso, saqueJoao.join() faz com que o fluxo pare aqui até que
            // a thread saqueJoao finalize.
            //
            // Depois disso, saqueMaria.join() garante que a thread saqueMaria também
            // termine antes do programa continuar.
            //
            // Isso é importante em cenários de paralelismo para sincronizar as threads,
            // evitando que o código continue antes que tarefas concorrentes sejam concluídas.
            saqueJoao.join();
            saqueMaria.join();
        } catch (InterruptedException e) {
            // Essa exceção ocorre caso a thread atual seja interrompida enquanto espera
            // outras threads terminarem. Aqui estamos apenas encapsulando em RuntimeException.
            throw new RuntimeException(e);
        }
        System.out.println("Saldo final: " + conta.getSaldo());
//
//        // saque do João
//        operacao.executa();
//        // saque da maria
//        operacao.executa();
    }
}
