package com.example.loginconfoto.Registro;

import static android.Manifest.permission_group.CAMERA;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.loginconfoto.databinding.ActivityRegistroBinding;
import com.example.loginconfoto.models.Usuario;

public class RegistroActivity extends AppCompatActivity {
    private ActivityRegistroBinding binding;
    private RegistroViewModel vmRegistro;
    private static int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vmRegistro.tomarFoto(requestCode, resultCode, data, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        vmRegistro = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RegistroViewModel.class);

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = binding.etDni.getText().toString();
                String nombre = binding.etNombre.getText().toString();
                String apellido = binding.etApellido.getText().toString();
                String email = binding.etEmailR.getText().toString();
                String password = binding.etPasswordR.getText().toString();
                BitmapDrawable drawable = (BitmapDrawable) binding.ivFoto.getDrawable();
                Bitmap foto = drawable.getBitmap();
                vmRegistro.guardarUsuario(dni, nombre, apellido, email, password);
            }
        });
        binding.btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intentFoto.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intentFoto, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        vmRegistro.getMUsuario().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.etDni.setText(usuario.getDni().toString());
                binding.etNombre.setText(usuario.getNombre());
                binding.etApellido.setText(usuario.getApellido());
                binding.etEmailR.setText(usuario.getEmail());
                binding.etPasswordR.setText(usuario.getPassword());
                binding.ivFoto.setImageBitmap(BitmapFactory.decodeByteArray(usuario.getFoto(),0,usuario.getFoto().length));
            }
        });
        vmRegistro.getMFoto().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                binding.ivFoto.setImageBitmap(bitmap);
            }
        });
        if(getIntent().getFlags() == Intent.FLAG_ACTIVITY_NEW_TASK)
        {
            vmRegistro.recuperarUsuario();
        }
    }

}