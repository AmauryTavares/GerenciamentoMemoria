package BestFit;

public class EstruturaGerenciamento {

	private Processo processo;
	private int posicaoInicial;
	private int tamanho;
	
	public EstruturaGerenciamento(Processo processo, int posicaoInicial, int tamanho) {
		this.processo = processo;
		this.posicaoInicial = posicaoInicial;
		this.tamanho = tamanho;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public int getPosicaoInicial() {
		return posicaoInicial;
	}

	public void setPosicaoInicial(int posicaoInicial) {
		this.posicaoInicial = posicaoInicial;
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
	
}
