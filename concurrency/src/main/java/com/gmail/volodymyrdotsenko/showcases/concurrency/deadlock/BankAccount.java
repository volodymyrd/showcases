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

    private void withdraw(double amount) {
        // Wait to simulate io like database access ...
        System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Start withdraw...");
        try {
            Thread.sleep(10l);
        } catch (InterruptedException e) {
        }
        balance -= amount;
        System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Withdraw finished");
    }

    private void deposit(double amount) {
        // Wait to simulate io like database access ...
        System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Start deposit...");
        try {
            Thread.sleep(10l);
        } catch (InterruptedException e) {
        }
        balance += amount;
        System.out.println(Thread.currentThread().getName() + ": Account id:" + id + " Deposit finished");
    }

    private static void transfer(BankAccount from, BankAccount to, double amount) {
        System.out.println(Thread.currentThread().getName() + ": Try lock account " + from.id);
        synchronized (from) {
            System.out.println(Thread.currentThread().getName() + ": Lock account " + from.id);
            from.withdraw(amount);
            System.out.println(Thread.currentThread().getName() + ": Try lock account " + to.id);
            synchronized (to) {
                System.out.println(Thread.currentThread().getName() + ": Lock account " + to.id);
                to.deposit(amount);
            }
        }
    }

    public static void main(String[] args) {
        final BankAccount fooAccount = new BankAccount(1, 100d);
        final BankAccount barAccount = new BankAccount(2, 100d);

        new Thread() {
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": Start transfer from " + fooAccount.id + " to " + barAccount.id);
                BankAccount.transfer(fooAccount, barAccount, 10d);
            }
        }.start();

        new Thread() {
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": Start transfer from " + barAccount.id + " to " + fooAccount.id);
                BankAccount.transfer(barAccount, fooAccount, 10d);
            }
        }.start();

    }
}