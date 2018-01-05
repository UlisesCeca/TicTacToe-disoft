package dominio;

public class Movimiento {

	protected Usuario jugador;
	
	protected int fila;
	protected int columna;
	protected Partida partida;

	public Movimiento(Usuario jugador, int fila, int columna) {
		this.jugador = jugador;
		this.fila = fila;
		this.columna = columna;
		this.partida = jugador.getPartida();
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public Usuario getJugador() {
		return jugador;
	}

	public void setJugador(Usuario jugador) {
		this.jugador = jugador;
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}

	public int getColumna() {
		return columna;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}

}
