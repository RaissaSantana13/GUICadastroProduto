package GUICadastroProduto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
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
        SwingUtilities.invokeLater(GUICadastroProduto::criarTelaCadastro);
    }

    /**
     * @wbp.parser.entryPoint
     */
    private static void criarTelaCadastro() {
        frameCadastro = new JFrame(TITULO_CADASTRO);
        frameCadastro.setSize(300, 300);
        frameCadastro.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNome = criarCampoTexto(gbc, 0, "Nome:");
        JTextField txtDescricao = criarCampoTexto(gbc, 1, "Descrição:");
        JTextField txtValor = criarCampoTexto(gbc, 2, "Valor:");
        JComboBox<String> cbDisponivel = criarComboBox(gbc, 3, "Disponível para venda:");

        JButton btnCadastrar = new JButton("Cadastrar");
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
        frameCadastro.getContentPane().add(new JLabel(rotulo), gbc);

        gbc.gridx = 1;
        JTextField campoTexto = new JTextField(15);
        frameCadastro.getContentPane().add(campoTexto, gbc);
        return campoTexto;
    }

    private static JComboBox<String> criarComboBox(GridBagConstraints gbc, int linha, String rotulo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        frameCadastro.getContentPane().add(new JLabel(rotulo), gbc);

        gbc.gridx = 1;
        JComboBox<String> comboBox = new JComboBox<>(OPCOES_DISPONIBILIDADE);
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
        frameListagem.setSize(400, 300);
        frameListagem.getContentPane().setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(COLUNAS_TABELA, 0);
        JTable tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        ordenarProdutos();
        atualizarTabela();

        JButton btnNovoCadastro = new JButton("Novo Cadastro");
        btnNovoCadastro.addActionListener(e -> {
            frameListagem.dispose();
            criarTelaCadastro();
        });

        frameListagem.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frameListagem.getContentPane().add(btnNovoCadastro, BorderLayout.SOUTH);

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