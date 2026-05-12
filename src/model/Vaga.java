package model;

public class Vaga {

    private String numero;
    private boolean ocupada;

    public Vaga(String numero) {
        this.numero = numero;
        this.ocupada = false;
    }

    public String getNumero() {
        return numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void ocupar() {
        ocupada = true;
    }

    public void liberar() {
        ocupada = false;
    }

    @Override
    public String toString() {
        return numero + (ocupada ? " [OCUPADA]" : " [LIVRE]");
    }
}