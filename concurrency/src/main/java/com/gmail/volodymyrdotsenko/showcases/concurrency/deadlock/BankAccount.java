package com.gmail.volodymyrdotsenko.showcases.concurrency.deadlock;

/**
 * Created by Volodymyr Dotsenko on 5/20/16.
 */
public class BankAccount {
    private double balance;
    private int id;

    BankAccount(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    void withdraw(double amount) {
        // Wait to simulate io like database access ...
        System.out.println("Start withdraw...");
        try {
            Thread.sleep(10l);
        } catch (InterruptedException e) {
        }
        balance -= amount;
        System.out.println("Withdraw finished");
    }

    void deposit(double amount) {
        // Wait to simulate io like database access ...
        System.out.println("Start deposit...");
        try {
            Thread.sleep(10l);
        } catch (InterruptedException e) {
        }
        balance += amount;
        System.out.println("Deposit finished");
    }

    static void transfer(BankAccount from, BankAccount to, double amount) {
        synchronized (from) {
            System.out.println("Lock account " + from.id);
            from.withdraw(amount);
            synchronized (to) {
                System.out.println("Lock account " + to.id);
                to.deposit(amount);
            }
        }
    }

    public static void main(String[] args) {
        final BankAccount fooAccount = new BankAccount(1, 100d);
        final BankAccount barAccount = new BankAccount(2, 100d);

        new Thread() {
            public void run() {
                System.out.println("Start transfer from " + fooAccount.id + " to " + barAccount.id);
                BankAccount.transfer(fooAccount, barAccount, 10d);
            }
        }.start();

        new Thread() {
            public void run() {
                System.out.println("Start transfer from " + barAccount.id + " to " + fooAccount.id);
                BankAccount.transfer(barAccount, fooAccount, 10d);
            }
        }.start();

    }
}