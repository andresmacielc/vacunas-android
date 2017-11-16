package com.vacunas.activity;

import android.os.Bundle;
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
                finish();
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
}
