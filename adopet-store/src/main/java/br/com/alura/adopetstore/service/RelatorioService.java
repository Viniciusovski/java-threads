package br.com.alura.adopetstore.service;

import br.com.alura.adopetstore.dto.ProdutoDTO;
import br.com.alura.adopetstore.dto.RelatorioEstoque;
import br.com.alura.adopetstore.dto.RelatorioFaturamento;
import br.com.alura.adopetstore.repository.EstoqueRepository;
import br.com.alura.adopetstore.repository.PedidoRepository;
import br.com.alura.adopetstore.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RelatorioService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Async
    public CompletableFuture<RelatorioEstoque> infoEstoque() {
        // Busca os produtos sem estoque no repositório
        var produtosSemEstoque = estoqueRepository.produtosComEstoqueZerado()
                .stream().map(ProdutoDTO::new) // transforma cada produto em um DTO
                .collect(Collectors.toList());

        // Retorna um CompletableFuture já concluído com o relatório
        // O CompletableFuture encapsula o resultado assíncrono, permitindo que
        // quem chamar esse método possa trabalhar com o valor futuramente,
        // sem bloquear a execução atual.
        return CompletableFuture.completedFuture(new RelatorioEstoque(produtosSemEstoque));
    }

    @Async
    public CompletableFuture<RelatorioFaturamento> faturamentoObtido() {
        var dataOntem = LocalDate.now().minusDays(1);

        // Calcula o faturamento total do dia anterior
        var faturamentoTotal = pedidoRepository.faturamentoTotalDoDia(dataOntem);

        // Calcula estatísticas de faturamento por categoria
        var estatisticas = pedidoRepository.faturamentoTotalDoDiaPorCategoria(dataOntem);

        // Retorna um CompletableFuture já concluído com o relatório
        // Assim como no método anterior, o uso de CompletableFuture permite
        // que o processamento seja tratado de forma assíncrona, integrando
        // com o suporte do Spring para métodos anotados com @Async.
        return CompletableFuture.completedFuture(new RelatorioFaturamento(faturamentoTotal, estatisticas));
    }

}