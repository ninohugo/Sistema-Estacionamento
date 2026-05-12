package main;

import model.*;
import service.Estacionamento;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Estacionamento est = new Estacionamento();

        while (true) {

            System.out.println("\n@@@ ESTACIONAMENTO @@@");

            System.out.println("1 - Cadastrar veículo");
            System.out.println("2 - Entrada");
            System.out.println("3 - Saída");
            System.out.println("4 - Listar estacionados");
            System.out.println("5 - Histórico");
            System.out.println("6 - Vagas livres");
            System.out.println("7 - N° de vagas livres1");
            System.out.println("0 - Sair");

            int op = sc.nextInt();
            sc.nextLine();

            try {

                switch (op) {

                    case 1:

                        System.out.print("Placa: ");
                        String placa = sc.nextLine();

                        System.out.print("Modelo: ");
                        String modelo = sc.nextLine();

                        System.out.print("Cor: ");
                        String cor = sc.nextLine();

                        System.out.println("1-Carro");
                        System.out.println("2-Moto");
                        System.out.println("3-Caminhonete");

                        int tipo = sc.nextInt();
                        sc.nextLine();

                        Veiculo v;

                        if (tipo == 1) {
                            v = new Carro(placa, modelo, cor);
                        } else if (tipo == 2) {
                            v = new Moto(placa, modelo, cor);
                        } else {
                            v = new Caminhonete(placa, modelo, cor);
                        }

                        est.cadastrarVeiculo(v);

                        break;

                    case 2:

                        System.out.print("Placa: ");
                        String placaEntrada = sc.nextLine();

                        est.listarVagasDisponiveis();

                        System.out.print("Escolha a vaga: ");
                        String vaga = sc.nextLine();

                        est.registrarEntrada(placaEntrada, vaga);

                        break;

                    case 3:

                        System.out.print("Placa: ");
                        String placaSaida = sc.nextLine();

                        est.registrarSaida(placaSaida);

                        break;

                    case 4:
                        est.listarEstacionados();
                        break;

                    case 5:
                        est.historico();
                        break;

                    case 6:
                        est.listarVagasDisponiveis();
                        break;

                    case 7:
                        est.quantidadeVagasDisponiveis();
                        break;

                    case 0:
                        System.exit(0);
                }

            } catch (Exception e) {

                System.out.println("ERRO: " + e.getMessage());
            }
        }
    }
}