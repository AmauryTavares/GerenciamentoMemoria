package BestFit;

import java.util.LinkedList;

public class GerenciadorMemoria {

	public GerenciadorMemoria() {	}

	public synchronized boolean alocar(boolean compactado, Processo processo, LinkedList<EstruturaGerenciamento> listaMemoriasAlocada, LinkedList<EstruturaGerenciamento> listaMemoriasDisponivel, int pagina, int memoriaTotal) {
		
		boolean sucesso = false;
		int memoriaNecessaria = processo.getMemoriaNecessaria();
		EstruturaGerenciamento melhor = null;
		
		for (EstruturaGerenciamento memoriaLivre : listaMemoriasDisponivel) {

			if (melhor == null && memoriaLivre.getTamanho() >= memoriaNecessaria) {
				melhor = memoriaLivre;
			} else if (memoriaLivre.getTamanho() >= memoriaNecessaria && memoriaLivre.getTamanho() < melhor.getTamanho()) {
				melhor = memoriaLivre;
			}
		}
		
		if (melhor != null && melhor.getTamanho() >= memoriaNecessaria) {
			EstruturaGerenciamento memoriaAlocada = new EstruturaGerenciamento(processo, melhor.getPosicaoInicial(),
					memoriaNecessaria + (pagina - memoriaNecessaria % pagina));
			listaMemoriasAlocada.add(memoriaAlocada);
			
			melhor.setPosicaoInicial(melhor.getPosicaoInicial() + memoriaAlocada.getTamanho());
			melhor.setTamanho(melhor.getTamanho() - memoriaAlocada.getTamanho());
			sucesso = true;
			System.out.println("Foi alocado " + memoriaAlocada.getTamanho() + " MB do processo " + processo.getNumProcesso() + ".");
			
			mostrarTabela(listaMemoriasAlocada, listaMemoriasDisponivel, memoriaTotal);
		} else {
			if (!compactado) {
				System.out.println("Memoria insuficiente para o Processo " + processo.getNumProcesso() + ", compactando memoria...");
				compactado = compactacao(listaMemoriasAlocada, listaMemoriasDisponivel, memoriaTotal);
				alocar(compactado, processo, listaMemoriasAlocada, listaMemoriasDisponivel, pagina, memoriaTotal);
			}
		}
		
		return sucesso;
	}
	
	
	public synchronized void desalocar(Processo processo, LinkedList<EstruturaGerenciamento> listaMemoriasAlocada, LinkedList<EstruturaGerenciamento> listaMemoriasDisponivel, int memoriaTotal) {
		EstruturaGerenciamento remover = null;
		for (EstruturaGerenciamento alocado : listaMemoriasAlocada) {
			if (alocado.getProcesso() == processo) {
				boolean liberado = false;
				for (EstruturaGerenciamento livre : listaMemoriasDisponivel) {
					if ((livre.getPosicaoInicial() + livre.getTamanho()) == alocado.getPosicaoInicial()) {
						livre.setTamanho(livre.getTamanho() + alocado.getTamanho());
						liberado = true;
					}
				}
				
				if (!liberado) {
					EstruturaGerenciamento novaMemoria = new EstruturaGerenciamento(null, alocado.getPosicaoInicial(), alocado.getTamanho());
					listaMemoriasDisponivel.add(novaMemoria);
				}	
				
				System.out.println("Foi desalocado " + alocado.getTamanho() + " MB do processo " + processo.getNumProcesso() + ".");
				
				remover = alocado;
				
			}
		}
		
		if (remover != null) {
			listaMemoriasAlocada.remove(remover);
		}
		
		unirMemoria(listaMemoriasDisponivel);
		
		mostrarTabela(listaMemoriasAlocada, listaMemoriasDisponivel, memoriaTotal);
		
	}

	public void mostrarTabela(LinkedList<EstruturaGerenciamento> listaMemoriasAlocada, LinkedList<EstruturaGerenciamento> listaMemoriasDisponivel, int memoriaTotal) {
		System.out.println("\nEstado atual da memoria:");
		
		int proximo = 0;
		
		while (proximo < memoriaTotal) {
			
			for (EstruturaGerenciamento e : listaMemoriasAlocada) {
				if (e.getPosicaoInicial() == proximo) {
					System.out.println("Processo: " + e.getProcesso().getNumProcesso() + " - Posicao Inicial: "
							+ e.getPosicaoInicial() + " - Tamanho (MB): " + e.getTamanho() + " - Frag. Interna: " 
							+ (e.getTamanho() - e.getProcesso().getMemoriaNecessaria()));
					proximo = e.getPosicaoInicial() + e.getTamanho();
				}
			}
			
			for (EstruturaGerenciamento e : listaMemoriasDisponivel) {
				if (e.getPosicaoInicial() == proximo) {
					System.out.println("Processo: vazio - Posicao Inicial: "
							+ e.getPosicaoInicial() + " - Tamanho (MB): " + e.getTamanho());
					proximo = e.getPosicaoInicial() + e.getTamanho();
				}
			}
			
		}

		System.out.println();
	}
	
	private void unirMemoria(LinkedList<EstruturaGerenciamento> listaMemoriasDisponivel) {
		boolean fim = false;
		
		while (!fim) {
			
			for (EstruturaGerenciamento e : listaMemoriasDisponivel) {
				EstruturaGerenciamento remover;
				fim = false;
				
				while(!fim) {
					remover = null;
					for (EstruturaGerenciamento x : listaMemoriasDisponivel) {
						if (e.getPosicaoInicial() + e.getTamanho() == x.getPosicaoInicial()) {
							e.setTamanho(e.getTamanho() + x.getTamanho());
							remover = x;
						}
					}
					
					if (remover != null) {
						listaMemoriasDisponivel.remove(remover);	
					} else {
						fim = true;
					}
				}
			}
		}
	}
	
	private boolean compactacao(LinkedList<EstruturaGerenciamento> listaMemoriasAlocada, LinkedList<EstruturaGerenciamento> listaMemoriasDisponivel, int memoriaTotal) {
		int proximo = 0;
		
		for (EstruturaGerenciamento e : listaMemoriasAlocada) {
			e.setPosicaoInicial(proximo);
			proximo += e.getTamanho();
		}
		
		listaMemoriasDisponivel.clear();
		EstruturaGerenciamento x = new EstruturaGerenciamento(null, proximo, memoriaTotal - proximo);
		listaMemoriasDisponivel.add(x);
		
		return true;
	}
	
}
