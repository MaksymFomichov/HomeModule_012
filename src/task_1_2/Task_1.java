package task_1_2;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Task_1 {
    private static Scanner sc;

    public static void runTask() throws ExecutionException, InterruptedException {
        double a, b;
        System.out.println("Введите А");
        a = checkNumber();
        System.out.println("Введите В");
        b = checkNumber();
        System.out.println("Какое математическое действие надо применить к этим числам:");
        String choice = sc.nextLine();
        getResult(a, b, choice);
    }

    private static void getResult(double a, double b, String choice) throws ExecutionException, InterruptedException {
        System.out.println("пришло "+choice);
        if (choice.equals("==") || choice.equals(">") || choice.equals(">")) {
            FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
                if (choice.equals("==")) {
                    return a == b;
                } else if (choice.equals(">")) {
                    return a > b;
                } else if (choice.equals("<")) {
                    return a < b;
                } else return null;
            });
            new Thread(futureTask).start();
            System.out.println(a + choice + b + "=" + futureTask.get());
        } else {
            FutureTask<Double> futureTask = new FutureTask<>(() -> {
                if (choice.equals("+")) {
                    return a + b;
                } else if (choice.equals("-")) {
                    return a - b;
                } else if (choice.equals("*")) {
                    return a * b;
                } else if (choice.equals("/")) {
                    return a / b;
                } else if (choice.equals("%")) {
                    return a % b;
                } else return null;
            });
            new Thread(futureTask).start();
            System.out.println(a + choice + b + "=" + futureTask.get());
        }
    }

    // проверка на ввод числа
    private static double checkNumber() {
        sc = new Scanner(System.in);
        double value;
        while (true) {
            try {
                double tempValue = sc.nextDouble();
                value = tempValue;
                sc.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Только число!\nПовторите ввод:");
                sc.nextLine();
            }
        }
        return value;
    }
}
