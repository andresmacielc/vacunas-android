package com.vacunas.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
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
    private ProgressBar mProgressView;
    private LinearLayout contentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signOut();

                showProgress(true);
                signOut();
                googleSignIn();
            }
        });

        contentLogin = (LinearLayout) findViewById(R.id.content);
        mProgressView = (ProgressBar) findViewById(R.id.progressbar_login);
        GoogleSignInAccount loggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(loggedAccount != null){
            ApiBuilder.build().getUsuario(loggedAccount.getEmail()).enqueue(LoginActivity.this);
        }else{
            showProgress(false);
        }
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
            ApiBuilder.build().getUsuario(account.getEmail()).enqueue(LoginActivity.this);
        } catch (ApiException e) {
            showProgress(false);
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void onResponse(Call<Respuesta<Usuario>> call, Response<Respuesta<Usuario>> response) {
        if (response.isSuccessful()) {
            if (response.body().respuesta != null ) {
                Usuario usuario = response.body().respuesta;
                String idUsuario = usuario.getIdUsuario().toString();
                String nroDocumento = usuario.getDocumento();
                String nombre = usuario.getNombres() + " " + usuario.getApellidos();
                Toast.makeText(LoginActivity.this, "Bienvenido "+ nombre, Toast.LENGTH_SHORT).show();
                String correo = usuario.getCorreoElectronico();

                Intent i = new Intent(getBaseContext(), HijosActivity.class);
                i.putExtra(HijosActivity.ID_PADRE, idUsuario);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        } else {
            Toast.makeText(LoginActivity.this, "Error al consultar datos del usuario", Toast.LENGTH_SHORT).show();
            showProgress(false);
        }
    }

    @Override
    public void onFailure(Call<Respuesta<Usuario>> call, Throwable t) {
        Toast.makeText(LoginActivity.this, "Error al consultar datos del usuario", Toast.LENGTH_SHORT).show();

    }
    public void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        contentLogin.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
}
