import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.Color;

import javax.swing.JFrame;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

@SuppressWarnings("serial")
public class Interface extends JFrame implements ActionListener {

    // atributos
    private final int NUM_BUTTONS = 3;
    private final int NUM_PAINELS = 4;
    private int key;

    private JFrame lastFrame;
    private final JLabel title;
    private final JButton[] buttons;
    private final JTextField textField;

    AVLTree tree = new AVLTree();

    public Interface() {
        // padronização da janela
        setTitle("Árvore AVL");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        title = new JLabel("Árvore AVL");
        title.setFont(new Font("Times New Roman", Font.BOLD, 25));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        buttons = new JButton[NUM_BUTTONS];
        buttons[0] = new JButton("Inserir");
        buttons[1] = new JButton("Remover");
        buttons[2] = new JButton("Ver árvore");

        for (JButton button : buttons) {
            button.addActionListener(this);
        }

        textField = new JTextField(4);

        JPanel panelTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panelTextField = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panelMain = new JPanel(new GridLayout(NUM_PAINELS, 1));

        panelTitle.add(title);
        panelButton.add(buttons[0]);
        panelButton.add(buttons[1]);
        panelButton.add(buttons[2]);
        panelTextField.add(new JLabel("Valor do nó:"));
        panelTextField.add(textField);

        panelMain.add(panelTitle);
        panelMain.add(new JPanel());
        panelMain.add(panelTextField);
        panelMain.add(panelButton);

        add(panelMain);

        setVisible(true);
    }

    private void clearTextField() {
        textField.setText("");
    }

    private boolean updateKey() {
        String text = textField.getText();
        if (!text.isEmpty()) {
            try {
                key = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    // função auxiliar para adicionar nós e arestas do grafo a partir da árvore AVL
    private void addNodes(Node node, Graph<Integer, String> graph) {
        if (node != null) {
            graph.addVertex(node.key);
            if (node.left != null) {
                graph.addEdge(node.key + "->" + node.left.key, node.key, node.left.key);
                addNodes(node.left, graph);
            }
            if (node.right != null) {
                graph.addEdge(node.key + "->" + node.right.key, node.key, node.right.key);
                addNodes(node.right, graph);
            }
        }
    }

    private void showFrame() throws SVGGraphics2DIOException {
        lastFrame = this;
        
        //cria um grafo direcionado esparsa
        Graph<Integer, String> graph = new DirectedSparseGraph<Integer, String>();

        // adiciona os nós e arestas do grafo a partir da árvore AVL
        addNodes(tree.root, graph);

        // cria um layout de grafo FRLayout
        FRLayout<Integer, String> layout = new FRLayout<Integer, String>(graph);

        // define o tamanho da imagem SVG
        Dimension size = new Dimension(1000, 800);

        // cria um servidor de imagem de visualização
        VisualizationImageServer<Integer, String> vs = new VisualizationImageServer<Integer, String>(layout,
                size);

        // configura o rótulo dos nós
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        // define as cores para os vértices e as arestas
        vs.getRenderContext().setVertexFillPaintTransformer(n -> Color.WHITE);
        vs.getRenderContext().setEdgeDrawPaintTransformer(e -> Color.BLACK);

        // cria um objeto SVGGraphics2D
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument(SVGConstants.SVG_NAMESPACE_URI, "svg", null);
        SVGGraphics2D svg = new SVGGraphics2D(document);

        // renderiza o grafo no objeto SVGGraphics2D
        vs.paint(svg);

        // grava o objeto SVGGraphics2D em um arquivo SVG
        svg.stream("output.svg");

        // exibe o grafo em uma janela JFrame
        JFrame frame = new JFrame();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                lastFrame.setVisible(true);
            }
        });

        frame.getContentPane().add(vs);
        frame.pack();
        frame.setVisible(true);
        lastFrame.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean sucesso = updateKey();

        if (e.getSource() == buttons[2]) {
            System.out.println("Exibindo arvore");

            try {
                showFrame();
            } catch (SVGGraphics2DIOException err) {
                err.printStackTrace();
            }
            return;
        }

        if (!sucesso) {
            System.out.println("Campo contem caractere nao numerico");
            return;
        }

        if (e.getSource() == buttons[0]) {
            System.out.println("Inserindo " + key);
            tree.insert(key);
        } else {
            if (tree.contains(key)) {
                tree.remove(key);
                System.out.println("Removendo " + key);
            } else {
                System.out.println("Não foi possível remover " + key);
            }
        }

        clearTextField();
    }
}