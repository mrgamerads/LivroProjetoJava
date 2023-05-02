package com.example.livro;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LivroRecyclerAdapter  extends RecyclerView.Adapter<LivroRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList <Livro> livroArrayList;
    DatabaseReference databaseReference;

    public LivroRecyclerAdapter(Context context, ArrayList<Livro> livroArrayList) {
        this.context = context;
        this.livroArrayList = livroArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.livro_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Livro livro = livroArrayList.get(position);

        holder.textTitulo.setText("Titulo: " + livro.getTitulo());
        holder.textAutor.setText("Autor: " + livro.getAutor());
        holder.textEditora.setText("Editora: " + livro.getEditora());
        holder.textAnoEdicao.setText("Ano Edição: " + livro.getAnoedicao());
        holder.textLocalizacao.setText("Localização: " + livro.getLocalizacao());

        holder.buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context,
                        livro.getTitulo(),
                        livro.getAutor(),
                        livro.getEditora(),
                        livro.getAnoedicao(),
                        livro.getLocalizacao());
            }
        });

        holder.buttonDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogConfirmDelete viewDialogConfirmDelete = new ViewDialogConfirmDelete();
                viewDialogConfirmDelete.showDialog(context, livro.getTitulo());
            }
        });
    }

    @Override
    public int getItemCount() {
        return livroArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textTitulo;
        TextView textAutor;
        TextView textEditora;
        TextView textAnoEdicao;
        TextView textLocalizacao;

        Button buttonDeletar;
        Button buttonAtualizar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitulo = itemView.findViewById(R.id.titulo);
            textAutor = itemView.findViewById(R.id.autor);
            textEditora = itemView.findViewById(R.id.editora);
            textAnoEdicao = itemView.findViewById(R.id.anoedicao);
            textLocalizacao = itemView.findViewById(R.id.localizacao);

            buttonDeletar = itemView.findViewById(R.id.deletar);
            buttonAtualizar = itemView.findViewById(R.id.atualizar);
        }
    }

    public class ViewDialogUpdate{
        public void showDialog(Context context, String id, String titulo, String autor, String editora, String anoedicao, String localizacao){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_add_new_livro);

            EditText tituloText = dialog.findViewById(R.id.titulo);
            EditText autorText= dialog.findViewById(R.id.autor);
            EditText editoraText = dialog.findViewById(R.id.editora);
            EditText anoedicaoText = dialog.findViewById(R.id.anoedicao);
            EditText localizacaoText = dialog.findViewById(R.id.localizacao);

            tituloText.setText(titulo);
            autorText.setText(autor);
            editoraText.setText(editora);
            anoedicaoText.setText(anoedicao);

            Button buttonAtualizar = dialog.findViewById(R.id.buttonAdicionar);
            Button buttonCancelar = dialog.findViewById(R.id.buttonCancelar);

            buttonAtualizar.setText("Atualizar");
            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAtualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String tituloLivro = tituloText.getText().toString();
                    String autorLivro =  autorText.getText().toString();
                    String editoraLivro = editoraText.getText().toString();
                    String anoedicaoLivro = anoedicaoText.getText().toString();
                    String localizacaoLivro = localizacaoText.getText().toString();

                    if(tituloLivro.isEmpty() || autorLivro.isEmpty() || editoraLivro.isEmpty() || anoedicaoLivro.isEmpty() || localizacaoLivro.isEmpty()){
                        Toast.makeText(context, "Por favor adicione todos os dados requisitados!", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        if(tituloLivro.equals(titulo) && autorLivro.equals(autor) && editoraLivro.equals(editora) && anoedicaoLivro.equals(anoedicao) && localizacaoLivro.equals(localizacao)){
                            Toast.makeText(context, "Dados não foram atualizados!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            databaseReference.child("LIVRO").child(id).setValue(new Livro(id, tituloLivro, autorLivro, editoraLivro, anoedicaoLivro, localizacaoLivro));
                            Toast.makeText(context, "Dados Atualizados com Sucesso!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        };

        public void showDialog(Context context, String titulo, String autor, String editora, String anoedicao, String localizacao) {
        }
    }

    public class ViewDialogConfirmDelete{
        public void showDialog(Context context, String id){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.view_dialog_confirm_delete);

            Button buttonDelete = dialog.findViewById(R.id.buttonDelete);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                            databaseReference.child("LIVRO").child(id).removeValue();
                            Toast.makeText(context, "Dados exluídos com Sucesso!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                    }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        };
    }
}
