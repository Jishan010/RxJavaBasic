package com.mobility.rxjavabasic;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private Disposable disposable;
    private Button addButton;
    private EditText editText;
    private ArrayList<String> animalList;
    Observable<String> animalDataObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = findViewById(R.id.addButton);
        editText = findViewById(R.id.editText);
        animalList = new ArrayList<>();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAnimalList(editText.getText().toString());
            }
        });

        animalDataObservable = getListOfAnimalObservable();
        animalDataObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("AnimalData", "onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(String list) {
                Log.d("AnimalData", list);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("AnimalData", "onError");

            }

            @Override
            public void onComplete() {
                Log.d("AnimalData", "Complete");
            }
        });
    }


    private Observable<String> getListOfAnimalObservable() {

        final List<String> animalData = getAnimalList();
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {

                if (!emitter.isDisposed()) {
                    for (String s : animalData) {
                        emitter.onNext(s);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });


    }


    private void setAnimalList(String animal) {
        animalList.add(animal);
    }


    private ArrayList<String> getAnimalList() {

        animalList.add("Cat");
        animalList.add("Dog");
        animalList.add("Lion");
        animalList.add("Tiger");
        animalList.add("Cow");

        return animalList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        Log.d("AnimalData", "Disposal Disposed");
    }
}
