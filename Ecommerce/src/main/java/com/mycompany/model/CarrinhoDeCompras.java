/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author raiane.souza
 */
public class CarrinhoDeCompras {

    private List<ItemCarrinho> itens;

    public CarrinhoDeCompras() {
        this.itens = new ArrayList<>();
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public void adicionarItem(ItemCarrinho novoItem) {
        for (ItemCarrinho item : itens) {
            if (item.getProduto().getId() == novoItem.getProduto().getId()) {
                item.setQuantidade(item.getQuantidade() + novoItem.getQuantidade());
                return; 
            }
        }
        itens.add(novoItem);
    }


}
