package geocat.service;

public class QueueInfo {

    private QueueGroupInfo queueGroupInfo;
    private int queueNumber;

    public QueueInfo(QueueGroupInfo queueGroupInfo, int queueNumber) {
        this.queueGroupInfo = queueGroupInfo;
        this.queueNumber = queueNumber;
    }

    public String queueName(){
        return queueGroupInfo.queueName(queueNumber);
    }

    public int parallelism(){
        return queueGroupInfo.getParallelism();
    }


    public QueueGroupInfo getQueueGroupInfo() {
        return queueGroupInfo;
    }

    public void setQueueGroupInfo(QueueGroupInfo queueGroupInfo) {
        this.queueGroupInfo = queueGroupInfo;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }
}
