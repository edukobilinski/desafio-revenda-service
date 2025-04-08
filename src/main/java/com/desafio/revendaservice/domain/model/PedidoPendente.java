package com.desafio.revendaservice.domain.model;import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoPendente {

    @Id @GeneratedValue
    private Long id;
    private Long pedidoOriginalId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ItemPedido> itens;
}