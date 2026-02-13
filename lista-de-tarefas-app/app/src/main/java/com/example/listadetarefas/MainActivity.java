package com.example.listadetarefas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView taskListView;
    private ArrayList<String> taskList;
    private ArrayAdapter<String> tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove a barra de Título superior
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listView);
        Button btnAdicionar = findViewById(R.id.button6);

        // 2. Inicializar a lista e o adaptador
        taskList = new ArrayList<>();
        // O layout 'simple_list_item_1' é um padrão do Android para listas de texto
        tasksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);

        // 3. Vincular o adaptador à ListView
        listView.setAdapter(tasksAdapter);

        // 4. Lógica do clique do botão
        btnAdicionar.setOnClickListener(v -> {
            adicionarNovoItem("Nova Tarefa " + (taskList.size() + 1));
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void adicionarNovoItem(String tarefa) {
        taskList.add(tarefa); // Adiciona o dado no ArrayList
        tasksAdapter.notifyDataSetChanged(); // Avisa o Adapter que a lista mudou e ele deve atualizar a tela
    }
}