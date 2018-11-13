package BestFit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class CPU extends Thread{
	
	private LinkedList<EstruturaGerenciamento> memoriasDisponivel = new LinkedList<>();
	private LinkedList<EstruturaGerenciamento> memoriasAlocada = new LinkedList<>();
	private static GerenciadorMemoria gerenciadorMemoria = new GerenciadorMemoria();
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Quantidade de processos para ser iniciado: ");
		int quant = scanner.nextInt();
		
		System.out.println("Quantidade de memoria total (MB) para ser iniciado: ");
		int memoriaTotal = scanner.nextInt();
		
		System.out.println("Tamanho da pagina de memoria (MB) para ser iniciado: ");
		int pagina = scanner.nextInt();
		
		EstruturaGerenciamento livre = new EstruturaGerenciamento(null, 0, memoriaTotal);  
		memoriasDisponivel.add(livre);
		
		ArrayList<Processo> processos = new ArrayList<>();
		Random rand = new Random();
		int soma = 0;
		
		for (int i = 0; i < quant; i++) {
			int memoriaNecessaria = (rand.nextInt(50) + memoriaTotal/quant);
			Processo processo = new Processo(i, memoriaNecessaria, "pronto");
			processos.add(processo);
			soma += memoriaNecessaria;
		}
		
		System.out.println("Total de memoria requerida: " + soma + " MB.");

		try {
			System.out.println("\nPressione ENTER para continuar...");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int i = 0;
		boolean sucesso;

		while (processos.size() != 0) {
			sucesso = true;
			try {
				Thread.sleep(20);
				int contAnterior = processos.size();
				if (processos.get(i).isAlive()) {
					System.out.println("Processo: " + processos.get(i).getNumProcesso() + " foi escalonado para a CPU.");
					processos.get(i).setEstado("execucao");
					processos.get(i).resume();
				} else {
					sucesso = gerenciadorMemoria.alocar(false, processos.get(i), memoriasAlocada, memoriasDisponivel, pagina, memoriaTotal);
					
					if (sucesso) {
						processos.get(i).start();
						processos.get(i).setEstado("execucao");
					} else {
						System.out.println("Nao foi possivel alocar memoria para o Processo " + processos.get(i).getNumProcesso() + ". =(\n");
					}
		
				}
				if (sucesso) {
					for (int x = 0; x < 100; x++) {
						if (processos.get(i).getEstado().equals("encerrado")) {
							x = 100;
							gerenciadorMemoria.desalocar(processos.get(i), memoriasAlocada, memoriasDisponivel, memoriaTotal);
							processos.remove(i);
						}
						Thread.sleep(50);
					}
				
					if (processos.size() == contAnterior && processos.size() != 1) {	// Processos se mantem 
						System.out.println("Processo: " + processos.get(i).getNumProcesso() + " foi pausado e retirado da CPU.\n");
						processos.get(i).suspend();
						processos.get(i).setEstado("pronto");
						i = (i + 1) % processos.size();
					} else if (processos.size() != contAnterior && i >= processos.size()) {	// Processo removido e eh o ultimo
						i = 0;
					}	// Processo removido e eh o ultimo
					
				} else {
					i = (i + 1) % processos.size();
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IllegalMonitorStateException e) {
				e.printStackTrace();
			}
		}
		
		scanner.close();	

	}

}
