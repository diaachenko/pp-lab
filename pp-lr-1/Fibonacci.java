import java.util.Scanner;
import java.math.BigInteger;

/**
 * Програма, що визначає котрі з n перших чисел ряду Фібоначчі можна подати у вигляді w^2+1, 
 * де w - будь-яке натуральне число.
*/

public class Fibonacci {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n; 
		BigInteger one = new BigInteger("1");
		
		/**
		 * Програма запитує користувача, скільки перших n чисел ряду Фібоначчі потрібно проаналізувати.
		 * (0 < n)
		 */
		
		do {
			System.out.print(" Введіть число n (0 < n): ");
			String inp = scanner.nextLine();
			n = Integer.parseInt(inp);
		} while (n < 1);
		
		scanner.close();
		
		FibNumber[] fibseq = new FibNumber[n];
		fibseq[0] = new FibNumber(new BigInteger("1"), new BigInteger("1"));
		
		System.out.println("\n Числя ряду Фібоначчі (з позначенням тих, що можна подати як w^2+1):");
		
		/**
		 * Якщо користував запитав проаналізувати лише перше число, то рахувати друге і далі не потрібно.
		 */
		
		if (n > 1) {
			fibseq[1] = new FibNumber(new BigInteger("2"), new BigInteger("1"));
			for (int a = 2; a < n; a++) {
				fibseq[a] = new FibNumber(new BigInteger(String.valueOf(a+1)), fibseq[a-2].getVal().add(fibseq[a-1].getVal()));
			}
		}
		
		for (int a = 0; a < fibseq.length; a++) {
			System.out.print("\n " + fibseq[a].getOrd() + "-е число ряду: " + fibseq[a].getVal() + ";");
			if (fibseq[a].isSquared()) {
				System.out.print("\t <- це число можна подати як w^2+1, де w = " + fibseq[a].getVal().subtract(one).sqrt());
			}
		}
		
		/** 
		 * Позначка для користувача, щоб він знав, що програма закінчила роботу.
		 */
		
		System.out.println("\n\n Кінець виводу.");
	}
}

/**
 * Клас: число ряду Фібоначчі.
 */

class FibNumber {
	BigInteger order;
	BigInteger value;
	
	/**
	 * Конструктор класу.
	 * @param order порядок числа у ряді.
	 * @param value значення числа ряду.
	 */
	
	public FibNumber(BigInteger order, BigInteger value) {
		this.order = order;
		this.value = value;
	}
	
	/**
	 * Метод .get() для порядку числа у ряді.
	 * @return порядок даного числа у ряді.
	 */
	
	public BigInteger getOrd() {
		return this.order;
	}
	
	/**
	 * Метод .get() для значення числа ряду.
	 * @return значення даного числа ряду.
	 */
	
	public BigInteger getVal() {
		return this.value;
	}
	
	/**
	 * Метод .set() для встановлення значення порядку числа у ряді.
	 * @param order порядок числа у ряді.
	 */
	
	public void setOrd(BigInteger order) {
		this.order = order;
	}
	
	/**
	 * Метод .set() для встановлення значення числа у ряді.
	 * @param value значення числа ряду.
	 */
	
	public void setVal(BigInteger value) {
		this.value = value;
	}
	
	/**
	 * Метод, що перевіряє чи дане число можна подати у вигляді w^2+1.
	 * @return true, якщо можна подати; false, якщо ні.
	 */
	
	public boolean isSquared() {
		BigInteger one = new BigInteger("1");
		
		BigInteger sqrted = this.value.subtract(one).sqrt();
		
		
		if (sqrted.multiply(sqrted).compareTo(this.value.subtract(one)) == 0)
			return true;
		else 
			return false;
	}
}