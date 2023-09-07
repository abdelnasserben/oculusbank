package com.dabel.oculusbank.repository;

import com.dabel.oculusbank.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {

}
