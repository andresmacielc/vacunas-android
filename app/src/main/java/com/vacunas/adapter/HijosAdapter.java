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
import com.vacunas.activity.HijosActivity;
import com.vacunas.activity.HijosDetallesActivity;
import com.vacunas.activity.LoginActivity;
import com.vacunas.rest.model.Hijo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrcpe on 14/11/2017.
 */

public class HijosAdapter  extends RecyclerView.Adapter<HijosAdapter.ViewHolder>  {

    private final List<Hijo> mValues = new ArrayList<>();
    private final Activity mContext;
    private String sexo;
    private String fecha;

    public HijosAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    public void setValues(List<Hijo> values) {
        mValues.clear();
        mValues.addAll(values);
        notifyDataSetChanged();
    }

    @Override
    public HijosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hijo, parent, false);
        return new HijosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HijosAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (holder.mItem != null) {

            holder.mNombreApellido.setText(holder.mItem.getNombres() + " "+holder.mItem.getApellidos());
            if(holder.mItem.getSexo().equals("M")){
                sexo = "MASCULINO";
            }else{
                sexo = "FEMENINO";
            }
            holder.mSexo.setText(sexo);
            holder.mEdad.setText(holder.mItem.getEdad().toString() + " años");
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            fecha = df.format(holder.mItem.getFechaNacimiento());
            holder.mFechaNacimiento.setText(fecha);
            //holder.idHijo =
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, HijosDetallesActivity.class);
                i.putExtra(HijosDetallesActivity.ID_HIJO, holder.mItem.getIdHijo().toString());
                i.putExtra(HijosDetallesActivity.NOMBRE_HIJO, holder.mItem.getNombres() + " "+holder.mItem.getApellidos());
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
        public Hijo mItem;
        //public String idHijo;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            datosHijos = (LinearLayout) view.findViewById(R.id.datos_hijo);
            mNombreApellido = (TextView) view.findViewById(R.id.nombre_apellido);
            mSexo = (TextView) view.findViewById(R.id.sexo);
            mEdad = (TextView) view.findViewById(R.id.edad);
            mFechaNacimiento = (TextView) view.findViewById(R.id.fecha_nacimiento);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombreApellido.getText() + "'";
        }
    }
}
