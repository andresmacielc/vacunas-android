package com.vacunas.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vacunas.R;

/**
 * Created by mrcpe on 14/11/2017.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);



        /*Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(2);
        item.setChecked(true);*/
    }

    /*@Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new HijosFragment())
                .commit();


        //navigationView.setCheckedItem(R.id.home);
    }*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Dialogo dialog = new Dialogo();
            dialog.setContext(this);
            dialog.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        // Cerrar el Menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        // Marcar el item del menu en el que se encuentra actualmente
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        fragment.onActivityResult(requestCode, resultCode, data);
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
/*
    public boolean estaLogueado() {
        return preferenceUtils.isLoggedFacebookIn() || preferenceUtils.isLoggedGoogleIn();
    }*/
}
