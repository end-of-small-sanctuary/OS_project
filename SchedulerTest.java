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

public class SchedulerTest {
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


	    //report detailed process method
	        private static void reportDetailedInfo(PCB[]Q1, PCB[] Q2,int totalProcesses) {
	        try {
	        PrintWriter writer = new PrintWriter("Report.txt");

	        
	        StringBuilder schedulingOrder = new StringBuilder();
	        StringBuilder processData = new StringBuilder();
           
	        int currentTime = 0;
	        int totalTurnaroundTime = 0;
	        int totalWaitingTime = 0;
	        int totalResponseTime = 0;
	        boolean done = true;
	        
	        
	         
	//start of algorithm method
	        do {
	                done = true;
			int SJIndex = -1;
	        	int SJBurst = Integer.MAX_VALUE;




	            //Q1 algorithm
	            if (Q1 != null||Q2!=null) {


	                for (int j = 0; j < totalProcesses; j++) {
	                    
	                    
	                    if (Q1[j] == null&&Q2[j]==null)
	                        continue;
                     if(Q1[j] !=null) 
	                    if (Q1[j].cpuBurst > 0) {
	                        done = false;
	                        
	                        if (Q1[j].arrivalTime <= currentTime) { //process can execute

                               if(Q1[j].startTime == -1)//never processed
	                            Q1[j].startTime = currentTime;
	                            schedulingOrder.append(Q1[j].processID).append(" | ");

	                            if (Q1[j].currentBurst > QUANTUM) {//recurring processes
	                                currentTime += QUANTUM;
	                                Q1[j].currentBurst -= QUANTUM;
	                            } 
                               else { //last recursion
	                                currentTime += Q1[j].currentBurst;
	                                Q1[j].terminationTime = currentTime;
                                   
	                                processData.append(Q1[j].toString()).append("\n");
                                   totalTurnaroundTime += Q1[j].turnaroundTime;
	                                totalWaitingTime += Q1[j].waitingTime;
	                                totalResponseTime += Q1[j].responseTime;
	                                Q1[j] = null;
	                            }
	                            
	                        }
	                    }
                       
                       
                    //Q2 algorithm
                   if(Q2[j] !=null && done != false) 
                    if (Q2[j].arrivalTime <= currentTime) {//process can execute
	                        done = false;
	                        if (Q2[j].arrivalTime == 0) {
	                            SJIndex = j;
	                            break; //execute process with arrival time zero immediately
	                        } else 
                              if (Q2[j].cpuBurst < SJBurst) {
	                            SJIndex = j;
	                            SJBurst = Q2[j].cpuBurst;
                               } 
	                }

	                
	            }//end of for loop
               
	      if (SJIndex != -1) {
	                    int index = SJIndex;
	                    

	                    Q2[index].startTime = currentTime;
	                    schedulingOrder.append(Q2[index].processID).append(" | ");
	                    currentTime += Q2[index].cpuBurst;
	                    Q2[index].terminationTime = currentTime;
	                    
                       processData.append("\n").append(Q2[index].toString()).append("\n");
                       totalTurnaroundTime += Q2[index].turnaroundTime;
	                    totalWaitingTime += Q2[index].waitingTime;
	                    totalResponseTime += Q2[index].responseTime;

	                    Q2[index] = null; //mark the process as completed
                  }
                    }} while (!done);

	        
	        
					System.out.print("Scheduling Order: " + schedulingOrder.toString() + "\n");
					DecimalFormat df = new DecimalFormat("#0.00");

//calculate averages
double avgTurnaround = (double) totalTurnaroundTime / totalProcesses;
double avgResponse = (double) totalResponseTime / totalProcesses;
double avgWaiting = (double) totalWaitingTime / totalProcesses;



//output to console
System.out.print("Process Data: \n" + processData.toString() + 
    "\n Average Turnaround Time: " + df.format(avgTurnaround) +
    "\n Average Response Time: " + df.format(avgResponse) +
    "\n Average Waiting Time: " + df.format(avgWaiting));

//write to file
writer.write("Scheduling Order: " + schedulingOrder.toString() + "\n");
writer.write("Process Data: \n" + processData.toString() +
    "\n Average Turnaround Time: " + df.format(avgTurnaround) +
    "\n Average Response Time: " + df.format(avgResponse) +
    "\n Average Waiting Time: " + df.format(avgWaiting));

	
	writer.close();

	  

	  }catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	}
