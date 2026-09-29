package com.portfolio.expensetracker.service;

import com.portfolio.expensetracker.dto.request.TransactionRequest;
import com.portfolio.expensetracker.dto.response.TransactionResponse;
import com.portfolio.expensetracker.entity.Category;
import com.portfolio.expensetracker.entity.Transaction;
import com.portfolio.expensetracker.entity.User;
import com.portfolio.expensetracker.enums.TransactionType;
import com.portfolio.expensetracker.exception.ResourceNotFoundException;
import com.portfolio.expensetracker.exception.UnauthorizedException;
import com.portfolio.expensetracker.repository.CategoryRepository;
import com.portfolio.expensetracker.repository.TransactionRepository;
import com.portfolio.expensetracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository,
                              UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<TransactionResponse> findAll(String username, TransactionType type, Long categoryId,
                                              LocalDate startDate, LocalDate endDate, Integer month, Integer year) {
        User user = findUser(username);
        if (month != null && year != null) {
            return transactionRepository.findByMonthYear(user.getId(), month, year)
                    .stream().map(TransactionResponse::from).toList();
        }
        return transactionRepository.findByFilters(user.getId(), type, categoryId, startDate, endDate)
                .stream().map(TransactionResponse::from).toList();
    }

    public TransactionResponse findById(Long id, String username) {
        Transaction t = findTransaction(id, username);
        return TransactionResponse.from(t);
    }

    @Transactional
    public TransactionResponse create(TransactionRequest request, String username) {
        User user = findUser(username);
        Category cat = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Transaction t = new Transaction();
        mapRequest(t, request, cat, user);
        return TransactionResponse.from(transactionRepository.save(t));
    }

    @Transactional
    public TransactionResponse update(Long id, TransactionRequest request, String username) {
        Transaction t = findTransaction(id, username);
        Category cat = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        mapRequest(t, request, cat, t.getUser());
        return TransactionResponse.from(transactionRepository.save(t));
    }

    @Transactional
    public void delete(Long id, String username) {
        Transaction t = findTransaction(id, username);
        transactionRepository.delete(t);
    }

    public String exportCsv(String username) {
        User user = findUser(username);
        List<Transaction> all = transactionRepository.findAllByUserOrderByDateDesc(user.getId());
        StringBuilder sb = new StringBuilder();
        sb.append("Date,Type,Category,Amount,Description\n");
        for (Transaction t : all) {
            sb.append(t.getDate()).append(",")
              .append(t.getType()).append(",")
              .append(t.getCategory().getName()).append(",")
              .append(t.getAmount()).append(",")
              .append(t.getDescription() != null ? "\"" + t.getDescription().replace("\"", "\"\"") + "\"" : "")
              .append("\n");
        }
        return sb.toString();
    }

    private void mapRequest(Transaction t, TransactionRequest req, Category cat, User user) {
        t.setAmount(req.amount());
        t.setDescription(req.description());
        t.setDescriptionPt(req.descriptionPt());
        t.setType(req.type());
        t.setCategory(cat);
        t.setDate(req.date());
        t.setRecurring(req.recurring() != null ? req.recurring() : false);
        t.setRecurrenceType(req.recurrenceType());
        t.setUser(user);
    }

    private Transaction findTransaction(Long id, String username) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        if (!t.getUser().getUsername().equals(username)) throw new UnauthorizedException("Not your transaction");
        return t;
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
