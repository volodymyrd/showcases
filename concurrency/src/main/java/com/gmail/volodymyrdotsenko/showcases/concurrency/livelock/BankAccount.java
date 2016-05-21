package com.gmail.volodymyrdotsenko.showcases.concurrency.livelock;

/**
 * Created by Volodymyr Dotsenko on 5/20/16.
 *
 * https://richardbarabe.wordpress.com/2014/02/21/java-deadlock-livelock-and-lock-starvation-examples/
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance;
    private int id;
    private Lock lock = new ReentrantLock();

    BankAccount(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    private boolean withdraw(double amount) {
        System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Withdraw: try lock");
        if (this.lock.tryLock()) {
            System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Start withdraw...");
            // Wait to simulate io like database access ...
            try {
                Thread.sleep(10l);
            } catch (InterruptedException e) {
            }
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Withdraw successful...");
            return true;
        }
        System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Withdraw failed...");
        return false;
    }

    boolean deposit(double amount) {
        System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Deposit: try lock");
        if (this.lock.tryLock()) {
            System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Start deposit...");
            // Wait to simulate io like database access ...
            try {
                Thread.sleep(10l);
            } catch (InterruptedException e) {
            }
            balance += amount;
            System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Deposit successful...");
            return true;
        }
        System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Deposit failed...");
        return false;
    }

    public boolean tryTransfer(BankAccount destinationAccount, double amount) {
        if (this.withdraw(amount)) {
            if (destinationAccount.deposit(amount)) {
                return true;
            } else {
                // destination account busy, refund source account.
                this.deposit(amount);
            }
        }

        return false;
    }

    public static void main(String[] args) {
        final BankAccount fooAccount = new BankAccount(1, 500d);
        final BankAccount barAccount = new BankAccount(2, 500d);

        new Thread(new Transaction(fooAccount, barAccount, 10d), "transaction-1").start();
        new Thread(new Transaction(barAccount, fooAccount, 10d), "transaction-2").start();

    }

}

class Transaction implements Runnable {
    private BankAccount sourceAccount, destinationAccount;
    private double amount;

    Transaction(BankAccount sourceAccount, BankAccount destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    public void run() {
        while (!sourceAccount.tryTransfer(destinationAccount, amount)) {
            System.out.println(Thread.currentThread().getName() + ": Transfer is failed, next attempt");

            continue;
        }
        System.out.printf("%s completed ", Thread.currentThread().getName());
    }
}