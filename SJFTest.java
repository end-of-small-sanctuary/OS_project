import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

//both this and the previous draft skip over the second Queue when run, i've yet to try solving this issue
//it will correctly run Q1 though

public class SJFTest {
//initialization
    private static final int QUANTUM = 3; 
    private static int processIndex=0;
    private static PCB[] Q1; 
    public static PCB[] Q2;
    private static int totalProcessesQ1= 0;
    private static int totalProcessesQ2= 0;

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
                     System.out.print("Enter total number of processes for Q1: ");
                    totalProcessesQ1 = scanner.nextInt();
                    System.out.print("Enter total number of processes for Q2: ");
                    totalProcessesQ2 = scanner.nextInt();
                    enterProcessInfo(totalProcessesQ1, totalProcessesQ2, scanner);
                    break;                   
                case 2:
                    if ( Q1!=null || Q2 != null) {
                        reportDetailedInfo(Q1,Q2,totalProcessesQ1,totalProcessesQ2);
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
    private static void enterProcessInfo(int totalProcessesQ1,int totalProcessesQ2, Scanner scanner) {
    
        Q1 = new PCB[totalProcessesQ1];
        Q2 = new PCB[totalProcessesQ2];
       int totalProcesses=0;
         for (int i = 0; i < totalProcessesQ1; i++) {
                 if(totalProcessesQ1==0)
                 break;
                     System.out.println("\nFirst Priority " + (i + 1) + ":");
                     
                     System.out.print("Enter priority (1 or 2): ");
                     int priority = scanner.nextInt();
                     System.out.print("Enter arrival time: ");
                     int arrivalTime = scanner.nextInt();
                     System.out.print("Enter CPU burst time: ");
                     int cpuBurst = scanner.nextInt();
         
                     //creates an object then inserts it into the array in each loop iteration
                     Q1[i] = new PCB("P" + (++totalProcesses), priority, arrivalTime, cpuBurst);
                    
                                         }
         
                                        
        for (int i = 0; i < totalProcessesQ2; i++) {
        if(totalProcessesQ2==0)
        break;
            System.out.println("\nSecond Priority " + (i + 1) + ":");
            System.out.print("Enter priority (1 or 2): ");
            int priority = scanner.nextInt();
            System.out.print("Enter arrival time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Enter CPU burst time: ");
            int cpuBurst = scanner.nextInt();

            //creates an object then inserts it into the array in each loop iteration
            Q2[i] = new PCB("P" + (++totalProcesses), priority, arrivalTime, cpuBurst);
            
}
    }


    //report detailed process method
        private static void reportDetailedInfo(PCB[]Q1, PCB[] Q2,int totalProcessesQ1,int totalProcessesQ2) {
        try {
        PrintWriter writer = new PrintWriter("Report1.txt");

        
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
      
//Q1 algorithm
if (Q1 != null)
for (int j = 0; j < Q1.length; j++) {
    if (Q1[j] == null)
        continue;
    if (Q1[j] .cpuBurst > 0 ) {
     done = false;
    //added here as to append the cpuBurst before any change, might move to the end of this method if i add a temp burst value
    processData.append(Q1[j].processID).append("\n").append(Q1[j].priority).append("\n").append(Q1[j].arrivalTime).append("\n").append(Q1[j].cpuBurst).append("\n"); 
       
        if (Q1[j] .responseTime == 0)
            Q1[j] .responseTime = currentTime + Q1[j].arrivalTime;
        Q1[j].waitingTime += currentTime - Q1[j].startTime;
        Q1[j].startTime = currentTime;
        schedulingOrder.append(Q1[j].processID).append(" | ");
        if (Q1[j].cpuBurst > QUANTUM) {
            currentTime += QUANTUM;
            Q1[j].cpuBurst -= QUANTUM;//removes the burst time
        } else {
            currentTime += Q1[j].cpuBurst;
            Q1[j].terminationTime = currentTime;
            Q1[j].turnaroundTime = Q1[j].terminationTime - Q1[j].arrivalTime;
            totalTurnaroundTime += Q1[j].turnaroundTime;
            Q1[j].waitingTime -= Q1[j].turnaroundTime; 
            totalWaitingTime += Q1[j].waitingTime;
            totalResponseTime += Q1[j].responseTime;
            Q1[j] = null;
       }
       } 
    }
    
   } while (!done);

 
//Q2 algorithm
if (Q2 != null)
 do {
            done = true;
            if(processIndex>=totalProcessesQ2)
            break;
       for (int l = 0; l <Q2.length; l++) {
        
       if(Q2[l] == null )
       break;
       if(l !=0)
       Arrays.sort(Q2);
         
        if (Q2[l].arrivalTime <= currentTime ) {
        processIndex++;
         done = false;
        //same with the previous algorithm
        processData.append(Q2[l] .processID).append("\n").append(Q2[l] .priority).append("\n").append(Q2[l] .arrivalTime).append("\n").append(Q2[l] .cpuBurst);
           
            if (Q2[l] .responseTime == 0)
                Q2[l].responseTime = currentTime - Q2[l] .arrivalTime;
            Q2[l] .startTime = currentTime;
            schedulingOrder.append(Q2[l] .processID).append(" | ");
            currentTime += Q2[l] .cpuBurst;
            Q2[l] .terminationTime = currentTime;
            Q2[l] .turnaroundTime = Q2[l] .terminationTime - Q2[l] .arrivalTime;
            totalTurnaroundTime += Q2[l] .turnaroundTime;
            Q2[l] .waitingTime = Q2[l] .turnaroundTime - Q2[l] .cpuBurst;
            totalWaitingTime += Q2[l] .waitingTime;
            totalResponseTime += Q2[l] .responseTime;
            
        }
    }
    
 } while (!done);


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
