package com.romnan.githubuser.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.romnan.githubuser.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {
    private final String TAG = MainViewModel.class.getSimpleName();
    private OnErrorReceivingDataListener onErrorReceivingDataListener;
    private MutableLiveData<ArrayList<User>> usersList = new MutableLiveData<>();
    private AsyncHttpClient client = new AsyncHttpClient();

    private static final int ERROR_NO_MATCH = 69;
    private static final int ERROR_UNKNOWN_EXCEPTION = 70;
    private static final int ERROR_NO_DATA = 71;

    public void searchUser(final String query) {
        final ArrayList<User> listItems = new ArrayList<>();

        //Creating HTTP Client instance
        client.addHeader("Authorization",
                "token ed903ca68d3ef4efdd21d3a502d83fac0836e3c3");
        client.addHeader("User-Agent", "request");

        //Search user
        String urlSearch = String.format("https://api.github.com/search/users?q=%s", query);
        client.get(urlSearch, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    //Parsing JSON
                    String response = new String(responseBody);
                    JSONObject responseObject = new JSONObject(response);
                    if (responseObject.getInt("total_count") == 0) {
                        onErrorReceivingDataListener.onErrorReceivingData(ERROR_NO_MATCH);
                    }
                    JSONArray items = responseObject.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        User user = new User();

                        String username = item.getString("login");
                        user.setAvatar(item.getString("avatar_url"));
                        user.setUsername(username);
                        setUserDetails(user, username, listItems);
                        listItems.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "exception " + e.getMessage());
                    onErrorReceivingDataListener.onErrorReceivingData(ERROR_UNKNOWN_EXCEPTION);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "onFailure: " + error.getMessage());
                onErrorReceivingDataListener.onErrorReceivingData(statusCode);
            }
        });
    }

    public void setFollowList(final String username, final String tabType) {
        final ArrayList<User> listItems = new ArrayList<>();

        //Creating HTTP Client instance
        client.addHeader("Authorization",
                "token ed903ca68d3ef4efdd21d3a502d83fac0836e3c3");
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
                        onErrorReceivingDataListener.onErrorReceivingData(ERROR_NO_DATA);
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
                    onErrorReceivingDataListener.onErrorReceivingData(ERROR_UNKNOWN_EXCEPTION);
                    Log.e(TAG, "exception " + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "onFailure: " + error.getMessage());
                onErrorReceivingDataListener.onErrorReceivingData(statusCode);
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

    public void clearData() {
        usersList.postValue(new ArrayList<User>());
    }

    public interface OnErrorReceivingDataListener {
        void onErrorReceivingData(int errorCode);
    }
}