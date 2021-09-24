package com.example.notesapp.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notesapp.R;
import com.example.notesapp.ViewModel.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private EditText enterLogin, enterPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        init();
    }

    private void init() {
        enterLogin = findViewById(R.id.enterLogin);
        enterPassword = findViewById(R.id.enterPassword);
        this.authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    protected void onStart() { // Переход на MainActivity, если пользователь уже вошел ранее
        super.onStart();
        FirebaseUser currUser = authViewModel.getMAuth().getCurrentUser();
        if (currUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void onClickSignUp(View view) { // Метод кнопки регистрации
        if (TextUtils.isEmpty(enterLogin.getText().toString()) || TextUtils.isEmpty(enterPassword
                .getText().toString())) { // Проверка заполнения полей
            Toast.makeText(getApplicationContext(), "Ошибка - логин или пароль не введён!",
                    Toast.LENGTH_SHORT).show();
        } else if (enterPassword.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "Пароль должен иметь длину не менее 6 символов", Toast.LENGTH_SHORT).show();
        } else { // Процесс регистрации
            authViewModel.getMAuth().createUserWithEmailAndPassword(enterLogin.getText().toString(), enterPassword
                    .getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Успешная регистрация", Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getApplicationContext(), "Письмо для подтверждения аккаунта отправлено на почту", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Email введён неверно или занят", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void onClickSignIn(View view) { // Метод кнопки входа
        if (TextUtils.isEmpty(enterLogin.getText().toString()) || TextUtils.isEmpty(enterPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Ошибка - логин или пароль не введён!", Toast.LENGTH_SHORT).show();
        } else if (enterPassword.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "Невозможная длина пароля", Toast.LENGTH_SHORT).show();
        } else {
            authViewModel.getMAuth().signInWithEmailAndPassword(enterLogin.getText().toString(), enterPassword.getText().toString()).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AuthActivity.this, MainActivity.class);
                    startActivity(i); // Переход на главный экран
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Неверный логин/пароль", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}