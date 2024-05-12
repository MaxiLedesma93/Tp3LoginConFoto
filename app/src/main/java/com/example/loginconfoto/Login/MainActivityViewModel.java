package com.example.loginconfoto.Login;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.loginconfoto.Registro.RegistroActivity;
import com.example.loginconfoto.models.Usuario;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainActivityViewModel extends AndroidViewModel {
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }
    public void loguearse(String email, String pass){
        File archivo = new File(getApplication().getFilesDir(), "usuario.dat");
        Usuario usuario = null;

        try {
            FileInputStream fi = new FileInputStream(archivo);
            BufferedInputStream bis = new BufferedInputStream(fi);
            ObjectInputStream ois = new ObjectInputStream(bis);

            while (true) {
                try {
                    usuario = (Usuario) ois.readObject();
                    if (usuario.getEmail().equals(email) && usuario.getPassword().equals(pass)) {
                        Intent intent = new Intent(getApplication(), RegistroActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplication().startActivity(intent);
                    } else {
                        Toast.makeText(getApplication(), "Usuario y/o Password Incorrectos", Toast.LENGTH_LONG).show();
                    }
                } catch (EOFException e) {
                    fi.close();
                    break;
                }
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
