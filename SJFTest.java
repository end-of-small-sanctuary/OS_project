import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

//both this and the previous draft skip over the second Queue when run, i've yet to try solving this issue
//it will correctly run Q1 though

/*
Refrences used:

[1] R. Fadatare, “Java sort array objects using comparable interface,” 
Java Guides, https://www.javaguides.net/2020/04/java-sort-array-objects-using-comparable-interface.html
[2] GfG, “Round Robin scheduling with different arrival times,” GeeksforGeeks,
 https://www.geeksforgeeks.org/round-robin-scheduling-with-different-arrival-times/ 

*/

public class SJFTest {
//initialization
  private static final int QUANTUM = 3; 
	    private static int processIndex=0;
	    private static PCB[] Q1; 
	    public static PCB[] Q2;
	    private static int Q1Processes= 0;
	    private static int Q2Processes= 0;
	    private static int totalProcesses= 0;

	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
	//menu 
	        System.out.println("Welcome to Multilevel Queue Scheduler");
	        while (true) {
	            System.out.println("\nMenu:");
	            System.out.println("1. Enter process information");
	            System.out.println("2. Report detailed information");
	            System.out.println("3. Exit");

	            System.out.print("Enter your choice: ");
	            int choice = scanner.nextInt();
	            
	            switch (choice) {
	                case 1:
	                     System.out.print("Enter total number of processes: ");
	                     totalProcesses = scanner.nextInt();
	                    enterProcessInfo(totalProcesses, scanner);
	                    break;                   
	                case 2:
	                    if ( Q1!=null || Q2 != null) {
	                        reportDetailedInfo(Q1,Q2,totalProcesses);
	                    } else {
	                        System.out.println("Please enter process information first.");
	                    }
	                    break;
	                case 3:
	                    System.out.println("Exiting the program.");
	                    scanner.close();
	                    return;
	                default:
	                    System.out.println("Invalid choice. Please enter again.");
	            }
	        }
	    }

	   //enter information loop method 
	    private static void enterProcessInfo(int totalProcesses, Scanner scanner) {
	    
	        Q1 = new PCB[totalProcesses];
	        Q2 = new PCB[totalProcesses];
	      for (int i = 0; i < totalProcesses; i++) {
	            System.out.println("\nProcess " + (i + 1) + ":");
	            System.out.print("Enter priority (1 or 2): ");
	            int priority = scanner.nextInt();
	            System.out.print("Enter arrival time: ");
	            int arrivalTime = scanner.nextInt();
	            System.out.print("Enter CPU burst time: ");
	            int cpuBurst = scanner.nextInt();

	            //creates an object then inserts it into the array in each loop iteration
	            PCB process = new PCB("P" + (i + 1), priority, arrivalTime, cpuBurst);
	            if (priority == 1){
	                Q1[i] = process;
	                Q1Processes++;}
	            else{
	                Q2[i] = process;
	                Q2Processes++;}
	}
	    }


	    //report detailed process method
	        private static void reportDetailedInfo(PCB[]Q1, PCB[] Q2,int totalProcesses) {
	        try {
	        PrintWriter writer = new PrintWriter("Report1.txt");

	        
	        StringBuilder schedulingOrder = new StringBuilder();
	        StringBuilder processData = new StringBuilder();
	        int currentTime = 0;
	        int timer=0;
	        int totalTurnaroundTime = 0;
	        int totalWaitingTime = 0;
	        int totalResponseTime = 0;
	        boolean done = true;
	        
	        
	         
	//start of algorithm method

	        do {
	            done = true;

	            // Q1 algorithm
	            if (Q1 != null) {
	                boolean processExecuted = false; // Flag to track if any process was executed

	                for (int j = 0; j < Q1Processes; j++) {
	                    timer++;
	                    
	                    if (Q1[j] == null)
	                        continue;

	                    if (Q1[j].cpuBurst > 0) {
	                        done = false;
	                        
	                        if (Q1[j].arrivalTime <= timer) { // Process can execute
	                            processExecuted = true;

	                            if (Q1[j].responseTime == 0)
	                                Q1[j].responseTime = currentTime + Q1[j].arrivalTime;

	                            Q1[j].waitingTime += currentTime - Q1[j].startTime;
	                            Q1[j].startTime = currentTime;
	                            schedulingOrder.append(Q1[j].processID).append(" | ");

	                            if (Q1[j].cpuBurst > QUANTUM) {
	                                currentTime += QUANTUM;
	                                Q1[j].cpuBurst -= QUANTUM;
	                            } else {
	                                currentTime += Q1[j].cpuBurst;
	                                Q1[j].terminationTime = currentTime;
	                                Q1[j].turnaroundTime = Q1[j].terminationTime - Q1[j].arrivalTime;
	                                totalTurnaroundTime += Q1[j].turnaroundTime;
	                                Q1[j].waitingTime -= Q1[j].turnaroundTime;
	                                totalWaitingTime += Q1[j].waitingTime;
	                                totalResponseTime += Q1[j].responseTime;
	                                processData.append(Q1[j].toString()).append("\n");
	                                Q1[j] = null;
	                            }
	                            timer += currentTime;
	                        }
	                    } else {
	                        timer++;
	                        continue;
	                    }
	                }

	                if (!processExecuted) {
	                    currentTime++;
	                }
	            }
	        } while (!done);

	 
	//Q2 algorithm
	        if (Q2 != null) {
	            do {
	                done = true;
	                int shortestJobIndex = -1;
	                int shortestJobBurst = Integer.MAX_VALUE;

	                for (int l = 0; l < Q2Processes; l++) {
	                    timer++;
	                    if (Q2[l] == null)
	                        continue;

	                    if (Q2[l].arrivalTime <= currentTime) {
	                        done = false;

	                        if (Q2[l].arrivalTime == 0) {
	                            shortestJobIndex = l;
	                            break; // Execute process with arrival time zero immediately
	                        } else if (Q2[l].cpuBurst < shortestJobBurst) {
	                            shortestJobIndex = l;
	                            shortestJobBurst = Q2[l].cpuBurst;
	                        }
	                    }
	                }

	                if (shortestJobIndex != -1) {
	                    int index = shortestJobIndex;
	                    if (Q2[index].responseTime == 0)
	                        Q2[index].responseTime = currentTime - Q2[index].arrivalTime;

	                    Q2[index].startTime = currentTime;
	                    schedulingOrder.append(Q2[index].processID).append(" | ");
	                    currentTime += Q2[index].cpuBurst;
	                    Q2[index].terminationTime = currentTime;
	                    Q2[index].turnaroundTime = Q2[index].terminationTime - Q2[index].arrivalTime;
	                    totalTurnaroundTime += Q2[index].turnaroundTime;
	                    Q2[index].waitingTime = Q2[index].turnaroundTime - Q2[index].cpuBurst;
	                    totalWaitingTime += Q2[index].waitingTime;
	                    totalResponseTime += Q2[index].responseTime;
	                    processData.append("\n").append(Q2[index].toString()).append("\n");

	                    Q2[index] = null; // Mark the process as completed
	                }
	            } while (!done);
	        }


	//console output
	System.out.print("Scheduling Order: " + schedulingOrder.toString() + "\n");
	System.out.print("Process Data: " + processData.toString() + "\n");
	//file output
	writer.write("Scheduling Order: " + schedulingOrder.toString() + "\n");
	writer.close();



	  

	  }catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	}
