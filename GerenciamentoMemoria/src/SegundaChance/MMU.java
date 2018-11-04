package SegundaChance;

import java.util.ArrayList;
import java.util.LinkedList;

public class MMU {

	private static int paginasFisicas[];
	private static ArrayList<ArrayList<PTE>> tabelaPaginas;
	private static LinkedList<EstruturaFila> fila;	//fila de indice
	
	public MMU(int qtPaginaPorProcesso, int qtProcesso, int pgFisica) {
		paginasFisicas = new int[pgFisica];
		tabelaPaginas = criarTabelaPaginas(qtPaginaPorProcesso, qtProcesso);
		fila = new LinkedList<>();
	}
	
	private ArrayList<ArrayList<PTE>> criarTabelaPaginas(int qtPaginaPorProcesso, int qtProcesso) {
		ArrayList<ArrayList<PTE>> paginasFisicas = new ArrayList<>();
		
		int proximo = 1;
		
		for (int j = 0; j < qtProcesso; j++) {
			ArrayList<PTE> paginaProcesso = new ArrayList<>();
			for (int i = 0; i < qtPaginaPorProcesso; i++) {
				PTE pte = new PTE(0, proximo++, 0);
				paginaProcesso.add(pte);
			}
			
			paginasFisicas.add(paginaProcesso);
		}
		
		return paginasFisicas;
	}
	
	public synchronized void requisitar(int paginaVirtual, int numProcesso) {	
		ArrayList<PTE> tabelaPaginaProcesso = tabelaPaginas.get(numProcesso);
		PTE pte = tabelaPaginaProcesso.get((paginaVirtual - 1) % tabelaPaginaProcesso.size());		// pte contem a pagina fisica

		if (pte.getBitValidade() == 1) {
			System.out.println("Pagina " + paginaVirtual + " mapeada na memoria fisica.(HIT)");
			//varre a fila e setar o bit de referencia
			
			for (EstruturaFila e : fila) {
				if (e.getPte().getPaginaFisica() == pte.getPaginaFisica()) {
					e.setBitReferencia(1);
				}
			}
			
		} else {
			System.out.println("Pagina " + paginaVirtual + " nao se encontra na memoria fisica.(MISS)");
			//varrer a fila procurando um pte para substituir e setar o bit de referencia com 1

			if (fila.size() >= paginasFisicas.length) {
				boolean achouPosicaoLivre = false;
				EstruturaFila remover = null;
				
				for (EstruturaFila e : fila) {
					if (!achouPosicaoLivre) {
						if (e.getBitReferencia() == 0) {
							remover = e;
							achouPosicaoLivre = true;
						} else {
							e.setBitReferencia(0);
						}
					}
				}
				
				if (achouPosicaoLivre) {
					remover.getPte().setBitValidade(0);  //seta o bit de validade para 0
					fila.remove(remover);
					paginasFisicas[remover.getPte().getPaginaFisica()] = paginaVirtual;	// substitui o valor na memoria fisica
					pte.setPaginaFisica(remover.getPte().getPaginaFisica());
					EstruturaFila novaEstrutura = new EstruturaFila(1, pte);
					fila.add(novaEstrutura);
					System.out.println("Pagina " + paginaVirtual + " foi mapeada na memoria fisica.");
				} else {
					requisitar(paginaVirtual, numProcesso);
				}
				
			} else {
				boolean adicionado = false;
				for (int i = 0; i < paginasFisicas.length; i++) {
					if (!adicionado) {
						if (paginasFisicas[i] == 0) {
							paginasFisicas[i] = paginaVirtual;
							EstruturaFila novaEstrutura = new EstruturaFila(1, pte);
							pte.setPaginaFisica(i);
							pte.setBitValidade(1);
							fila.add(novaEstrutura);
							System.out.println("Pagina " + paginaVirtual + " foi mapeada na memoria fisica.");
							adicionado = true;
						}
					}
				}
			}	
		}
		
		mostrarMemoriaFisica();
		
	}
	
	public void resetarReferencias() {
		for (EstruturaFila e : fila) {
			e.setBitReferencia(0);
		}
		
		System.out.println("### Todos os bits de referencia foram resetados ###");
		
	}
	
	private void mostrarMemoriaFisica() {
		System.out.println("\nMemoria fisica atual:");
		
		for (int i = 0; i < paginasFisicas.length; i++) {
			System.out.println("Indice: " + i + " - dados: " + paginasFisicas[i]);
		}
		
		System.out.println("\nEstado da fila atual");
		
		for (EstruturaFila e : fila) {
			System.out.println("Bit Ref.: " + e.getBitReferencia() + " -  Pag. Virtual: " + e.getPte().getIndice()
					+ " - Pag. Fisica: " + e.getPte().getPaginaFisica());
		}
		
		System.out.println();
		
	}
	
}
