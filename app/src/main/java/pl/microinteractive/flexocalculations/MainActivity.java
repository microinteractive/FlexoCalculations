package pl.microinteractive.flexocalculations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    RadioGroup rg;
    RadioButton rb;
    Button bt;
    EditText naklad_et;
    EditText szerokosc_et;
    EditText pokrycie_et;
    EditText odpad_et;
    public static TableRow odpad_tr;
    TextView wynik_tv;
    TextView wynikNetto_tv;
    TextView odpad_tv;
    SharedPreferences ustawienia;


    double przelicznik = 100.00;
    double zalanie, odpad_procent, anilox_vol;
    double wynik, naklad, szerokosc, pokrycie, odpad = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        ustawienia = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(mToolbar);
        mToolbar.setSubtitle("Kalkulator Flexograficzny");

        rg = (RadioGroup)findViewById(R.id.jednostka_rg);
        naklad_et = (EditText)findViewById(R.id.naklad_et);
        szerokosc_et = (EditText)findViewById(R.id.szerokosc_et);
        pokrycie_et = (EditText)findViewById(R.id.pokrycie_et);
        odpad_et = (EditText)findViewById(R.id.odpad_et);
        wynik_tv = (TextView)findViewById(R.id.wynik_tv);
        wynikNetto_tv = (TextView)findViewById(R.id.wynikNetto_tv);
        odpad_tv = (TextView)findViewById(R.id.odpad_tv);
        odpad_tr = (TableRow)findViewById(R.id.odpad_tr);

        if(ustawienia.getBoolean("ust_odpad_procent", true)){
           odpad_tr.setVisibility(View.GONE);
           odpad_procent = Double.valueOf(ustawienia.getString("ust_procent_odpad", "0.5"));
        } else {
            odpad_tr.setVisibility(View.VISIBLE);
            //odpad_procent = Double.valueOf(ustawienia.getString("ust_procent_odpad", "0.5"));
        }

        anilox_vol = Double.valueOf(ustawienia.getString("ust_anilox_vol", "10"));
        zalanie = Double.valueOf(ustawienia.getString("ust_zalanie", "3"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_exit){
            exitApp();
        }
        if(item.getItemId() == R.id.action_setting) {
            Intent intent = new Intent(MainActivity.this, UstawieniaActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_info) {

        }
        return super.onOptionsItemSelected(item);
    }

    public void rbclick(View v) {
        int radiobuttonid = rg.getCheckedRadioButtonId();
        rb = (RadioButton)findViewById(radiobuttonid);

        switch (rb.getText().toString()) {
            case "mm":
                przelicznik = 1000;
                break;
            case "m":
                przelicznik = 1;
                break;
            case "cm":
                przelicznik = 100;
                break;
        }

        //Toast.makeText(getBaseContext(), "Jednostka " + rb.getText() + "\nprzelicznik: " + przelicznik, Toast.LENGTH_LONG).show();

    }
    public void paintRequire_calc(View v) {

        boolean naklad_ok, odpad_ok, szerokosc_ok, pokrycie_ok = false;

        if (TextUtils.isEmpty(naklad_et.getText().toString())) {
            naklad_et.setError("Podaj nakład");
            return;
        } else {
            naklad_ok = true;
            naklad = Double.valueOf(naklad_et.getText().toString());
        }
        if(!ustawienia.getBoolean("ust_odpad_procent", true)) {
            if (TextUtils.isEmpty(odpad_et.getText().toString())) {
                odpad_et.setError("Podaj odpad");
                return;
            } else {
                odpad_ok = true;
                odpad = Double.valueOf(odpad_et.getText().toString());
            }
        } else {
            odpad_ok = true;
        }
        if(TextUtils.isEmpty(szerokosc_et.getText().toString())) {
            szerokosc_et.setError("Podaj szerokość");
            return;
        } else {
            szerokosc_ok = true;
            szerokosc = Double.valueOf(szerokosc_et.getText().toString());
        }
        if(TextUtils.isEmpty(pokrycie_et.getText().toString())){
            pokrycie_et.setError("Podaj pokrycie");
            return;
        } else {
            pokrycie_ok = true;
            pokrycie = Double.valueOf(pokrycie_et.getText().toString());
        }

        if(naklad_ok && szerokosc_ok && pokrycie_ok && odpad_ok) {

            if(ustawienia.getBoolean("ust_odpad_procent", true)){
                odpad_procent = Double.valueOf(ustawienia.getString("ust_procent_odpad", "0.005"));
                odpad = naklad * (odpad_procent / 100);
            }

            zalanie = Double.valueOf(ustawienia.getString("ust_zalanie", ""));
            anilox_vol = Double.valueOf(ustawienia.getString("ust_anilox_vol", ""));
            szerokosc = szerokosc / przelicznik;

            wynik = (zalanie + ((naklad + odpad) * szerokosc) * 0.00001 * 0.3 * pokrycie * anilox_vol * 1.15);

            wynik_tv.setText(String.valueOf(wynik) + " kg");
            wynikNetto_tv.setText(String.valueOf(wynik - zalanie) + " kg");
            odpad_tv.setText(String.valueOf(odpad) + " m");
        } else {
            wynik_tv.setText("");
            wynikNetto_tv.setText("");
            odpad_tv.setText("");
        }

    }
    public void exitApp() {
        Toast.makeText(getBaseContext(), "Koniec", Toast.LENGTH_LONG).show();
        finish();
    }
}
