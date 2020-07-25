package com.romnan.consumerapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.romnan.consumerapp.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {
    private final String TAG = MainViewModel.class.getSimpleName();
    private static final String TOKEN = "token 8befcd295d25e40a2350c035d5001b5b4b50ff1e";
    private OnErrorReceivingDataListener onErrorReceivingDataListener;
    private MutableLiveData<ArrayList<User>> usersList = new MutableLiveData<>();
    private AsyncHttpClient client = new AsyncHttpClient();

    public void setFollowList(final String username, final String tabType) {
        final ArrayList<User> listItems = new ArrayList<>();

        //Creating HTTP Client instance
        client.addHeader("Authorization", TOKEN);
        client.addHeader("User-Agent", "request");

        //Search user
        String url = String.format("https://api.github.com/users/%s/%s", username, tabType);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    //Parsing JSON
                    String response = new String(responseBody);
                    JSONArray responseArray = new JSONArray(response);
                    if (responseArray.length() == 0) {
                        onErrorReceivingDataListener.onErrorReceivingData("Not found");
                    }

                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject item = responseArray.getJSONObject(i);
                        User user = new User();

                        String username = item.getString("login");
                        user.setAvatar(item.getString("avatar_url"));
                        user.setUsername(username);
                        setUserDetails(user, username, listItems);
                        listItems.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onErrorReceivingDataListener.onErrorReceivingData(e.getMessage());
                    Log.e(TAG, "exception " + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "onFailure: " + error.getMessage());
                onErrorReceivingDataListener.onErrorReceivingData(error.getMessage());
            }
        });
    }

    public void setUserDetails(final User user, final String username, final ArrayList<User> listItems) {
        //Set user details
        String urlDetails = "https://api.github.com/users/" + username;
        client.get(urlDetails, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    //Parsing JSON
                    String response = new String(responseBody);
                    JSONObject responseObject = new JSONObject(response);

                    user.setId(responseObject.getInt("id"));
                    String name = responseObject.getString("name");
                    if (!name.equals("null")) {
                        user.setName(name);
                    } else {
                        user.setName("user-" + responseObject.getInt("id"));
                    }

                    String location = responseObject.getString("location");
                    if (!location.equals("null")) {
                        user.setLocation(location);
                    } else {
                        user.setLocation("Unknown");
                    }

                    user.setCompany(responseObject.getString("company"));
                    user.setRepositories(responseObject.getInt("public_repos"));
                    user.setFollowing(responseObject.getInt("following"));
                    user.setFollowers(responseObject.getInt("followers"));
                    usersList.postValue(listItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "exception " + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "onFailure: " + error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<User>> getUser() {
        return usersList;
    }

    public void setOnErrorReceivingDataListener(OnErrorReceivingDataListener onErrorReceivingDataListener) {
        this.onErrorReceivingDataListener = onErrorReceivingDataListener;
    }

    public interface OnErrorReceivingDataListener {
        void onErrorReceivingData(String errorMessage);
    }
}