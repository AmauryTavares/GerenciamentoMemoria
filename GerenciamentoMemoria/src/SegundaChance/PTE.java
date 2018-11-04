package SegundaChance;

public class PTE {

	private int bitValidade;
	private int numeroPaginaVirtual;
	private int paginaFisica;
	
	public PTE(int bitValidade, int indice, int paginaFisica) {
		this.bitValidade = bitValidade;
		this.numeroPaginaVirtual = indice;
		this.paginaFisica = paginaFisica;
	}

	public int getBitValidade() {
		return bitValidade;
	}

	public void setBitValidade(int bitReferencia) {
		this.bitValidade = bitReferencia;
	}

	public int getPaginaFisica() {
		return paginaFisica;
	}

	public void setPaginaFisica(int paginaFisica) {
		this.paginaFisica = paginaFisica;
	}

	public int getIndice() {
		return numeroPaginaVirtual;
	}
	
}
