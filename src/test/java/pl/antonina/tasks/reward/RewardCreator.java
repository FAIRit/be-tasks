package pl.antonina.tasks.reward;

class RewardCreator {

    static RewardData createRewardData() {
        RewardData rewardData = new RewardData();
        rewardData.setName("1Zabawka");
        rewardData.setUrl("1Http://...");
        rewardData.setPoints(10);
        return rewardData;
    }

    static RewardData createNewRewardData() {
        RewardData rewardData = new RewardData();
        rewardData.setName("2Grzechotka");
        rewardData.setUrl("2Http://...");
        rewardData.setPoints(20);
        return rewardData;
    }
}