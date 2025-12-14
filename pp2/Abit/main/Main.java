package Abit.main;

import Abit.abitcll.Abiturient;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {		
		Scanner input = new Scanner(System.in);
		
		AbitList abits = new AbitList();
		System.out.println(" Ввід даних закінчено.\n");
		
		System.out.print(" Введіть ім'я, власника(-ів) якого ви хочете знайти:  ");
		String name_s = input.nextLine();
		
		abits.printNamesakes(name_s);
		
		double check_as = 99.9;
		System.out.print("\n Введіть середній бал, абітурієнтів :  ");
		do {
			if (check_as != 99.9) 
				System.out.print(" Хибне значення! Спробуй ще раз:  ");
			check_as = Double.parseDouble(input.nextLine());
			
		} while ((check_as < 0) || (check_as > 100));
		
		abits.printHigher(check_as);
		
		int first_n = 1;
		System.out.print("\n Введіть число абітурієнтів з найвищим балом, яких ви хочете побачити:  ");
		do {
			if (first_n != 1) 
				System.out.print(" Хибне значення! Спробуй ще раз:  ");
			first_n = Integer.parseInt(input.nextLine());
			
		} while ((first_n < 1) || (first_n > abits.getN()));
	
		abits.printFirstN(first_n);
		
		abits.printAll();
		
		System.out.println("\n Додайте елемент: ");
		abits.add();
		
		abits.printAll();
		
		int del_i = 2;
		System.out.println("\n Елемент з яким ID ви хочете видалити? ");
		do {
			if (del_i != 2) 
				System.out.print(" Хибне значення! Спробуй ще раз:  ");
			del_i = Integer.parseInt(input.nextLine());
			
		} while ((del_i < 1) || (del_i > abits.getN()));
		
		abits.delete(del_i);

		abits.printAll();
		
		System.out.print("\n Кінець задачі.");
		input.close();
		
	}
}

class AbitList {
	public ArrayList<Abiturient> array;
	int elem_n;
	
	public AbitList() throws FileNotFoundException {
		this.elem_n = 0;
		
		File Sorc = new File("D:\\Desktop\\dataset.txt");
		Scanner input = new Scanner(Sorc);
		
		this.array = new ArrayList<Abiturient> ();
		
		while(input.hasNextLine()) {
			array.add(new Abiturient());
			
			array.get(elem_n).setID(elem_n + 1);
			array.get(elem_n).setName(input.nextLine());
			array.get(elem_n).setSurname(input.nextLine());
			array.get(elem_n).setFath_name(input.nextLine());
			array.get(elem_n).setAdress(input.nextLine());
			array.get(elem_n).setNumber(input.nextLine());
			array.get(elem_n).setAssesment(Double.parseDouble(input.nextLine()));
			
			this.elem_n++;
		}
		
		input.close();
	}
	
	public int getN() {
		return this.elem_n;
	}
	
	public void sort_by_mark() {
        
        for (int i = 1; i < elem_n; i++) {
            Abiturient key = array.get(i);
            int j = i - 1;

            while (j >= 0 && array.get(j).getAssesment() > key.getAssesment()) {
                array.set(j+1, array.get(j));
                j = j - 1;
            }
            array.set(j+1, key);
        }
    }
	
	public void sort_by_id() {
		for (int i = 1; i < elem_n; i++) {
            Abiturient key = array.get(i);
            int j = i - 1;

            while (j >= 0 && array.get(j).getID() > key.getID()) {
                array.set(j+1, array.get(j));
                j = j - 1;
            }
            array.set(j+1, key);
        }
	}
	
	public void printNamesakes(String name) {
		System.out.println(" Список абітурієнтів з ім'ям " + name + ":");
		boolean b = true;
		for (int i = 0; i < elem_n; i++) {
			if (array.get(i).isNamesake(name)) {
				System.out.println("\t" + array.get(i));
				b = false;
			}
		}
		if (b) 
			System.out.println("\t Абітурієнтів з таким ім'ям немає.");
	}
	
	public void printHigher(double check_as) {
		boolean b = true;
		System.out.println(" Список абітурієнтів, бал яких вище за " + check_as + " :");
		for (int i = 0; i < elem_n; i++) {
			if (array.get(i).isHigher(check_as)) {
				System.out.println("\t" + array.get(i));
				b = false;
			} 
		}
		if (b) 
			System.out.println("\t Абітурієнтів, що мають вищий бал немає.");
	}
	
	public void printFirstN(int n) {
		this.sort_by_mark();
		
		System.out.println(" Перші " + n + " абітурієнтів за середнім балом:");
		for (int i = 1; i <= n; i++) {
			System.out.println("\t" + array.get(elem_n - i));
		}
	}
	
	public void printAll() {	
		this.sort_by_id();
		
		System.out.println("\n Всі записи: ");
		for (int i = 0; (i < this.elem_n); i++)
			System.out.println("\t" + array.get(i));
	}
	
	public void add() {
		Scanner input = new Scanner(System.in);
		array.add(new Abiturient());
		
		array.get(elem_n).setID(elem_n + 1);
		System.out.print(" Введіть ім'я абітурієнта:  ");
		array.get(elem_n).setName(input.nextLine());
		
		System.out.print(" Його прізвище:  ");
		array.get(elem_n).setSurname(input.nextLine());
		
		System.out.print(" Його по-батькові:  ");
		array.get(elem_n).setFath_name(input.nextLine());
		
		System.out.print(" Його адресу:  ");
		array.get(elem_n).setAdress(input.nextLine());
		
		System.out.print(" Номер телефону:  ");
		array.get(elem_n).setNumber(input.nextLine());
		
		System.out.print(" Його середній бал:  ");
		do {
			if (array.get(elem_n).getAssesment() != 67.67) 
				System.out.print( "Хибне значення! Спробуйте ще раз:  ");
			
			array.get(elem_n).setAssesment(input.nextDouble());
			input.nextLine();
			
		} while ((array.get(elem_n).getAssesment() > 100) || (array.get(elem_n).getAssesment() < 0));
		
		elem_n++;
	}
	
	public void delete(int id) {
		int i = id - 1;
		
		array.remove(i);
		elem_n--;
	}
}
