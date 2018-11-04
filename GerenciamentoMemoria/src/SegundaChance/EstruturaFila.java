package SegundaChance;

public class EstruturaFila {

	private int bitReferencia;
	private PTE pte;
	
	public EstruturaFila(int bitReferencia, PTE pte) {
		this.bitReferencia = bitReferencia;
		this.pte = pte;
	}

	public int getBitReferencia() {
		return bitReferencia;
	}

	public void setBitReferencia(int bitReferencia) {
		this.bitReferencia = bitReferencia;
	}

	public PTE getPte() {
		return pte;
	}
	
}
