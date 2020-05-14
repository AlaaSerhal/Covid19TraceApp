package lb.com.network.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import lb.com.network.project.Utils.Utils;
import lb.com.network.project.R;
import lb.com.network.project.model.ApiBaseResponse;
import lb.com.network.project.model.User;
import lb.com.network.project.rest.ApiClient;
import lb.com.network.project.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText mobileEditText;
    private static final String PREF_NAME = "MyAppPreferences";
    private static int PRIVATE_MODE = 0;
    public static final String sysIdKey = "sysIdKey";
    public static final String nameKey = "nameKey";
    public static final String mobileKey = "mobileKey";
    public static final String IsFirsTime = "isFirstTimeKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        nameEditText = (EditText)findViewById(R.id.name);
        mobileEditText = (EditText)findViewById(R.id.mobile);
    }

    public void start(View view) {
        String name = nameEditText.getText().toString();
        String mobile = mobileEditText.getText().toString();

        if(name == null  || name.isEmpty()){
            Toast.makeText(this,"Name is required", Toast.LENGTH_SHORT);
            return;
        }

        if(mobile == null  || mobile.isEmpty()){
            Toast.makeText(this,"Mobile is required", Toast.LENGTH_SHORT);
            return;
        }

        SharedPreferences prefs = this.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        String systemID = Utils.generateUidNamespace();


        //ToDo: send data to back-end
        User user = new User();
        user.setUserID(systemID);
        user.setFullName(name);
        user.setMobileNumber(Integer.parseInt(mobile));
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ApiBaseResponse> call = apiService.createUser(user);

        call.enqueue(new Callback<ApiBaseResponse>() {
            @Override
            public void onResponse(Call<ApiBaseResponse> call, Response<ApiBaseResponse> response) {
                //store in shared preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(sysIdKey, systemID);
                editor.putString(nameKey, name);
                editor.putString(mobileKey, mobile);
                editor.putBoolean(IsFirsTime,false);
                editor.commit();
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ApiBaseResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}


