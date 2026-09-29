package com.portfolio.expensetracker.config;

import com.portfolio.expensetracker.entity.*;
import com.portfolio.expensetracker.enums.RecurrenceType;
import com.portfolio.expensetracker.enums.TransactionType;
import com.portfolio.expensetracker.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, CategoryRepository categoryRepository,
                      TransactionRepository transactionRepository, BudgetRepository budgetRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@expense.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin = userRepository.save(admin);

        Category food = createCategory("Food", "Alimentação", "🍔", TransactionType.EXPENSE, null);
        Category transport = createCategory("Transport", "Transporte", "🚗", TransactionType.EXPENSE, null);
        Category entertainment = createCategory("Entertainment", "Entretenimento", "🎬", TransactionType.EXPENSE, null);
        Category health = createCategory("Health", "Saúde", "🏥", TransactionType.EXPENSE, null);
        Category housing = createCategory("Housing", "Moradia", "🏠", TransactionType.EXPENSE, null);
        Category utilities = createCategory("Utilities", "Serviços", "💡", TransactionType.EXPENSE, null);
        Category education = createCategory("Education", "Educação", "📚", TransactionType.EXPENSE, null);
        Category clothing = createCategory("Clothing", "Vestuário", "👕", TransactionType.EXPENSE, null);
        Category salary = createCategory("Salary", "Salário", "💰", TransactionType.INCOME, null);
        Category freelance = createCategory("Freelance", "Freelance", "💻", TransactionType.INCOME, null);
        Category investments = createCategory("Investments", "Investimentos", "📈", TransactionType.INCOME, null);

        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        createTransaction(new BigDecimal("5500.00"), "Monthly salary", "Salário mensal", TransactionType.INCOME, salary, now.withDayOfMonth(5), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("1200.00"), "Freelance web project", "Projeto web freelance", TransactionType.INCOME, freelance, now.withDayOfMonth(15), false, null, admin);
        createTransaction(new BigDecimal("350.00"), "Stock dividends", "Dividendos de ações", TransactionType.INCOME, investments, now.withDayOfMonth(20), false, null, admin);

        createTransaction(new BigDecimal("1200.00"), "Rent payment", "Pagamento de aluguel", TransactionType.EXPENSE, housing, now.withDayOfMonth(1), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("85.50"), "Electricity bill", "Conta de luz", TransactionType.EXPENSE, utilities, now.withDayOfMonth(10), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("45.90"), "Supermarket groceries", "Compras no supermercado", TransactionType.EXPENSE, food, now.withDayOfMonth(3), false, null, admin);
        createTransaction(new BigDecimal("32.00"), "Restaurant lunch", "Almoço no restaurante", TransactionType.EXPENSE, food, now.withDayOfMonth(7), false, null, admin);
        createTransaction(new BigDecimal("120.00"), "Gas station", "Posto de gasolina", TransactionType.EXPENSE, transport, now.withDayOfMonth(5), false, null, admin);
        createTransaction(new BigDecimal("55.00"), "Cinema tickets", "Ingressos de cinema", TransactionType.EXPENSE, entertainment, now.withDayOfMonth(12), false, null, admin);
        createTransaction(new BigDecimal("250.00"), "Health insurance", "Plano de saúde", TransactionType.EXPENSE, health, now.withDayOfMonth(8), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("89.90"), "Online course", "Curso online", TransactionType.EXPENSE, education, now.withDayOfMonth(14), false, null, admin);
        createTransaction(new BigDecimal("150.00"), "New shoes", "Sapatos novos", TransactionType.EXPENSE, clothing, now.withDayOfMonth(18), false, null, admin);
        createTransaction(new BigDecimal("67.80"), "Pharmacy", "Farmácia", TransactionType.EXPENSE, health, now.withDayOfMonth(9), false, null, admin);
        createTransaction(new BigDecimal("22.50"), "Streaming subscription", "Assinatura de streaming", TransactionType.EXPENSE, entertainment, now.withDayOfMonth(1), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("75.00"), "Internet bill", "Conta de internet", TransactionType.EXPENSE, utilities, now.withDayOfMonth(15), true, RecurrenceType.MONTHLY, admin);

        LocalDate prevMonth = now.minusMonths(1);
        createTransaction(new BigDecimal("5500.00"), "Monthly salary", "Salário mensal", TransactionType.INCOME, salary, prevMonth.withDayOfMonth(5), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("800.00"), "Freelance design work", "Trabalho de design freelance", TransactionType.INCOME, freelance, prevMonth.withDayOfMonth(20), false, null, admin);
        createTransaction(new BigDecimal("1200.00"), "Rent payment", "Pagamento de aluguel", TransactionType.EXPENSE, housing, prevMonth.withDayOfMonth(1), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("92.30"), "Electricity bill", "Conta de luz", TransactionType.EXPENSE, utilities, prevMonth.withDayOfMonth(10), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("210.00"), "Weekly groceries", "Compras da semana", TransactionType.EXPENSE, food, prevMonth.withDayOfMonth(6), false, null, admin);
        createTransaction(new BigDecimal("38.00"), "Bus pass", "Passe de ônibus", TransactionType.EXPENSE, transport, prevMonth.withDayOfMonth(1), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("250.00"), "Health insurance", "Plano de saúde", TransactionType.EXPENSE, health, prevMonth.withDayOfMonth(8), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("65.00"), "Books", "Livros", TransactionType.EXPENSE, education, prevMonth.withDayOfMonth(12), false, null, admin);
        createTransaction(new BigDecimal("22.50"), "Streaming subscription", "Assinatura de streaming", TransactionType.EXPENSE, entertainment, prevMonth.withDayOfMonth(1), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("180.00"), "Winter jacket", "Jaqueta de inverno", TransactionType.EXPENSE, clothing, prevMonth.withDayOfMonth(22), false, null, admin);

        LocalDate twoMonthsAgo = now.minusMonths(2);
        createTransaction(new BigDecimal("5500.00"), "Monthly salary", "Salário mensal", TransactionType.INCOME, salary, twoMonthsAgo.withDayOfMonth(5), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("1500.00"), "Freelance API project", "Projeto de API freelance", TransactionType.INCOME, freelance, twoMonthsAgo.withDayOfMonth(25), false, null, admin);
        createTransaction(new BigDecimal("1200.00"), "Rent payment", "Pagamento de aluguel", TransactionType.EXPENSE, housing, twoMonthsAgo.withDayOfMonth(1), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("78.40"), "Electricity bill", "Conta de luz", TransactionType.EXPENSE, utilities, twoMonthsAgo.withDayOfMonth(10), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("155.00"), "Groceries", "Compras", TransactionType.EXPENSE, food, twoMonthsAgo.withDayOfMonth(4), false, null, admin);
        createTransaction(new BigDecimal("95.00"), "Gas station", "Posto de gasolina", TransactionType.EXPENSE, transport, twoMonthsAgo.withDayOfMonth(15), false, null, admin);
        createTransaction(new BigDecimal("250.00"), "Health insurance", "Plano de saúde", TransactionType.EXPENSE, health, twoMonthsAgo.withDayOfMonth(8), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("120.00"), "Concert tickets", "Ingressos de show", TransactionType.EXPENSE, entertainment, twoMonthsAgo.withDayOfMonth(18), false, null, admin);
        createTransaction(new BigDecimal("22.50"), "Streaming subscription", "Assinatura de streaming", TransactionType.EXPENSE, entertainment, twoMonthsAgo.withDayOfMonth(1), true, RecurrenceType.MONTHLY, admin);
        createTransaction(new BigDecimal("75.00"), "Internet bill", "Conta de internet", TransactionType.EXPENSE, utilities, twoMonthsAgo.withDayOfMonth(15), true, RecurrenceType.MONTHLY, admin);

        List<Category> expenseCategories = List.of(food, transport, entertainment, health, housing, utilities, education, clothing);
        for (Category cat : expenseCategories) {
            Budget budget = new Budget();
            budget.setCategory(cat);
            budget.setAmount(getBudgetAmount(cat.getName()));
            budget.setMonth(currentMonth);
            budget.setYear(currentYear);
            budget.setUser(admin);
            budgetRepository.save(budget);
        }
    }

    private BigDecimal getBudgetAmount(String categoryName) {
        return switch (categoryName) {
            case "Food" -> new BigDecimal("400.00");
            case "Transport" -> new BigDecimal("200.00");
            case "Entertainment" -> new BigDecimal("150.00");
            case "Health" -> new BigDecimal("350.00");
            case "Housing" -> new BigDecimal("1300.00");
            case "Utilities" -> new BigDecimal("200.00");
            case "Education" -> new BigDecimal("150.00");
            case "Clothing" -> new BigDecimal("200.00");
            default -> new BigDecimal("100.00");
        };
    }

    private Category createCategory(String name, String namePt, String icon, TransactionType type, User user) {
        Category cat = new Category();
        cat.setName(name);
        cat.setNamePt(namePt);
        cat.setIcon(icon);
        cat.setType(type);
        cat.setUser(user);
        return categoryRepository.save(cat);
    }

    private void createTransaction(BigDecimal amount, String desc, String descPt, TransactionType type,
                                    Category category, LocalDate date, boolean recurring,
                                    RecurrenceType recurrenceType, User user) {
        Transaction tx = new Transaction();
        tx.setAmount(amount);
        tx.setDescription(desc);
        tx.setDescriptionPt(descPt);
        tx.setType(type);
        tx.setCategory(category);
        tx.setDate(date);
        tx.setRecurring(recurring);
        tx.setRecurrenceType(recurrenceType);
        tx.setUser(user);
        transactionRepository.save(tx);
    }
}
