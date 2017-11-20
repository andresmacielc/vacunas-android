package com.vacunas.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vacunas.R;
import com.vacunas.adapter.HijosAdapter;
import com.vacunas.rest.ApiBuilder;
import com.vacunas.rest.model.Hijo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mrcpe on 14/11/2017.
 */

public class HijosActivity  extends AppCompatActivity
        implements Callback<List<Hijo>> , ActionMode.Callback {

    public static final String ID_PADRE = "id_padre";
    private static final String TAG = HijosActivity.class.getSimpleName();

    private HijosAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar mProgressView;
    private List<Hijo> listaHijos;
    private List<Hijo> listaHijosFiltrados;
    private String idPadre;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hijos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Lista de hijos");
        toolbar.setNavigationIcon(R.drawable.ic_power);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarCierre();
                //cerrarSesion();
                //finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.lista_hijos);
        mProgressView = (ProgressBar) findViewById(R.id.progressbar);
        //showProgress(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new HijosAdapter(this);
        recyclerView.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idPadre = extras.getString(ID_PADRE);
        }

        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new HijosFragment())
                .commit();*/
        ApiBuilder.build().getHijos(idPadre).enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Hijo>>  call, Response<List<Hijo>> response) {
        if (response.isSuccessful()) {
            listaHijos = new ArrayList<>();
            for (Hijo hijo : response.body()) {
                listaHijos.add(hijo);
                adapter.setValues(listaHijos);
            }
        }
        //showProgress(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filtro_hijos, menu);
        SearchView searchView = (SearchView) menu.getItem(0).getActionView();
        final SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (listaHijos != null) {
                    if (s != null && s.length() > 2) {
                        listaHijosFiltrados = new ArrayList<>();
                        for (Hijo hijo : listaHijos) {
                            Hijo datosHijo = hijo;
                            if (datosHijo != null) {
                                String nombre = datosHijo.getNombres();
                                String apellido = datosHijo.getApellidos();
                                if ( (nombre != null && nombre.toLowerCase().contains(s.toLowerCase()))
                                        || (apellido != null && apellido.toLowerCase().contains(s.toLowerCase())) ) {
                                    listaHijosFiltrados.add(hijo);
                                }
                            }
                        }
                        adapter.setValues(listaHijosFiltrados);
                    } else {
                        adapter.setValues(listaHijos);
                    }
                }
                return false;
            }
        };
        searchView.setOnQueryTextListener(listener);

        return true;
    }

    @Override
    public void onFailure(Call<List<Hijo>>  call, Throwable t) {
        Log.e(TAG, "Error al obtener leyes", t);
        showProgress(false);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrar:
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        //adapter.setValues(listaLeyes);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public static class Dialogo extends DialogFragment {

        private Activity context;

        public Dialogo() {
        }

        public void setContext(Activity context) {
            this.context = context;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    context.finish();
                }
            };
            DialogInterface.OnClickListener rechazarListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(getResources().getString(R.string.salir_app)).setTitle("Confirmar");
            builder.setPositiveButton(getResources().getString(R.string.btn_aceptar), aceptarListener);
            builder.setNegativeButton(getResources().getString(R.string.btn_rechazar), rechazarListener);
            AlertDialog dialog = builder.create();
            return dialog;
        }

    }


    public static class DialogoSingOut extends DialogFragment {

        private Activity context;

        public DialogoSingOut() {
        }

        public void setContext(Activity context) {
            this.context = context;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    new HijosActivity().cerrarSesion(context);
                    Intent i = new Intent(context, LoginActivity.class);
                    startActivity(i);
                    context.finish();
                }
            };
            DialogInterface.OnClickListener rechazarListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(getResources().getString(R.string.cerrar_sesion)).setTitle("Confirmar");
            builder.setPositiveButton(getResources().getString(R.string.btn_aceptar), aceptarListener);
            builder.setNegativeButton(getResources().getString(R.string.btn_rechazar), rechazarListener);
            AlertDialog dialog = builder.create();
            return dialog;
        }

    }
    @Override
    public void onBackPressed() {
        Dialogo dialog = new Dialogo();
        dialog.setContext(this);
        dialog.show(getSupportFragmentManager(), "");
    }

    private void confirmarCierre() {
        DialogoSingOut dialog = new DialogoSingOut();
        dialog.setContext(this);
        dialog.show(getSupportFragmentManager(), "");
    }

    private void cerrarSesion(Activity context) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

}
