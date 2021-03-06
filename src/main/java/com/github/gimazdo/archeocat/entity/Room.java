package com.github.gimazdo.archeocat.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Room {

    @Id
    private Long id;

    @Lob
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    private Image image;

}
