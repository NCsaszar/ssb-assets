package com.SecureSentinel.CardsLoans.Repository;

import com.SecureSentinel.CardsLoans.Model.CardOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardOfferRepository extends JpaRepository<CardOffer,Integer> {
    @Query(value = "SELECT co FROM CardOffer co WHERE(:creditLimit IS NULL OR co.creditLimit <= :creditLimit)")
    List<CardOffer> findCardOfferByCreditLimit(@Param("creditLimit") Integer creditLimit);

    CardOffer findCardOfferByCardOfferName(String name);

}
