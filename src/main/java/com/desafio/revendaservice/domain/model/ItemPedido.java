package com.desafio.revendaservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido {

    @Id @GeneratedValue
    private Long id;
    private String produto;
    private int quantidade;
}