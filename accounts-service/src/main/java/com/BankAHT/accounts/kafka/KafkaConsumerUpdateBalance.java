package com.BankAHT.accounts.kafka;

import com.BankAHT.accounts.dto.AccountDto;
import com.BankAHT.accounts.dto.MessageUpdateBalanceTransaction;
import com.BankAHT.accounts.repository.MessageTransactionRepository;
import com.BankAHT.accounts.service.impl.AccountServiceImlp;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaConsumerUpdateBalance {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerUpdateBalance.class);
    private final AccountServiceImlp accountService;
    private final MessageTransactionRepository messageTransactionRepository;

    private static int dem=0;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

//    @RetryableTopic(backoff = @Backoff(delay = 2000L,multiplier = 2))
    @KafkaListener(
            topics = "balance_updates",
            groupId = "TransactionGroup",
            id = "balanceListener"
    )
    @Transactional
    public void consume(MessageUpdateBalanceTransaction messageTransaction) {
        try {
            LOGGER.info(String.format("Event message received => %s", messageTransaction));
            messageTransactionRepository.save(messageTransaction);

            // Fetch account
            AccountDto account = accountService.fetchAccount(messageTransaction.getAccountId());

            // Update balance
            Long newBalance = account.getBalance() + messageTransaction.getAmount();
            account.setBalance(newBalance);
            accountService.updateBalance(account.getAccountId(),newBalance);
            dem++;
        System.out.println("so lan nhan: "+dem);
        } catch (Exception e) {
            LOGGER.error("Error processing message", e);
            // Handle exception appropriately
            throw new RuntimeException();
        }

    }

}
