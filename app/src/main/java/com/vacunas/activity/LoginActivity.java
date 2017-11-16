package com.vacunas.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.vacunas.R;
import com.vacunas.rest.ApiBuilder;
import com.vacunas.rest.model.Respuesta;
import com.vacunas.rest.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity implements Callback<Respuesta<Usuario>>  {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.

        
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signOut();
                googleSignIn();
            }
        });
    }
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.


            updateUI("SUCCESS" , account );

            ApiBuilder.build().getUsuario(account.getEmail()).enqueue(LoginActivity.this);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI("FAILED" , null);
        }
    }

    private void updateUI(String o, GoogleSignInAccount account) {
        Toast.makeText(LoginActivity.this, "Login " + o + ", cuenta: "+account.getDisplayName() +", correo: "+ account.getEmail(), Toast.LENGTH_SHORT).show();
        //Log.i(TAG, "Login " + o + ", cuenta: "+account.getDisplayName() +", correo: "+ account.getEmail());
    }

    @Override
    public void onResponse(Call<Respuesta<Usuario>> call, Response<Respuesta<Usuario>> response) {
        if (response.isSuccessful()) {
            if (response.body().respuesta != null ) {
                // Cliente registrado
                //showProgress(false);
                Usuario usuario = response.body().respuesta;
                String idUsuario = usuario.getIdUsuario().toString();
                String nroDocumento = usuario.getDocumento();
                String nombre = usuario.getNombres() + " " + usuario.getApellidos();
                String correo = usuario.getCorreoElectronico();

                Intent i = new Intent(getBaseContext(), HijosActivity.class);
                i.putExtra(HijosActivity.ID_PADRE, idUsuario);
                startActivity(i);
                finish();
                //String telefono = usuario.get();
                /*if (isGoogleLogin) {
                    preferenceUtils.loginGoogle(userId, nroDocumento, nombre, correo, telefono, photoUrl);
                } else {
                    preferenceUtils.loginFacebook(userId, nroDocumento, nombre, correo, telefono, photoUrl);
                }
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                finish();*/
            } else {
                Toast.makeText(LoginActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Error al consultar datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<Respuesta<Usuario>> call, Throwable t) {
        Toast.makeText(LoginActivity.this, "Error al consultar datos del usuario", Toast.LENGTH_SHORT).show();

    }
}
