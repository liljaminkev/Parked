package com.parked.parked;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;

public class NavActivity extends SingleFragmentActivity {

    private static final int REQUEST_ERROR = 0;

    @Override
    protected Fragment createFragment() {
        return NavFragment.newInstance();
    }

    @Override
    protected void onResume(){
        super.onResume();

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if(errorCode != ConnectionResult.SUCCESS){
            Dialog errorDialog = apiAvailability.getErrorDialog(this, errorCode, REQUEST_ERROR,
                    new DialogInterface.OnCancelListener(){

                        @Override
                        public void onCancel(DialogInterface dialog){
                            finish();
                        }

                    });

            errorDialog.show();
        }

    }

}
