package com.desafio.revendaservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoRevenda {

    @Id @GeneratedValue
    private Long id;
    private Long revendaId;
    private String clienteId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    private boolean enviado;
}