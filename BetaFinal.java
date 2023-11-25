package TesteBingo;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class BetaFinal {
	public static String[] baseNomes;
	public static String[][] matrizJogadores;
	public static int[] numerosNaoSorteados = criarNumerosNaoSorteados();
	public static int[][] todasJogadas = new int[12][6];
	public static String[][][] tabelaAcertos;
	public static int indiceJogadaAtual;
	public static int[] numerosSorteados = new int[60];
	public static boolean ganhador = false;
	public static String nomeGanhador = null;

	public static void main(String[] args) {// verificado
		baseNomes = solicitarNomesJogadores().split("-");
		String escolha = solicitarTipoCartela();
		if (escolha.equalsIgnoreCase("A")) {
			int[][] cartelas = gerarCartelaAutomaticamente(baseNomes.length);
			exibirEColetarCartelas(baseNomes, cartelas);
		} else if (escolha.equalsIgnoreCase("M")) {
			int[][] cartelas = solicitarCartelasManualmente(baseNomes.length);
			exibirEColetarCartelas(baseNomes, cartelas);
		} else {
			System.out.println("Escolha inválida. Encerrando o programa.");
		}
	//	exibirMatrizJogadores();
		consolidarAcertos();
		escolhaTipoSorteio();
		imprimirTabelaAcertos();
		imprimirTodasJogadas();
	}

	private static String solicitarNomesJogadores() {// varificado
		Scanner scanner = new Scanner(System.in);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++:");
		System.out.println("BEM-VINDO AO GRANDE BINGO ' ADA'. MILHÕES EM PRÊMIOS NUNCA SORTEADOS");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++:");
		System.out.println("===  Insira os nomes dos jogadores separados por hífen:  ===");
		return scanner.nextLine();
	}

	private static String solicitarTipoCartela() {// verificado
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nDeseja gerar as cartelas automaticamente (A) ou manualmente (M)?");
		String escolha = scanner.nextLine().trim();
		return escolha.isEmpty() ? scanner.nextLine().trim() : escolha;
	}

//	private static int[][] gerarCartelaAutomaticamente(int quantidadeCartelas) {
//		int[][] cartelas = new int[quantidadeCartelas][5];
	// for (int i = 0; i < quantidadeCartelas; i++) {
	// for (int j = 0; j < 5; j++) {
	// cartelas[i][j] = (int) (Math.random() * 60) + 1;
	// }
//		}
//		return cartelas;
//	}

	private static int[][] gerarCartelaAutomaticamente(int quantidadeCartelas) { // verificado
		int[][] cartelas = new int[quantidadeCartelas][5];
		for (int i = 0; i < quantidadeCartelas; i++) {
			Set<Integer> numerosUtilizados = new HashSet<>();
			for (int j = 0; j < 5; j++) {
				int numeroAleatorio;
				do {
					numeroAleatorio = (int) (Math.random() * 60) + 1;
				} while (numerosUtilizados.contains(numeroAleatorio));
				numerosUtilizados.add(numeroAleatorio);
				cartelas[i][j] = numeroAleatorio;
			}
		}
		return cartelas;
	}

	private static int[][] solicitarCartelasManualmente(int quantidadeCartelas) { // verificado
		Scanner scanner = new Scanner(System.in);
		int[][] cartelas = new int[quantidadeCartelas][5];
		boolean entradaValida = false;
		while (!entradaValida) {
			System.out.println("Insira os arrays de números separados por hífen para cada jogador:");
			String[] arraysCartelas = scanner.nextLine().split("-");
			if (arraysCartelas.length == quantidadeCartelas) {
				entradaValida = true;
				for (int i = 0; i < quantidadeCartelas; i++) {
					String[] numeros = arraysCartelas[i].split(",");
					for (int j = 0; j < 5; j++) {
						cartelas[i][j] = Integer.parseInt(numeros[j].trim());
					}
				}
			} else {
				System.out.println(
						"O número de arrays inseridos não corresponde ao número de jogadores. Tente novamente.");
			}
		}
		return cartelas;
	}

	private static void exibirEColetarCartelas(String[] nomes, int[][] cartelas) { // verificado
		System.out.println("\nCartelas dos jogadores:");
		matrizJogadores = new String[nomes.length][6];
		for (int i = 0; i < nomes.length; i++) {
			System.out.print(nomes[i] + ": ");
			for (int j = 0; j < 5; j++) {
				matrizJogadores[i][0] = nomes[i];
				matrizJogadores[i][j + 1] = String.valueOf(cartelas[i][j]);
				System.out.print(cartelas[i][j] + " ");
			}
			System.out.println();
		}

		// exibirMatrizJogadores(); colocado no alto
		consolidarAcertos();
		System.out.println();
		criarNumerosNaoSorteados();
		// imprimirNumerosNaoSorteados();
		// escolhaTipoSorteio();
	}

	private static void exibirMatrizJogadores() {
		System.out.println("Conteúdo da matrizJogadores:");
		for (String[] jogador : matrizJogadores) {
			System.out.print(jogador[0] + ": ");
			for (int i = 1; i < jogador.length; i++) {
				System.out.print(jogador[i] + " ");
			}
			System.out.println();
		}
	}

	private static int[] criarNumerosNaoSorteados() {
		int[] numeros = new int[61];
		for (int i = 0; i <= 60; i++) {
			numeros[i] = i;
		}
		return numeros;
	}

	public static void imprimirNumerosNaoSorteados() {
		System.out.println("Numeros não sorteados:");
		for (int numero : numerosNaoSorteados) {
			if (numero != 0) {
				System.out.print(numero + " ");
			}
		}
		System.out.println();
	}

	private static void escolhaTipoSorteio() {
		Scanner scanner = new Scanner(System.in);
		String escolha = "";
		do {
			System.out.println("\nDeseja realizar o sorteio de forma automática (A) ou manual (M)?");
			escolha = scanner.nextLine().trim();
			if (escolha.equalsIgnoreCase("A")) {
				sorteioAutomatico();
				return;
			} else if (escolha.equalsIgnoreCase("M")) {
				sorteioManual(scanner);
			} else {
				System.out.println("Opção inválida. Por favor, escolha 'A' para automático ou 'M' para manual.");
			}
		} while (!escolha.equalsIgnoreCase("A") && !escolha.equalsIgnoreCase("M"));
	}

	private static void sorteioAutomatico() {
		Scanner scanner = new Scanner(System.in);
		if (todasJogadas == null) {
			todasJogadas = new int[12][5];
			indiceJogadaAtual = 0;
		}
		Random random = new Random();
		boolean numerosDisponiveis = true;
		while (numerosDisponiveis) {
			// confereRodada();
			int[] novaJogada = new int[5];
			for (int i = 0; i < 5; i++) {
				if (todosNumerosSorteados()) {
					numerosDisponiveis = false;
					break;
				}
				int index;
				do {
					index = random.nextInt(numerosNaoSorteados.length);
				} while (numerosNaoSorteados[index] == 0);
				novaJogada[i] = numerosNaoSorteados[index];
				numerosNaoSorteados[index] = 0;
				numerosSorteados[indiceJogadaAtual * 5 + i] = novaJogada[i];
			}
			todasJogadas[indiceJogadaAtual] = novaJogada;
			indiceJogadaAtual++;

			// criaRanking();
			// consolidarAcertos();
			// confereRodada();
			System.out.println("\nNúmeros sorteados nesta rodada:");
			for (int j = (indiceJogadaAtual - 1) * 5; j < indiceJogadaAtual * 5; j++) {
				if (numerosSorteados[j] != 0) {
					System.out.print(numerosSorteados[j] + " ");
				}
			}
			System.out.println();
			// imprimirNumerosNaoSorteados();
			// imprimirNumerosSorteados();
			// exibirJogada(novaJogada);
			// imprimirTodasJogadas();
			imprimirNumerosSorteados();
			imprimirNumerosNaoSorteados();
			System.out.println();
			int quantidadeNumerosDisponiveis = 0;
			for (int numero : numerosNaoSorteados) {
				if (numero != 0) {
					quantidadeNumerosDisponiveis++;
				}
			}
			if (numerosDisponiveis && quantidadeNumerosDisponiveis < 5) {
				System.out.println("Jogo encerrado. Não há mais números para sortear.");
				// imprimirTabelaAcertos();
				// imprimirTodasJogadas();
				break;
			}
			confereRodada();
			criaRanking();

			// imprimirTabelaAcertos();
			if (ganhador) {
				System.out.println("\nO jogo não vai prosseguir. Já temos um ganhador.");
				System.out.println("Parabéns!!! " + nomeGanhador);
				// imprimirTabelaAcertos();
				// imprimirTodasJogadas();
				return;
			}
			System.out.println("\nAperte 'x' para encerrar ou qualquer outra tecla para continuar:");
			String opcao = scanner.nextLine();
			if (opcao.equalsIgnoreCase("x")) {
				System.out.println("A seu pedido... jogo encerrado.");
				return;
			}
		}
	}

	private static void sorteioManual(Scanner scanner) {
	    int[] numerosAceitos = new int[5];
	    while (!ganhador) {
	        System.out.println("\nDeseja inserir os próximos números? (S/N)");
	        String entrada = scanner.nextLine();
	        if (!entrada.equalsIgnoreCase("S")) {
	            break;
	        }
	        System.out.println("\nInsira um conjunto de 5 números no formato '1,2,3,4,5':");
	        String entradaNumeros = scanner.nextLine();
	        String[] numerosString = entradaNumeros.split(",");
	        if (numerosString.length != 5) {
	            System.out.println("\nPor favor, insira exatamente 5 números separados por vírgula.");
	            continue;
	        }
	        int[] numeros = new int[5];
	        boolean todosNumerosValidos = true;
	        for (int i = 0; i < 5; i++) {
	            numeros[i] = Integer.parseInt(numerosString[i].trim());
	            if (!numeroDisponivel(numeros[i])) {
	                System.out.println("O número " + numeros[i] + " não está disponível para sorteio.");
	                int novoNumero = solicitarNovoNumero(scanner);
	                if (numeroDisponivel(novoNumero)) {
	                    marcarNumeroSorteado(numeros[i]);
	                    numeros[i] = novoNumero;
	                } else {
	                    System.out.println("\nNúmero inválido ou já foi utilizado. Tente novamente.");
	                    todosNumerosValidos = false;
	                    break;
	                }
	            }
	        }
	        if (todosNumerosValidos) {
	            System.out.println("\nNúmeros desta rodada:");
	            for (int i = 0; i < 5; i++) {
	                numerosAceitos[i] = numeros[i];
	                marcarNumeroSorteado(numeros[i]);
	                numerosSorteados[indiceJogadaAtual * 5 + i] = numeros[i]; // Inserir no array numerosSorteados
	                System.out.print(numeros[i] + " ");
	            }
	            System.out.println();
	            int indiceVazio = encontrarIndice(0);
	            if (indiceVazio != -1) {
	                todasJogadas[indiceJogadaAtual][0] = indiceJogadaAtual + 1;
	                for (int i = 0; i < 5; i++) {
	                    todasJogadas[indiceJogadaAtual][i + 1] = numerosAceitos[i];
	                }
	                indiceJogadaAtual++;
	            } else {
	                System.out.println("A matriz está cheia. Não é possível adicionar mais jogadas.");
	                break;
	            }
	            System.out.println();
	            imprimirNumerosSorteados();
	            System.out.println();
				imprimirNumerosNaoSorteados();
	            confereRodada();
	            criaRanking();
	        }
	    }
	    
	    if (ganhador) {
	        System.out.println("\nO jogo não vai prosseguir. Já temos um ganhador.");
	        System.out.println("Parabéns!!! " + nomeGanhador);
	    }
	}

	private static int encontrarIndice(int numero) {
		for (int i = 0; i < numerosNaoSorteados.length; i++) {
			if (numerosNaoSorteados[i] == numero) {
				return i;
			}
		}
		return -1; // Retornar um valor negativo para indicar que o número não foi encontrado no
					// array.
	}

	// Método para solicitar um novo número ao usuário
	private static int solicitarNovoNumero(Scanner scanner) {
		System.out.print("Insira um novo número para substituir: ");
		return Integer.parseInt(scanner.nextLine().trim());
	}

	// Método para verificar se o número está disponível em numerosNaoSorteados
	private static boolean numeroDisponivel(int numero) {
		for (int i = 0; i < numerosNaoSorteados.length; i++) {
			if (numerosNaoSorteados[i] == numero) {
				return true;
			}
		}
		return false;
	}

	// Método para marcar um número como sorteado
	private static void marcarNumeroSorteado(int numero) {
		for (int i = 0; i < numerosNaoSorteados.length; i++) {
			if (numerosNaoSorteados[i] == numero) {
				numerosNaoSorteados[i] = 0;
				break;
			}
		}
	}

	private static void imprimirNumerosSorteados() {
		System.out.println("\nNúmeros sorteados até o momento:");
		for (int numero : numerosSorteados) {
			if (numero != 0) {
				System.out.print(numero + " ");
			}
		}
		System.out.println();
	}

	private static void imprimirTodasJogadas() { // verificado
		System.out.println("\nTodas as jogadas:");
		for (int i = 0; i < todasJogadas.length; i++) {
			System.out.print("Jogada " + (i + 1) + ": ");
			for (int j = 0; j < todasJogadas[i].length; j++) {
				System.out.printf("%02d", todasJogadas[i][j]);
				if (j != todasJogadas[i].length - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();
		}
	}

	private static boolean todosNumerosSorteados() {
		for (int numero : numerosNaoSorteados) {
			if (numero != 0) {
				return false;
			}
		}
		return true;
	}

	private static void exibirJogada(int[] jogada) { // verificado
		System.out.print("\nJogada " + indiceJogadaAtual + ": ");
		for (int i = 0; i < jogada.length; i++) {
			System.out.printf("%02d", jogada[i]);
			if (i != jogada.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println();
	}

	private static void consolidarAcertos() { // verificado
		if (matrizJogadores == null || matrizJogadores.length == 0 || matrizJogadores[0].length == 0) {
			System.out.println("A matriz Matriz Jogadores está vazia ou não foi inicializada corretamente.");
			return;
		}
		int quantidadeJogadores = matrizJogadores.length;
		tabelaAcertos = new String[quantidadeJogadores][3][5];
		for (int i = 0; i < quantidadeJogadores; i++) {
			// Nome do jogador na primeira posição da matriz
			tabelaAcertos[i][0][0] = matrizJogadores[i][0];
			// Números da cartela do jogador na segunda posição da matriz
			for (int j = 1; j < matrizJogadores[i].length && j <= 5; j++) {
				try {
					tabelaAcertos[i][1][j - 1] = String.valueOf(Integer.parseInt(matrizJogadores[i][j]));
				} catch (NumberFormatException e) {
					System.out.println("Erro ao converter número: " + e.getMessage());
				}
			}
			// Preencher posições vazias com espaços em branco
			for (int k = 1; k < tabelaAcertos[i][1].length; k++) {
				if (tabelaAcertos[i][1][k] == null) {
					tabelaAcertos[i][1][k] = " "; // Espaço em branco ou outro marcador de vazio desejado
				}
			}
			// Inicialização do array de acertos com zeros
			tabelaAcertos[i][2] = new String[] { "0", "0", "0", "0", "0" };
		}
		// imprimirTabelaAcertos(); // Adicionando esta chamada para imprimir a matriz
		// após a consolidação
	}

	private static void confereRodada() {
		if (numerosSorteados == null || numerosSorteados.length == 0) {
			System.out.println("O array numerosSorteados está vazio ou não foi inicializado corretamente.");
			return;
		}

		if (tabelaAcertos == null || tabelaAcertos.length == 0 || tabelaAcertos[0].length == 0) {
			System.out.println("A matriz tabelaAcertos está vazia ou não foi inicializada corretamente.");
			return;
		}

		for (int indiceJogador = 0; indiceJogador < tabelaAcertos.length; indiceJogador++) {
			for (int j = 1; j < tabelaAcertos[indiceJogador][1].length + 1; j++) {
				for (int numeroSorteado : numerosSorteados) {
					if (tabelaAcertos[indiceJogador][1][j - 1] != null
							&& tabelaAcertos[indiceJogador][1][j - 1].equals(String.valueOf(numeroSorteado))) {
						tabelaAcertos[indiceJogador][2][j - 1] = "1";
					}
				}
			}
		}
	}

	private static void criaRanking() {
		if (tabelaAcertos == null || tabelaAcertos.length == 0 || tabelaAcertos[0].length == 0) {
			System.out.println("A matriz tabelaAcertos está vazia ou não foi inicializada corretamente.");
			return;
		}
		int[][] pontuacaoJogadores = new int[tabelaAcertos.length][2];
		String nomePrimeiroGanhador = null;
		for (int i = 0; i < tabelaAcertos.length; i++) {
			int pontuacao = 0;
			for (int j = 0; j < tabelaAcertos[i][2].length; j++) {
				if (tabelaAcertos[i][2][j].equals("1")) {
					pontuacao++;
				}
			}
			pontuacaoJogadores[i][0] = i;
			pontuacaoJogadores[i][1] = pontuacao;
			if (pontuacao >= 5 && nomePrimeiroGanhador == null) {
				nomePrimeiroGanhador = tabelaAcertos[i][0][0];
			}
		}
		if (nomePrimeiroGanhador != null) {
			ganhador = true;
			nomeGanhador = nomePrimeiroGanhador;
		}
		for (int i = 0; i < pontuacaoJogadores.length - 1; i++) {
			for (int j = 0; j < pontuacaoJogadores.length - i - 1; j++) {
				if (pontuacaoJogadores[j][1] < pontuacaoJogadores[j + 1][1]) {
					int[] temp = pontuacaoJogadores[j];
					pontuacaoJogadores[j] = pontuacaoJogadores[j + 1];
					pontuacaoJogadores[j + 1] = temp;
				}
			}
		}
		System.out.println("Ranking dos três maiores pontuadores:\n");
		for (int i = 0; i < Math.min(3, pontuacaoJogadores.length); i++) {
			int indiceJogador = pontuacaoJogadores[i][0];
			int pontuacao = pontuacaoJogadores[i][1];
			System.out.println("Jogador: " + tabelaAcertos[indiceJogador][0][0] + ", Pontuação: " + pontuacao);
		}
	}

	private static void imprimirTabelaAcertos() { // verificado
		if (tabelaAcertos != null) {
			System.out.println("\nT A B E L A  D E  A C E R T O S:");
			for (String[][] jogador : tabelaAcertos) {
				System.out.print("(");
				if (jogador[0][0] != null) {
					System.out.print(jogador[0][0] + " ) (");
				} else {
					System.out.print("null ) (");
				}
				for (int j = 0; j < jogador[1].length; j++) {
					System.out.print(jogador[1][j] + " ");
				}
				System.out.print(") (");
				for (int j = 0; j < jogador[2].length; j++) {
					System.out.print(jogador[2][j] + " ");
				}
				System.out.println(")");
			}
		} else {
			System.out.println("A matriz está vazia ou não foi inicializada.");
		}
	}

}
