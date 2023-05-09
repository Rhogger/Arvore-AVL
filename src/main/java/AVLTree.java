public class AVLTree {

	// Crio a raíz
	Node root;

	// Retorna a altura do nó especificado
	private int getSubtreeHeight(Node node) {

		// Se o nó não existir
		if (node == null) {
			// Retorna uma altura de 0
			return 0;
		}

		return node.height;
	}

	// Retorna o fator de balanceamento do nó raiz da subárvore
	private int getBalanceFactor(Node node) {
		if (node == null) {
			return 0;
		}

		// Faz o calculo da altura da subárvore esquerda da raíz subtraindo com a
		// subárvore direita.
		return getSubtreeHeight(node.left) - getSubtreeHeight(node.right);
	}

	// Rotaciona a subárvore à esquerda do nó especificado para a direita
	private Node rotateSimpleRight(Node node) {
		// (AUX) Filho esquerdo da raíz da subárvore
		Node leftChild = node.left;
		// (AUX) Filho Direito do filho esquerdo da raíz da subárvroe(leftChild)
		Node rightGrandChild = leftChild.right;
		// (AUX) Filho direito da raíz da subárvore
		leftChild.right = node;
		// (NÓ) Filho esquerdo da raíz da subárvore agora recebe o valor do seu filho
		// direito
		node.left = rightGrandChild;
		// (NÓ) Recalcula a altura dessa subárvore
		node.height = Math.max(getSubtreeHeight(node.left), getSubtreeHeight(node.right)) + 1;
		// Recalcula a altura da subárvore esquerda
		leftChild.height = Math.max(getSubtreeHeight(leftChild.left), getSubtreeHeight(leftChild.right)) + 1;

		return leftChild;
	}

	// Rotaciona a subárvore à direita do nó especificado para a esquerda
	private Node rotateSimpleLeft(Node node) {
		// Filho direito da raíz da subárvore
		Node rightChild = node.right;
		// Filho esquerdo do filho direito da raíz da subárvroe(rightChild)
		Node leftGrandChild = rightChild.left;
		// Filho esquerdo da raíz da subárvore
		rightChild.left = node;
		// Filho direito da raíz da subárvore agora recebe o valor do seu filho esquerdo
		node.right = leftGrandChild;
		// Recalcula a altura dessa subárvore
		node.height = Math.max(getSubtreeHeight(node.left), getSubtreeHeight(node.right)) + 1;
		// Recalcula a altura da subárvore direita
		rightChild.height = Math.max(getSubtreeHeight(rightChild.left), getSubtreeHeight(rightChild.right)) + 1;

		return rightChild;
	}

	// Insere um novo nó com a chave especificada na árvore AVL
	// Node raíz, ou raíz da subárvore (quando houver recursão) e key é o valor que
	// quero inserir
	private Node insert(Node node, int key) {
		// Quando a raíz não existir, eu simplesmente insiro, ou seja, crio a raíz
		if (node == null) {
			return new Node(key);
		}

		// Verifico onde o meu valor será inserido, de forma que se o valor passado como
		// parâmetro (que eu quero inserir) é menor do que a raíz
		if (key < node.key) {
			// Pego a subárvore esquerda e executo a função insert de novo, ou seja,
			// utilizo de recursão, na proxima vez que eu for verificar, eu estarei
			// analisando a posição exata onde o nó ficará
			node.left = insert(node.left, key);
		} else if (key > node.key) {
			// Pego a subárvore direita e executo a função insert de novo, ou seja,
			// utilizo de recursão, na proxima vez que eu for verificar, eu estarei
			// analisando a posição exata onde o nó ficará
			node.right = insert(node.right, key);
		} else {
			return node;
		}

		// Atualiza a altura do nó que está sendo analisado
		node.height = Math.max(getSubtreeHeight(node.left), getSubtreeHeight(node.right)) + 1;

		
		// Crio uma variável que será o valor do resultado do fator de balanceamento
		int balanceFactor = getBalanceFactor(node);

		// Se o fator de balanceamento for maior que 1 e o valor do nó esquerdo da raíz (da subárvore)
		if (balanceFactor > 1 && key < node.left.key)
			return rotateSimpleRight(node);

		// Se o fator de balanceamento for 
		if (balanceFactor < -1 && key > node.right.key)
			return rotateSimpleLeft(node);

		// Se o fator de balanceamento for maior que 1 e 
		if (balanceFactor > 1 && key > node.left.key) {
			node.left = rotateSimpleLeft(node.left);
			return rotateSimpleRight(node);
		}

		// Se o fator de balanceamento for 
		if (balanceFactor < -1 && key < node.right.key) {
			node.right = rotateSimpleRight(node.right);
			return rotateSimpleLeft(node);
		}

		return node;
	}

	// Função que insere na interface
	public void insert(int key) {
		// Utiliza a raíz pra receber os valores, porque conforme for balanceando a
		// árvore, a raíz será atualizada
		root = insert(root, key);
	}

	private Node remove(Node node, int key) {
		if (node == null) {
			return null;
		}

		// Busca o nó com a chave especificada
		if (key < node.key) {
			node.left = remove(node.left, key);
		} else if (key > node.key) {
			node.right = remove(node.right, key);
		} else {
			// Caso 1: nó a ser removido não tem filhos
			if (node.left == null && node.right == null) {
				return null;
			}

			// Caso 2: nó a ser removido tem apenas um filho
			if (node.left == null) {
				return node.right;
			} else if (node.right == null) {
				return node.left;
			}

			// Caso 3: nó a ser removido tem dois filhos
			Node minRight = findMin(node.right);
			node.key = minRight.key;
			node.right = remove(node.right, minRight.key);
		}

		// Atualiza a altura do nó
		node.height = 1 + Math.max(getSubtreeHeight(node.left), getSubtreeHeight(node.right));

		// Verifica o fator de balanceamento do nó e realiza as rotações necessárias
		int balanceFactor = getBalanceFactor(node);
		
		if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
			return rotateSimpleRight(node);
		}
		if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
			node.left = rotateSimpleLeft(node.left);
			return rotateSimpleRight(node);
		}
		if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
			return rotateSimpleLeft(node);
		}
		if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
			node.right = rotateSimpleRight(node.right);
			return rotateSimpleLeft(node);
		}

		return node;
	}

	public void remove(int key) {
		root = remove(root, key);
	}

	private Node findMin(Node node) {
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}

	public boolean contains(int value) {
		if (search(value) == true) {
			return true;
		} else {
			return false;
		}
	}

	public boolean search(int value) {
		return search(root, value);
	}

	private boolean search(Node node, int value) {
		if (node == null) {
			return false;
		}
		if (value == node.key) {
			return true;
		}
		if (value < node.key) {
			return search(node.left, value);
		} else {
			return search(node.right, value);
		}
	}
}