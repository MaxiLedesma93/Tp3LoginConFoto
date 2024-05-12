package com.example.loginconfoto.Registro;


import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.loginconfoto.models.Usuario;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegistroViewModel  extends AndroidViewModel {
    private MutableLiveData<Usuario> mUsuario;
    private MutableLiveData<Bitmap> mFoto;
    private byte[] bytesFoto;

    public RegistroViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Usuario> getMUsuario() {
        if (mUsuario == null) {
            mUsuario = new MutableLiveData<>();
        }
        return mUsuario;
    }
    public MutableLiveData<Bitmap> getMFoto(){
    if(mFoto== null){
        mFoto = new MutableLiveData<>();
    }
    return mFoto;
    }


    public void recuperarUsuario() {
        File archivo = new File(getApplication().getFilesDir(), "usuario.dat");
        Usuario usu = new Usuario();

        try {
            FileInputStream fis = new FileInputStream(archivo);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            try {
                usu = (Usuario) ois.readObject();

                String nombre = usu.getNombre();
                String apellido = usu.getApellido();
                String mail = usu.getEmail();
                String pass = usu.getPassword();
                long dni = usu.getDni();
                Bitmap foto = BitmapFactory.decodeByteArray(usu.getFoto(),0, usu.getFoto().length);
                getMUsuario().setValue(usu);
                fis.close();
            } catch (EOFException eof) {
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplication(), "Error al acceder al archivo FileNotFound", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplication(), "Error al acceder al archivo IOExcep", Toast.LENGTH_LONG).show();
        }
    }

    public void guardarUsuario(String dni, String nombre, String apellido, String email, String password) {
        Usuario usu = new Usuario(Long.parseLong(dni),nombre, apellido, email, password, bytesFoto);
        File archivo = new File(getApplication().getFilesDir(), "usuario.dat");
            try {
                if(archivo.length()!=0){
                    archivo.delete();
                }
                FileOutputStream fos = new FileOutputStream(archivo, false);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(usu);
                bos.flush();
                fos.close();
                usu = null;
                Toast.makeText(getApplication(), "Usuario Guardado", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplication(), "Error al acceder al archivo FileNotFound", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplication(), "Error al acceder al archivo IOExcep", Toast.LENGTH_LONG).show();
            }
    }

    public void tomarFoto(int requestCode, int resultCode, @Nullable Intent data, int REQUEST_IMAGE_CAPTURE ){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Recupero los datos provenientes de la camara.
            Bundle extras = data.getExtras();
            //Casteo a bitmap lo obtenido de la camara.
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //Rutina para optimizar la foto,
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            mFoto.setValue(imageBitmap);


            //Rutina para convertir a un arreglo de byte los datos de la imagen
           bytesFoto = baos.toByteArray();

        }
    }
}




