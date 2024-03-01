package com.example.vacanciesbot.repository;

import com.example.vacanciesbot.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByNameAndLastname(String name, String lastname);
}
