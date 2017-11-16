package com.vacunas.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vacunas.R;
import com.vacunas.activity.LoginActivity;
import com.vacunas.rest.model.Hijo;
import com.vacunas.rest.model.Vacuna;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrcpe on 15/11/2017.
 */

public class HijosDetallesAdapter  extends RecyclerView.Adapter<HijosDetallesAdapter.ViewHolder>   {

    private final List<Vacuna> mValues = new ArrayList<>();
    private final Activity mContext;
    private String sexo;
    private String fecha;

    public HijosDetallesAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    public void setValues(List<Vacuna> values) {
        mValues.clear();
        mValues.addAll(values);
        notifyDataSetChanged();
    }

    @Override
    public HijosDetallesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_hijo, parent, false);
        return new HijosDetallesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HijosDetallesAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (holder.mItem != null) {

            holder.mNombreApellido.setText(holder.mItem.getNombreVacuna());
            /*if(holder.mItem.getSexo().equals("M")){
                sexo = "MASCULINO";
            }else{
                sexo = "FEMENINO";
            }*/
            holder.mSexo.setText(holder.mItem.getNombreVacuna());
            /*holder.mEdad.setText(holder.mItem.getEdad().toString() + " años");
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            fecha = df.format(holder.mItem.getFechaNacimiento());
            holder.mFechaNacimiento.setText(fecha);*/
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(i);

            }
        };
        holder.datosHijos.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final LinearLayout datosHijos;
        public final TextView mNombreApellido;
        public final TextView mSexo;
        public final TextView mEdad;
        public final TextView mFechaNacimiento;
        public Vacuna mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            datosHijos = (LinearLayout) view.findViewById(R.id.datos_detalles_hijo);
            mNombreApellido = (TextView) view.findViewById(R.id.txtNombre);
            mSexo = (TextView) view.findViewById(R.id.txtAsiento);
            mEdad = (TextView) view.findViewById(R.id.edad);
            mFechaNacimiento = (TextView) view.findViewById(R.id.fecha_nacimiento);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombreApellido.getText() + "'";
        }
    }
}
