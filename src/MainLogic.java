public  interface MainLogic {
    public void StartTheWork();

    public void addBSC();

    public void stopBSC();

    public String addNewQueues(String message, int FR) throws InterruptedException;

    public int getCounter();

    public void delete_BTS();

    public int CheckHowManyBTSS();

    public int CheckHowManyBTSR();

    public void ClearTheQueueOf();
}
