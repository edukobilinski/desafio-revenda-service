package com.desafio.revendaservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contato {

    @Id @GeneratedValue
    private Long id;
    private String nome;
    private boolean principal;
}