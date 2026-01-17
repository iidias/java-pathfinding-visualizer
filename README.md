# 🗺️ Java Pathfinding Visualizer

> Um visualizador interativo de algoritmos de busca de caminho (Pathfinding) desenvolvido em **Java Swing**. O projeto demonstra graficamente como o computador "pensa" para encontrar a rota mais curta entre dois pontos, desviando de obstáculos.

![Java Badge](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing Badge](https://img.shields.io/badge/Java_Swing-GUI-blue?style=for-the-badge)

## 📸 Screenshots

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/54d99c90-1a42-4fe5-a5f0-80152cfb7633" width="400" alt="Menu Principal">
      <br>
      <em>Menu Principal</em>
    </td>
    <td align="center">
      <img width="784" height="843" alt="image" src="https://github.com/user-attachments/assets/99ce78fd-ebe8-4af5-8245-a23f49135aa7" width="400" alt="Como Jogar">
      <br>
      <em>Tela de Instruções</em>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/da59278b-42d1-401b-a439-c2702c8bdf6b" width="400" alt="Jogo">
      <br>
      <em>Gameplay</em>
    </td>
  </tr>
</table>

## 🚀 Funcionalidades

* **Algoritmo de Dijkstra:** Garante matematicamente o menor caminho possível.
* **Editor de Mapas Interativo:**
    * 🖌️ **Desenhar Muros:** Clique e arraste com o botão esquerdo para criar obstáculos.
    * 🖐️ **Drag & Drop:** Arraste os pontos de **Início (Verde)** e **Fim (Vermelho)** para qualquer lugar.
    * 🧼 **Borracha:** Use o botão direito para apagar muros.
* **Interface Completa:** Menu inicial, tela de instruções detalhada e feedback visual de vitória/derrota.
* **Métricas:** Contador de passos (nós percorridos) ao final da execução.

## 🛠️ Tecnologias e Conceitos Aplicados

Este projeto foi desenvolvido para demonstrar domínio em:

* **Java Core & POO:** Estrutura de classes, herança e polimorfismo.
* **Java Swing (GUI):** Manipulação de gráficos 2D (`Graphics g`), `CardLayout` para navegação de telas e Event Listeners (Mouse/Teclado).
* **Estruturas de Dados:** Uso intensivo de Matrizes (`Node[][]`) e **Priority Queue** (Fila de Prioridade) para a lógica do Dijkstra.
* **Multithreading:** Execução do algoritmo em uma thread separada para não travar a interface gráfica durante a animação.

## 🧠 Como Funciona o Algoritmo (Dijkstra)

O algoritmo começa no nó de origem (Verde) e explora os vizinhos camada por camada, como se fosse água derramada no chão.
1.  Ele atribui uma distância preliminar a cada nó (0 para o início, infinito para os outros).
2.  Ele visita o nó com a menor distância não visitada.
3.  Calcula a distância para os vizinhos. Se encontrar um caminho mais curto, atualiza a distância e define o "pai" desse vizinho.
4.  Repete até chegar ao destino (Vermelho).
5.  **Backtracking:** Ao final, o programa volta do destino até o início seguindo os "pais" para desenhar a linha azul (o caminho final).

## 💻 Como Executar

### Pré-requisitos
* Java Development Kit (JDK) 8 ou superior.

### Pelo Eclipse/IntelliJ (Recomendado)
1.  Importe a pasta do projeto.
2.  Localize a classe `PathfindingApp.java` no pacote `principal`.
3.  Clique com o botão direito > **Run As > Java Application**.

### Pelo Terminal
Compile e execute a partir da pasta raiz do código fonte (`src`):

```bash
# Compilar
javac -d . PathfindingApp.java

# Executar
java principal.PathfindingApp
```

## 🎮 Controles

A interação é feita principalmente pelo mouse, mas existem atalhos de teclado disponíveis:

| Ação | Comando |
| :--- | :--- |
| **Desenhar Muros** | 🖱️ Botão **Esquerdo** do Mouse (Clicar e Arrastar) |
| **Mover Início/Fim** | 🖱️ Botão **Esquerdo** (Arrastar os quadrados Verde/Vermelho) |
| **Apagar Muros** | 🖱️ Botão **Direito** do Mouse |
| **Iniciar Algoritmo** | ▶️ Botão **INICIAR** na tela (ou tecla `ENTER`) |
| **Resetar Mapa** | 🔄 Botão **LIMPAR** na tela (ou tecla `R`) |
