package com.example.fitclothesco.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fitclothesco.R;
import com.example.fitclothesco.model.Venta;

import java.util.ArrayList;

public class ListViewVentasAdapter extends BaseAdapter {

    Context context;
    ArrayList<Venta> ventaData;
    LayoutInflater layoutInflater;
    Venta ventaModel;

    public ListViewVentasAdapter(Context context, ArrayList<Venta> ventaData) {
        this.context = context;
        this.ventaData = ventaData;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return ventaData.size();
    }

    @Override
    public Object getItem(int position) {
        return ventaData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null){
            rowView = layoutInflater.inflate(R.layout.lista_ventas,
                    null,
                    true);
        }
        //enlazar vistas
        TextView nombres = rowView.findViewById(R.id.nombres);
        TextView prendas = rowView.findViewById(R.id.prendas);
        TextView precio = rowView.findViewById(R.id.precio);
        TextView fecha = rowView.findViewById(R.id.fecha);

        ventaModel = ventaData.get(position);
        nombres.setText(ventaModel.getNombreCliente());
        prendas.setText(ventaModel.getPrenda());
        precio.setText(ventaModel.getTotalVenta());
        fecha.setText(ventaModel.getFecha());

        return rowView;
    }
}
