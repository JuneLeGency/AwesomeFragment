package studio.legency.statefragment;

public interface RequestCallBack {

    void networkError();

    void loginNeeded();

    void failedWithError(String error_message);
}
