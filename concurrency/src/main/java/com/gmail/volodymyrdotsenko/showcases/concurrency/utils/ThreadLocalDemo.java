package com.gmail.volodymyrdotsenko.showcases.concurrency.utils;

public class ThreadLocalDemo {

	public static void main(String[] args) {
		new Thread(new Task(3000)).start();
		new Thread(new Task(7000)).start();
		new Thread(new Task(10000)).start();
	}

	private static class Task implements Runnable {

		private final int sleep;

		public Task(int sleep) {
			this.sleep = sleep;
		}

		@Override
		public void run() {
			Context context = ContextHolder.getContext();
			context.put(Thread.currentThread().getName());
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println(Thread.currentThread().getName() + ": val=" + context.get());
		}

	}

	private static class Context {
		private String val;

		void put(String val) {
			this.val = val;
		}

		String get() {
			return val;
		}
	}

	private static class ContextHolder {
		private static ThreadLocal<Context> context = new ThreadLocal<Context>() {
			@Override
			protected Context initialValue() {
				return new Context();
			}
		};

		public static Context getContext() {
			return context.get();
		}
	}
}