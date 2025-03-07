package GUICadastroProduto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Produto {
    String nome;
    String descricao;
    double valor;
    boolean disponivel;

    public Produto(String nome, String descricao, double valor, boolean disponivel) {
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.disponivel = disponivel;
    }
}

public class GUICadastroProduto {
    private static final String[] COLUNAS_TABELA = {"Nome", "Valor"};
    private static final String[] OPCOES_DISPONIBILIDADE = {"Sim", "Não"};
    private static final String TITULO_CADASTRO = "Cadastro de Produto";
    private static final String TITULO_LISTAGEM = "Listagem de Produtos";

    private static List<Produto> produtos = new ArrayList<>();
    private static JFrame frameCadastro;
    private static JFrame frameListagem;
    private static DefaultTableModel tableModel;

    public static void main(String[] args) {
        try {
            // Usa o tema padrão do sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(GUICadastroProduto::criarTelaCadastro);
    }

    private static void criarTelaCadastro() {
        frameCadastro = new JFrame(TITULO_CADASTRO);
        frameCadastro.setSize(400, 300);
        frameCadastro.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNome = criarCampoTexto(gbc, 0, "Nome:");
        JTextField txtDescricao = criarCampoTexto(gbc, 1, "Descrição:");
        JTextField txtValor = criarCampoTexto(gbc, 2, "Valor:");
        JComboBox<String> cbDisponivel = criarComboBox(gbc, 3, "Disponível para venda:");

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBackground(new Color(0, 150, 136)); // Cor de fundo
        btnCadastrar.setForeground(Color.WHITE); // Cor do texto
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setOpaque(true);
        btnCadastrar.setBorderPainted(false);
        btnCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCadastrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efeito de hover no botão
        btnCadastrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCadastrar.setBackground(new Color(0, 120, 104)); // Cor mais escura no hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCadastrar.setBackground(new Color(0, 150, 136)); // Cor original
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        frameCadastro.getContentPane().add(btnCadastrar, gbc);

        btnCadastrar.addActionListener(e -> cadastrarProduto(txtNome, txtDescricao, txtValor, cbDisponivel));

        frameCadastro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameCadastro.setLocationRelativeTo(null);
        frameCadastro.setVisible(true);
    }

    private static JTextField criarCampoTexto(GridBagConstraints gbc, int linha, String rotulo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        JLabel label = new JLabel(rotulo);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(60, 60, 60)); // Cor do texto
        frameCadastro.getContentPane().add(label, gbc);

        gbc.gridx = 1;
        JTextField campoTexto = new JTextField(20);
        campoTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoTexto.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // Bordas sutis
        campoTexto.setBackground(new Color(245, 245, 245)); // Fundo suave
        frameCadastro.getContentPane().add(campoTexto, gbc);
        return campoTexto;
    }

    private static JComboBox<String> criarComboBox(GridBagConstraints gbc, int linha, String rotulo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        JLabel label = new JLabel(rotulo);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(60, 60, 60)); // Cor do texto
        frameCadastro.getContentPane().add(label, gbc);

        gbc.gridx = 1;
        JComboBox<String> comboBox = new JComboBox<>(OPCOES_DISPONIBILIDADE);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(new Color(245, 245, 245)); // Fundo suave
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // Bordas sutis
        frameCadastro.getContentPane().add(comboBox, gbc);
        return comboBox;
    }

    private static void cadastrarProduto(JTextField txtNome, JTextField txtDescricao, JTextField txtValor, JComboBox<String> cbDisponivel) {
        String nome = txtNome.getText().trim();
        String descricao = txtDescricao.getText().trim();
        double valor;
        try {
            valor = Double.parseDouble(txtValor.getText().trim());
            if (valor < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frameCadastro, "Valor inválido! Insira um número positivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean disponivel = cbDisponivel.getSelectedItem().equals("Sim");

        produtos.add(new Produto(nome, descricao, valor, disponivel));
        abrirTelaListagem();
        frameCadastro.dispose();
    }

    private static void abrirTelaListagem() {
        if (frameListagem != null) {
            frameListagem.dispose();
        }

        frameListagem = new JFrame(TITULO_LISTAGEM);
        frameListagem.setSize(500, 400);
        frameListagem.getContentPane().setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(COLUNAS_TABELA, 0);
        JTable tabela = new JTable(tableModel);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabela.setGridColor(new Color(224, 224, 224));
        tabela.setShowGrid(true);
        tabela.setIntercellSpacing(new Dimension(0, 0));

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ordenarProdutos();
        atualizarTabela();

        JButton btnNovoCadastro = new JButton("Novo Cadastro");
        btnNovoCadastro.setBackground(new Color(0, 150, 136)); // Cor de fundo
        btnNovoCadastro.setForeground(Color.WHITE); // Cor do texto
        btnNovoCadastro.setFocusPainted(false);
        btnNovoCadastro.setOpaque(true);
        btnNovoCadastro.setBorderPainted(false);
        btnNovoCadastro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNovoCadastro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efeito de hover no botão
        btnNovoCadastro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNovoCadastro.setBackground(new Color(0, 120, 104)); // Cor mais escura no hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNovoCadastro.setBackground(new Color(0, 150, 136)); // Cor original
            }
        });

        // Ação do botão "Novo Cadastro"
        btnNovoCadastro.addActionListener(e -> {
            frameListagem.dispose(); // Fecha a tela de listagem
            SwingUtilities.invokeLater(() -> criarTelaCadastro()); // Recria a tela de cadastro na EDT
        });

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotoes.add(btnNovoCadastro);

        frameListagem.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frameListagem.getContentPane().add(panelBotoes, BorderLayout.SOUTH);

        frameListagem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameListagem.setLocationRelativeTo(null);
        frameListagem.setVisible(true);
    }

    private static void ordenarProdutos() {
        produtos.sort(Comparator.comparingDouble(p -> p.valor));
    }

    private static void atualizarTabela() {
        tableModel.setRowCount(0); 
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{p.nome, String.format("R$ %.2f", p.valor)});
        }
    }
}