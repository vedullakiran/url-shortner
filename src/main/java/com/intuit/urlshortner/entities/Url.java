package com.intuit.urlshortner.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;


@Data
@Accessors(chain = true)
@Entity
@Table(
        name = "url",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "shortUrl"),
        },
        indexes = {
                @Index(columnList = "userId, hashedUrl")
        }
)
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private boolean isCustom; //storing custom url
    private String hashedUrl; // hashed URL for long url
    private String userId;
}
