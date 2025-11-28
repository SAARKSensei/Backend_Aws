package com.sensei.backend.repository;

import com.sensei.backend.entity.TransactionMaster;
import com.sensei.backend.enums.TransactionStatus;
import com.sensei.backend.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionMasterRepository extends JpaRepository<TransactionMaster, String> {

    Optional<TransactionMaster> findByGatewayTransactionId(String gatewayTransactionId);

    List<TransactionMaster> findByChildIdOrderByTransactionDateDesc(String childId);

    List<TransactionMaster> findByParentIdOrderByTransactionDateDesc(String parentId);

    List<TransactionMaster> findByStatus(TransactionStatus status);

    List<TransactionMaster> findByTransactionType(TransactionType transactionType);

    List<TransactionMaster> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<TransactionMaster> findByChildIdAndStatus(String childId, TransactionStatus status);

    @Query("SELECT SUM(t.finalAmount) FROM TransactionMaster t WHERE t.status = 'SUCCESS' AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenue(@Param("startDate") LocalDateTime startDate, 
                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(t.finalAmount) FROM TransactionMaster t WHERE t.transactionType = :type AND t.status = 'SUCCESS'")
    BigDecimal getRevenueByType(@Param("type") TransactionType type);
}
