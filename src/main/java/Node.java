import java.util.ArrayList;
import java.util.List;

// Classe da Árvore AVL que representa um Nó, tendo como valor (key),
// altura e outros nós da esquerda e direita.
public class Node {
	int key;
	int height;
	Node left, right;
	
	// Construtor do Nó, que obriga a passar o valor do nó e a sua altura é 1
	//Por que 1? Porque cada nó tem como padrão altura 1, sendo assim, a cada inserção/remoção de um nó dessa subárvore onde o nó instanciado seja a raíz, a altura será ajustada.
	// A altura é referente a subárvore, tendo como raíz esse nó.
	Node(int key) {
		this.key = key;
		this.height = 1;
	}

	// Método que busca os filhos de uma subárvore.
	public List<Node> getChildren(Node node) {
		
		// Lista dos filhos
		List<Node> children = new ArrayList<>();
		
		//Se o filho esquerdo for diferente de null, ou seja, existir, ele adiciona na lista
		if (node.left != null) {
			children.add(node.left);
		}
		
		//Se o filho direito existir, ele adiciona na lista
		if (node.right != null) {
			children.add(node.right);
		}
		return children;
	}
}