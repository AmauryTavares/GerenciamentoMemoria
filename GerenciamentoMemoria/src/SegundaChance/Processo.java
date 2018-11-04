package SegundaChance;

import java.util.ArrayList;
import java.util.Random;

public class Processo extends Thread{

	private int numProcesso;
	private ArrayList<Integer> paginasVituais;
	private String estado;
	
	public Processo(int numProcesso, ArrayList<Integer> paginasVituais, String estado) {
		this.numProcesso = numProcesso;
		this.paginasVituais = paginasVituais;
		this.estado = estado;
		printarInicio();
	}
	
	private void printarInicio() {
		String texto = "Processo " + this.numProcesso + " criado, paginas virtuais: ";
		for (int i : paginasVituais) {
			texto += i + " ";
		}
		System.out.println(texto);
	}
	
	@Override
	public void run() {
		
		System.out.println("Processo " + numProcesso + " iniciado!");
		
		Random rand = new Random();
		
		for (int i = 0; i < 6; i++) {
			try {
				int paginaSelecionada = paginasVituais.get(rand.nextInt(paginasVituais.size()));
				System.out.println("O Processo " + numProcesso + " solicitou a pagina virtual " + paginaSelecionada + " .");
				CPU.requisitar(paginaSelecionada, numProcesso);
				Thread.sleep(rand.nextInt(3000) + 800); // tempo processando
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		estado = "encerrado";
		System.out.println("Processo " + numProcesso + " encerrado!");
		
	}

	public int getNumProcesso() {
		return numProcesso;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}
