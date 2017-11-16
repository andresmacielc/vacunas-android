package com.vacunas.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.vacunas.adapter.HijosDetallesAdapter;
import com.vacunas.rest.ApiBuilder;
import com.vacunas.rest.model.Hijo;
import com.vacunas.rest.model.Vacuna;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HijosDetallesActivity extends AppCompatActivity
        implements Callback<List<Vacuna>>, ActionMode.Callback {


    public static final String ID_HIJO = "id_hijo";
    public static final String NOMBRE_HIJO= "nombre_hijo";
    private static final String TAG = HijosActivity.class.getSimpleName();

    private HijosDetallesAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar mProgressView;
    private List<Vacuna> listaVacunas;
    private List<Vacuna> listaVacunasFiltrados;
    private String idHijo;
    private String nombreHijo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hijos_detalles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDet);
        setSupportActionBar(toolbar);
        setTitle("Vacunas del hijo");
        toolbar.setNavigationIcon(R.mipmap.ic_atras);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.lista_detalles_hijos);
        mProgressView = (ProgressBar) findViewById(R.id.progressbar);
        //showProgress(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new HijosDetallesAdapter(this);
        recyclerView.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idHijo = extras.getString(ID_HIJO);
            nombreHijo = extras.getString(NOMBRE_HIJO);
            setTitle("Vacunas de "+nombreHijo);
        }

        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new HijosFragment())
                .commit();*/
        ApiBuilder.build().getVacunasHijos(idHijo).enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Vacuna>> call, Response<List<Vacuna>> response) {
        if (response.isSuccessful()) {
            listaVacunas = new ArrayList<>();
            for (Vacuna vacuna : response.body()) {
                listaVacunas.add(vacuna);
                adapter.setValues(listaVacunas);
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
                if (listaVacunas != null) {
                    if (s != null && s.length() > 2) {
                        listaVacunasFiltrados = new ArrayList<>();
                        for (Vacuna vacuna : listaVacunas) {
                            Vacuna datosVacuna = vacuna;
                            if (datosVacuna != null) {
                                String nombre = datosVacuna.getNombreVacuna();
                                if ( (nombre != null && nombre.toLowerCase().contains(s.toLowerCase()))) {
                                    listaVacunasFiltrados.add(vacuna);
                                }
                            }
                        }
                        adapter.setValues(listaVacunasFiltrados);
                    } else {
                        adapter.setValues(listaVacunas);
                    }
                }
                return false;
            }
        };
        searchView.setOnQueryTextListener(listener);

        return true;
    }

    @Override
    public void onFailure(Call<List<Vacuna>>  call, Throwable t) {
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
