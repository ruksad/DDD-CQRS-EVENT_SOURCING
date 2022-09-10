package com.springbank.bankacc.core.events;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FundsWithdrawnEvent {
    private String id;
    private double amount;
    private double balance;
}