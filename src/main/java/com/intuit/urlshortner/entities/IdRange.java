package com.intuit.urlshortner.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@Accessors(chain = true)
@Table(
        name = "id_range",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "startId"),
        }
)
public class IdRange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startId;
}
