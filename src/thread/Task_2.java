package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Task_2 {
    private static ExecutorService threadPool;


    public static void runTask() throws ExecutionException, InterruptedException {
        int arrayInt[] = generateArray(80);
        int countCore = Runtime.getRuntime().availableProcessors();
        threadPool = Executors.newFixedThreadPool(countCore);
        //System.out.println("Enter N");
        for (int i = 0; i < 3; i++) {
            System.out.println((i + 1) + " прогон " + "- режим Thread результат " + startModeThread(countCore, arrayInt));
            System.out.println((i + 1) + " прогон " + "- режим ThreadPool результат " + startModeThreadPool(countCore, arrayInt));
            System.out.println((i + 1) + " прогон закончен");
        }
    }

    private static Double startModeThread(int countCore, int[] arrayInt) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        double result = 0;
        int iter = 0;
        int partArraySize = arrayInt.length / countCore;
        List<FutureTask> taskList = new ArrayList<>();
        for (int i = 0; i < countCore; i++) {
            if (i == 0) {
                iter = 0;
            } else {
                iter += partArraySize;
            }
            int finalIter = iter;
            FutureTask<Double> futureTask = new FutureTask<Double>(new Callable<Double>() {
                @Override
                public Double call() throws Exception {
                    double tempResult = 0;
                    for (int j = finalIter; j < finalIter + partArraySize; j++) {
                        tempResult += (Math.cos(arrayInt[j]) + Math.sin(arrayInt[j]));
                    }
                    return tempResult;
                }
            });
            taskList.add(futureTask);
            new Thread(futureTask).start();
        }
        for (FutureTask value : taskList) {
            result += (double) value.get();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Режим Thread посчитан за " + (endTime - startTime) + "ms");
        return result;
    }

    private static Double startModeThreadPool(int countCore, int[] arrayInt) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        double result = 0;
        int iter = 0;
        int partArraySize = arrayInt.length / countCore;
        List<FutureTask> taskList = new ArrayList<>();
        for (int i = 0; i < countCore; i++) {
            if (i == 0) {
                iter = 0;
            } else {
                iter += partArraySize;
            }
            int finalIter = iter;

            FutureTask<Double> futureTask = new FutureTask<Double>(new Callable<Double>() {
                @Override
                public Double call() throws Exception {
                    double tempResult = 0;
                    for (int j = finalIter; j < finalIter + partArraySize; j++) {
                        tempResult += (Math.cos(arrayInt[j]) + Math.sin(arrayInt[j]));
                    }
                    return tempResult;
                }
            });
            taskList.add(futureTask);
            threadPool.submit(futureTask);
        }
        for (FutureTask value : taskList) {
            result += (double) value.get();
        }
        threadPool.shutdown();
        long endTime = System.currentTimeMillis();
        System.out.println("Режим ThreadPool посчитан за " + (endTime - startTime) + "ms");
        return result;
    }


    // плучаем массив
    private static int[] generateArray(int count) throws ExecutionException, InterruptedException {
        int array[] = new int[count];
        int[] finalArray = array;
        FutureTask<int[]> futureTask = new FutureTask<>(() -> {
            for (int i = 0; i < count; i++) {
                finalArray[i] = i;
            }
            return finalArray;
        });
        long startTime = System.currentTimeMillis();
        new Thread(futureTask).start();
        array = futureTask.get();
        long endTime = System.currentTimeMillis();
        System.out.println("Массив заполнен за " + (endTime - startTime) + "ms");
        return array;
    }

    // проверка на ввод числа
    private static int checkNumber() {
        Scanner sc = new Scanner(System.in);
        int value;
        while (true) {
            try {
                int tempValue = sc.nextInt();
                value = tempValue;
                sc.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Только целое число!\nПовторите ввод:");
                sc.nextLine();
            }
        }
        return value;
    }
}
