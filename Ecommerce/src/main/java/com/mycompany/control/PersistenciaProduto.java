/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.control;

import com.mycompany.model.Produto;
import com.mycompany.dao.ConexaoBD; 
import com.mycompany.dao.ConexaoMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.model.CarrinhoDeCompras;
import com.mycompany.model.ItemCarrinho;
import com.mycompany.model.Produto;
/**
 *
 * @author raiane.souza
 */
public class PersistenciaProduto implements RepositorioDeProdutos {
    private ConexaoBD conexaoBD = new ConexaoMySQL(); 
    
    private static final String NOME_TABELA = "produtos"; 
    private static final String ID = "id"; 
    private static final String NOME = "nome"; 
    private static final String DESCRICAO = "descricao"; 
    private static final String PRECO = "preco"; 
    private static final String QUANTIDADE_ESTOQUE = "quantidade_estoque"; 
    
    private final CarrinhoDeCompras carrinho;

    public PersistenciaProduto(CarrinhoDeCompras carrinho) {
        this.carrinho = carrinho;
    }

    @Override
    public List<Produto> listarTodos() throws Exception {
        List<Produto> produtos = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexao = conexaoBD.obterConexao();
            
            String sql = "SELECT * FROM produtos"; 

            ps = conexao.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getDouble("preco"),
                    rs.getInt("quantidade_estoque")
                );
                produtos.add(produto);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao listar produtos no banco de dados: " + e.getMessage());
        } finally {
            conexaoBD.fecharConexao(conexao); 
        }
        return produtos;
    }

    @Override
    public Produto buscarPorId(int id) throws Exception { 
        Connection conexao = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexao = conexaoBD.obterConexao();
            
            String sql = "SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = ?"; 

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, id); 
            rs = ps.executeQuery();

            if (rs.next()) {
                Produto produto = new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getDouble("preco"),
                    rs.getInt("quantidade_estoque")
                );
                return produto; 
            } else {
                throw new Exception("Produto com ID " + id + " não encontrado no estoque.");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar produto no banco de dados: " + e.getMessage()); 
        } finally {
            conexaoBD.fecharConexao(conexao); 
        }
    }
    
    @Override
    public boolean adicionarProduto(int idProduto, int quantidade) throws Exception {
        
        Produto produto = this.buscarPorId(idProduto); 
        
        if (produto.getQuantidade_estoque() < quantidade) {
            System.out.println("Estoque insuficiente.");
            return false;
        }
        
        this.carrinho.adicionarItem(new ItemCarrinho(produto, quantidade)); 
        System.out.println("Produto '" + produto.getNome() + "' (Qtd: " + quantidade + ") adicionado ao carrinho.");  
        
        return true;
    }
}
