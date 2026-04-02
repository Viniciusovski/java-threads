package br.com.alura;

import java.math.BigDecimal;

public class OperacaoSaque implements Runnable{

    private Conta conta;
    private BigDecimal valor;

    public OperacaoSaque(Conta conta, BigDecimal valor) {
        this.conta = conta;
        this.valor = valor;
    }

    // synchronized - Serve para evitar que múltiplas threads acessem um recurso ao mesmo tempo
    // Atua nos dados compartilhados
    // Garante consistência (thread safety)
    // Como estivesse dizendo: "Só uma thread por vez pode executar esse trecho"
    // Evita Condição de corrida (race condition)
    public synchronized void executa() {
        System.out.println("Iniciando operação de saque.");
        var saldoAtual = conta.getSaldo();

        if (saldoAtual.compareTo(valor) >= 0) {
            System.out.println("Debitando valor da conta");
            conta.debitaSaldo(valor);
            System.out.println("Saldo atual: " +conta.getSaldo());
        }
        System.out.println("Finalizando operação de saque.");
    }

    @Override
    public void run() {
        executa();
        System.out.println("Thread: --" + Thread.currentThread().getName());
    }
}
