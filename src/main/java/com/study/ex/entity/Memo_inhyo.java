package com.study.ex.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo_inhyo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column
    private String memoText;
}
