package BestFit;

import java.util.Random;

public class Processo extends Thread{

	private int numProcesso;
	private int memoriaNecessaria;
	private String estado;
	
	public Processo(int numProcesso, int memoriaNecessaria, String estado) {
		this.numProcesso = numProcesso;
		this.memoriaNecessaria = memoriaNecessaria;
		this.estado = estado;
		System.out.println("Processo " + this.numProcesso + " criado, com memoria necessaria de " + this.memoriaNecessaria + " MB.");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		
		System.out.println("Processo " + numProcesso + " iniciado!");
		
		Random rand = new Random();
	
		try {
			Thread.currentThread().suspend();
			Thread.sleep(rand.nextInt(15000) + 4000); // tempo processando
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		estado = "encerrado";
		System.out.println("Processo " + numProcesso + " encerrado!");
		
	}

	public int getNumProcesso() {
		return numProcesso;
	}

	public int getMemoriaNecessaria() {
		return memoriaNecessaria;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}
