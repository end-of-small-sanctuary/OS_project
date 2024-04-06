import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

//both this and the previous draft skip over the second Queue when run, i've yet to try solving this issue
//it will correctly run Q1 though

public class SchedulerTest {
//initialization
    private static final int QUANTUM = 3; 
    private static PCB[] Q1; 
    private static PCB[] Q2; 
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
                    if (Q1 != null && Q2 != null) {
                        reportDetailedInfo(Q1,Q1,totalProcesses);
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
            else
                Q2[i]  = process;
        }
    }


    //report detailed process method
        private static void reportDetailedInfo(PCB[] Q1, PCB[] Q2, int totalProcesses) {
        try {
        PrintWriter writer = new PrintWriter("Report1.txt");

        
        StringBuilder schedulingOrder = new StringBuilder();
        StringBuilder processData = new StringBuilder();
        int currentTime = 0;
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;
        int totalResponseTime = 0;
        boolean done = true;
        int rem_bt[] = new int[totalProcesses];
        for (int i = 0; i < Q1.length; i++)
         
//start of algorithm method
while (true) {
      done = true;
      
      
//Q1 algorithm
for (int l = 0; i < Q1.length; i++) {
    if (Q1[i] == null)
        break;
    if (Q1[i] .cpuBurst > 0 ) {
    //added here as to append the cpuBurst before any change, might move to the end of this method if i add a temp burst value
    processData.append(Q1[i].processID).append("\n").append(Q1[i].priority).append("\n").append(Q1[i].arrivalTime).append("\n").append(Q1[i].cpuBurst); 
        done= false;
        if (Q1[i] .responseTime == 0)
            Q1[i] .responseTime = currentTime + Q1[i].arrivalTime;
        Q1[i].waitingTime += currentTime - Q1[i].startTime;
        Q1[i].startTime = currentTime;
        schedulingOrder.append(Q1[i].processID).append(" | ");
        if (Q1[i].cpuBurst > QUANTUM) {
            currentTime += QUANTUM;
            Q1[i].cpuBurst -= QUANTUM;//removes the burst time
        } else {
            currentTime += Q1[i].cpuBurst;
            Q1[i].terminationTime = currentTime;
            Q1[i].turnaroundTime = Q1[i].terminationTime - Q1[i].arrivalTime;
            totalTurnaroundTime += Q1[i].turnaroundTime;
            Q1[i].waitingTime -= Q1[i].turnaroundTime; 
            totalWaitingTime += Q1[i].waitingTime;
            totalResponseTime += Q1[i].responseTime;
            Q1[i] = null;
       }
       } 
    }
     if (done == true)
              break;
}


//Q2 algorithm

for (int l = 0; l < Q2.length; l++) {
        if (Q2[l] == null)
            break;
        if (Q2[l] .arrivalTime <= currentTime) {
        //same with the previous algorithm
        processData.append(Q2[l] .processID).append("\n").append(Q2[l] .priority).append("\n").append(Q2[l] .arrivalTime).append("\n").append(Q2[l] .cpuBurst);
            done = false;
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
            Q2[l]  = null;
        }
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
