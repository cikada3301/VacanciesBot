package com.example.vacanciesbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String lastname;

    @Column
    private String position;

    @Column
    private String resumeId;

    @Column
    private String access;

    @Column
    private String refresh;

    @Column(name = "expire_time")
    private ZonedDateTime expireTime;
}

