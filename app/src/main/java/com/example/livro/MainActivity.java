package com.example.livro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    ArrayList<Livro> livroArrayList;
    LivroRecyclerAdapter adapter;

    Button buttonAdicionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        livroArrayList = new ArrayList<>();

        buttonAdicionar = findViewById(R.id.buttonAdicionar);

        buttonAdicionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
                viewDialogAdd.showDialog(MainActivity.this);
            }

        });

        lerDados();
    }

    private void lerDados() {

        databaseReference.child("LIVRO").orderByChild("titulo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                livroArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Livro livro = dataSnapshot.getValue(Livro.class);
                    livroArrayList.add(livro);
                }
                adapter = new LivroRecyclerAdapter(MainActivity.this, livroArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public class ViewDialogAdd{
        public void showDialog(Context context){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_add_new_livro);

            EditText titulo = dialog.findViewById(R.id.titulo);
            EditText autor= dialog.findViewById(R.id.autor);
            EditText editora = dialog.findViewById(R.id.editora);
            EditText anoedicao = dialog.findViewById(R.id.anoedicao);
            EditText localizacao = dialog.findViewById(R.id.localizacao);

            Button buttonAdicionar = dialog.findViewById(R.id.buttonAdicionar);
            Button buttonCancelar = dialog.findViewById(R.id.buttonCancelar);

            buttonAdicionar.setText("Adicionar");
            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = "Livro" + new Date().getTime();
                    String tituloLivro = titulo.getText().toString();
                    String autorLivro =  autor.getText().toString();
                    String editoraLivro = editora.getText().toString();
                    String anoedicaoLivro = anoedicao.getText().toString();
                    String localizacaoLivro = localizacao.getText().toString();

                    if(tituloLivro.isEmpty() || autorLivro.isEmpty() || editoraLivro.isEmpty() || anoedicaoLivro.isEmpty() || localizacaoLivro.isEmpty()){
                        Toast.makeText(context, "Por favor adicione todos os dados requisitados!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        databaseReference.child("LIVRO").child(id).setValue(new Livro(id, tituloLivro, autorLivro, editoraLivro, anoedicaoLivro, localizacaoLivro));
                        Toast.makeText(context, "Feito!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        };
    }
}