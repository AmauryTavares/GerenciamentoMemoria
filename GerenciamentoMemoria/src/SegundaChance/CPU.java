package SegundaChance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CPU extends Thread{
	
	private static int quant = 0;
	private static int pgFisica = 0;
	private static int pgVirtual = 0;
	private static MMU mmu = null;
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Quantidade de processos para ser iniciado: ");
		quant = scanner.nextInt();
		
		System.out.println("Quantidade de paginas fisicas para ser iniciado: ");
		pgFisica = scanner.nextInt();
		
		System.out.println("Quantidade de paginas virtuais de cada processo: ");
		pgVirtual = scanner.nextInt();
		
		mmu = new MMU(pgVirtual, quant, pgFisica);
		ArrayList<Processo> processos = new ArrayList<>();

		int proximoValor = 1;
		
		for (int i = 0; i < quant; i++) {
			ArrayList<Integer> paginasVirtuais = new ArrayList<>();
			
			for (int j = 0; j < pgVirtual; j++) {
				paginasVirtuais.add(proximoValor++);
			}
			
			Processo processo = new Processo(i, paginasVirtuais, "pronto");
			processos.add(processo);
		}

		try {
			System.out.println("\nPressione ENTER para continuar...");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int i = 0;
		int contador = 0;

		while (processos.size() != 0) {
			try {
				Thread.sleep(20);
				int contAnterior = processos.size();
				if (processos.get(i).isAlive()) {
					System.out.println("Processo: " + processos.get(i).getNumProcesso() + " foi escalonado para a CPU.");
					processos.get(i).setEstado("execucao");
					processos.get(i).resume();
				} else {
					processos.get(i).start();
					processos.get(i).setEstado("execucao");
				}
	
				for (int x = 0; x < 100; x++) {
					if (processos.get(i).getEstado().equals("encerrado")) {
						x = 100;
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
				
				if (++contador == 10) {
					mmu.resetarReferencias();
				} else {
					contador = contador % 10;
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IllegalMonitorStateException e) {
				e.printStackTrace();
			}
		}
		
		scanner.close();	

	}

	public synchronized static void requisitar(int paginaVirtual, int numProcesso) {
		mmu.requisitar(paginaVirtual, numProcesso);
	}
	
}
