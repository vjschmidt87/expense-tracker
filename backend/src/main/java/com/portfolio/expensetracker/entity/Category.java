package com.portfolio.expensetracker.entity;

import com.portfolio.expensetracker.enums.TransactionType;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "name_pt", nullable = false, length = 50)
    private String namePt;

    @Column(length = 10)
    private String icon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNamePt() { return namePt; }
    public void setNamePt(String namePt) { this.namePt = namePt; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
