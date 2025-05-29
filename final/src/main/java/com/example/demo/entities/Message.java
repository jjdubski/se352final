package com.example.demo.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

//@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    private Property property;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column
    private String reply;

    public Message() {
    }

    public Message(User sender, User recipient, String content, Property property, String reply) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.property = property;
        this.reply = reply;
    }
}
