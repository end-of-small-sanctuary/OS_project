public class PCB implements Comparable<PCB> {
    String processID;
    int priority;
    int arrivalTime=0;
    int cpuBurst;
    int startTime;
    int terminationTime;
    int turnaroundTime;
    int waitingTime;
    int responseTime;

    // Constructor
    public PCB(String processID, int priority, int arrivalTime, int cpuBurst) {
        this.processID = processID;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.cpuBurst = cpuBurst;
        this.startTime = 0;
        this.terminationTime = 0;
        this.turnaroundTime = 0;
        this.waitingTime = 0;
        this.responseTime = 0;
    }
    public void printData(){
    System.out.print("\n"+this.processID+"\n"+this.priority);
    }
    public int getArrivalTime(){
        return arrivalTime;
    }

    @Override
    public int compareTo(PCB o){
    
    int value1 = this.getArrivalTime() - o.getArrivalTime();
        if (value1 == 0) 
            return this.cpuBurst - o.cpuBurst;
           return value1;
    }
    }

