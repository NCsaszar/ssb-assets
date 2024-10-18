package com.SecureSentinel.CardsLoans.Repository;

import com.SecureSentinel.CardsLoans.Model.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCardRepository extends JpaRepository<UserCard,Integer> {
    List<UserCard> findUserCardsByUserID(Integer user_id);
    List<UserCard> findUserCardsByAccountID(Integer accountID);
    UserCard findUserCardByCardID(Integer card_id);
    List<UserCard> findActiveUserCardsByUserID(Integer user_id);
    List<UserCard> findActiveUserCardsByAccountID(Integer accountID);


    @Query("SELECT uc FROM UserCard uc " +
            "WHERE (:cardID IS NULL OR uc.cardID = :cardID) " +
            "AND (:userID IS NULL OR uc.userID = :userID) " +
            "AND (:accountID IS NULL OR uc.accountID = :accountID)")
    List<UserCard> findUserCardsByCardIDAndUserIDAndAccountID(@Param("cardID") Integer cardID, @Param("userID") Integer userID,@Param("accountID") Integer accountID);

}
