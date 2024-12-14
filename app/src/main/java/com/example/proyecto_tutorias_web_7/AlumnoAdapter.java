package com.example.proyecto_tutorias_web_7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.proyecto_tutorias_web_7.modelo.Alumno;

public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder> {

    private List<Alumno> listaAlumnos;

    public AlumnoAdapter(List<Alumno> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }

    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alumno, parent, false);
        return new AlumnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoViewHolder holder, int position) {
        Alumno alumno = listaAlumnos.get(position);

        holder.tvNumControl.setText("Número de Control: " + alumno.getNumControl());
        holder.tvNombre.setText("Nombre: " + alumno.getNombre());
        holder.tvApellidoP.setText("Apellido Paterno: " + alumno.getApellidoP());
        holder.tvApellidoM.setText("Apellido Materno: " + alumno.getApellidoM());
        holder.tvFechaNacimiento.setText("Fecha de Nacimiento: " + alumno.getFechaNacimiento());
        holder.tvTelefono.setText("Teléfono: " + alumno.getTelefono());
        holder.tvEmail.setText("Email: " + alumno.getEmail());
        holder.tvCarrera.setText("Carrera: " + alumno.getCarreraId());

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setMessage("¿Estás seguro de que deseas eliminar este alumno?")
                    .setCancelable(false)
                    .setPositiveButton("Sí", (dialog, id) -> {
                        eliminarAlumno(position);
                        Toast.makeText(v.getContext(), "Alumno eliminado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }

    public void updateData(List<Alumno> nuevosAlumnos) {
        this.listaAlumnos = nuevosAlumnos;
        notifyDataSetChanged();
    }

    private void eliminarAlumno(int position) {
        listaAlumnos.remove(position);
        notifyItemRemoved(position);
    }

    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumControl, tvNombre, tvApellidoP, tvApellidoM, tvFechaNacimiento, tvTelefono, tvEmail, tvCarrera;

        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumControl = itemView.findViewById(R.id.tv_num_control);
            tvNombre = itemView.findViewById(R.id.tv_nombre);
            tvApellidoP = itemView.findViewById(R.id.tv_apellido_paterno);
            tvApellidoM = itemView.findViewById(R.id.tv_apellido_materno);
            tvFechaNacimiento = itemView.findViewById(R.id.tv_fecha_nacimiento);
            tvTelefono = itemView.findViewById(R.id.tv_telefono);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvCarrera = itemView.findViewById(R.id.tv_carrera);
        }
    }
}
