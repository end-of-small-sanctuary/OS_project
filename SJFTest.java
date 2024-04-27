import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;
import java.text.DecimalFormat;

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
	            if (priority == 1)
	                Q1[i] = process;

	            else if(priority == 2)
	                Q2[i] = process;

	}
	    }


	    private static void reportDetailedInfo(PCB[] Q1, PCB[] Q2, int totalProcesses) {
			try {
				PrintWriter writer = new PrintWriter("Report.txt");
		
				StringBuilder schedulingOrder = new StringBuilder();
				StringBuilder processData = new StringBuilder();
		
				int currentTime = 0;
				int totalTurnaroundTime = 0;
				int totalWaitingTime = 0;
				int totalResponseTime = 0;
				boolean done;
		
				// Initialize currentTime to the earliest arrival time if no processes start at zero
				if (currentTime == 0) {
					int earliestArrival = Integer.MAX_VALUE;
					for (PCB pcb : Q1) {
						if (pcb != null && pcb.arrivalTime < earliestArrival) {
							earliestArrival = pcb.arrivalTime;
						}
					}
					for (PCB pcb : Q2) {
						if (pcb != null && pcb.arrivalTime < earliestArrival) {
							earliestArrival = pcb.arrivalTime;
						}
					}
					currentTime = earliestArrival;
				}
		
				do {
					done = true;
					int SJIndex = -1;
					int SJBurst = Integer.MAX_VALUE;
		
					for (int j = 0; j < totalProcesses; j++) {
						// Process Q1 based on Round Robin scheduling
						if (Q1[j] != null && Q1[j].cpuBurst > 0 && Q1[j].arrivalTime <= currentTime) {
							done = false;
							if (Q1[j].startTime == -1) Q1[j].startTime = currentTime; // Set start time for unprocessed process
							schedulingOrder.append(Q1[j].processID).append(" | ");
		
							int executionTime = Math.min(Q1[j].currentBurst, QUANTUM);
							currentTime += executionTime;
							Q1[j].currentBurst -= executionTime;
		
							if (Q1[j].currentBurst == 0) {
								Q1[j].terminationTime = currentTime;
								processData.append(Q1[j].toString()).append("\n");
								totalTurnaroundTime += Q1[j].turnaroundTime;
								totalWaitingTime += Q1[j].waitingTime;
								totalResponseTime += Q1[j].responseTime;
								Q1[j] = null;  // Process is completed
							}
						}
		
						// Process Q2 based on SJF scheduling
						if (Q2[j] != null && Q2[j].cpuBurst > 0 && Q2[j].arrivalTime <= currentTime && done!=false)) {
							done = false;
							if (Q2[j].cpuBurst < SJBurst) {
								SJIndex = j;
								SJBurst = Q2[j].cpuBurst;
							}
						}
					}
		
					if (SJIndex != -1) {
						Q2[SJIndex].startTime = currentTime;
						schedulingOrder.append(Q2[SJIndex].processID).append(" | ");
						currentTime += Q2[SJIndex].cpuBurst;
						Q2[SJIndex].terminationTime = currentTime;
						processData.append(Q2[SJIndex].toString()).append("\n");
						totalTurnaroundTime += Q2[SJIndex].turnaroundTime;
						totalWaitingTime += Q2[SJIndex].waitingTime;
						totalResponseTime += Q2[SJIndex].responseTime;
						Q2[SJIndex] = null;  // Process is completed
					}
				} while (!done);
		
				DecimalFormat df = new DecimalFormat("#0.00");
		
				System.out.println("Scheduling Order: " + schedulingOrder);
				System.out.println("Process Data: "+"\n" + processData);
				System.out.println("Average Turnaround Time: " + df.format((double) totalTurnaroundTime / totalProcesses));
				System.out.println("Average Waiting Time: " + df.format((double) totalWaitingTime / totalProcesses));
				System.out.println("Average Response Time: " + df.format((double) totalResponseTime / totalProcesses));
		
				writer.println("Scheduling Order: " + schedulingOrder);
				writer.println("Process Data: " + processData);
				writer.printf("Average Turnaround Time: %s\n", df.format((double) totalTurnaroundTime / totalProcesses));
				writer.printf("Average Waiting Time: %s\n", df.format((double) totalWaitingTime / totalProcesses));
				writer.printf("Average Response Time: %s\n", df.format((double) totalResponseTime / totalProcesses));
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
