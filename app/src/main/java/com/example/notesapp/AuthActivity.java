package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {

    private EditText enterLogin, enterPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        init();
    }

    private void init(){
        enterLogin = findViewById(R.id.enterLogin);
        enterPassword = findViewById(R.id.enterPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser = mAuth.getCurrentUser();
        if (currUser != null){
            // TODO перебрасывание на главный экран
            Toast.makeText(getApplicationContext(), "User is not null", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "User is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSignUp(View view){ // Метод кнопки регистрации
        if (TextUtils.isEmpty(enterLogin.getText().toString()) && !TextUtils.isEmpty(enterPassword.getText().toString())){ // Проверка заполнения полей
            Toast.makeText(getApplicationContext(), "Ошибка - логин или пароль не введён!", Toast.LENGTH_SHORT).show();
        } else if(enterPassword.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "Пароль должен иметь длину не менее 6 символов", Toast.LENGTH_SHORT).show();
        } else { // Процесс регистрации
            mAuth.createUserWithEmailAndPassword(enterLogin.getText().toString(), enterPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Письмо для подтверждения аккаунта отправлено на почту", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onClickSignIn(View view){ // Метод кнопки входа
        if(TextUtils.isEmpty(enterLogin.getText().toString()) && !TextUtils.isEmpty(enterPassword.getText().toString())){
            Toast.makeText(getApplicationContext(), "Ошибка - логин или пароль не введён!", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(enterLogin.getText().toString(),enterPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AuthActivity.this, EditActivity.class);
                        startActivity(i); // Переход на главный экран
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Неверный логин/пароль", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}