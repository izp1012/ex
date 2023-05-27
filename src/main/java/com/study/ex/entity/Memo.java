package com.study.ex.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="Memo_inhyo")
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column
    private String memoText;
}
